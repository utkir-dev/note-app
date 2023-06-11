package com.example.mynotes.presentation.ui.screens.main.share

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import androidx.core.content.ContextCompat
import com.example.mynotes.R
import com.example.mynotes.contstants.FILE_NAME
import com.example.mynotes.contstants.FOLDER_NAME
import com.example.mynotes.presentation.utils.extensions.huminizeForFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream

suspend fun CreatePDF(
    context: Context,
    mapData: LinkedHashMap<String, LinkedHashMap<String, List<String>>>
): Flow<ByteArray> = flow {

    val list = ArrayList<PdfModel>()
    var result = false

    val textFirstPage = "KUNLIK HISOB KITOBLAR"
    val textFirstPage1 = System.currentTimeMillis().huminizeForFile()
    val textFirstPage2 = "sanadagi ma'lumotlar"
    mapData.forEach { mainItem ->
        list.add(PdfModel(priority = 1, text = mainItem.key))
        mainItem.value.forEach { subItem ->
            list.add(PdfModel(priority = 2, text = subItem.key))
            if (subItem.value.isEmpty()) {
                list.add(PdfModel(priority = 5, text = ""))
            } else if (subItem.value.size == 1) {
                list.add(PdfModel(priority = 5, text = subItem.value[0]))
            } else {
                list.add(PdfModel(priority = 3, text = subItem.value[0]))
            }
            if (subItem.value.size > 1) {
                for (i in 1..subItem.value.size - 1) {
                    if (i == subItem.value.size - 1) {
                        list.add(PdfModel(priority = 5, text = subItem.value[i]))
                    } else {
                        list.add(PdfModel(priority = 4, text = subItem.value[i]))
                    }
                }
            }
        }
    }
    val pageWidth = 600
    val pageHeight = 1100
    val step = 30F
    val xBegin = 20F
    val xPadding = 6F
    val yPadding = 6F
    val xEnd = pageWidth - xBegin
    val xCenter = (pageWidth - 2 * xBegin) * 0.45F
    val yTop = 20F
    val yBottom = pageHeight - yTop


    val pdfDocument: PdfDocument = PdfDocument()

    // for image and first page
    val paintFirstPage: Paint = Paint()
    paintFirstPage.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    paintFirstPage.color = ContextCompat.getColor(context, R.color.blue)
    paintFirstPage.textSize = 40F
    paintFirstPage.textAlign = Paint.Align.CENTER

    // for title
    val paintTitle = Paint()
    paintTitle.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    paintTitle.color = ContextCompat.getColor(context, R.color.black)
    paintTitle.textSize = 24F
    paintTitle.textAlign = Paint.Align.CENTER

    // for Background
    val paintBackground: Paint = Paint()
    paintBackground.color = ContextCompat.getColor(context, R.color.grey)

    // for text and lines
    val paint: Paint = Paint()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 18F
    paint.strokeWidth = 1F
    paint.color = ContextCompat.getColor(context, R.color.black)

    // eniga 100F bulsa 20F lik harfdan 8.7 ta sigyapti , buyiga kichik harflarda 50% bush joy qolyapti
    // eniga 100F bulsa 15F lik harfdan 12 ta sigyapti , buyiga kichik harflarda 60% bush joy qolyapti

    // first page
    val bmp: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_first_page)
    val scaledbmp: Bitmap = Bitmap.createScaledBitmap(bmp, 400, 400, false)
    val firstPageInfo: PdfDocument.PageInfo? =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val firstPage: PdfDocument.Page = pdfDocument.startPage(firstPageInfo)
    val canvasFirst: Canvas = firstPage.canvas
    canvasFirst.drawBitmap(scaledbmp, 100F, 200F, paintFirstPage)
    canvasFirst.drawText(textFirstPage, 300F, 100F, paintFirstPage)
    canvasFirst.drawText(textFirstPage1, 300F, 700F, paintFirstPage)
    canvasFirst.drawText(textFirstPage2, 300F, 750F, paintFirstPage)
    pdfDocument.finishPage(firstPage)


    // other pages

    var pageNumber = 1
    var index = -1

    while (index <= list.size - 1) {
        var y = yTop + step
        pageNumber++

        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        val canvas: Canvas = myPage.canvas

        while (y < yBottom) {
            index++
            if (index == list.size) {
                break
            }
            when (list[index].priority) {
                1 -> {
                    y += step
                    // background
                    canvas.drawRect(xBegin, y - step, xEnd, y, paintBackground)
//                    var shapeDrawable: ShapeDrawable
//                    val left = 100
//                    val top = 100
//                    val right = 600
//                    val bottom = 400
//
//                    // draw rectangle shape to canvas
//                    shapeDrawable = ShapeDrawable(RectShape())
//                    shapeDrawable.setBounds( left, top, right, bottom)
//                    shapeDrawable.getPaint().setColor(ContextCompat.getColor(context, R.color.grey))
//                    shapeDrawable.draw(canvas)

                    canvas.drawText(list[index].text, xCenter + xPadding, y - yPadding, paintTitle)
                    canvas.drawLine(xBegin, y - step, xBegin, y, paint)
                    canvas.drawLine(xEnd, y - step, xEnd, y, paint)
                    canvas.drawLine(xBegin, y - step, xEnd, y - step, paint)
                    canvas.drawLine(xBegin, y, xEnd, y, paint)
                }
                2 -> {
                    canvas.drawText(list[index].text, xBegin + xPadding, y - yPadding, paint)
                    canvas.drawLine(xBegin, y - step, xEnd, y - step, paint)
                    canvas.drawLine(xBegin, y - step, xBegin, y, paint)
                    canvas.drawLine(xEnd, y - step, xEnd, y, paint)
                    canvas.drawLine(xCenter, y - step, xCenter, y, paint)
                    continue
                }
                3 -> {
                    //  y -= step
                    canvas.drawText(list[index].text, xCenter + xPadding, y - yPadding, paint)
                    //
                }
                4 -> {
                    canvas.drawText(list[index].text, xCenter + xPadding, y - yPadding, paint)
                    canvas.drawLine(xBegin, y - step, xBegin, y, paint)
                    canvas.drawLine(xEnd, y - step, xEnd, y, paint)
                    canvas.drawLine(xCenter, y - step, xCenter, y, paint)
                }
                5 -> {
                    canvas.drawText(list[index].text, xCenter + xPadding, y - yPadding, paint)
                    canvas.drawLine(xBegin, y, xEnd, y, paint)
                    canvas.drawLine(xBegin, y - step, xBegin, y, paint)
                    canvas.drawLine(xEnd, y - step, xEnd, y, paint)
                    canvas.drawLine(xCenter, y - step, xCenter, y, paint)
                }
            }
            y += step
        }
        pdfDocument.finishPage(myPage)
    }

    val folder: File? = context.getExternalFilesDir(FOLDER_NAME)
    val file = File(folder, "$FILE_NAME.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        emit(file.readBytes())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    pdfDocument.close()
}.flowOn(Dispatchers.IO)
