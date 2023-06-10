package com.example.mynotes.presentation.ui.screens.main.share


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.dialogs.DialogBoxLoading
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

class ShareScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: ShareViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(
    vm: ShareViewModelImp,
) {
    val success by vm.success.collectAsStateWithLifecycle()
    if (!success) {
        DialogBoxLoading()
    }
    var type by remember {
        mutableStateOf(-1)
    }
    when (type) {
        1 -> {
            ShareFile(shareTye = ShareType.HTML)
        }
        2 -> {
            ShareFile(shareTye = ShareType.TXT)
        }
        3 -> {
            ShareFile(shareTye = ShareType.PDF)
        }

    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    vm.back()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.customColors.textColor
                    )
                }
                MyText(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = "Ulashish",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
                Icon(
                    painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_menu_share_mtrl_alpha),
                    contentDescription = "back arrow",
                    tint = MaterialTheme.customColors.textColor
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.customColors.backgroundBrush)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyText(
                text = "Hamma ma'lumotni bir faylga jamlab quyidagi formatlarda jo'natishingiz mumkin",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                MyText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            type = 1
                        }
                        .padding(horizontal = 12.dp),
                    text = "hisob_kitoblar.html",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                MyText(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = "Web brauzerlar uchun",
                    color = MaterialTheme.customColors.subTextColor,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                MyText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            type = 2
                        }
                        .padding(horizontal = 12.dp),
                    text = "hisob_kitoblar.txt",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                MyText(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = "Oddiy text",
                    color = MaterialTheme.customColors.subTextColor,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                MyText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            type = 3
                        }
                        .padding(horizontal = 12.dp),
                    text = "hisob_kitoblar.pdf",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                MyText(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = "Rasmli fayl",
                    color = MaterialTheme.customColors.subTextColor,
                    fontSize = 12.sp
                )
            }


        }
    }
//    when (state.value) {
//        is UiState.Progress -> {
//            DialogBoxLoading()
//        }
//        is UiState.Error -> {
//            message = "Xatolik. Bu email ro'yxatdan o'tgan bo'lishi mumkin"
//            visibilityAlert = true
//        }
//
//    }
}
