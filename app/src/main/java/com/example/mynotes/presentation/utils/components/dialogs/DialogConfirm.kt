package com.example.mynotes.presentation.utils.components.dialogs

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mynotes.domain.models.ModelDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText


@Composable
fun DialogConfirm(
    clazz: ModelDomain,
    message: String = "",
    onDismiss: () -> Unit,
    buttonAction: (Boolean) -> Unit
) {
    val durationUp: Int = 250
    val durationDown: Int = 100
    val scaleUp: Float = 1.02f
    val scaleDown: Float = 0.8f
    val scale = remember {
        Animatable(1f)
    }
    LaunchedEffect(key1 = scale) {
        scale.animateTo(
            scaleDown,
            animationSpec = tween(durationDown),
        )
        scale.animateTo(
            scaleUp,
            animationSpec = tween(durationUp),
        )
        scale.animateTo(
            1f,
            animationSpec = tween(durationUp),
        )
    }

    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .scale(
                    scale = scale.value
                )
                .padding(8.dp)

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
                        text = if (message.isNotEmpty()) message else "${clazz.name}ni o'chirishga ishonchingiz komilmi?",
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
                            textSize = 16.sp,
                            onClick = {
                                onDismiss()

                            }) {

                        }
                        MyButton(
                            text = "HA",
                            background = Green,
                            textSize = 16.sp,
                            onClick = {
                                buttonAction(true)
                            })
                        {
                        }
                    }
                }
            }
        }
    }
}