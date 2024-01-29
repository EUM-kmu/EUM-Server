package eum.backed.server.common.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class Relationships {
    private Map<String, Relationship> relationshipMap;

    @Getter
    @Builder
    public static class Relationship{
        private RelationshipData data;
    }
    @Getter
    @Builder
    public static class RelationshipData{
        private String type;
        private Long id;

    }

    public Relationships(Map<String, Relationship> relationshipMap) {
        this.relationshipMap = relationshipMap;
    }
}
