package eum.backed.server.domain.auth.user;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.block.Block;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.withdrawaluser.WithdrawalUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Users extends BaseTimeEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String uid;
    private String email;
    private String password;
    private String phone;
    private Boolean isBanned;
    private boolean isDeleted;

    public void setDeleted() {
        isDeleted = true;
    }

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
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
    private List<MarketComment> marketComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();


    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private WithdrawalUser withdrawalUser;




//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void removeEmail() {
        this.email = "";
    }
}