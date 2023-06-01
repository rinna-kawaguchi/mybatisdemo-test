package com.example.mybatisdemo2.service;

import com.example.mybatisdemo2.entity.Name;
import com.example.mybatisdemo2.exception.ResourceNotFoundException;
import com.example.mybatisdemo2.mapper.NameMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NameServiceImplTest {

    @InjectMocks
    NameServiceImpl nameServiceImpl;

    @Mock
    NameMapper nameMapper;

    @Test
    public void findAllですべてのユーザーが返されること() {
        doReturn(List.of(new Name(1, "tanaka"), new Name(2, "koyama"))).when(nameMapper).findAll();

        List<Name> actual = nameServiceImpl.findAll();
        assertThat(actual).isEqualTo(List.of(new Name(1, "tanaka"), new Name(2, "koyama")));
        verify(nameMapper).findAll();
    }

    @Test
    public void 存在しないidをクエリパラメータで指定した時にからのリストが帰ってくること() {
        List<Name> emptyList = new ArrayList<Name>();
        doReturn(emptyList).when(nameMapper).findById(99);

        List<Name> actual = nameServiceImpl.findById(99);
        assertThat(actual).isEqualTo(emptyList);
        verify(nameMapper).findById(99);
    }

    @Test
    public void 存在する名前のIDを指定した時に正常に名前が返されること() {
        doReturn(Optional.of(new Name(1, "tanaka"))).when(nameMapper).findById2(1);

        Name actual = nameServiceImpl.findById2(1);
        assertThat(actual).isEqualTo(new Name(1, "tanaka"));
        verify(nameMapper).findById2(1);
    }

    @Test
    public void 存在しないidを指定した時ResourceNotFoundExceptionがスローされること1() {
        when(nameMapper.findById2(99)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> nameServiceImpl.findById2(99));
        verify(nameMapper).findById2(99);
    }

    @Test
    public void 存在しないIDを指定した時に例外がスローされること2() {
        doReturn(Optional.empty()).when(nameMapper).findById2(99);

        assertThatThrownBy(() -> nameServiceImpl.findById2(99))
                .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo( "This id is not found");
                });
    }

    @Test
    public void 存在しないIDを指定した時に例外がスローされること3() {
        doReturn(Optional.empty()).when(nameMapper).findById2(99);

        assertThatThrownBy(() -> nameServiceImpl.findById2(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("This id is not found");
    }

    @Test
    public void 存在しないIDを指定した時に例外がスローされること4() {
        doReturn(Optional.empty()).when(nameMapper).findById2(99);

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> nameServiceImpl.findById2(99));
    }

}
