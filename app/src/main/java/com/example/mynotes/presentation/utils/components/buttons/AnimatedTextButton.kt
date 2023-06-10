package com.example.mynotes.presentation.utils.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.text.MyText
import kotlinx.coroutines.launch

@Composable
fun AnimatedTextButton(
    text: String = "",
    fontSize: TextUnit = 32.sp,
    color: Color = White,
    duration: Int = 40,
    animateToSelfSize: Float = 1.0f,
    animateUpScale: Float = 1.3f,
    animateDownScale: Float = 0.8f,
    onClicked: (String) -> Unit
) {
    val interactionSource = MutableInteractionSource()

    val coroutineScope = rememberCoroutineScope()

    var enabled by remember {
        mutableStateOf(false)
    }

    val scale = remember {
        Animatable(1f)
    }
    MyText(
        modifier = Modifier
            .scale(scale = scale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = true,
                    radius = 250.dp,
                    color = Color.Green
                )
            ) {
                onClicked(text.trim())
                enabled = !enabled
                coroutineScope.launch {
                    scale.animateTo(
                        animateUpScale,
                        animationSpec = tween(duration),
                    )
//                    scale.animateTo(
//                        animateDownScale,
//                        animationSpec = tween(duration),
//                    )
                    scale.animateTo(
                        animateToSelfSize,
                        animationSpec = tween(duration),
                    )

                }
            },
        text = text,
        fontSize = fontSize,
        color = color
    )

}
