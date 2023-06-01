package com.example.mybatisdemo2.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateForm {

  @NotBlank
  private String name;
}
