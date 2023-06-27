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
 * author: Syler
 * time: 2023/6/27 13:46
 */
@Slf4j
public class EasyExcelHelper {

    public static void main(String[] args) {
        List<ManagedDependencyCoordinate> coordinates =
                getManagedDependencyCoordinatesFromExcel("flink-java/src/main/resources/docs/springboot托管依赖.xlsx");
        coordinates.forEach(EasyExcelHelper::console);
    }

    private static void console(ManagedDependencyCoordinate coordinate) {
        log.info(coordinate.toString());
    }

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

        public static ManagedDependencyCoordinateReadListener create(
                List<ManagedDependencyCoordinateExcelDto> recordList) {
            return new ManagedDependencyCoordinateReadListener(recordList);
        }
    }
}
