package org.mastercs.bigdata.flink_java.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * author: Syler
 * time: 2023/6/27 13:44
 */
@Getter
@Setter
@ToString
public class ManagedDependencyCoordinateExcelDto {
    @ExcelProperty(index = 0)
    private String groupId;

    @ExcelProperty(index = 1)
    private String artifactId;

    @ExcelProperty(index = 2)
    private String version;

    public static ManagedDependencyCoordinate convert(ManagedDependencyCoordinateExcelDto dto) {
        ManagedDependencyCoordinate coordinate = new ManagedDependencyCoordinate();
        coordinate.setGroupId(dto.groupId);
        coordinate.setVersion(dto.version);
        coordinate.setArtifactId(dto.artifactId);
        return coordinate;
    }
}
