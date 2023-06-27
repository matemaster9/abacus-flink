package org.mastercs.bigdata.flink_java.model;


/**
 * author: Syler
 * time: 2023/6/27 13:26
 */
public class ManagedDependencyCoordinate {
    private String groupId;
    private String artifactId;
    private String version;

    public static ManagedDependencyCoordinate unknown() {
        ManagedDependencyCoordinate coordinate = new ManagedDependencyCoordinate();
        coordinate.setArtifactId("unknown");
        coordinate.setGroupId("unknown");
        coordinate.setVersion("1.0-SNAPSHOT");
        return coordinate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ManagedDependencyCoordinate{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
