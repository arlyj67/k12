package com.schoolproject.k12.service.implementation

import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.entity.User
import com.schoolproject.k12.service.PdfGeneratorService
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PdfGeneratorServiceImpl : PdfGeneratorService {

    private val studentBaseDir = "/app/accounts/students"
    private val employeeBaseDir = "/app/accounts/employees"
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")

    // ──────────────────────────────────────────────
    // Student PDF
    // ──────────────────────────────────────────────

    override fun generateStudentAccountPDF(student: Student, user: User, temporaryPassword: String) {
        val directory = File(studentBaseDir)
        if (!directory.exists()) directory.mkdirs()

        val document = createDocument("$studentBaseDir/${student.studentNumber}.pdf")

        // Header
        addHeader(document, student.school.schoolName, "Student Account Credentials")

        // Table
        val table = createTable()
        addRow(table, "Student Name", student.fullName)
        addRow(table, "Student Number", student.studentNumber)
        addRow(table, "School", student.school.schoolName)
        addRow(table, "School Year", student.schoolYear)
        addRow(table, "Grade Level", "Grade ${student.studentLevel}")
        addRow(table, "Username", user.username)
        addRow(table, "Temporary Password", temporaryPassword)
        addRow(table, "Account Status", "Pending — awaiting registrar approval")
        addRow(table, "Date Generated", LocalDateTime.now().format(dateFormatter))

        document.add(table)
        addFooter(document)
        document.close()
    }

    // ──────────────────────────────────────────────
    // Employee PDF
    // ──────────────────────────────────────────────

    override fun generateEmployeeAccountPDF(employee: Employee, user: User, temporaryPassword: String) {
        val directory = File(employeeBaseDir)
        if (!directory.exists()) directory.mkdirs()

        val document = createDocument("$employeeBaseDir/${employee.employeeNumber}.pdf")

        // Header
        addHeader(document, employee.school.schoolName, "Employee Account Credentials")

        // Table
        val table = createTable()
        addRow(table, "Employee Name", employee.fullName)
        addRow(table, "Employee Number", employee.employeeNumber)
        addRow(table, "School", employee.school.schoolName)
        addRow(table, "Role", employee.role.name)
        addRow(table, "Username", user.username)
        addRow(table, "Temporary Password", temporaryPassword)
        addRow(table, "Account Status", "Active")
        addRow(table, "Date Generated", LocalDateTime.now().format(dateFormatter))

        document.add(table)
        addFooter(document)
        document.close()
    }

    // ──────────────────────────────────────────────
    // Shared Helpers
    // ──────────────────────────────────────────────

    private fun createDocument(filePath: String): Document {
        val pdfWriter = PdfWriter(filePath)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)
        document.setMargins(40f, 50f, 40f, 50f)
        return document
    }

    private fun addHeader(document: Document, schoolName: String, title: String) {
        document.add(
            Paragraph(schoolName)
                .setBold()
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY)
        )
        document.add(
            Paragraph(title)
                .setFontSize(13f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY)
        )
        document.add(
            Paragraph("─".repeat(60))
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
        )
        document.add(Paragraph("\n"))
    }

    private fun createTable(): Table {
        return Table(UnitValue.createPercentArray(floatArrayOf(40f, 60f)))
            .setWidth(UnitValue.createPercentValue(100f))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
    }

    private fun addRow(table: Table, label: String, value: String) {
        table.addCell(
            Cell().add(Paragraph(label).setBold().setFontSize(11f))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(8f)
        )
        table.addCell(
            Cell().add(Paragraph(value).setFontSize(11f))
                .setPadding(8f)
        )
    }

    private fun addFooter(document: Document) {
        document.add(Paragraph("\n"))
        document.add(
            Paragraph("⚠ Please keep this document confidential. Change your password upon first login.")
                .setFontSize(9f)
                .setItalic()
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
        )
    }
}