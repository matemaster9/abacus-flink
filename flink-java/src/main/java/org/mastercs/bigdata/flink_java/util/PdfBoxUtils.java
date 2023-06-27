package org.mastercs.bigdata.flink_java.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Syler
 * time: 2023/6/27 10:41
 */
public final class PdfBoxUtils {

    private PdfBoxUtils() {
    }

    public static List<String> extractWords(String pdfPath) {
        File pdf = new File(pdfPath);
        try (PDDocument document = PDDocument.load(pdf)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return extractWordsFromText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> extractWordsFromText(String text) {
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

    public static String removeHyphens(String input) {
        return input.replace("-", "");
    }
}
