package com.example.mynotes.presentation.utils.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.mynotes.presentation.utils.components.image.Gray
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.text.MyText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Button",
    textSize: TextUnit = 18.sp,
    background: Color = Gray,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = background,
        contentColor = White
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,

    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    animationDuration: Int = 100,
    scaleUp: Float = 1.1f,
    content: @Composable RowScope.() -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val scale = remember {
        Animatable(1f)
    }

    Button(
        onClick = {
            coroutineScope.launch(Dispatchers.Main) {
                scale.animateTo(
                    scaleUp,
                    animationSpec = tween(animationDuration),
                )
                scale.animateTo(
                    1f,
                    animationSpec = tween(animationDuration),
                )
            }
            onClick()
        },
        modifier = modifier
            .scale(
                scale = scale.value
            ),
//  ,          .clickable(interactionSource = interactionSource, indication = null) {
////
////
////            }
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        MyText(text = text, fontSize = textSize)
        content()
    }

}

