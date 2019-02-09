package ru.otus.l15.dataset;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@MappedSuperclass
public abstract class DataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotFound(action = NotFoundAction.IGNORE)
    private long id;

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
