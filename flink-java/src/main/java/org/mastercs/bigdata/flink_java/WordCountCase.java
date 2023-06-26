package org.mastercs.bigdata.flink_java;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author: cyborg
 * Create: 2023/5/2 14:08
 */
public class WordCountCase {

    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> text = env.readTextFile("file:///path/to/file");
    }


    public static void readTextFile(String path) {

    }
}
