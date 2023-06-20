package com.example.mynotes.presentation.ui.screens.main.givecredit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.ModelDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.buttons.buttonColors
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.EmptyRowSizeble
import com.example.mynotes.presentation.utils.items.ItemEmptyRow

class GiveCreditScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: GiveCreditViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(
    viewModel: GiveCreditViewModelImp
) {
    val persons by viewModel.persons.collectAsStateWithLifecycle(emptyList())
    val pockets by viewModel.pockets.collectAsStateWithLifecycle(emptyList())
    val currencies by viewModel.currencies.collectAsStateWithLifecycle(emptyList())
    val wallets by viewModel.wallets.collectAsStateWithLifecycle(emptyList())
    val balances by viewModel.balances.collectAsStateWithLifecycle(emptyList())

    val person by viewModel.person.collectAsStateWithLifecycle()
    val curency by viewModel.currency.collectAsStateWithLifecycle()
    val pocket by viewModel.pocket.collectAsStateWithLifecycle()

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    var visibilityConfirm by remember { mutableStateOf(false) }
    var visibilityValidationAmount by remember { mutableStateOf(false) }

    var visibilityList by remember { mutableStateOf(false) }
    var list by remember {
        mutableStateOf(listOf<ModelDomain>())
    }
    if (visibilityList) {
        PopupGetCredit(persons = list, viewModel = viewModel, offset = offset) {
            visibilityList = false
        }
    }

    var amountTransaction by rememberSaveable {
        mutableStateOf("")
    }
    var comment by rememberSaveable {
        mutableStateOf("")
    }
    var message by rememberSaveable {
        mutableStateOf("")
    }
    if (visibilityConfirm) {
        DialogConfirm(clazz = person, message = message, onDismiss = {
            visibilityConfirm = false
        }) { boo ->
            if (boo) {
                viewModel.addTransaction(
                    amountTransaction.trim().toDouble(),
                    comment,
                    balances.sumOf { it.amount * (1 / it.rate) }
                )
                viewModel.back()
            }
            visibilityConfirm = false
        }
    }

    val toolBarHeight = 56.dp
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .fillMaxWidth()
                    .height(toolBarHeight)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.back()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.customColors.textColor
                    )
                }
                MyText(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    text = "Qarz berish",
                    fontSize = 20.sp,
                    color = MaterialTheme.customColors.textColor,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        content = {
            if (pockets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    MyText(
                        text = "Hamyon mavjud emas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (persons.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    MyText(
                        text = "Shaxslar mavjud emas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (currencies.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    MyText(
                        text = "Valyuta mavjud emas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(top = toolBarHeight)
            ) {
                LazyColumn(
                    Modifier
                        .background(MaterialTheme.customColors.backgroundDialog)
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        MyText(
                            text = "Kimga qarz bermoqchisiz",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.customColors.subTextColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }

                    // Person
                    item {
                        ItemEmptyRow(
                            widthBorder = 1.dp,
                            colorBoreder = MaterialTheme.customColors.subTextColor,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            background = MaterialTheme.customColors.backgroundDialog,
                            onItemClicked = { it ->
                                list = persons
                                if (offset == it) {
                                    visibilityList = !visibilityList
                                } else {
                                    offset = it
                                    visibilityList = true
                                }
                            }) {
                            if (person.name.isEmpty() && persons.isNotEmpty()) {
                                viewModel.setPerson(persons[0])
                            }
                            MyText(
                                text = if (person.name.isNotEmpty()) person.name else if (persons.isNotEmpty()) persons[0].name else "Shaxs ismi",
                                color = MaterialTheme.customColors.textColor,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Icon(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                painter = painterResource(id = R.drawable.ic_spinner),
                                contentDescription = "spinner"
                            )
                        }
                    }

                    // Currency
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp), value = amountTransaction,
                                onValueChange = {
                                    amountTransaction = it
                                },
                                label = {
                                    MyText(
                                        text = "Summa kiriting",
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.customColors.subTextColor
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                shape = RoundedCornerShape(15.dp),
                                colors = buttonColors()
                            )
                            EmptyRowSizeble(
                                width = 150.dp,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalPading = 1.dp,
                                horizontalPading = 5.dp,
                                background = MaterialTheme.customColors.backgroundDialog,
                                onItemClicked = {
                                    list = currencies
                                    if (offset == it) {
                                        visibilityList = !visibilityList
                                    } else {
                                        offset = it
                                        visibilityList = true
                                    }
                                }) {
                                if (curency.name.isEmpty() && currencies.isNotEmpty()) {
                                    viewModel.setCurrency(currencies[0])
                                }

                                MyText(
                                    text = if (curency.name.isNotEmpty()) curency.name else if (currencies.isNotEmpty()) currencies[0].name else "Dollar",
                                    color = MaterialTheme.customColors.textColor,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Icon(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    painter = painterResource(id = R.drawable.ic_spinner),
                                    contentDescription = "spinner"
                                )
                            }
                        }
                    }
                    if (visibilityValidationAmount) item {
                        MyText(
                            text = "Summani tekshiring",
                            fontSize = 14.sp,
                            color = MaterialTheme.customColors.errorText,
                        )
                    }
                    // Pocket
                    item {
                        MyText(
                            text = "Qaysi hamyondan",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.customColors.subTextColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                    item {
                        ItemEmptyRow(
                            widthBorder = 1.dp,
                            colorBoreder = MaterialTheme.customColors.subTextColor,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalPading = 1.dp,
                            horizontalPading = 5.dp,
                            background = MaterialTheme.customColors.backgroundDialog,
                            onItemClicked = {
                                list = pockets
                                if (offset == it) {
                                    visibilityList = !visibilityList
                                } else {
                                    offset = it
                                    visibilityList = true
                                }

                            }) {
                            if (pocket.name.isEmpty() && pockets.isNotEmpty()) {
                                viewModel.setPocket(pockets[0])
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                MyText(
                                    text = if (pocket.name.isNotEmpty()) pocket.name else if (pockets.isNotEmpty()) pockets[0].name else "Hamyon nomi",
                                    color = MaterialTheme.customColors.textColor,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                val wallet: WalletDomain? =
                                    wallets.filter {
                                        it.ownerId == viewModel.pocket.value.id && it.currencyId == viewModel.currency.value.id
                                    }.firstOrNull()

                                MyText(
                                    text = if (wallet == null) "0 ${curency.name}" else
                                        "${wallet.balance.huminize()} ${curency.name}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 5.dp),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.customColors.subTextColor
                                )
                            }

                            Icon(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                painter = painterResource(id = R.drawable.ic_spinner),
                                contentDescription = "spinner"
                            )
                        }
                    }

                    // comment
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 200.dp)
                                .padding(5.dp),
                            value = comment,
                            onValueChange = {
                                comment = it
                            },
                            label = {
                                MyText(
                                    text = "Izoh (ixtiyoriy)",
                                    color = MaterialTheme.customColors.subTextColor
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = buttonColors()
                        )
                    }

                    // buttons

                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MyButton(
                                onClick = { viewModel.back() },
                                text = "Bekor",
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gray,
                                    contentColor = White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) { }

                            MyButton(
                                onClick = {
                                    val fromWallet: WalletDomain? =
                                        wallets.filter { it.ownerId == pocket.id && it.currencyId == curency.id }
                                            .firstOrNull()

                                    val (isValid, amount) = isValidAmount(
                                        amountTransaction,
                                        fromWallet
                                    )
                                    if (isValid && person.name.isNotEmpty()) {
                                        visibilityValidationAmount = false
                                        message =
                                            "${person.name}ga $amount ${curency.name} qarz berishga ishonchingiz komilmi?"
                                        visibilityConfirm = true
                                    } else {
                                        visibilityValidationAmount = true
                                    }
                                }, text = "Tasdiq",
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green, contentColor = White
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)

                            ) {}
                        }
                    }

                }
            }


        }
    )
}


private fun isValidAmount(amount: String, wallet: WalletDomain?): Pair<Boolean, Double> {
    var n = -1.0
    var valid = false
    try {
        n = amount.trim().toDouble()
        wallet?.let { valid = it.balance >= n }
    } catch (_: Exception) {
    }
    return Pair(n >= 0 && valid, n)
}

