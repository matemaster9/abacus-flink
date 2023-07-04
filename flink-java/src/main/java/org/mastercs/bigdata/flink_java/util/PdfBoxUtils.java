package org.mastercs.bigdata.flink_java.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用pdfbox完成相关pdf操作
 * <p>
 * author: Syler
 * time: 2023/6/27 10:41
 */
@Slf4j
public final class PdfBoxUtils {

    private PdfBoxUtils() {
    }

    public static void main(String[] args) {
        List<String> words = extractWords("flink-java/docs/spring-boot-reference.pdf");
        log.info("文档单词数：{}", words.size());
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

    /**
     * 基于有限状态机提取pdf文件中的英文单词，单词格式：单词，及含有连字符的单词
     *
     * @param text 文本
     * @return words
     */
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
