package com.example.mynotes.presentation.ui.screens.main.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemInOutPocket

class IncomeScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: IncomeViewModelImp = getViewModel()
        ShowIncomes(viewModel)
    }
}

@Composable
fun ShowIncomes(
    viewModel: IncomeViewModelImp
) {
    val pockets by viewModel.pockets.collectAsStateWithLifecycle(emptyList())
    val pocketItems by viewModel.mapPocket.collectAsStateWithLifecycle(emptyMap())

    var visibilityAddDialog by remember {
        mutableStateOf(false)
    }
    var visibilityIncomeDialog by remember {
        mutableStateOf(false)
    }
    var visibilityConfirm by remember {
        mutableStateOf(false)
    }
    var pocket by remember { viewModel.pocket }

    if (visibilityAddDialog) {
        DialogPocket(pocket) { cur ->
            cur?.let { pocket = it }
            if (pocket.isValid()) {
                viewModel.add(pocket)
            }
            visibilityAddDialog = false
        }
    }

    if (visibilityIncomeDialog) {
        DialogIncome(viewModel) {
            visibilityIncomeDialog = false
        }
    }
    if (visibilityConfirm) {
        DialogConfirm(clazz = pocket) { boo, clazz ->
            val pock = clazz as PocketDomain
            if (boo && pock.isValid()) {
                //  viewModel.delete(pock)
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
                    .background(MaterialTheme.customColors.backgroundBrush)
                    .fillMaxWidth()
                    .height(toolBarHeight)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                    modifier = Modifier.weight(1.0f),
                    text = "Kirim",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.customColors.textColor,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = toolBarHeight)
                    .background(brush = MaterialTheme.customColors.backgroundBrush)
            ) {
                item {
                    MyText(
                        text = if (pockets.isEmpty()) "Hamyon mavjud emas" else "Qaysi hamyonga kirim qilmoqchisiz",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.customColors.subTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
//                itemsIndexed(pockets) { index, value ->
//                    ItemInOutPocket(text = value.name, onItemClicked = {
//                        viewModel.setPocket(list[index])
//                        visibilityIncomeDialog = true
//                    })
//                }

                itemsIndexed(pockets) { index, pocket ->
                    val chips = pocketItems[pocket.id]?.wallets?.filter { it.balance >= 0.01 }
                        ?: emptyList()
                    ItemInOutPocket(
                        text = pocket.name,
                        chipsVisibility = true,
                        chips = chips,
                        onItemClicked = {
                            viewModel.setPocket(pockets[index])
                            visibilityIncomeDialog = true
                        })
                }


                item {
                    ButtonAdd(text = "Yangi hamyon qo'shish", onClicked = {
                        pocket = PocketDomain("")
                        visibilityAddDialog = true
                    })
                }
            }
        }
    )

}