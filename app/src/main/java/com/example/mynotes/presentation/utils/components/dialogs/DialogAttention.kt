package com.example.mynotes.presentation.utils.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    scope.launch {
        startTimer()
            .stateIn(this, SharingStarted.WhileSubscribed(3000), 0)
            .collect {
                onDismiss()
            }
    }
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
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
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .height(2.dp)
                            .background(MaterialTheme.customColors.borderColor)
                    )
                    MyText(
                        text = message,
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
                        horizontalArrangement = Arrangement.End
                    ) {

                        MyButton(
                            text = "OK",
                            background = Green,
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

fun startTimer() = flow {
    for (i in 1..5) {
        delay(1000)
        emit(i)
    }
}