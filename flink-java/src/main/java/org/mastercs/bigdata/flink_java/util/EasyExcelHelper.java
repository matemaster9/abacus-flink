package org.mastercs.bigdata.flink_java.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.mastercs.bigdata.flink_java.model.ManagedDependencyCoordinate;
import org.mastercs.bigdata.flink_java.model.ManagedDependencyCoordinateExcelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 旨在使用easy excel组件，将系统中的excel数据读取的内存中，以对象列表的形式出现
 * <p>
 * author: Syler
 * time: 2023/6/27 13:46
 */
@Slf4j
public class EasyExcelHelper {

    public static void main(String[] args) {
        List<ManagedDependencyCoordinate> coordinates =
                getManagedDependencyCoordinatesFromExcel("flink-java/docs/springboot托管依赖.xlsx");
        coordinates.forEach(EasyExcelHelper::console);
    }

    private static void console(ManagedDependencyCoordinate coordinate) {
        log.info(coordinate.toString());
    }

    /**
     * 将spring boot托管依赖的数据读取的列表中
     *
     * @param excel ManagedDependencyCoordinate excel文件
     * @return List<ManagedDependencyCoordinate>
     */
    public static List<ManagedDependencyCoordinate> getManagedDependencyCoordinatesFromExcel(String excel) {
        List<ManagedDependencyCoordinateExcelDto> coordinates = new ArrayList<>();
        var readerBuilder = EasyExcelFactory.read(
                excel,
                ManagedDependencyCoordinateReadListener.create(coordinates)
        );
        var sheetBuilder = readerBuilder.sheet(0)
                .head(ManagedDependencyCoordinateExcelDto.class)
                .headRowNumber(1);
        sheetBuilder.doRead();
        return coordinates.stream().map(ManagedDependencyCoordinateExcelDto::convert).collect(Collectors.toList());
    }


    /**
     * 读excel监听器，详见阿里开源组件easy excel
     */
    private static class ManagedDependencyCoordinateReadListener implements ReadListener<ManagedDependencyCoordinateExcelDto> {

        private final List<ManagedDependencyCoordinateExcelDto> recordList;

        public ManagedDependencyCoordinateReadListener(List<ManagedDependencyCoordinateExcelDto> recordList) {
            this.recordList = recordList;
        }

        @Override
        public void invoke(ManagedDependencyCoordinateExcelDto data, AnalysisContext context) {
            recordList.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // code doAfterAllAnalysed
        }

        /**
         * 创建ManagedDependencyCoordinateReadListener实例
         *
         * @param recordList excel数据读取容器
         * @return ManagedDependencyCoordinateReadListener instance
         */
        public static ManagedDependencyCoordinateReadListener create(
                List<ManagedDependencyCoordinateExcelDto> recordList) {
            return new ManagedDependencyCoordinateReadListener(recordList);
        }
    }
}
