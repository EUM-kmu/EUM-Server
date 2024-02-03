package eum.backed.server.common.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Response<T> {
    private Data<T> data;
    private List<Data> included;

    @Builder
    public Response(final Data<T> data, List<Data> included){
        this.data = data;
        this.included = included;
    }


    @Builder
    public Response(final Data<T> data){
        this.data = data;
        this.included =Collections.emptyList();
    }


    public static  Response of(Data data,  List<Data> included){
        return new Response(data, included);

    }
    public static  Response of(Data data){
        return new Response(data);

    }


}
