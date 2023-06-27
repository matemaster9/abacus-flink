package org.mastercs.bigdata.flink_java.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * author: Syler
 * time: 2023/6/27 11:14
 */
public class FlinkFileSource {

    private static final String AUTO_CONFIG_TXT = "flink-java/src/main/resources/docs/springboot自动配置类.txt";
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
        dataStreamSource.print();
        env.execute(FILE_SOURCE_NAME);
    }
}
