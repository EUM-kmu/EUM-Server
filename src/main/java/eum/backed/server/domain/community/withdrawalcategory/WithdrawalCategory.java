package eum.backed.server.domain.community.withdrawalcategory;

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
public class WithdrawalCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalCategoryId;

    @Column
    private String content;

    @OneToMany(mappedBy = "withdrawalCategory")
    private List<WithdrawalUser> withdrawalUsers = new ArrayList<>();
}
