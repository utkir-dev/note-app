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
import com.example.data.db.remote_models.*
import com.example.data.repositories.intrefaces.*
import com.google.firebase.firestore.ktx.firestore
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.*
import javax.inject.Inject

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

    @SuppressLint("SimpleDateFormat")
    override suspend fun uploadDataAsFile() {
        Log.d("worker", "uploadDataAsFile inside begin")


        //  downloadByNodes(getLastUpdatedDatabaseTime() - BACK_TIME)
        val lastUploadedDate = shared.getLong(KEY_UPLOADED_DATE)
        if (lastUploadedDate < System.currentTimeMillis() - DAY_MILLS)
            coroutineScope {
                launch(Dispatchers.IO) {
                    val time = yearBeginTime()
                    val r1 = persons.getFromDate(time)
                    val r2 = pockets.getFromDate(time)
                    val r3 = currencies.getFromDate(time)
                    val r4 = wallets.getFromDate(time)
                    val r5 = transactions.getFromDate(time)

                    combine(r1, r2, r3, r4, r5) { persons,
                                                  pockets,
                                                  currencies,
                                                  wallets,
                                                  transactions ->

                        val allData = AllData(
                            date = System.currentTimeMillis() - BACK_TIME,
                            currency = currencies.map { it.toRemote() },
                            pocket = pockets.map { it.toRemote() },
                            person = persons.map { it.toRemote() },
                            wallet = wallets.map { it.toRemote() },
                            transaction = transactions.map { it.toRemote() }
                        )
                        val dataString = Gson().toJson(allData).toString()
                        val dataCompressed = compress(dataString) ?: ByteArray(1)

                        val fileName = dateFormat.format(Date())

                        val storage = remoteStorage.storage.child("${auth.currentUser?.uid}/$PATH")

                        val size = storage.child("$fileName.json").metadata.addOnSuccessListener {
                            it.sizeBytes
                        }.await().sizeBytes

                        if (dataCompressed.size >= size) {

                            // upload original file

                            storage.child("$fileName.json")
                                .putBytes(dataCompressed)
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

                            storage.child("$COPY${fileName}.json")
                                .putBytes(dataCompressed)
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

                        Log.d("worker", "uploadDataAsFile inside end")

                    }.collect()
                }
            }
    }

    private suspend fun isDownloadAvialable(): Boolean {
        return pockets.getCount() == 0 ||
                currencies.getCount() == 0 ||
                transactions.getCount() == 0 ||
                !shared.getBoolean(KEY_DOWNLOADED_DATE)
    }

    override suspend fun downloadAllData() {
        val storage = remoteStorage.storage.child("${auth.currentUser?.uid}/$PATH")
        if (isDownloadAvialable())
            coroutineScope {
                launch(Dispatchers.IO) {
                    storage.listAll()
                        .addOnSuccessListener { listResult ->
                            listResult.items.filter { !it.name.startsWith(COPY) }
                                .forEach { fileRemote ->
                                    var size = 0L
                                    var copyFileSize = 0L
                                    runBlocking {
                                        size = fileRemote.metadata.addOnSuccessListener {
                                            it.sizeBytes
                                        }.await().sizeBytes
                                        copyFileSize =
                                            storage.child("$COPY${fileRemote.name}").metadata.addOnSuccessListener {
                                                it.sizeBytes
                                            }.await().sizeBytes
                                    }
                                    val file = if (copyFileSize > size) {
                                        storage.child("$COPY${fileRemote.name}")
                                    } else {
                                        fileRemote
                                    }
                                    file.getBytes(10_000_000).addOnSuccessListener { byteArray ->
                                        val gson = Gson()
                                        val jsonString =
                                            JsonParser.parseString(decompress(byteArray))
                                        val type = object : TypeToken<AllData>() {}.type
                                        var date = 0L
                                        try {
                                            val allData: AllData = gson.fromJson(jsonString, type)
                                            date = allData.date
                                            runBlocking {
                                                val r1 =
                                                    async { persons.addPersons(allData.person.map { it.toLocal() }) }
                                                val r2 =
                                                    async { pockets.addPockets(allData.pocket.map { it.toLocal() }) }
                                                val r3 =
                                                    async { currencies.addCurrencies(allData.currency.map { it.toLocal() }) }
                                                val r4 =
                                                    async { wallets.addWallets(allData.wallet.map { it.toLocal() }) }
                                                val r5 =
                                                    async { transactions.addTransactions(allData.transaction.map { it.toLocal() }) }
                                                r1.await()
                                                r2.await()
                                                r3.await()
                                                r4.await()
                                                r5.await()
                                            }
                                        } catch (_: Exception) {
                                            date = 0L
                                        }

                                        // check fireStore nodes after uploading date

                                        downloadByNodes(date)
                                    }
                                }
                        }
                }
            }
    }

    override suspend fun checkNotLoadedDatas() {
        val remote =
            remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")
        coroutineScope {
            launch(Dispatchers.IO) {
                val r1 = persons.getNotUploaded(false)
                val r2 = pockets.getNotUploaded(false)
                val r3 = currencies.getNotUploaded(false)
                val r4 = wallets.getNotUploaded(false)
                val r5 = transactions.getNotUploaded(false)
                combine(r1, r2, r3, r4, r5) { person,
                                              pocket,
                                              currency,
                                              wallet,
                                              transaction ->
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
                            remote.collection(Const.TRANSACTIONS).document(p.id).set(p.toRemote())
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


                    Log.d("worker", "checkNotLoadedDatas inside end")

                }.collect()
            }
        }
    }

    @SuppressLint("HardwareIds")
    override suspend fun observeDevice() = callbackFlow {
        val remote =
            remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")
        val subscription =
            remote.collection(DEVICES).document(DEVICES).addSnapshotListener { value, error ->

                val device = value?.toObject(UserDevice::class.java)
                val id: String =
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


                device?.let {
                    if (it.id != id) {
                        trySend(true)
                    } else {
                        trySend(false)
                    }
                }
            }
        awaitClose { subscription.remove() }
    }

    @SuppressLint("HardwareIds")
    override suspend fun saveDevice() {
        val remote =
            remoteDatabase.storageRef.firestore.collection(USERS)
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

    @SuppressLint("HardwareIds")
    override suspend fun getRemoteDevice(): UserDevice? {
        var device: UserDevice? = null
        runBlocking {
            remoteDatabase.storageRef.firestore.collection(USERS)
                .document(auth.currentUser?.uid ?: "")
                .collection(DEVICES).document(DEVICES).get().addOnSuccessListener {
                    device = it.toObject(UserDevice::class.java)
                }.await()
            Log.d("worker", "getRemoteDevice rep : $device")
        }
        return device
    }

    @SuppressLint("HardwareIds")
    override suspend fun getLocalDeviceId() =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


    private fun downloadByNodes(date: Long) {
        runBlocking {
            val remote =
                remoteDatabase.storageRef.firestore.collection(
                    USERS
                )
                    .document(auth.currentUser?.uid ?: "")

            remote.collection(Const.CURRENCIES)
                .whereGreaterThanOrEqualTo(
                    DATE, date
                )
                .get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty)
                        try {
                            val list =
                                snapshot.map {
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

            remote.collection(Const.POCKETS)
                .whereGreaterThanOrEqualTo(
                    DATE, date
                )
                .get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty)
                        try {
                            val list =
                                snapshot.map {
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

            remote.collection(Const.PERSONS)
                .whereGreaterThanOrEqualTo(
                    DATE, date
                )
                .get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty)
                        try {
                            val list =
                                snapshot.map {
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

            remote.collection(Const.WALLETS)
                .whereGreaterThanOrEqualTo(
                    DATE, date
                )
                .get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty)
                        try {
                            val list =
                                snapshot.map {
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

            remote.collection(Const.TRANSACTIONS)
                .whereGreaterThanOrEqualTo(
                    DATE, date
                ).orderBy(DATE)
                .limitToLast(10_000)
                .get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty)
                        try {
                            val list =
                                snapshot.map {
                                    it.toObject(
                                        TransactionRemote::class.java
                                    )
                                }
                            runBlocking {
                                transactions.addTransactions(
                                    list.map { it.toLocal() })
                            }
                        } catch (_: Exception) {
                        }
                }.await()

            shared.saveBoolean(KEY_DOWNLOADED_DATE, true)

            Log.d("enter", " shared.saveBoolean: true")
        }
    }

    private fun getLastUpdatedDatabaseTime(): Long = maxOf(
        persons.getLastUpdatedTime(),
        pockets.getLastUpdatedTime(),
        currencies.getLastUpdatedTime(),
        wallets.getLastUpdatedTime(),
        transactions.getLastUpdatedTime()
    )

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