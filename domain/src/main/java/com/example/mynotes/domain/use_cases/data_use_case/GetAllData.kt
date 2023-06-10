package com.example.mynotes.domain.use_cases.data_use_case

import com.example.common.Type
import com.example.common.getTypeEnum
import com.example.common.getTypeNumber
import com.example.common.getTypeText
import com.example.mynotes.domain.use_cases.getcredit_use_case.person_use_case.PersonUseCases
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import com.example.mynotes.domain.use_cases.transaction_use_case.TransactionUseCases
import com.example.mynotes.domain.use_cases.wallet_use_case.WalletUseCases
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.extensions.huminizeForFile
import com.example.mynotes.presentation.utils.extensions.round
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllData @Inject constructor(
    private val pocketUseCases: PocketUseCases,
    private val personUseCases: PersonUseCases,
    private val historyUseCase: TransactionUseCases,
    private val walletUseCases: WalletUseCases,
) {
    suspend operator fun invoke(): Flow<LinkedHashMap<String, LinkedHashMap<String, List<String>>>> =
        flow {
            val balanceFlow = walletUseCases.getBalances.invoke()
            val walletsByowners = walletUseCases.getWalletsByOwnes.invoke()
            val poketsFlow = pocketUseCases.getAll.invoke()
            val personsFlow = personUseCases.getAll.invoke()
            val history = historyUseCase.getHistoryForHome.invoke(1000)

            val mainMap = LinkedHashMap<String, LinkedHashMap<String, List<String>>>()
            combine(
                balanceFlow,
                walletsByowners,
                poketsFlow,
                personsFlow,
                history
            ) { balan, w, pock, per, his ->

                // balances

                val keyMap1 = "Balans"
                val map1 = LinkedHashMap<String, List<String>>()
                val keyBalance =
                    "Jami balans: ${balan.sumOf { it.amount * (1 / it.rate) }.huminize()} $"
                val valueBalance = balan.map { "${it.amount.huminize()} ${it.currencyName}" }
                map1.put(keyBalance, valueBalance)


                // pokets

                val keyMap2 = "Hamyonlar"

                val map2 = LinkedHashMap<String, List<String>>()
                pock.forEach { pocketDomain ->
                    val keyPocket = pocketDomain.name
                    val valuePoket = w.filter {
                        pocketDomain.id == it.ownerId
                    }.map { "${it.currencyBalance.huminize()} ${it.currencyName}" }
                    map2.put(keyPocket, valuePoket)
                }

                // persons

                val keyMap3 = "Qarzdorlar"

                val map3 = LinkedHashMap<String, List<String>>()
                per.filter {
                    w.filter { it.currencyBalance.round() > 0 }.map { it.ownerId }
                        .contains(it.id)
                }.forEach { pocketDomain ->
                    val keyPocket = pocketDomain.name
                    val valuePoket = w.filter {
                        pocketDomain.id == it.ownerId && it.currencyBalance > 0
                    }.map { "${it.currencyBalance.huminize()} ${it.currencyName}" }
                    map3.put(keyPocket, valuePoket)
                }

                val keyMap4 = "Haqdorlar"

                val map4 = LinkedHashMap<String, List<String>>()
                per.filter {
                    w.filter { it.currencyBalance.round() < 0 }.map { it.ownerId }
                        .contains(it.id)
                }.forEach { pocketDomain ->
                    val keyPocket = pocketDomain.name
                    val valuePoket = w.filter {
                        pocketDomain.id == it.ownerId && it.currencyBalance < 0
                    }.map { "${it.currencyBalance.huminize()} ${it.currencyName}" }
                    map4.put(keyPocket, valuePoket)
                }

                val keyMap5 = "Tarix"

                val map5 = LinkedHashMap<String, List<String>>()

                his.forEach { item ->

                    val list = ArrayList<String>()
                    var incr = ""
                    if (item.title == getTypeNumber(Type.INCOME) || item.title == getTypeNumber(Type.CREDIT)) {
                        incr = "+"
                    } else if (item.title == getTypeNumber(Type.OUTCOME) || item.title == getTypeNumber(
                            Type.DEBET
                        )
                    ) {
                        incr = "-"
                    }
                    val title = getTypeText(item.title)
                    val converted = "$incr${item.amount.huminize()} ${item.currency}"
                    list.add("$title: $converted")


                    if (!item.fromName.isNullOrEmpty()) {
                        val pocket = if (item.isFromPocket) " hamyon" else ""
                        val from = (item.fromName ?: "") + "${pocket}dan"
                        if (getTypeEnum(item.title) == Type.CONVERTATION) {
                            val textMinus = "-${item.moneyFrom?.huminize()} ${item.moneyNameFrom}"
                            list.add("$from $textMinus")
                        } else {
                            list.add(from)
                        }
                    }
                    if (!item.toName.isNullOrEmpty()) {
                        val pocket = if (item.isToPocket) " hamyon" else ""
                        val to = (item.toName ?: "") + "${pocket}ga"
                        if (getTypeEnum(item.title) == Type.CONVERTATION) {
                            val textPlus = "+${item.moneyTo?.huminize()} ${item.moneyNameTo}"
                            list.add("$to $textPlus")
                        } else {
                            list.add(to)
                        }
                    }

                    val kurs =
                        if (item.rateFrom > item.rateTo) {
                            "1 ${item.moneyNameTo} = ${(item.rateFrom / item.rateTo).huminize()} ${item.moneyNameFrom}"
                        } else if (item.rateFrom < item.rateTo) {
                            "1 ${item.moneyNameFrom} = ${(item.rateTo / item.rateFrom).huminize()} ${item.moneyNameTo}"
                        } else if (item.rateFrom != 1.0 && item.rateTo != 1.0) {
                            "1$ = ${item.rateTo.huminize()}"
                        } else ""
                    if (kurs.trim().isNotEmpty()) {
                        list.add("kurs: $kurs")
                    }
                    val oldBalance = "Bundan oldingi balans: ${item.balance.huminize()} $"
                    list.add(oldBalance)
                    var izoh = ""
                    item.comment?.let {
                        if (it.isNotEmpty()) {
                            izoh = "izoh: $it"
                            list.add(izoh)
                        }
                    }
                    map5.put(item.date.huminizeForFile(), list)
                }
                mainMap.put(keyMap1, map1)
                mainMap.put(keyMap2, map2)
                mainMap.put(keyMap3, map3)
                mainMap.put(keyMap4, map4)
                mainMap.put(keyMap5, map5)

                emit(mainMap)

            }.collect()
        }
}