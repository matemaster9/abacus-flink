package org.mastercs.bigdata.flink_java;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * author: Syler
 * time: 2023/6/26 14:06
 */
@Slf4j
public class IntroToDataStream {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Person> flintstones = environment.fromElements(
                new Person("cyborg", 24),
                new Person("matemaster", 24),
                new Person("syler", 17)
        );
        flintstones.filter(IntroToDataStream::filterAdult).print();
        environment.execute("filter");
    }

    private static boolean filterAdult(Person person) {
        return person.getAge() > 18;
    }

    public static void javaTuple() {
        Tuple2<String, Integer> person = Tuple2.of("cyborg", 24);
        log.info(person.f0);
        log.info(String.valueOf(person.f1));
    }
}
