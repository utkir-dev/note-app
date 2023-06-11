package com.example.mynotes.presentation.ui.screens.home

import android.Manifest
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.MainActivity
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.Red
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.connection.NetworkState
import com.example.mynotes.presentation.utils.connection.networkState
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemEmptyRow
import com.example.mynotes.presentation.utils.items.ItemHistory
import com.example.mynotes.presentation.utils.theme.ThemeState
import kotlinx.coroutines.*

class HomeScreen() : AppScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeViewModelImp = getViewModel()

        viewModel.observeDevice()
        ShowDrawer(viewModel)
    }
}

@Composable
fun ShowDrawer(viewModel: HomeViewModelImp) {
    val dispatcher = viewModel::onEventDispatcher
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val conf = LocalConfiguration.current

    var pressed by remember {
        mutableStateOf(0)
    }
    var visibilityDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    when (pressed) {
        1 -> {}
        2 -> {
            dispatcher.invoke(DirectionType.SETTINGS)
        }
        3 -> {
            dispatcher.invoke(DirectionType.SHARE)
        }
    }
    if (visibilityDialog) {
        DialogConfirm(
            CurrencyDomain("", name = "☝ Diqqat !!!"),
            message = "Akkauntdan chiqib ketmoqchimisiz?. Qaytib kirish uchun login va parollaringiz esingizdami ? Ma'lumotlar serverga saqlandimi ? ",
            onDismiss = {
                visibilityDialog = false
            }) {
            if (true) {
                dispatcher.invoke(DirectionType.SIGNOUT)
            }
            visibilityDialog = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.customColors.backgroundBrush)
                    .fillMaxHeight()
                    .fillMaxWidth(
                        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) 0f else
                            0.7f
                    )
            ) {
                DrawerHeader()
                DrawerMenuItem(
                    iconDrawableId = R.drawable.ic_home,
                    text = "Asosiy oyna",
                    onItemClick = {
                        scope.launch {
                            async {
                                pressed = 1
                                drawerState.close()

                            }

                        }

                    }
                )
                DrawerMenuItem(
                    iconDrawableId = R.drawable.ic_settings,
                    text = "Sozlashlar",
                    onItemClick = {
                        scope.launch {
                            async {
                                drawerState.close()

                            }
                            async { pressed = 2 }
                        }
                    }
                )

                DrawerMenuItem(
                    iconDrawableId = androidx.appcompat.R.drawable.abc_ic_menu_share_mtrl_alpha,
                    text = "Ulashish",
                    onItemClick = {
                        scope.launch {
                            async {
                                drawerState.close()

                            }
                            async { pressed = 3 }

                        }
                    }
                )
                DrawerMenuItem(
                    iconDrawableId = R.drawable.ic_exit,
                    text = "Chiqish",
                    color = Red,
                    rotate = true,
                    onItemClick = {
                        visibilityDialog = true

                    }
                )
            }
        },
        content = {
            ShowHome(viewModel, drawerState, scope)
        }
    )
}

@Composable
fun ShowHome(viewModel: HomeViewModelImp, drawerState: DrawerState, scope: CoroutineScope) {
    val context = LocalContext.current
    val network by remember {
        mutableStateOf(context.networkState)
    }
    if (network == NetworkState.AVAILABLE) {
        viewModel.checkData()
    }


    val dispatcher = viewModel::onEventDispatcher
    val balanceList by viewModel.balances.collectAsStateWithLifecycle(emptyList())
    val history by viewModel.history.collectAsStateWithLifecycle(emptyList())
    // val dialogProgress = viewModel.isLoading.collectAsStateWithLifecycle(false)
    var visibilityAlert by remember {
        mutableStateOf(false)
    }

    if (visibilityAlert) {
        DialogAttention("Bu ma'lumot serverga saqlanmagan. Internetni yo'qib qaytadan bosing !") {
            visibilityAlert = false
        }
    }
//    if (!dialogProgress.value && !firstShown && (network == NetworkState.AVAILABLE)) {
//        DialogBoxLoading()
//    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MaterialTheme.customColors.backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    // BlockScreen()
                    //dispatcher(DirectionType.SIGNOUT)
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "menu icon",
                        tint = MaterialTheme.customColors.iconColor
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                MyText(
                    text = "Asosiy oyna",
                    color = MaterialTheme.customColors.textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                IconButton(onClick = {
                    dispatcher(DirectionType.CHANGE_NIGHT_MODE)
                    ThemeState.darkModeState.value = !ThemeState.darkModeState.value
                }) {
                    Icon(
                        if (ThemeState.darkModeState.value) painterResource(id = R.drawable.ic_day_mode)
                        else painterResource(id = R.drawable.ic_night_mode),
                        contentDescription = "menu icon",
                        tint = MaterialTheme.customColors.iconColor
                    )
                }
            }
        }
        item {
            MenuBig(
                "Balans: ${balanceList.sumOf { it.amount * (1 / it.rate) }.huminize()} $",
                dispatcher
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig("Kirim", dispatcher, 0.5f, directionType = DirectionType.INCOME)
                MenuBig("Chiqim", dispatcher, directionType = DirectionType.OUTCOME)
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig("Qarz olish", dispatcher, 0.5f, directionType = DirectionType.GETCREDIT)
                MenuBig("Qarz berish", dispatcher, directionType = DirectionType.GIVECREDIT)
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig(
                    "Ayirboshlash",
                    dispatcher,
                    0.5f,
                    directionType = DirectionType.CONVERTATION
                )
                MenuBig("Shaxslar", dispatcher, directionType = DirectionType.PERSONS)
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig(
                    "Hamyonlar",
                    dispatcher,
                    widthPercent = 0.5f,
                    directionType = DirectionType.POCKETS
                )
                MenuBig("Valyutalar", dispatcher, directionType = DirectionType.CURRENCIES)
            }
        }
        if (history.isNotEmpty()) {
            item {
                MyText(
                    text = "Tarix :  ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(horizontal = 10.dp),
                    color = MaterialTheme.customColors.textColor,
                    textAlign = TextAlign.Start
                )
            }
            items(items = history, key = { it.hashCode() }) { historyItem ->
                ItemHistory(item = historyItem) {
                    visibilityAlert = true
                    viewModel.checkNotUploads()
                }
            }
            item {
                MyText(
                    text = "Hammasini ko'rish  >>",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable {
                            dispatcher(DirectionType.HISTORY)
                        },
                    color = MaterialTheme.customColors.textColor,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun MenuBig(
    text: String,
    dispatcher: (DirectionType) -> Unit,
    widthPercent: Float = 1f,
    verticalPadding: Dp = 10.dp,
    horizontalPadding: Dp = 10.dp,
    directionType: DirectionType = DirectionType.BALANCE,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(widthPercent)
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.customColors.borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                dispatcher(directionType)
            },
        shadowElevation = 6.dp,
        color = MaterialTheme.customColors.backgroundItem
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding)
                .padding(vertical = verticalPadding),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = MaterialTheme.customColors.textColor
        )

    }
}


@Composable
fun DrawerHeader() {
    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .height(200.dp),
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = "header",
            contentScale = ContentScale.FillBounds,
            alpha = 0.7f
        )
        val fontSizeBig = 22.sp
        val fontSizeSmall = 18.sp
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = Green,
                                fontSize = fontSizeBig
                            )
                        ) {
                            append("K")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,

                                color = White,
                                fontSize = fontSizeSmall
                            )
                        ) {
                            append("unlik")
                        }
                    },
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,

                                color = Green,
                                fontSize = fontSizeBig
                            )
                        ) {
                            append("H")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,

                                color = White,
                                fontSize = fontSizeSmall
                            )
                        ) {
                            append("isob")
                        }
                    },
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,

                                color = Green,
                                fontSize = fontSizeBig
                            )
                        ) {
                            append("K")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,

                                color = White,
                                fontSize = fontSizeSmall
                            )
                        ) {
                            append("itoblar")
                        }
                    },
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_header),
                contentDescription = "icon",
                modifier = Modifier
                    .scale(1.5f)
                    .padding(5.dp)
            )
        }
    }
}


@Composable
private fun DrawerMenuItem(
    iconDrawableId: Int,
    text: String,
    weight: FontWeight = FontWeight.Medium,
    color: Color = MaterialTheme.customColors.textColor,
    rotate: Boolean = false,
    onItemClick: () -> Unit
) {
    ItemEmptyRow(onItemClicked = {
        onItemClick()
    }) {
        Icon(
            modifier = Modifier.rotate(if (rotate) 180f else 0f),
            painter = painterResource(iconDrawableId),
            contentDescription = null,
            tint = MaterialTheme.customColors.subTextColor
        )
        MyText(
            text = text,
            color = color,
            fontWeight = weight,
            modifier = Modifier.padding(start = 5.dp),
            fontSize = 17.sp
        )
    }
}

