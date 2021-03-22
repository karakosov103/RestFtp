package ai.leantech.restftp.model;

import java.util.List;

public class FileInfo {

    public FileInfo(FileType type, String name, String path, List<FileInfo> content) {
        this.type = type;
        this.name = name;
        this.path = path;
        this.content = content;
    }

    public FileInfo(FileType type, String name, String path) {
        this(type, name, path, null);
    }

    private FileType type;

    private String name;

    private String path;

    private List<FileInfo> content;

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileInfo> getContent() {
        return content;
    }

    public void setContent(List<FileInfo> content) {
        this.content = content;
    }
}
