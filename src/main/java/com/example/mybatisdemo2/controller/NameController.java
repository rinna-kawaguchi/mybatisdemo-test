package com.example.mybatisdemo2.controller;

import com.example.mybatisdemo2.form.CreateForm;
import com.example.mybatisdemo2.entity.Name;
import com.example.mybatisdemo2.response.NameResponse;
import com.example.mybatisdemo2.service.NameService;
import com.example.mybatisdemo2.exception.ResourceNotFoundException;
import com.example.mybatisdemo2.form.UpdateForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class NameController {

    // field フィールド
    private final NameService nameService;

    // constructor コンストラクター
    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    @GetMapping("/names")
    public List<NameResponse> names() {
        List<Name> names = nameService.findAll();
        List<NameResponse> nameResponses = names.stream().map(NameResponse::new).toList();
        return nameResponses;
    }

    @GetMapping("/selectname")
    public List<NameResponse> selectname(@RequestParam (value = "id") int id) {
        List<Name> names = nameService.findById(id);
        List<NameResponse> nameResponses = names.stream().map(NameResponse::new).toList();
        return nameResponses;
    }

    @GetMapping("/names/{id}")
    public Name selectname2(@PathVariable (value = "id") int id) {
        return nameService.findById2(id);
    }

    @GetMapping("/selectnamegt")
    public List<NameResponse> selectnamegt(@RequestParam (value = "id") int id) {
        List<Name> names = nameService.findByIdGreaterThan(id);
        List<NameResponse> nameResponses = names.stream().map(NameResponse::new).toList();
        return nameResponses;
    }

    @PostMapping("/names")
    public ResponseEntity<String> createname(
            @RequestBody @Validated CreateForm form, UriComponentsBuilder uriBuilder) {
        Name name = nameService.saveName(form);
        URI url = uriBuilder
                .path("/names/" + name.getId())
                .build()
                .toUri();
        return ResponseEntity.created(url).body("user successfully created");
    }

    @PatchMapping("/names/{id}")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody UpdateForm form) {
        nameService.updateName(id, form.getName());
        System.out.println("id:" + id);
        System.out.println("name:" + form.getName());
        return ResponseEntity.ok("user successfully updated");
    }

    @DeleteMapping("/names/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        nameService.deleteName(id);
        return ResponseEntity.ok(Map.of("message", "user successfully deleted"));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(
            ResourceNotFoundException e, HttpServletRequest request) {

        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "Status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());

        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "Status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", "Please enter your name",
                "path", request.getRequestURI());

        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }
}
