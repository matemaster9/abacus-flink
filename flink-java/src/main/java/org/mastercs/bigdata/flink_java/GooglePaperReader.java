package org.mastercs.bigdata.flink_java;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

import static org.mastercs.bigdata.flink_java.GooglePaperConstant.*;

/**
 * author: Syler
 * time: 2023/6/26 17:16
 */
@Slf4j
public class GooglePaperReader {

    public static void main(String[] args) {
        collect();
    }

    @SneakyThrows
    public static void collect() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // DataStreamSource中的元素，来自每次SourceContext#collect收集到的信息
        DataStreamSource<String> dataStreamSource = env.addSource(PDFSourceFunction.self());
        dataStreamSource.print();
        env.execute("print paper");
    }

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

        public static PDFSourceFunction self() {
            return new PDFSourceFunction();
        }
    }
}
