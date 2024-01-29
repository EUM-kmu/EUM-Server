package eum.backed.server.common.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class Response<T> {
    @Getter
    @NoArgsConstructor
    public class Data{
        private String type;
        private long Id;
        private T attributes;
        private Relationships relationships;
    }
    private Data data;
    private List<Data> included;




}
