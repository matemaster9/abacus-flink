package org.mastercs.bigdata.flink_java;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.mastercs.bigdata.flink_java.util.PdfBoxUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mastercs.bigdata.flink_java.GooglePaperConstant.*;

/**
 * author: Syler
 * time: 2023/6/26 16:35
 */
@Slf4j
public class GooglePaperAnalyzer {

    public static void main(String[] args) {
        analyzePaperByUsingJavaStream();
        analyzePaperByUsingApacheFlink();
    }

    @SneakyThrows
    public static void analyzePaperByUsingApacheFlink() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.addSource(PDFSourceFunction.self());

        env.execute("analyzePaper");
    }

    public static void analyzePaperByUsingJavaStream() {
        List<String> words = extractWordsFromGooglePaper();

        // 统计单词出现的次数
        Map<String, Integer> frequency = words.stream()
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(t -> t, Collectors.collectingAndThen(Collectors.toList(), List::size)));
        log.info(frequency.toString());

        // 基于guava集合实现
        Multiset<String> frequencySet = HashMultiset.create(words);
        log.info(frequencySet.toString());
    }

    public static List<String> extractWordsFromGooglePaper() {
        File[] papers = new File[] {
                new File(GFS_PDF),
                new File(BIGTABLE_PDF),
                new File(MAPREDUCE_PDF)
        };

        StringBuilder textBuilder = new StringBuilder(1024);
        for (File paper : papers) {
            try(PDDocument document = PDDocument.load(paper)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                textBuilder.append(text);
            } catch (IOException e) {
                log.error(e.getMessage());
                return Collections.emptyList();
            }
        }

        return PdfBoxUtils.extractWordsFromText(textBuilder.toString());
    }

    private static class PDFSourceFunction extends RichSourceFunction<String> {

        @Override
        public void run(SourceContext<String> sourceContext) throws Exception {
            File[] papers = new File[] {
                    new File(GFS_PDF),
                    new File(BIGTABLE_PDF),
                    new File(MAPREDUCE_PDF)
            };
            for (File paper : papers) {
                try(PDDocument document = PDDocument.load(paper)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);
                    sourceContext.collect(text);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

        }

        @Override
        public void cancel() {
            // code to cancel
        }

        public static PDFSourceFunction self() {
            return new PDFSourceFunction();
        }
    }

    private static class TextToWords implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String text, Collector<Tuple2<String, Integer>> collector) throws Exception {

        }
    }
}
