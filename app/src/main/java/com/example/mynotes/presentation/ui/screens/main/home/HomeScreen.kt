package com.example.mynotes.presentation.ui.screens.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.ui.screens.block.BlockScreen
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemHistory
import com.example.mynotes.presentation.utils.theme.ThemeState
import kotlinx.coroutines.launch

class HomeScreen() : AppScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeViewModelImp = getViewModel()
        ShowDrawer(viewModel)
    }
}

@Composable
fun ShowDrawer(viewModel: HomeViewModelImp) {

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var width by remember {
        mutableStateOf(0f)
    }
    if (drawerState.isClosed) width = 0f else width = 0.7f
    // deal, click, drama, cross, have, home, beyond, remind, flat, stand, buffalo, garage
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxHeight()
                    .fillMaxWidth(width)
            ) {
                DrawerHeader()
                DrawerMenuItem(
                    iconDrawableId = R.drawable.ic_home,
                    text = "Home",
                    onItemClick = {
                        scope.launch { drawerState.close() }

                    }
                )
                DrawerMenuItem(
                    iconDrawableId = R.drawable.ic_settings,
                    text = "Settings",
                    onItemClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        content = {
            ShowHome(viewModel)
        }
    )
}

@Composable
fun ShowHome(viewModel: HomeViewModelImp) {
    val dispatcher = viewModel::onEventDispatcher
    val balance by viewModel.balance.collectAsStateWithLifecycle(0.0)
    val historyList by viewModel.historyList.collectAsStateWithLifecycle(emptyList())
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
                    BlockScreen()
                    //dispatcher(DirectionType.SIGNOUT)
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
            MenuBig("Balans: ${balance.huminize()} $", dispatcher)
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
                MenuBig("Qarz olish", dispatcher, 0.5f)
                MenuBig("Qarz berish", dispatcher)
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig("Qarzdorlar", dispatcher, 0.5f)
                MenuBig("Haqdorlar", dispatcher)
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

        items(items = historyList, key = { it.hashCode() }) { historyItem ->
            ItemHistory(
                item = historyItem,
                onItemClicked = { })
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Header",
            fontSize = 40.sp,
            color = MaterialTheme.customColors.textColor
        )
    }
}


@Composable
private fun DrawerMenuItem(
    iconDrawableId: Int,
    text: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconDrawableId),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}

