package eum.backed.server.domain.community.avatar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarId;

    @Column
    private String avatarPhotoUrl;
    private String simpleAvatarPhotoUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private AvatarName avatarName;


}
