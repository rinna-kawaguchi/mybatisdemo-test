package com.example.mybatisdemo2.response;

import com.example.mybatisdemo2.entity.Name;

public class NameResponse {

    private String name;

    public NameResponse(Name name) {
        this.name = name.getName();
    }

    public String getName() {
        return name;
    }
}
