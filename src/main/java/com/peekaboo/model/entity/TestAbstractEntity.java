package com.peekaboo.model.entity;

/**
 * Created by Oleksii on 09.08.2016.
 */
public abstract class TestAbstractEntity {

    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || id == null || getClass() != o.getClass()) return false;

        TestAbstractEntity entity = (TestAbstractEntity) o;

        if (!id.equals(entity.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }
}
