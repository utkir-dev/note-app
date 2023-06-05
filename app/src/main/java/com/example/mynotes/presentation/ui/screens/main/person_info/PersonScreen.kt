package com.example.mynotes.presentation.ui.screens.main.person_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemHistory
import com.example.mynotes.presentation.utils.items.ItemHistoryPerson

class PersonScreen(val person: PersonDomain) : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: PersonViewModelImp = getViewModel()
        Show(viewModel, person)
    }
}

@Composable
fun Show(
    viewModel: PersonViewModelImp,
    person: PersonDomain
) {

    val walletsByOwners by viewModel.walletsByOwners.collectAsStateWithLifecycle(emptyList())
    val history by viewModel.history.collectAsStateWithLifecycle(emptyList())
    viewModel.setPerson(person)


    val toolBarHeight = 80.dp
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(toolBarHeight)
            ) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.customColors.backgroundBrush)
                        .fillMaxWidth()
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
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.customColors.textColor
                    )
                    MyText(
                        text = person.name,
                        fontSize = 20.sp,
                        color = MaterialTheme.customColors.textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        content = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .background(MaterialTheme.customColors.backgroundBrush)
                    .padding(top = toolBarHeight)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                LazyColumn(
                    Modifier
                        .background(MaterialTheme.customColors.backgroundBrush)
                        .fillMaxSize()
                        .padding(8.dp),
                    // verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "Qarzlari:",
                                color = MaterialTheme.customColors.textColor
                            )
                            Column {
                                val list = walletsByOwners.filter { it.currencyBalance >= 0.01 }
                                if (list.isEmpty()) {
                                    MyText(
                                        text = "yo'q",
                                        color = MaterialTheme.customColors.textColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    list.forEach {
                                        MyText(
                                            text = "${it.currencyBalance.huminize()} ${it.currencyName}",
                                            color = MaterialTheme.customColors.textColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "Haqlari:",
                                color = MaterialTheme.customColors.textColor
                            )
                            Column {
                                val list = walletsByOwners.filter { it.currencyBalance < 0.0 }
                                if (list.isEmpty()) {
                                    MyText(
                                        text = "yo'q",
                                        color = MaterialTheme.customColors.textColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    list.forEach {
                                        MyText(
                                            text = "${(-it.currencyBalance).huminize()} ${it.currencyName}",
                                            color = MaterialTheme.customColors.textColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "tel raqam:",
                                color = MaterialTheme.customColors.textColor
                            )
                            MyText(
                                text = if (person.phone.isEmpty()) "kiritilmagan" else person.phone,
                                color = MaterialTheme.customColors.textColor
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "manzil:",
                                color = MaterialTheme.customColors.textColor
                            )
                            MyText(
                                text = if (person.address.isEmpty()) "kiritilmagan" else person.address,
                                color = MaterialTheme.customColors.textColor
                            )
                        }
                    }
                    if (history.isNotEmpty()) {
                        item {
                            MyText(
                                text = "Tarixi:",
                                color = MaterialTheme.customColors.textColor
                            )
                        }
                        items(items = history, key = { it.hashCode() }) { historyItem ->
                            ItemHistoryPerson(
                                item = historyItem,
                                onItemClicked = { })
                        }
                    }
                }
            }
        }
    )
}


