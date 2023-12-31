package eum.backed.server.domain.community.block;

import eum.backed.server.domain.community.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private Users blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private Users blocked;


    public static Block toEntity(Users blocker, Users blocked) {
        return Block.builder().blocker(blocker).blocked(blocked).build();
    }
}
