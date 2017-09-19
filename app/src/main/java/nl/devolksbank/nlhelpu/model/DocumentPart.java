package nl.devolksbank.nlhelpu.model;

public class DocumentPart {
    private long id;
    private String fileLocation;
    private FileType type;

    public DocumentPart(long id, String fileLocation, FileType type) {
        this.id = id;
        this.fileLocation = fileLocation;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DocumentPart{" +
                "id=" + id +
                ", fileLocation='" + fileLocation +
                ", type=" + type +
                '}';
    }
}
