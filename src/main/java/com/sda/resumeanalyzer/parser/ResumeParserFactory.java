package com.sda.resumeanalyzer.parser;

public class ResumeParserFactory {

    public static ResumeParser getParser(String fileName) {
        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".pdf")) {
            return new PdfResumeParser();
        }

        if (lowerName.endsWith(".docx")) {
            return new DocxResumeParser();
        }

        throw new IllegalArgumentException("Unsupported file type. Please upload PDF or DOCX.");
    }
}