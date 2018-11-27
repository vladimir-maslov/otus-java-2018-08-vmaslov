package ru.otus.l10.dataset;

public abstract class DataSet {
    private long id;

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
