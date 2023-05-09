package com.example.mynotes.presentation.utils.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mynotes.domain.models.ModelDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.Red
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.types.PopupType

@Composable
fun DialogConfirm(
    clazz: ModelDomain,
    onDismiss: (Boolean, clazz: ModelDomain) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss(false, clazz) }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    MyText(
                        text = clazz.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = MaterialTheme.customColors.textColor
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .height(2.dp)
                            .background(MaterialTheme.customColors.borderColor)
                    )
                    MyText(
                        text = "${clazz.name}ni o'chirishga ishonchingiz komilmi?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.customColors.textColor
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MyButton(
                            text = "YO'Q",
                            onClick = { onDismiss(false, clazz) }) {

                        }
                        MyButton(
                            text = "HA",
                            background = Green,
                            onClick = { onDismiss(true, clazz) }) {
                        }
                    }
                }
            }
        }
    }
}