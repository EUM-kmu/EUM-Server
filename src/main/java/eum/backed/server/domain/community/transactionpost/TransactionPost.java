package eum.backed.server.domain.community.transactionpost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.category.TransactionCategory;
import eum.backed.server.domain.community.comment.TransactionComment;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionPostId;

    @Column
    private String title;
    private String contents;
    private int pay;
    private String location;
    private int volunteerTime;
    private Boolean needHelper;
    private int maxNumOfPeople;
    private int currentAcceptedPeople;
    private Date startDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @Enumerated(EnumType.STRING)
    private Slot slot;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private TransactionCategory transactionCategory;

    @OneToMany(mappedBy = "transactionPost", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @OneToMany(mappedBy = "transactionPost")
    private List<TransactionComment> transactionComments = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }
    public void addCurrentAcceptedPeople(){
        this.currentAcceptedPeople += 1;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
    public void updateStartDate(Date startDate) {this.startDate = startDate;}
    public void updateSlot(Slot slot) {this.slot = slot;}
    public  void updateLocation(String location) {this.location = location;}
    public  void updateDong(Dong dong){this.dong = dong;}
}
