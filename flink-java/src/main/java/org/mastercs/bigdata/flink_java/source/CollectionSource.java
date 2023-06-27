package org.mastercs.bigdata.flink_java.source;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import net.datafaker.providers.base.File;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Syler
 * time: 2023/6/27 9:48
 */
@Slf4j
public class CollectionSource {

    public static void main(String[] args) {
        fromElementSource();
        fromCollectionSource();
    }

    @SneakyThrows
    public static void fromElementSource() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<MobileFile> dataStreamSource = env.fromElements(
                MobileFile.create(),
                MobileFile.create(),
                MobileFile.create(),
                MobileFile.create()
        );
        dataStreamSource.map(MobileFile::getExtension).print();
        env.execute("from elements");
    }

    @SneakyThrows
    public static void fromCollectionSource() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<MobileFile> dataStreamSource = env.fromCollection(MobileFile.batchCreate(100));
        dataStreamSource.map(MobileFile::getExtension).print();
        env.execute("from collection");
    }


    public static class MobileFile {

        private static final File FILE_FAKER;

        static {
            Faker faker = new Faker();
            FILE_FAKER = faker.file();
        }

        private String name;
        private String mimeType;
        private String extension;

        public static MobileFile create() {
            MobileFile mobileFile = new MobileFile();
            mobileFile.setName(FILE_FAKER.fileName());
            mobileFile.setExtension(FILE_FAKER.extension());
            mobileFile.setMimeType(FILE_FAKER.mimeType());
            return mobileFile;
        }

        public static List<MobileFile> batchCreate(int batchSize) {
            List<MobileFile> mobileFiles = new ArrayList<>(batchSize);
            for (int i = 0; i < batchSize; i++) {
                mobileFiles.add(create());
            }
            return mobileFiles;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        @Override
        public String toString() {
            return "MobileFile{" +
                    "name='" + name + '\'' +
                    ", mimeType='" + mimeType + '\'' +
                    ", extension='" + extension + '\'' +
                    '}';
        }
    }
}
