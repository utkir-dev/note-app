package com.example.mynotes.presentation.ui.screens.main.share

import android.content.Context
import com.example.mynotes.contstants.FILE_NAME
import com.example.mynotes.contstants.FOLDER_NAME
import com.example.mynotes.presentation.utils.extensions.huminizeForFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset


suspend fun CreateHtml(
    context: Context,
    mapData: LinkedHashMap<String, LinkedHashMap<String, List<String>>>
): Flow<ByteArray> = flow {
    val builder = StringBuilder()
    val title = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head> <meta charset=\"utf-8\">" +
            "<style>\n" +
            "table, th, td {\n" +
            "  border: 1px solid black;\n" +
            "  border-collapse: collapse;\n" +
            "  vertical-align: top;\n" +
            "  text-align:start;\n" +
            "}\n" +
            "th, td {\n" +
            "  padding: 5px;\n" +
            "}\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h2 style=\"text-align:start; color: blue;\">${
                System.currentTimeMillis().huminizeForFile()
            }" + " sanadagi ma'lumotlar</h2>"

    builder.append(title)
    mapData.forEach { mainItem ->
        val mainTitle = "<table style=\"width:600px ; \">\n" +
                "  <tr  style=\"background-color: #ededed; \">\n" +
                "    <th colspan=\"2\" style=\"text-align:center;\">${mainItem.key}</th>\n" +
                "  </tr>\n"                     // masalan: Haqdorlar
        builder.append(mainTitle)

        mainItem.value.forEach { subItem ->
            val subTitle = "<tr><td >${subItem.key}</td>\n"   // masalan: Vali
            builder.append(subTitle)
            builder.append("<td>")
            subItem.value.forEach {
                builder.append("<li>$it</li>")    // qolgan ma'lumotlar
            }
            builder.append("</td></tr>")
        }
        val endTable = "</table></body>"
        builder.append(endTable)
        builder.append("<br><br>")
    }
    builder.append("</html>")
    val data = builder.toString().toByteArray(Charset.forName("UTF-8"))

    val folder: File? = context.getExternalFilesDir(FOLDER_NAME)
    val file = File(folder, "$FILE_NAME.html")
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(data)
        emit(file.readBytes())
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
}.flowOn(Dispatchers.IO)