package org.mastercs.bigdata.flink_java;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: Syler
 * time: 2023/6/26 15:47
 */
@Slf4j
public class GFSAnalytics {

    private static final String GFS_PDF = "flink-java/src/main/resources/docs/gfs.pdf";

    public static void main(String[] args) {
        readWordsToConsole(GFS_PDF);
        printAnalytics();
    }

    public static void printAnalytics() {
        List<String> words = extractWordsFromPdf(GFS_PDF);
        log.info(words.toString());
    }

    public static List<String> extractWordsFromPdf(String pdfPath) {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return extractWords(text);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public static List<String> extractWords(String text) {
        List<String> words = new ArrayList<>();
        StringBuilder wordBuilder = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c) || c == '-') {
                wordBuilder.append(c);
            } else if (wordBuilder.length() > 0) {
                words.add(removeHyphens(wordBuilder.toString()));
                wordBuilder.setLength(0);
            }
        }

        if (wordBuilder.length() > 0) {
            words.add(wordBuilder.toString());
        }

        return words;
    }

    public static void readWordsToConsole(String pdfPath) {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            log.info(text);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String removeHyphens(String input) {
        return input.replace("-", "");
    }
}
