//package eum.backed.server.common.DTO;
//
//import eum.backed.server.domain.admin.inquiry.Status;
//import lombok.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@Setter
//@RequiredArgsConstructor
//@Component
//public class DataResponse<T> {
//
//    private int state;
//    private String result;
//    private String massage;
//    private ErrorResponse error;
//    private T data;
//    public DataResponse(T data){
//        this.data = data;
//    }
//
//    public DataResponse(ErrorResponse errorResponse) {
//        this.error = errorResponse;
//    }
//
//
//    public DataResponse success(Object data,String msg, HttpStatus status){
//        DataResponse body = DataResponse.builder()
//                .data(data)
//                .state(status.value())
//                .result("success")
//                .massage(msg)
//                .build();
//        return body;
//    }
//    public DataResponse<T> success(T data, String msg) {
//        return success(data, msg, HttpStatus.OK);
//    }
//
//    public DataResponse success(String msg){
//        return success(Collections.emptyList(), msg, HttpStatus.OK);
//    }
//    public DataResponse success(T data){
//        return success(data, null, HttpStatus.OK);
//    }
//    public DataResponse fail(ErrorResponse error, String msg, HttpStatus status) {
//        DataResponse body = DataResponse.builder()
//                .state(status.value())
////                .data(data)
////                .result("fail")
//                .error(error)
//                .massage(msg)
////                .error(Collections.emptyList())
//                .build();
//        return body;
//    }
//    public DataResponse<ErrorResponse> fail(ErrorResponse error) {
//        return fail(error,"",HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    public DataResponse fail(String msg, HttpStatus status){
//        return fail(null, msg, status);
//    }
////    public DataResponse invalidFields(LinkedList<LinkedHashMap<String,String>> errors){
////        return DataResponse.builder()
////                .state(HttpStatus.BAD_REQUEST.value())
////                .data(Collections.emptyList())
////                .result("fail")
////                .massage("")
////                .error(errors)
////                .build();
////    }
//
//}
