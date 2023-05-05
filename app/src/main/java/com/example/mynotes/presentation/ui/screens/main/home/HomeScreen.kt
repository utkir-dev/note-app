package com.example.mynotes.presentation.ui.screens.main.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.image.*
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.theme.ThemeState

class HomeScreen() : AppScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeViewModelImp = getViewModel()
        val dispatcher = viewModel::onEventDispatcher
        ShowHome(dispatcher)
    }
}

@Composable
fun ShowHome(dispatcher: (DirectionType) -> Unit) {
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
                IconButton(onClick = { dispatcher(DirectionType.SIGNOUT) }) {
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
            MenuBig("Balans: 5000 $", dispatcher)
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MenuBig("Kirim", dispatcher, 0.5f)
                MenuBig("Chiqim", dispatcher)
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
                modifier = Modifier.fillMaxWidth(),
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
                MenuBig("Hamyonlar", dispatcher, 0.5f)
                MenuBig("Valyutalar", dispatcher)
            }
        }
        item {
            Text(
                text = "Tarix :  ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp),

                textAlign = TextAlign.Start
            )
        }
        HistoryList()
        item {
            Text(
                text = "Hammasini ko'rish  >>",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp)
                    .clickable {

                    },

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
                dispatcher(DirectionType.BALANCE)
            },
        // shape = RoundedCornerShape(15.dp),
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

fun LazyListScope.HistoryList() {
    items(10) {
        Text(
            text = "12.02.2023   Ravshanga   -100$",
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.End
        )
    }
}
