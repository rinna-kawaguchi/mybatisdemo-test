package com.example.mybatisdemo2.service;

import com.example.mybatisdemo2.entity.Name;
import com.example.mybatisdemo2.form.CreateForm;

import java.util.List;

public interface NameService {

    // 名前を全部取得する
    List<Name> findAll();
    List<Name> findById(int id);
    Name findById2(int id);
    List<Name> findByIdGreaterThan(int id);
    Name saveName(CreateForm form);
    void updateName(int id, String name);
    void deleteName(int id);
}
