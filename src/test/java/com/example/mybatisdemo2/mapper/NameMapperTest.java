package com.example.mybatisdemo2.mapper;

import com.example.mybatisdemo2.entity.Name;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NameMapperTest {

    @Autowired
    NameMapper nameMapper;


    @Test
    @Sql(
        scripts = {"classpath:/sqlannotation/delete-names.sql", "classpath:/sqlannotation/insert-names.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Transactional
    void 全てのユーザーが取得できること1() {
        List<Name> names = nameMapper.findAll();
        assertThat(names)
                .hasSize(3)
                .contains(
                        new Name(1, "清水"),
                        new Name(2, "小山"),
                        new Name(3, "田中")
                );
    }

    @Test
    @DataSet(value = "datasets/names.yml")
    @Transactional
    void 全てのユーザーが取得できること2() {
        List<Name> names = nameMapper.findAll();
        assertThat(names)
            .hasSize(3)
            .contains(
                new Name(1, "清水"),
                new Name(2, "小山"),
                new Name(3, "田中")
            );
    }

}