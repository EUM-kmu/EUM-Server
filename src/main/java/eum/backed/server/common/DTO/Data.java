package eum.backed.server.common.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class Data<T> {
    private Long Id;
    private String type;
    private T attributes;
    private Map<String,Relationships> relationships;

    @Builder
    public Data(final String type, final Long id, final T attributes, final Map<String, Relationships> relationships) {
        this.type = type;
        this.Id = id;
        this.attributes = attributes;
        this.relationships = relationships;
    }
    @Builder
    public Data(final String type, final Long id, final T attributes) {
        this.type = type;
        this.Id = id;
        this.attributes = attributes;
    }
    public static Data of(final String type, final Long id, final Object attribute, final Map<String ,Relationships> relationships ){
        return new Data(type, id, attribute, relationships);
    }
    public static Data of(final String type, final Long id, final Object attribute){
        return new Data(type, id, attribute);
    }
}

