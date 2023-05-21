package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemInOutPocket
import com.example.mynotes.presentation.utils.items.ItemPerson
import com.example.mynotes.presentation.utils.types.PopupType

class PersonsScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: PersonsViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(
    viewModel: PersonsViewModelImp
) {
    val persons by viewModel.persons.collectAsStateWithLifecycle(emptyList())

    val personItems by viewModel.mapPerson.collectAsStateWithLifecycle(emptyMap())

    var visibilityAddDialog by remember {
        mutableStateOf(false)
    }
    var visibilityIncomeDialog by remember {
        mutableStateOf(false)
    }
    var visibilityConfirm by remember {
        mutableStateOf(false)
    }
    var currentPerson by remember { viewModel.person }

    var visibilityPopup by remember {
        mutableStateOf(false)
    }
    var offsetPopup by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    if (visibilityPopup) {
        PopupDialog(currentPerson.name, offsetPopup) { type ->
            when (type) {
                PopupType.EDIT -> {
                    visibilityAddDialog = true
                }
                PopupType.DELETE -> {
                    visibilityConfirm = true
                }
                PopupType.CANCEL -> {
                    visibilityPopup = false
                }
            }
            visibilityPopup = false
        }
    }
    if (visibilityAddDialog) {
        DialogPerson(currentPerson) { cur ->
            cur?.let { currentPerson = it }
            if (currentPerson.isValid()) {
                viewModel.add(currentPerson)
            }
            visibilityAddDialog = false
        }
    }

    if (visibilityConfirm) {
        DialogConfirm(clazz = currentPerson) { boo, clazz ->
            val pock = clazz as PocketDomain
            if (boo && pock.isValid()) {
                // viewModel.delete(pock)
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
                    text = "Shaxslar",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.customColors.textColor,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            ButtonAdd(
                text = "Yangi odam qo'shish", onClicked = {
                    currentPerson = PersonDomain("")
                    visibilityAddDialog = true
                })
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "hammasi",
                                color = MaterialTheme.customColors.subTextColor,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                            )

                            MyText(
                                text = "qarzdorlar",
                                color = MaterialTheme.customColors.subTextColor,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                            )
                            MyText(
                                text = "haqdorlar",
                                color = MaterialTheme.customColors.subTextColor,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                            )

                        }
                    }
                    itemsIndexed(persons) { index, person ->
                        val chips = personItems[person.id]?.wallets?.filter { it.balance >= 0.01 }
                            ?: emptyList()
                        ItemPerson(
                            person = person,
                            onItemClicked = {
                                viewModel.setPerson(persons[index])
                                visibilityIncomeDialog = true
                            },
                            onMenuMoreClicked = { offset ->
                                currentPerson = person
                                offsetPopup = offset
                                visibilityPopup =
                                    !visibilityPopup// if (currencyDomain == listCurrency[index]) !visibilityPopup else true

                            }
                        )
                    }
                }
            }

        }
    )

}


private fun isValidAmount(amount: String): Pair<Boolean, Double> {
    var n = -1.0
    try {
        n = amount.trim().toDouble()
    } catch (_: Exception) {
    }
    return Pair(n > 0, n)
}

