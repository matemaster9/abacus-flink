package org.mastercs.bigdata.flink_java;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author: cyborg
 * Create: 2023/6/27 21:36
 */
public class FlinkWordCount {

    private static final String AUTO_CONFIG_TXT = "flink-java/docs/springboot自动配置类.txt";
    private static final String FILE_SOURCE_NAME = "SpringBootAutoConfigure";

    public static void main(String[] args) throws Exception {
        // 创建环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 创建file system连接
        FileSource<String> autoConfigurationClassFileSource = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                new Path(AUTO_CONFIG_TXT)
        ).build();

        // 添加source
        DataStreamSource<String> dataStreamSource = env.fromSource(
                autoConfigurationClassFileSource,
                WatermarkStrategy.noWatermarks(),
                FILE_SOURCE_NAME
        );

        // 执行
        dataStreamSource.flatMap(wordToCharCountTuple())
                .returns(Types.TUPLE(Types.CHAR, Types.INT))
                .keyBy(tuple -> tuple.f0)
                .sum(1)
                .print();
        env.execute(FILE_SOURCE_NAME);
    }

    public static FlatMapFunction<String, Tuple2<Character, Integer>> wordToCharCountTuple() {
        return (word, collector) -> {
            char[] chars = word.toCharArray();
            for (char item : chars) {
                collector.collect(Tuple2.of(item, 1));
            }
        };
    }
}
