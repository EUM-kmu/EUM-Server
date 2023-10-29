package eum.backed.server.domain.community.user;

import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.admin.inquiry.Inquiry;
import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.TransactionComment;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.promotionpost.PromotionPost;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.sleeperuser.SleeperUser;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.voteresult.VoteResult;
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
public class Users extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String email;
    private String password;
    private String phone;
    private boolean banned;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;



    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> authorities = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToOne(mappedBy = "user")
    private UserBankAccount userBankAccount;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<TransactionPost> transactionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<OpinionPost> opinionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<TransactionComment> transactionComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<SleeperUser> sleeperUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<VotePost> votePosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<VoteComment> voteComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<VoteResult> voteResults = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<OpinionComment> opinionComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PromotionPost> promotionPosts = new ArrayList<>();


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