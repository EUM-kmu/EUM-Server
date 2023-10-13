package eum.backed.server.domain.community.user;

import eum.backed.server.domain.admin.inquiry.Inquiry;
import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.comment.Comment;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.sleeperuser.SleeperUser;
import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.region.GU.Gu;
import lombok.*;
import org.hibernate.type.CharacterType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Users extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String email;
    private String password;
    private String introduction;
    private String name;
    private String sex;
    private Date birth;
    private String nickname;
    private String address;
    private String phone;
    private boolean banned;
    private int totalVolunteerTime;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeOfCharacter typeOfCharacter;



    @ManyToOne
    @JoinColumn(name="gu_id")
    private Gu gu;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> authorities = new ArrayList<>();


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<SleeperUser> sleeperUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Inquiry> inquiries = new ArrayList<>();

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}