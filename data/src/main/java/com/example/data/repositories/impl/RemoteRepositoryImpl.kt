package com.example.data.repositories.impl

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.example.common.UserDevice
import com.example.data.constants.Const
import com.example.data.constants.Const.DEVICES
import com.example.data.constants.Const.USERS
import com.example.data.db.dao.*
import com.example.data.db.entities.Currency
import com.example.data.db.remote_models.*
import com.example.data.repositories.intrefaces.*
import com.example.mynotes.presentation.utils.extensions.huminize
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class RemoteRepositoryImpl @Inject constructor(
    private val remoteStorage: RemoteStorage,
    private val remoteDatabase: RemoteDatabase,
    private val persons: PersonDao,
    private val pockets: PocketDao,
    private val currencies: CurrencyDao,
    private val wallets: WalletDao,
    private val transactions: TransactionDao,
    private val shared: SharedPrefRepository,
    private val auth: AuthRepository,
    private val context: Context
) : RemoteRepository {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy")
    private val BACK_TIME = 600_000
    private val DAY_MILLS = 86_400_000
    private val DATE = "date"
    private val PATH = "all_data"
    private val COPY = "copy"
    private val KEY_UPLOADED_DATE = "key_uploaded_date"
    private val KEY_DOWNLOADED_DATE = "key_downloaded_date"
    private var subscription: ListenerRegistration? = null
    // private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @SuppressLint("SimpleDateFormat")
    override suspend fun uploadDataAsFile(doNow: Boolean?) {
        if (auth.currentUser != null) {
            //  downloadByNodes(getLastUpdatedDatabaseTime() - BACK_TIME)
            val lastUploadedDate = shared.getLong(KEY_UPLOADED_DATE)
            if ((lastUploadedDate < System.currentTimeMillis() - DAY_MILLS) || doNow ?: true) coroutineScope {
                launch(Dispatchers.IO) {
                    val time = yearBeginTime()
                    val r1 = persons.getFromDate(time)
                    val r2 = pockets.getFromDate(time)
                    val r3 = currencies.getFromDate(time)
                    val r4 = wallets.getFromDate(time)
                    val r5 = transactions.getFromDate(time)

                    combine(
                        r1,
                        r2,
                        r3,
                        r4,
                        r5
                    ) { persons, pockets, currencies, wallets, transactions ->

                        val allData = AllData(date = System.currentTimeMillis() - BACK_TIME,
                            currency = currencies.map { it.toRemote() },
                            pocket = pockets.map { it.toRemote() },
                            person = persons.map { it.toRemote() },
                            wallet = wallets.map { it.toRemote() },
                            transaction = transactions.map { it.toRemote() })
                        val dataString = Gson().toJson(allData).toString()
                        val dataCompressed = compress(dataString) ?: ByteArray(1)

                        val fileName = dateFormat.format(Date())

                        val storage = remoteStorage.storage.child("${auth.currentUser?.uid}/$PATH")

                        val size = storage.child("$fileName.json").metadata.addOnSuccessListener {
                            it.sizeBytes
                        }.await().sizeBytes

                        if (dataCompressed.size >= size) {

                            // upload original file

                            storage.child("$fileName.json").putBytes(dataCompressed)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        runBlocking {
                                            shared.saveLong(
                                                KEY_UPLOADED_DATE,
                                                System.currentTimeMillis() - BACK_TIME
                                            )
                                        }
                                    }
                                }.await()

                            // upload copyFile

                            storage.child("$COPY${fileName}.json").putBytes(dataCompressed)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        runBlocking {
                                            it.result.totalByteCount
                                            shared.saveLong(
                                                KEY_UPLOADED_DATE,
                                                System.currentTimeMillis() - BACK_TIME
                                            )
                                        }
                                    }
                                }.await()
                        }
                    }.collect()
                }
            }
        }
    }


    private suspend fun isDownloadAvialable(): Boolean {
        return pockets.getCount() == 0 || currencies.getCount() == 0 || transactions.getCount() == 0 || !shared.getBoolean(
            KEY_DOWNLOADED_DATE
        )
    }

    override suspend fun downloadAllData(): Flow<Boolean> = callbackFlow {
        if (auth.currentUser != null) {
//           val storage = remoteStorage.storage.child("${auth.currentUser?.uid}/$PATH")
            if (isDownloadAvialable()) {
                downloadByNodes(0) {
                    channel.trySend(it)
                }
//
//                coroutineScope {
//                    launch(Dispatchers.IO) {
//                        storage.listAll().addOnSuccessListener { listResult ->
//                            if (listResult.items.isEmpty()) {
//                                runBlocking {
//                                    downloadByNodes(0) {
//                                        channel.trySend(it)
//                                    }
//                                }
//
//                            } else {
//                                listResult.items.filter { !it.name.startsWith(COPY) }
//                                    .forEach { fileRemote ->
//                                        var size = 0L
//                                        var copyFileSize = 0L
//                                        runBlocking {
//                                            size = fileRemote.metadata.addOnSuccessListener {
//                                                it.sizeBytes
//                                            }.await().sizeBytes
//                                            copyFileSize =
//                                                storage.child("$COPY${fileRemote.name}").metadata.addOnSuccessListener {
//                                                    it.sizeBytes
//                                                }.await().sizeBytes
//                                        }
//                                        val file = if (copyFileSize > size) {
//                                            storage.child("$COPY${fileRemote.name}")
//                                        } else {
//                                            fileRemote
//                                        }
//                                        runBlocking {
//                                            file.getBytes(10_000_000)
//                                                .addOnSuccessListener { byteArray ->
//                                                    val gson = Gson()
//                                                    val jsonString =
//                                                        JsonParser.parseString(decompress(byteArray))
//                                                    val type = object : TypeToken<AllData>() {}.type
//                                                    var date = 0L
//                                                    try {
//                                                        val allData: AllData =
//                                                            gson.fromJson(jsonString, type)
//                                                        date = allData.date
//                                                        runBlocking {
//                                                            val r5 = async {
//                                                                transactions.addTransactions(allData.transaction.map { it.toLocal() })
//                                                            }
//
//                                                            val r1 =
//                                                                async { persons.addPersons(allData.person.map { it.toLocal() }) }
//                                                            val r2 =
//                                                                async { pockets.addPockets(allData.pocket.map { it.toLocal() }) }
//                                                            val r3 = async {
//                                                                currencies.addCurrencies(allData.currency.map { it.toLocal() })
//                                                            }
//                                                            val r4 =
//                                                                async { wallets.addWallets(allData.wallet.map { it.toLocal() }) }
//
//                                                            r1.await()
//                                                            r2.await()
//                                                            r3.await()
//                                                            r4.await()
//                                                            r5.await()
//                                                        }
//                                                    } catch (_: Exception) {
//                                                        date = 0L
//                                                    }
//                                                    runBlocking {
//                                                        downloadByNodes(date) {
//                                                            trySend(it)
//                                                        }
//                                                    }
//                                                }.await()
//
//                                        }
//                                        trySend(true)
//                                    }
//                            }
//
//                        }.addOnFailureListener {
//                            runBlocking {
//                                downloadByNodes(0) {
//                                    channel.trySend(it)
//                                }
//                                awaitClose { channel.close() }
//                            }
//                        }.addOnCanceledListener {
//                            runBlocking {
//                                downloadByNodes(0) {
//                                    channel.trySend(it)
//                                }
//                            }
//                        }
//                        Log.d("HomeViewModelImp", "HomeViewModelImp finish")
//
//                    }
//                }
            } else {
                trySend(true)
            }
        }
        awaitClose {
            channel.close()
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun downloadByNodes(date: Long, function: (Boolean) -> Unit) {
        coroutineScope {
            launch(Dispatchers.IO) {
                if (auth.currentUser != null)
                    runBlocking {
                        val remote = remoteDatabase.storageRef.firestore.collection(
                            USERS
                        ).document(auth.currentUser?.uid ?: "")

                        remote.collection(Const.TRANSACTIONS).orderBy("date").limitToLast(100).get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) try {
                                    val list = snapshot.map {
                                        it.toObject(
                                            TransactionRemote::class.java
                                        )
                                    }
                                    runBlocking {
                                        transactions.addTransactions(list.map { it.toLocal() })
                                    }
                                } catch (_: Exception) {
                            }
                        }.await()

                        remote.collection(Const.CURRENCIES).whereGreaterThanOrEqualTo(
                            DATE, date
                        ).get().addOnSuccessListener { snapshot ->
                            if (!snapshot.isEmpty) try {
                                val list = snapshot.map {
                                    it.toObject(
                                        CurrencyRemote::class.java
                                    )
                                }
                                runBlocking {
                                    currencies.addCurrencies(list.map { it.toLocal() })
                                }
                            } catch (_: Exception) {
                            }
                        }.await()

                        remote.collection(Const.POCKETS).whereGreaterThanOrEqualTo(
                            DATE, date
                        ).get().addOnSuccessListener { snapshot ->
                            if (!snapshot.isEmpty) try {
                                val list = snapshot.map {
                                    it.toObject(
                                        PocketRemote::class.java
                                    )
                                }
                                runBlocking {
                                    pockets.addPockets(list.map { it.toLocal() })
                                }
                            } catch (_: Exception) {
                            }
                        }.await()

                        remote.collection(Const.PERSONS).whereGreaterThanOrEqualTo(
                            DATE, date
                        ).get().addOnSuccessListener { snapshot ->
                            if (!snapshot.isEmpty) try {
                                val list = snapshot.map {
                                    it.toObject(
                                        PersonRemote::class.java
                                    )
                                }
                                runBlocking {
                                    persons.addPersons(list.map {
                                        it.toLocal()
                                    })
                                }
                            } catch (_: Exception) {
                            }
                        }.await()

                        remote.collection(Const.WALLETS).whereGreaterThanOrEqualTo(
                            DATE, date
                        ).get().addOnSuccessListener { snapshot ->
                            if (!snapshot.isEmpty) try {
                                val list = snapshot.map {
                                    it.toObject(
                                        WalletRemote::class.java
                                    )
                                }
                                runBlocking {
                                    wallets.addWallets(list.map { it.toLocal() })
                                }
                            } catch (_: Exception) {
                            }
                        }.await()

//
//                    if (currencies.getCount() == 0) {
//                        currencies.add(
//                            Currency(
//                                id = UUID.randomUUID().toString(),
//                                name = "dollar",
//                                rate = 1.0,
//                                date = System.currentTimeMillis()
//                            )
//                        )
//                    }

                        shared.saveBoolean(KEY_DOWNLOADED_DATE, true)
                    }
                function(true)
            }
        }
    }

    override suspend fun checkNotLoadedDatas() {
        if (auth.currentUser != null) {
            val remote = remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")
            coroutineScope {
                launch(Dispatchers.IO) {
                    val r1 = persons.getNotUploaded(false)
                    val r2 = pockets.getNotUploaded(false)
                    val r3 = currencies.getNotUploaded(false)
                    val r4 = wallets.getNotUploaded(false)
                    val r5 = transactions.getNotUploaded(false)
                    combine(r1, r2, r3, r4, r5) { person, pocket, currency, wallet, transaction ->
                        val task1 = async {
                            person.forEach { p ->
                                remote.collection(Const.PERSONS).document(p.id).set(p.toRemote())
                                    .addOnSuccessListener {
                                        runBlocking { persons.add(p.copy(uploaded = true)) }
                                    }
                            }
                        }
                        val task2 = async {
                            pocket.forEach { p ->
                                remote.collection(Const.POCKETS).document(p.id).set(p.toRemote())
                                    .addOnSuccessListener {
                                        runBlocking { pockets.add(p.copy(uploaded = true)) }
                                    }
                            }
                        }
                        val task3 = async {
                            currency.forEach { p ->
                                remote.collection(Const.CURRENCIES).document(p.id).set(p.toRemote())
                                    .addOnSuccessListener {
                                        runBlocking { currencies.add(p.copy(uploaded = true)) }
                                    }
                            }
                        }
                        val task4 = async {
                            wallet.forEach { p ->
                                remote.collection(Const.WALLETS).document(p.id).set(p.toRemote())
                                    .addOnSuccessListener {
                                        runBlocking { wallets.add(p.copy(uploaded = true)) }
                                    }
                            }
                        }
                        val task5 = async {
                            transaction.forEach { p ->
                                remote.collection(Const.TRANSACTIONS).document(p.id)
                                    .set(p.toRemote())
                                    .addOnSuccessListener {
                                        runBlocking { transactions.add(p.copy(uploaded = true)) }
                                    }
                            }
                        }
                        task1.await()
                        task2.await()
                        task3.await()
                        task4.await()
                        task5.await()

                    }.collect()
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    override suspend fun observeDevice(): Flow<Boolean> = callbackFlow {
        if (auth.currentUser != null) {
            val remote = remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")

            subscription =
                remote.collection(DEVICES).document(DEVICES).addSnapshotListener { value, error ->
                    val device = value?.toObject(UserDevice::class.java)
                    val id: String = Settings.Secure.getString(
                        context.contentResolver, Settings.Secure.ANDROID_ID
                    )
                    device?.let {
                        if (it.id != id) {
                            trySend(true)
                        } else {
                            trySend(false)
                        }
                    }
                }
        }
        awaitClose { }

    }.flowOn(Dispatchers.IO)

    override suspend fun stopObserveDevice() {
        runBlocking {
            subscription?.remove()
        }
    }

    @SuppressLint("HardwareIds")
    override suspend fun saveDevice() {
        if (auth.currentUser != null) {
            val remote = remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")
            val id: String =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val device = UserDevice(
                id = id,
                date = System.currentTimeMillis(),
                name = "",
                info = "",
            )
            remote.collection(DEVICES).document(DEVICES).set(device).await()
        }
    }

    @SuppressLint("HardwareIds")
    override suspend fun getRemoteDevice(): UserDevice? {
        var device: UserDevice? = null
        if (auth.currentUser != null) {
            runBlocking {
                remoteDatabase.storageRef.firestore.collection(USERS)
                    .document(auth.currentUser?.uid ?: "").collection(DEVICES).document(DEVICES)
                    .get()
                    .addOnSuccessListener {
                        device = it.toObject(UserDevice::class.java)
                    }.await()
            }
        }
        return device
    }

    @SuppressLint("HardwareIds")
    override suspend fun getLocalDeviceId() =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    override suspend fun getNotUploadedDataCount(): Flow<Int> = callbackFlow {
        coroutineScope {
            launch(Dispatchers.IO) {
                val count1 = persons.getNotUploadedCount(false)
                val count2 = pockets.getNotUploadedCount(false)
                val count3 = currencies.getNotUploadedCount(false)
                val count4 = wallets.getNotUploadedCount(false)
                val count5 = transactions.getNotUploadedCount(false)
                combine(
                    count1,
                    count2,
                    count3,
                    count4,
                    count5
                ) { c1, c2, c3, c4, c5 ->
                    trySend(c1 + c2 + c3 + c4 + c5)
                }.collect()
            }
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    private fun compress(data: String): ByteArray? {
        val bos = ByteArrayOutputStream(data.length)
        val gzip = GZIPOutputStream(bos)
        gzip.write(data.toByteArray())
        gzip.close()
        val compressed = bos.toByteArray()
        bos.close()
        return compressed
    }

    private fun decompress(compressed: ByteArray?): String {
        var str = ""
        try {
            val bis = ByteArrayInputStream(compressed)
            val gis = GZIPInputStream(bis)
            val br = BufferedReader(InputStreamReader(gis, "UTF-8"))
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            br.close()
            gis.close()
            bis.close()
            str = sb.toString()
        } catch (_: java.lang.Exception) {
        }
        return str
    }

    private fun yearBeginTime(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, cal[Calendar.YEAR])
        cal.set(Calendar.MONTH, 0)
        cal.set(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time.time - 10 * DAY_MILLS
    }
}