package com.sda.resumeanalyzer.parser;

import java.io.File;

public interface ResumeParser {
    String parse(File file) throws Exception;
}