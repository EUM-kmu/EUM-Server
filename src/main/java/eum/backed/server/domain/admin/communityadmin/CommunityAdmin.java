package eum.backed.server.domain.admin.communityadmin;

import eum.backed.server.domain.admin.inquiryanswer.InquiryAnswer;
import eum.backed.server.domain.admin.notification.Notification;
import eum.backed.server.common.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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
public class CommunityAdmin extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityAminId;

    @Column
    private String name;
    private String password;
    private String phone;
    private String address;
    private String email;
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "communityAdmin", orphanRemoval = true)
    private List<InquiryAnswer> inquiryAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "communityAdmin", orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

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
