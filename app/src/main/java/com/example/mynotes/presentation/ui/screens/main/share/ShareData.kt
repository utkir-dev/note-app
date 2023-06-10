package com.example.mynotes.presentation.ui.screens.main.share

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mynotes.BuildConfig
import com.example.mynotes.contstants.FILE_NAME
import com.example.mynotes.contstants.FOLDER_NAME
import com.example.mynotes.presentation.MainActivity
import java.io.*


@Composable
fun ShareFile(shareTye: ShareType) {
    val context = LocalContext.current as MainActivity
    val folder: File? = context.getExternalFilesDir(FOLDER_NAME)
    var type = ""
    var extension = ""
    when (shareTye) {
        ShareType.PDF -> {
            type = "application/pdf"; extension = "pdf"
        }
        ShareType.HTML -> {
            type = "text/html"; extension = "html"
        }
        ShareType.TXT -> {
            type = "text/plain"; extension = "txt"
        }
        ShareType.JSON -> {
            type = "application/json"; extension = "json"
        }
    }
    val file = File(folder, "$FILE_NAME.$extension")
    val extraTitle = "title" // or url =>  http://www....
    val shareWith = "ShareWith"
    val uri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    //  intent.putExtra(Intent.EXTRA_TEXT, extraTitle)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, shareWith),
        null
    )
}

