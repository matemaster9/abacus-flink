package org.mastercs.bigdata.flink_java.source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 验证socket创建DataStream
 * <p>
 * author: Syler
 * time: 2023/6/27 15:13
 */
public class FlinkSocketSource {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        // 创建输入
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);

        // ETL(Extract、Transformation、Load)操作
        DataStream<Tuple2<String, Integer>> dataStream = dataStreamSource
                .flatMap(Splitter.create())
                .keyBy(value -> value.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .sum(1);

        // 创建输出
        dataStream.print();
        env.execute("Window WordCount");
    }

    private static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        private static final String DELIMITER = " ";

        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word : sentence.split(DELIMITER)) {
                out.collect(new Tuple2<>(word, 1));
            }
        }

        public static Splitter create() {
            return new Splitter();
        }
    }
}
