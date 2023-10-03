package eum.backed.server.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class DataResponse<T> {
    private int state;
    private String result;
    private String massage;
    private Object error;
    private T data;
    public DataResponse(T data){
        this.data = data;
    }

    public DataResponse success(Object data,String msg, HttpStatus status){
        DataResponse body = DataResponse.builder()
                .data(data)
                .state(status.value())
                .result("success")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return body;
    }
    public DataResponse<T> success(T data, String msg) {
        return success(data, msg, HttpStatus.OK);
    }
    public DataResponse success(String msg){
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }
    public DataResponse success(T data){
        return success(data, null, HttpStatus.OK);
    }
    public DataResponse fail(Object data, String msg, HttpStatus status) {
        DataResponse body = DataResponse.builder()
                .state(status.value())
                .data(data)
                .result("fail")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return body;
    }
    public DataResponse fail(String msg, HttpStatus status){
        return fail(Collections.emptyList(), msg, status);
    }

}
