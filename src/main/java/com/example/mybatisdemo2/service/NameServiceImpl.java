package com.example.mybatisdemo2.service;

import com.example.mybatisdemo2.entity.Name;
import com.example.mybatisdemo2.exception.ResourceNotFoundException;
import com.example.mybatisdemo2.form.CreateForm;
import com.example.mybatisdemo2.mapper.NameMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NameServiceImpl implements NameService {

    // field
    private final NameMapper nameMapper;

    //constructor
    public NameServiceImpl(NameMapper nameMapper) {
        this.nameMapper = nameMapper;
    }

    public List<Name> findAll() {
        return nameMapper.findAll();
    }

    @Override
    public List<Name> findById(int id) {
        return nameMapper.findById(id);
    }

    public Name findById2(int id) {
        return nameMapper.findById2(id).orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
    }

    public List<Name> findByIdGreaterThan(int id) {
        return nameMapper.findByIdGreaterThan(id);
    }

    @Override
    public Name saveName(CreateForm form) {
        Name name = new Name(form.getName());
        nameMapper.save(name);
        return name;
    }

    @Override
    public void updateName(int id, String name) {
        nameMapper.findById2(id).orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
        Name updateName = new Name(id, name);
        nameMapper.updateName(updateName);
    }

    public void deleteName(int id) {
        nameMapper.findById2(id).orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
        nameMapper.deleteName(id);
    }
}
