package com.example.mediscan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.mediscan.db.entity.Report
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.property.VerticalAlignment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PDFUtil {
    companion object {
        fun openPDF(context: Context, file: File) {
            try {
                val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                }
                context.startActivity(Intent.createChooser(intent, "Open PDF"))
                
            } catch (e: Exception) {
                Toast.makeText(context, "No PDF viewer found!", Toast.LENGTH_SHORT).show()
            }
        }
        
        
        fun createReportPDF(context: Context, reports: List<Report>) {
            try {
                // File location
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "health_report.pdf")
                
                // Create PDF Writer & Document
                val pdfWriter = PdfWriter(FileOutputStream(file))
                val pdfDocument = PdfDocument(pdfWriter)
                // pdfDocument.addEventHandler(PdfDocumentEvent.INSERT_PAGE, HeaderFooterHandler())
                val document = com.itextpdf.layout.Document(pdfDocument, PageSize.A4)
                document.setMargins(8f, 8f, 8f, 8f)
                
                
                // Loop through each report and create a card
                for (report in reports) {
                    document.add(createReportCard(report))
                    document.add(Paragraph("\n")) // Add space between cards
                }
                
                document.close()
                // Toast.makeText(context, "PDF saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                openPDF(context, file)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            }
        }
        
        class HeaderFooterHandler : IEventHandler {
            override fun handleEvent(event: Event) {
                val docEvent = event as PdfDocumentEvent
                val pdfDocument = docEvent.document
                val page = docEvent.page
                val canvas = PdfCanvas(page)
                
                val pageSize = page.pageSize
                val doc = com.itextpdf.layout.Document(pdfDocument)
                
                // HEADER - Add at the top
                val headerText = Paragraph("Medical Report").setBold().setFontSize(14f)
                doc.showTextAligned(headerText, pageSize.width / 2, pageSize.top - 30f, TextAlignment.CENTER, VerticalAlignment.TOP)
                
                // FOOTER - Add at the bottom
                val footerText = Paragraph("Page ${pdfDocument.getPageNumber(page)}")
                doc.showTextAligned(footerText, pageSize.width / 2, pageSize.bottom + 20f, TextAlignment.CENTER, VerticalAlignment.BOTTOM)
                
                canvas.release()
            }
        }
        
        
        private fun createReportCard(report: Report): Table {
            val cardTable = Table(UnitValue.createPercentArray(floatArrayOf(6f, 3f)))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMargins(0f, 32f, -12f, 32f)
                .setBackgroundColor(DeviceRgb(240, 248, 255)) // Light Blue background
            
            // Left Section - Test Details
            val leftCell = Cell().setPadding(15f).setBorder(Border.NO_BORDER)
            leftCell.add(Paragraph(report.testName ?: "Test Name").setBold().setFontSize(16f))
            leftCell.add(Paragraph(report.explanation ?: "").setFontSize(12f))
            
            // Right Section - Test Result
            val rightCell = Cell()
                .setPadding(15f)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(DeviceRgb(255, 235, 235)) // Light Red for test value section
                .setTextAlignment(TextAlignment.CENTER)
            
            rightCell.add(Paragraph("Your Result Value").setFontSize(8f))
            
            
            // Add test value and test unit
            val testValue = Text("${report.testValue ?: "N/A"}").setFontSize(18f).setBold().setFontColor(DeviceRgb(200, 0, 0))
            val reportUnit = Text(" ${report.unit ?: ""}").setFontSize(10f).setBold().setFontColor(DeviceRgb(200, 0, 0))
            rightCell.add(Paragraph().add(testValue).add(reportUnit))
            
            // Add blank space
            rightCell.add(Paragraph("\n").setFontSize(10f))
            
            // Add Normal Range
            rightCell.add(Paragraph("Normal Value:").setFontSize(8f))
            rightCell.add(Paragraph("${report.lowerLimit} - ${report.upperLimit} ${report.unit}").setFontSize(10f).setBold())
            
            
            // Add Cells to Table
            cardTable.addCell(leftCell)
            cardTable.addCell(rightCell)
            
            return cardTable
        }
        
        /*private fun createPDF(context: Context, textContent: String) {
            try {
                // Define the file path (Downloads folder)
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "${System.currentTimeMillis()}.pdf"
                )
                
                // Create PdfWriter and PdfDocument
                val pdfWriter = PdfWriter(FileOutputStream(file))
                val pdfDocument = PdfDocument(pdfWriter)
                val document = com.itextpdf.layout.Document(pdfDocument, PageSize.A4)
                
                // Add text content
                document.add(Paragraph(textContent))
                
                // Close document
                document.close()
                
                // Show success message
                Toast.makeText(context, "PDF saved to: " + file.absolutePath, Toast.LENGTH_SHORT).show()
                openPDF(context, file)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save PDF: " + e.message, Toast.LENGTH_SHORT).show()
            }
        }*/
        
    }
}