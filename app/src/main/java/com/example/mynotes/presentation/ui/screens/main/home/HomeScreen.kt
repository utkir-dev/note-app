package com.example.mynotes.presentation.ui.screens.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.text.MyText

class HomeScreen() : AppScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeViewModelImp = getViewModel()
        val dispatcher = viewModel::onEventDispatcher
        ShowHome(dispatcher)
    }
}

fun LazyListScope.HistoryList(

) {
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

@Composable
fun ShowHome(dispatcher: (DirectionType) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Button(onClick = {
                dispatcher(DirectionType.SIGNOUT)
            }) {
                MyText(text = "Exit")
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
            .clickable {
                dispatcher(DirectionType.BALANCE)
            },
        // shape = RoundedCornerShape(15.dp),
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding)
                .padding(vertical = verticalPadding),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

    }
}

