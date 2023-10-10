package eum.backed.server.controller.community.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class Response<T> {
    @Getter
    @Builder
    public static class Body{
        private int state;
        private String result;
        private String massage;
        private Object data;
        private Object error;
    }

    public ResponseEntity<?> success(Object data, String msg, HttpStatus status){
        Body body = Body.builder()
                .state(status.value())
                .data(data)

                .result("success")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }
    public Body su(Object data, String msg, HttpStatus status){
        Body body = Body.builder()
                .state(status.value())
                .data(data)

                .result("success")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return body;
    }
    public ResponseEntity<?> success(String msg){
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }
    public Body succ(String msg){
        return su(Collections.emptyList(), msg, HttpStatus.OK);
    }

    public Body success(Object data){
        return su(data, null, HttpStatus.OK);
    }
    public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("fail")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }
    public ResponseEntity<?> fail(String msg, HttpStatus status){
        return fail(Collections.emptyList(), msg, status);
    }
    public ResponseEntity<?> invalidFields(LinkedList<LinkedHashMap<String,String>> errors){
        Body body = Body.builder()
                .state(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .result("fail")
                .massage("")
                .error(errors)
                .build();
        return ResponseEntity.ok(body);
    }
}