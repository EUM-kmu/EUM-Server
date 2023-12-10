package eum.backed.server.domain.community.user;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.admin.inquiry.Inquiry;
import eum.backed.server.domain.community.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.block.Block;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.promotionpost.PromotionPost;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.sleeperuser.SleeperUser;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.voteresult.VoteResult;
import eum.backed.server.domain.community.withdrawaluser.WithdrawalUser;
import io.swagger.annotations.ApiModelProperty;
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
    private String uid;
    private String email;
    private String password;
    private String phone;
    private Boolean isBanned;
    private Boolean isDeleted;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "role", allowableValues = "ROLE_USER, ROLE_TEMPORARY_USER, ROLE_AUTH_USER, ROLE_ORGANIZATION")
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "social signIn type")
    private SocialType socialType;



    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "blocker")
    private List<Block> blockers = new ArrayList<>();

    @OneToMany(mappedBy = "blocked")
    private List<Block> blockedUsers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToOne(mappedBy = "user")
    private UserBankAccount userBankAccount;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<MarketPost> marketPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<OpinionPost> opinionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<MarketComment> marketComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private SleeperUser sleeperUser;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private WithdrawalUser withdrawalUser;

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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<LikeOpinionPost> likeOpinionPosts  = new ArrayList<>();





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

    public void updateRole(Role role) {
        this.role = role;
    }

    public void removeEmail() {
        this.email = "";
    }
}