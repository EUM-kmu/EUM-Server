package eum.backed.server.common.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Relationships {

    private List<RelationshipData> data;

    @Getter
    @Builder
    public static class RelationshipData{
        private String type;
        private Long id;
    }
    public Relationships(List<RelationshipData> data) {
        this.data = data;
    }
    public static Relationships of(List<RelationshipData> relationshipData){
        return new Relationships(relationshipData);
    }




}
