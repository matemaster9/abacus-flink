package org.mastercs.bigdata.flink_java;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
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

    /**
     * 利用flink计算引擎，获取Google三篇大数据论文中单词的出现频率
     */
    @SneakyThrows
    public static void analyzePaperByUsingApacheFlink() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.addSource(PDFSourceFunction.self());
        // returns方法存在的意义就是避免泛型擦除导致flink不清楚返回类型，具体查看TypeInformation
        dataStreamSource
                .flatMap(textToWords())
                .returns(Types.STRING)
                .print();
        env.execute("analyzePaper");
    }

    /**
     * 创建实例
     *
     * @return FlatMapFunction lambda实现
     */
    public static FlatMapFunction<String, String> textToWords() {
        return (text, collector) -> {
            List<String> words = PdfBoxUtils.extractWordsFromText(text);
            for (String word : words) {
                collector.collect(word);
            }
        };
    }

    /**
     * 利用Java stream流实现单词出现频率的统计
     */
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
        File[] papers = new File[]{
                new File(GFS_PDF),
                new File(BIGTABLE_PDF),
                new File(MAPREDUCE_PDF)
        };

        StringBuilder textBuilder = new StringBuilder(1024);
        for (File paper : papers) {
            try (PDDocument document = PDDocument.load(paper)) {
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

    /**
     * 利用pdfbox解析pdf文档，将读取到的pdf内容收集到flink中
     */
    private static class PDFSourceFunction extends RichSourceFunction<String> {

        @Override
        public void run(SourceContext<String> sourceContext) throws Exception {
            File[] papers = new File[]{
                    new File(GFS_PDF),
                    new File(BIGTABLE_PDF),
                    new File(MAPREDUCE_PDF)
            };
            for (File paper : papers) {
                try (PDDocument document = PDDocument.load(paper)) {
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

        /**
         * 工厂函数
         *
         * @return PDFSourceFunction instance
         */
        public static PDFSourceFunction self() {
            return new PDFSourceFunction();
        }
    }
}
