package com.example.data.repositories.impl

import android.annotation.SuppressLint
import android.util.JsonReader
import android.util.Log
import com.example.data.db.dao.*
import com.example.data.db.remote_models.AllData
import com.example.data.repositories.intrefaces.RemoteDatabase
import com.example.data.repositories.intrefaces.RemoteRepository
import com.example.data.repositories.intrefaces.RemoteStorage
import com.example.data.repositories.intrefaces.SharedPrefRepository
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import javax.inject.Inject
import kotlin.reflect.cast
import kotlin.reflect.safeCast

class RemoteRepositoryImpl @Inject constructor(
    private val storage: RemoteStorage,
    private val database: RemoteDatabase,
    private val persons: PersonDao,
    private val pockets: PocketDao,
    private val currencies: CurrencyDao,
    private val wallets: WalletDao,
    private val transactions: TransactionDao,
    private val shared: SharedPrefRepository
) : RemoteRepository {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy")
    private val PATH = "all_data"
    private val KEY_DATA = "key_data"

    @SuppressLint("SimpleDateFormat")
    override suspend fun upload() {
        coroutineScope {
            launch(Dispatchers.IO) {
                val r1 = persons.getAll()
                val r2 = pockets.getAll()
                val r3 = currencies.getAll()
                val r4 = wallets.getAll()
                val r5 = transactions.getAll()

                combine(r1, r2, r3, r4, r5) { persons,
                                              pockets,
                                              currencies,
                                              wallets,
                                              transactions ->

                    val allData = AllData(
                        date = System.currentTimeMillis() - 100_000,
                        currency = currencies.map { it.toRemote() },
                        pocket = pockets.map { it.toRemote() },
                        person = persons.map { it.toRemote() },
                        wallet = wallets.map { it.toRemote() },
                        transaction = transactions.map { it.toRemote() }
                    )
                    val dataAllBytes = Gson().toJson(allData).toString().toByteArray()

                    val compressor = Deflater()
                    compressor.setLevel(Deflater.BEST_COMPRESSION)
                    compressor.setInput(dataAllBytes)
                    compressor.finish()

                    Log.d("compressor", "compressor: $compressor")

                    val fileName = dateFormat.format(Date())

                    storage.storage.child("$PATH/$fileName.json").putBytes(dataAllBytes)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                runBlocking {
                                    shared.saveLong(KEY_DATA, System.currentTimeMillis())
                                }
                            }
                        }

                }.collect()
            }
        }
    }

    override suspend fun download() {
        coroutineScope {
            launch(Dispatchers.IO) {
                storage.storage.child(PATH).listAll().addOnSuccessListener {
                    it.items.forEach {
                        it.getBytes(10_000_000).addOnSuccessListener { byteArray ->
                            val gson = Gson()
                            val jsonString = JsonParser.parseString(String(byteArray))
                            val type = object : TypeToken<AllData>() {}.type
                            val allData: AllData = gson.fromJson(jsonString, type)
                            if (allData != null) {
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
                                    val res1: List<Long> = r1.await()
                                    val res2: List<Long> = r2.await()
                                    val res3: List<Long> = r3.await()
                                    val res4: List<Long> = r4.await()
                                    val res5: List<Long> = r5.await()

                                    if (res1.isNotEmpty() && res2.isNotEmpty() && res3.isNotEmpty() && res4.isNotEmpty() && res5.isNotEmpty()) {
                                        // all data downloaded


                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}