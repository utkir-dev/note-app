package com.example.mynotes.presentation.ui.screens.block

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.ui.screens.splash.SplashViewModelIml
import com.example.mynotes.presentation.utils.components.text.MyText
import javax.inject.Inject

class BlockScreen @Inject constructor() : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SplashViewModelIml = getViewModel()
        Block()
    }
}

@Composable
fun Block() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MyText(
                    text = "PIN-code:",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Surface(
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = Color.White
                            )

                    ) { }
                    Surface(
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = Color.White
                            )

                    ) { }
                    Surface(
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = Color.White
                            )
                            .background(Color.Blue)

                    ) { }
                    Surface(
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = Color.White
                            )

                    ) { }
                }


            }

        }

        item {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                    }) { MyText(text = "1", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "2", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "3", fontSize = 32.sp, color = Color.White) }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                    }) { MyText(text = "4", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "5", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "6", fontSize = 32.sp, color = Color.White) }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                    }) { MyText(text = "7", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "8", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "9", fontSize = 32.sp, color = Color.White) }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                    }) { MyText(text = "<=", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "0", fontSize = 32.sp, color = Color.White) }
                    TextButton(onClick = {
                    }) { MyText(text = "Ok", fontSize = 32.sp, color = Color.White) }

                }
            }

        }
    }

}