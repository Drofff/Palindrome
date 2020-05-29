package com.drofff.palindrome.service;

import com.drofff.palindrome.exception.PalindromeException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.drofff.palindrome.utils.FileUtils.relativeToAbsolutePath;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static com.itextpdf.kernel.geom.PageSize.A4;

@Service
public class PdfServiceImpl implements PdfService {

    private static final PageSize PDF_DOCUMENT_SIZE = A4;

    private static final String FONT_ENCODING = "Identity-H";

    @Value("${application.font}")
    private String fontPath;

    @Override
    public Document newPdfDocument(String path) {
        try {
            return createPdfDocument(path);
        } catch(FileNotFoundException e) {
            throw new PalindromeException(e.getMessage());
        }
    }

    private Document createPdfDocument(String path) throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        return new Document(pdfDocument, PDF_DOCUMENT_SIZE);
    }

    @Override
    public Table tableFromMap(Map<String, String> map) {
        validateNotNull(map, "Table map should not be null");
        Table table = new Table(2);
        map.forEach(addRowToTableConsumer(table));
        return table;
    }

    private BiConsumer<String, String> addRowToTableConsumer(Table table) {
        return (leftCellValue, rightCellValue) -> {
            addCellToTable(leftCellValue, table);
            addCellToTable(rightCellValue, table);
        };
    }

    private void addCellToTable(String cellValue, Table table) {
        Paragraph paragraph = paragraphOfText(cellValue);
        table.addCell(paragraph);
    }

    @Override
    public Paragraph paragraphOfText(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(applicationFont());
        return paragraph;
    }

    private PdfFont applicationFont() {
        try {
            String absoluteFontPath = relativeToAbsolutePath(fontPath);
            return PdfFontFactory.createFont(absoluteFontPath, FONT_ENCODING, false);
        } catch(IOException e) {
            throw new PalindromeException("Error while loading font");
        }
    }

}