package org.mastercs.bigdata.flink_java;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * author: Syler
 * time: 2023/6/26 15:05
 */
public class ReadText {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FileSource<String> words = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                new Path("")
        ).build();
        DataStreamSource<String> alibaba = env.fromSource(
                words,
                WatermarkStrategy.noWatermarks(),
                "Alibaba"
        );
        alibaba.filter(word -> word.length() > 10).print();
        env.execute("fileSource");
    }
}
