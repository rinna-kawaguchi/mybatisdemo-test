package com.example.mybatisdemo2.mapper;

import com.example.mybatisdemo2.entity.Name;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NameMapper {
    @Select("SELECT * FROM names")
    List<Name> findAll();

    @Select("SELECT * FROM names WHERE id = #{id}")
    List<Name> findById(int id);

    @Select("SELECT * FROM names WHERE id = #{id}")
    Optional<Name> findById2(int id);

    @Select("SELECT * FROM names WHERE id > #{id}")
    List<Name> findByIdGreaterThan(int id);

    @Insert("insert into names (name) values (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Name name);

    @Update("update names set name = #{name} where id = #{id}")
    void updateName(Name updateName);

    @Delete("delete from names where id = #{id}")
    void deleteName(int id);
}
