package eum.backed.server.commumityapi.domain.user;

import eum.backed.server.admin.domain.inquiry.Inquiry;
import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.apply.Apply;
import eum.backed.server.commumityapi.domain.comment.Comment;
import eum.backed.server.commumityapi.domain.scrap.Scrap;
import eum.backed.server.commumityapi.domain.sleeperuser.SleeperUser;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.region.GU.Gu;
import lombok.*;
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
    private boolean isBanned;
    private int totalVolunteerTime;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


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
    @ManyToOne
    @JoinColumn(name="gu_id")
    private Gu gu;

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