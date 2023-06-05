package com.example.mynotes.presentation.utils.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotes.domain.models.ModelDomain
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.contstants.ALERT_DIALOG_TIME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DialogAttention(
    message: String = "",
    time: Int = ALERT_DIALOG_TIME,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val durationUp: Int = 200
    val durationDown: Int = 100
    val scaleUp: Float = 1.02f
    val scaleDown: Float = 0.9f
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
    scope.launch {
        startTimer(time)
            .collect {
                if (it == time) {
                    onDismiss()
                }
            }
    }
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .scale(scale.value)
                .padding(8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.customColors.backgroundDialog)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    MyText(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = MaterialTheme.customColors.textColor
                    )
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 3.dp)
//                            .height(2.dp)
//                            .background(MaterialTheme.customColors.subTextColor)
//                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {

                        MyButton(
                            text = "OK",
                            background = Green,
                            textSize = 16.sp,
                            onClick = {
                                onDismiss()
                            })
                        {
                        }
                    }
                }
            }
        }
    }
}

fun startTimer(time: Int) = flow {
    for (i in 1..time) {
        delay(1000)
        emit(i)
    }
}