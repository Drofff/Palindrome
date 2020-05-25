package com.drofff.palindrome.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.util.Map;

public interface PdfService {

    Document newPdfDocument(String path);

    Table tableFromMap(Map<String, String> map);

    Paragraph paragraphOfText(String text);

}
