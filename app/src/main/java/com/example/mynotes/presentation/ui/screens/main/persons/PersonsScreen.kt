package com.example.mynotes.presentation.ui.screens.main.persons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.DialogPocket
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.round
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
    var menu by remember {
        mutableStateOf(0)
    }
    val walletsByOwners by viewModel.walletsByOwners.collectAsStateWithLifecycle(emptyList())
    val persons by viewModel.persons.collectAsStateWithLifecycle(emptyList())
    val wallets by viewModel.wallets.collectAsStateWithLifecycle(emptyList())
    var visibilityAlert by remember {
        mutableStateOf(false)
    }
    var alertMessage by remember {
        mutableStateOf("")
    }
    if (visibilityAlert) {
        DialogAttention(message = alertMessage) {
            visibilityAlert = false
        }
    }
    val list = if (menu == 1)
        persons.filter {
            walletsByOwners.filter { it.currencyBalance.round() > 0 }.map { it.ownerId }
                .contains(it.id)
        }
    else if (menu == 2) persons.filter {
        walletsByOwners.filter { it.currencyBalance.round() < 0 }.map { it.ownerId }
            .contains(it.id)
    }
    else persons

    var visibilityAddDialog by remember {
        mutableStateOf(false)
    }

    var visibilityConfirm by remember {
        mutableStateOf(false)
    }

    var visibilityPopup by remember {
        mutableStateOf(false)
    }
    var offsetPopup by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    if (visibilityPopup) {
        PopupDialog(viewModel.getPerson().name, offsetPopup) { type ->
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
        visibilityPopup = false
        DialogPerson(viewModel, persons) {
            visibilityAddDialog = false
        }
    }

    if (visibilityConfirm) {
        visibilityPopup = false
        DialogConfirm(clazz = viewModel.getPerson(), onDismiss = {
            visibilityConfirm = false
        }) { boo ->
            if (boo) {
                if (wallets.filter { it.ownerId == viewModel.getPerson().id }.size > 0) {
                    alertMessage = "Bu shaxs bilan muomala qilingan. O'chirish mumkin emas !"
                    visibilityAlert = true
                } else {
                    viewModel.delete()
                }
            }
            visibilityConfirm = false
        }
    }

    val toolBarHeight = 94.dp
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
                    MyText(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        text = "Shaxslar",
                        fontSize = 20.sp,
                        color = MaterialTheme.customColors.textColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyButton(
                        onClick = {
                            menu = 0
                        },
                        text = "hamma",
                        textSize = 14.sp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (menu == 0) Green else Gray,
                            contentColor = White
                        )
                    ) {}
                    MyButton(
                        onClick = {
                            menu = 1
                        },
                        text = "qarzdor",
                        textSize = 14.sp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (menu == 1) Green else Gray,
                            contentColor = White
                        )
                    ) {}
                    MyButton(
                        onClick = {
                            menu = 2
                        },
                        text = "haqdor",
                        textSize = 14.sp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (menu == 2) Green else Gray,
                            contentColor = White
                        )
                    ) {}

                }
            }
        },
        bottomBar = {
            ButtonAdd(
                text = "Yangi odam qo'shish", onClicked = {
                    viewModel.person.value = PersonDomain("")
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
                    itemsIndexed(list) { index, owner ->
                        val chips =
                            walletsByOwners.filter { owner.id == it.ownerId && it.currencyBalance.round() != 0.0 }
                        ItemPerson(
                            person = owner,
                            chips = chips,
                            onItemClicked = {
                                viewModel.navigateToPerson(owner)
                                //setPerson(owner)
                            },
                            onMenuMoreClicked = { offset ->
                                viewModel.person.value = owner
                                offsetPopup = offset
                                visibilityPopup =
                                    !visibilityPopup// if (currencyDomain == listCurrency[index]) !visibilityPopup else true
                            },
                            onIconClicked = {
                                alertMessage =
                                    "Bu shaxs serverga saqlanmagan. Internetni yo'qib qaytadan bosing"
                                visibilityAlert = true
                            }
                        )
                    }
                }
            }
        }
    )
}


