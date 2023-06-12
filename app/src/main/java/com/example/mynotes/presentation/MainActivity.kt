package com.example.mynotes.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.mynotes.contstants.*
import com.example.mynotes.domain.background_task.CheckDatabase
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.shared_pref_use_case.SharedPrefUseCases
import com.example.mynotes.presentation.ui.dispatcher.NavigationHandler
import com.example.mynotes.presentation.ui.screens.splash.SplashScreen
import com.example.mynotes.presentation.utils.components.buttons.AnimatedTextButton
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.image.*
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.theme.ThemeState
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationHandler: NavigationHandler

    @Inject
    lateinit var shared: SharedPrefUseCases


    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createPeriodicWorkRequest()
        runBlocking {
            ThemeState.darkModeState.value = shared.getBoolean.invoke(KEY_NIGHT_MODE)

        }
//        ThemeState.darkModeState.value = when (AppCompatDelegate.getDefaultNightMode()) {
//            AppCompatDelegate.MODE_NIGHT_YES -> true
//            else -> false
//        }
        val apiVersion = Build.VERSION.SDK_INT

        val permission = MutableStateFlow(false)
        if (apiVersion < Build.VERSION_CODES.Q) {
            requestPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                permission.value = it
            }
        }

        setContent {
            val isPermitted by permission.collectAsStateWithLifecycle()

            if ((apiVersion < Build.VERSION_CODES.Q && isPermitted) || apiVersion >= Build.VERSION_CODES.Q) {
                MyNotesTheme(darkTheme = ThemeState.darkModeState.value) {
                    val opened by isOpen.collectAsState()
                    if (!opened) {
                        Block()
                    }
                    Navigator(SplashScreen()) { navigator ->
                        LaunchedEffect(key1 = navigator) {
                            navigationHandler.navigationBuffer.onEach {
                                it.invoke(navigator)
                            }.collect()
                        }
                        CurrentScreen()
                    }

//                val modifier = if (opened) Modifier.fillMaxSize() else Modifier.size(0.dp)
//                Box(modifier = modifier) {
//
//                }
                }
            } else {
                ShowSettings()
            }
        }
    }

    private fun createPeriodicWorkRequest() {
        val builder = Data.Builder()
        builder.putString(KEY_WORK_MANAGER_ID, "11111")
        val data = builder.build()

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val checker = PeriodicWorkRequestBuilder<CheckDatabase>(
            60, TimeUnit.MINUTES
        ).setInputData(data).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "periodicDatabaseChecker", ExistingPeriodicWorkPolicy.KEEP, checker
        )
    }


    override fun onStart() {
        super.onStart()
        val diff = System.currentTimeMillis() - TIME_OUT
        if (diff <= DEFAULT_BLOCK_SCREEN_TIME && OPEN) {
            isOpen.value = true
        } else {
            OPEN = false
            isOpen.value = false
        }
    }
    override fun onStop() {
        super.onStop()
        TIME_OUT = System.currentTimeMillis()
    }

    fun requestPermissions(vararg permissions: String, function: (Boolean) -> Unit) {
        Dexter.withContext(this)
            .withPermissions(
                *permissions
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            function(true)
                        } else {
                            showSettingsDialog(*permissions)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(this, "Xatolik", Toast.LENGTH_SHORT).show()
            }
            .check()
    }

    private fun showSettingsDialog(vararg permissions: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ruxsat kerak !")
        builder.setMessage("${getPermissionNames(*permissions)}larga ruxsat talab etiladi!")
        builder.setPositiveButton("Sozlamalar") { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Bekor") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun getPermissionNames(vararg permissions: String): String {
        var names = ""
        permissions.forEach {
            when (it) {
                Manifest.permission.CAMERA -> names = "kamera, $names"
                Manifest.permission.REQUEST_INSTALL_PACKAGES -> names = "dastur o'rnatish, $names"
                Manifest.permission.READ_EXTERNAL_STORAGE -> names = "xotiradan o'qish, $names"
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> names = "xotiraga yozish, $names"
                Manifest.permission.ACCESS_COARSE_LOCATION -> names = "geo lokaciya, $names"
                Manifest.permission.ACCESS_FINE_LOCATION -> names = "geo lokaciya, $names"
            }
        }
        return if (names.length > 2) names.trim().substring(0, names.length - 2) else ""
    }

    companion object {
        var TIME_OUT = 0L
        var OPEN = false
        val isOpen = MutableStateFlow(false)
    }

}

@Composable
fun ShowSettings() {
    val context = LocalContext.current as MainActivity
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(5.dp)) {
            MyText(
                text = "Ruxsat kerak!",
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
                text = "Ma'lumotlarni faylga saqlash va ularni o'qish uchun xotiraga ruxsat kerak",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                color = MaterialTheme.customColors.textColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MyButton(
                    text = "Bekor",
                    textSize = 16.sp,
                    onClick = {
                        context.finish()

                    }) {

                }
                MyButton(
                    text = "Sozlamalar",
                    background = Green,
                    textSize = 16.sp,
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)
                    })
                {
                }
            }
        }

    }
}

@Composable
fun Block() {
    val activity = LocalContext.current as MainActivity
    var pinCode =
        runBlocking {
            activity.shared.getString.invoke(KEY_PINCODE)
        }
    if ((pinCode ?: "").isEmpty()) {
        pinCode = DEFAULT_PINCODE
    }
    var check by remember {
        mutableStateOf(false)
    }
    if (check) {
        runBlocking {
            MainActivity.OPEN = true
            MainActivity.isOpen.value = true
        }
    }

    Dialog(
        onDismissRequest = {
            // onDismiss()
        }, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var typedText by remember { mutableStateOf("") }
            val countPin = 4
            val fontSize1 = 32.sp
            val fontSize2 = 28.sp
            val fontSize3 = 24.sp
            val color = Color.White

//            val infiniteTransition = rememberInfiniteTransition()
//            val colorBackground by infiniteTransition.animateColor(
//                initialValue = Purple, targetValue = Purple2, animationSpec = infiniteRepeatable(
//                    animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
//                )
//            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Purple),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MyText(
                            text = "PIN-code:",
                            color = color,
                            fontSize = fontSize3,
                        )

                        Spacer(modifier = Modifier.padding(16.dp))
                    }
                }
                item {
                    LazyRow {
                        for (i in 1..countPin) {
                            item {
                                PinBox(typedText, i)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
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

                            AnimatedTextButton(
                                text = " 1 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 2 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 3 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTextButton(
                                text = " 4 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 5 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 6 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTextButton(
                                text = " 7 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 8 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = " 9 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTextButton(
                                text = "<=", fontSize = fontSize1
                            ) {
                                if (typedText.length > 0) {
                                    typedText = typedText.substring(0, typedText.length - 1)
                                }
                            }
                            AnimatedTextButton(
                                text = " 0 ", fontSize = fontSize1
                            ) {
                                if (typedText.length < countPin) {
                                    typedText = "$typedText$it"
                                }
                            }
                            AnimatedTextButton(
                                text = "OK", fontSize = fontSize2
                            ) {
                                if (typedText == pinCode) {
                                    check = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinBox(text: String, i: Int) {
    val durationUp: Int = 50
    val scaleUp: Float = 1.5f
    val scaleDown: Float = 0.2f
    val scale = remember {
        Animatable(1f)
    }
    if (text.length >= i) {
        LaunchedEffect(key1 = scale) {
            scale.animateTo(
                scaleUp,
                animationSpec = tween(durationUp),
            )
            scale.animateTo(
                1f,
                animationSpec = tween(durationUp),
            )
        }
    } else {
        LaunchedEffect(key1 = scale) {
            scale.animateTo(
                scaleDown,
                animationSpec = tween(durationUp),
            )
            scale.animateTo(
                1f,
                animationSpec = tween(durationUp),
            )
        }
    }
    Box(
        modifier = Modifier
            .scale(scale.value)
            .padding(7.dp)
            .size(22.dp)
            .clip(CircleShape)
            .background(if (text.length >= i) White else Color.Transparent)
            .border(2.dp, Color.White, CircleShape)
    )
}