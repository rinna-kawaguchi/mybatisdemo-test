package com.example.mybatisdemo2.entity;

import java.util.Objects;

public class Name {
    private int id;
    private String name;

    // constructorやgetterを作成する。必要に応じてsetterを作成する
    public Name(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Name(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return id == name1.id && Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
