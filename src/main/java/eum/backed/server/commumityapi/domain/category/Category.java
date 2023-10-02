package eum.backed.server.commumityapi.domain.category;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column
    private String contents;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
}
