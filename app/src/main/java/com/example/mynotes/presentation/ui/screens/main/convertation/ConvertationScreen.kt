package com.example.mynotes.presentation.ui.screens.main.convertation

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
import androidx.compose.ui.draw.rotate
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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class ConvertationScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: ConvertationViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(
    viewModel: ConvertationViewModelImp
) {
    val pockets by viewModel.pockets.collectAsStateWithLifecycle(emptyList())
    val currencies by viewModel.currencies.collectAsStateWithLifecycle(emptyList())
    val wallets by viewModel.wallets.collectAsStateWithLifecycle(emptyList())
    val balances by viewModel.balances.collectAsStateWithLifecycle(emptyList())

    val currencyFrom by viewModel.currencyFrom.collectAsStateWithLifecycle()
    val currencyTo by viewModel.currencyTo.collectAsStateWithLifecycle()
    val currencyConvert by viewModel.currency.collectAsStateWithLifecycle()
    val pocketFrom by viewModel.pocketFrom.collectAsStateWithLifecycle()
    val pocketTo by viewModel.pocketTo.collectAsStateWithLifecycle()

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var type by remember { mutableStateOf(ConvertType.FROM_POCKET) }

    var visibilityConfirm by remember { mutableStateOf(false) }
    var visibilityValidationAmount by remember { mutableStateOf(false) }
    var visibilityList by remember { mutableStateOf(false) }
    var list by remember {
        mutableStateOf(listOf<ModelDomain>())
    }
    if (visibilityList) {
        PopupGetCredit(list = list, viewModel = viewModel, offset = offset, type = type) {
            visibilityList = false
        }
    }

    var amountTransaction by rememberSaveable {
        mutableStateOf("")
    }

    if (visibilityConfirm) {
        DialogConfirm(clazz = pocketTo, onDismiss = {
            visibilityConfirm = false
        }) { boo ->
//            if (boo && pock.isValid()) {
//                 viewModel.delete(pock)
//            }
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
                    text = "Ayirboshlash",
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

                    // pocket from
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
                                type = ConvertType.FROM_POCKET
                                list = pockets
                                if (offset == it) {
                                    visibilityList = !visibilityList
                                } else {
                                    offset = it
                                    visibilityList = true
                                }

                            }) {
                            if (pocketFrom.name.isEmpty() && pockets.isNotEmpty()) {
                                viewModel.setPocketFrom(pockets[0])
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                MyText(
                                    text = if (pocketFrom.name.isNotEmpty()) pocketFrom.name else if (pockets.isNotEmpty()) pockets[0].name else "Hamyon nomi",
                                    color = MaterialTheme.customColors.textColor,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                val wallet: WalletDomain? =
                                    wallets.filter {
                                        it.ownerId == viewModel.pocketFrom.value.id && it.currencyId == viewModel.currencyFrom.value.id
                                    }.firstOrNull()

                                MyText(
                                    text = if (wallet == null) "0 ${currencyFrom.name}" else
                                        "${wallet.balance.huminize()} ${currencyFrom.name}",
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

                    // Currency from
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "Qaysi valyutadan",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.customColors.subTextColor,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                            )
                            EmptyRowSizeble(
                                width = 150.dp,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalPading = 1.dp,
                                horizontalPading = 5.dp,
                                background = MaterialTheme.customColors.backgroundDialog,
                                onItemClicked = {
                                    type = ConvertType.FROM_CURRENCY
                                    list = currencies
                                    if (offset == it) {
                                        visibilityList = !visibilityList
                                    } else {
                                        offset = it
                                        visibilityList = true
                                    }
                                }) {
                                if (currencyFrom.name.isEmpty() && currencies.isNotEmpty()) {
                                    viewModel.setCurrencyFrom(currencies[0])
                                }

                                MyText(
                                    text = if (currencyFrom.name.isNotEmpty()) currencyFrom.name else if (currencies.isNotEmpty()) currencies[0].name else "Dollar",
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


                    item {
                        Icon(
                            modifier = Modifier.rotate(180f),
                            tint = Green,
                            painter = painterResource(id = R.drawable.ic_arrow_circle),
                            contentDescription = "double arrow"
                        )
                    }

                    // Pocket to
                    item {
                        MyText(
                            text = "Qaysi hamyonga",
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
                                type = ConvertType.TO_POCKET
                                list = pockets
                                if (offset == it) {
                                    visibilityList = !visibilityList
                                } else {
                                    offset = it
                                    visibilityList = true
                                }

                            }) {
                            if (pocketTo.name.isEmpty() && pockets.isNotEmpty()) {
                                viewModel.setPocketTo(pockets[0])
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                MyText(
                                    text = if (pocketTo.name.isNotEmpty()) pocketTo.name else if (pockets.isNotEmpty()) pockets[0].name else "Hamyon nomi",
                                    color = MaterialTheme.customColors.textColor,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                val wallet: WalletDomain? =
                                    wallets.filter {
                                        it.ownerId == viewModel.pocketTo.value.id && it.currencyId == viewModel.currencyTo.value.id
                                    }.firstOrNull()

                                MyText(
                                    text = if (wallet == null) "0 ${currencyTo.name}" else
                                        "${wallet.balance.huminize()} ${currencyTo.name}",
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
                    // Currency to
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "Qaysi valyutaga",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.customColors.subTextColor,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                            )
                            EmptyRowSizeble(
                                width = 150.dp,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalPading = 1.dp,
                                horizontalPading = 5.dp,
                                background = MaterialTheme.customColors.backgroundDialog,
                                onItemClicked = {
                                    type = ConvertType.TO_CURRENCY
                                    list = currencies
                                    if (offset == it) {
                                        visibilityList = !visibilityList
                                    } else {
                                        offset = it
                                        visibilityList = true
                                    }
                                }) {
                                if (currencyTo.name.isEmpty() && currencies.isNotEmpty()) {
                                    viewModel.setCurrencyTo(currencies[0])
                                }

                                MyText(
                                    text = if (currencyTo.name.isNotEmpty()) currencyTo.name else if (currencies.isNotEmpty()) currencies[0].name else "Dollar",
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

                    // Currency convertation

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
                                    type = ConvertType.CURRENCY
                                    list =
                                        currencies.filter { it.id == currencyFrom.id || it.id == currencyTo.id }
                                    if (offset == it) {
                                        visibilityList = !visibilityList
                                    } else {
                                        offset = it
                                        visibilityList = true
                                    }
                                }) {
                                if (currencyConvert.name.isEmpty() && currencies.isNotEmpty()) {
                                    viewModel.setCurrency(currencies[0])
                                }

                                MyText(
                                    text = if (currencyConvert.name.isNotEmpty()) currencyConvert.name else if (currencies.isNotEmpty()) currencies[0].name else "Dollar",
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

                    // buttons
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MyButton(
                                onClick = { viewModel.back() },
                                text = "Bekor",
                                textSize = 16.sp,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gray, contentColor = White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) { }

                            MyButton(
                                onClick = {
                                    val fromWallet: WalletDomain? =
                                        wallets.firstOrNull { it.ownerId == pocketFrom.id && it.currencyId == currencyFrom.id }
                                    try {
                                        val n = amountTransaction.trim().toDouble()
                                        fromWallet?.let { wallet ->
                                            val amountDollar = (1 / currencyConvert.rate) * n
                                            val balanceDollar = wallet.balance / currencyFrom.rate
                                            if (balanceDollar >= amountDollar) {
                                                visibilityValidationAmount = false
                                                viewModel.addTransaction(
                                                    n,
                                                    wallet,
                                                    amountDollar,
                                                    "",
                                                    balances.sumOf { it.amount * (1 / it.rate) })
                                                viewModel.back()
                                            } else {
                                                visibilityValidationAmount = true
                                            }
                                        }
                                    } catch (_: Exception) {
                                        visibilityValidationAmount = true

                                    }
                                },
                                text = "Tasdiq",
                                textSize = 16.sp,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green, contentColor = White
                                ),
                                modifier = Modifier
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



