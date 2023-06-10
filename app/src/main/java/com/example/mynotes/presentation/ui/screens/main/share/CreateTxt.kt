package com.example.mynotes.presentation.ui.screens.main.share


import android.content.Context
import com.example.mynotes.contstants.FILE_NAME
import com.example.mynotes.contstants.FOLDER_NAME
import com.example.mynotes.presentation.utils.extensions.huminizeForFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset


fun CreateTxt(
    context: Context,
    mapData: LinkedHashMap<String, LinkedHashMap<String, List<String>>>
): Boolean {
    var result = false
    val builder = StringBuilder()
    builder.append("${System.currentTimeMillis().huminizeForFile()}\nsanadagi ma'lumotlar\n\n")
    mapData.forEach { mainItem ->
        builder.append("\n-----  ${mainItem.key}  -----\n")
        mainItem.value.forEach { subItem ->
            builder.append("\n\n${subItem.key}\n")
            subItem.value.forEach {
                builder.append("  ${it}\n")
            }
        }
    }
    val data = builder.toString().toByteArray(Charset.forName("UTF-8"))

    val folder: File? = context.getExternalFilesDir(FOLDER_NAME)
    val file = File(folder, "$FILE_NAME.txt")
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(data)
        result = true
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return result
}