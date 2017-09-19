package nl.devolksbank.nlhelpu.model;

import java.io.Serializable;

import nl.devolksbank.nlhelpu.util.SectionsProvider;

public class SectionModel implements Serializable {

    private final int id;
    private final String name;

    public SectionModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNameResourceId() {
        return SectionsProvider.getNameResourceId(id);
    }

    public int getContentResourceId() {
        return SectionsProvider.getContentResourceId(id);
    }

    public int getDetailsResourceId() {
        return SectionsProvider.getDetailsResourceId(id);
    }

    @Override
    public String toString() {
        // TODO: use value from strings.xml here in some way, or by updating the SectionViewAdapter
        return name;
    }
}
