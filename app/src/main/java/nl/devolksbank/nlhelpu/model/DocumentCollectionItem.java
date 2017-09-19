package nl.devolksbank.nlhelpu.model;

import java.io.Serializable;

public class DocumentCollectionItem implements Serializable {
    private final String name;
    private final String status;
    private final FileType type;
    private final boolean isItemPresent;

    public DocumentCollectionItem(final String name, final String status, final FileType type, final boolean isItemPresent) {
        this.name = name;
        this.status = status;
        this.type = type;
        this.isItemPresent = isItemPresent;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public boolean isItemPresent() {
        return isItemPresent;
    }

    public FileType getType() {
        return type;
    }
}