package com.example.mynotes.presentation.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.White
import com.example.mynotes.presentation.utils.components.text.MyText
import javax.inject.Inject

class SplashScreen @Inject constructor() : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SplashViewModelIml = getViewModel()
        ShowSplash()
    }
}

@Composable
private fun ShowSplash() {
    val durationUp: Int = 800
    val durationDown: Int = 100
    val scaleUp: Float = 1.3f
    val scaleDown: Float = 0.5f
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = "splash",
            contentScale = ContentScale.Crop,
        )
        val fontSizeBig = 36.sp
        val fontSizeSmall = 32.sp
        Column() {

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,

                            color = Green,
                            fontSize = fontSizeBig
                        )
                    ) {
                        append("K")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,

                            color = White,
                            fontSize = fontSizeSmall
                        )
                    ) {
                        append("unlik")
                    }
                },
                modifier = Modifier.scale(scale.value)
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,

                            color = Green,
                            fontSize = fontSizeBig
                        )
                    ) {
                        append("H")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,

                            color = White,
                            fontSize = fontSizeSmall
                        )
                    ) {
                        append("isob")
                    }
                },
                modifier = Modifier.scale(scale.value)
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,

                            color = Green,
                            fontSize = fontSizeBig
                        )
                    ) {
                        append("K")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,

                            color = White,
                            fontSize = fontSizeSmall
                        )
                    ) {
                        append("itoblar")
                    }
                },
                modifier = Modifier.scale(scale.value),
            )
        }

    }
}