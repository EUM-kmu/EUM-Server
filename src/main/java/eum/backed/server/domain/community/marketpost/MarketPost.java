package eum.backed.server.domain.community.marketpost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModelProperty;
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
public class MarketPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketPostId;

    @Column
    private String title;
    private String contents;
    private Long pay;
    private String location;
    private int volunteerTime;
    private int maxNumOfPeople;
    private int currentAcceptedPeople;
    private Date startDate;

    @Column
    @Enumerated(EnumType.STRING)
    private MarketType marketType;
    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Status", allowableValues = "RECRUITING, TRADING, COMPLETED")
    private Status status;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Slot", allowableValues = "AM, PM, ALL")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private MarketCategory marketCategory;

    @OneToMany(mappedBy = "marketPost", orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Regions regions;

    @OneToMany(mappedBy = "marketPost")
    private List<MarketComment> marketComments = new ArrayList<>();

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
    public  void updateDong(Regions regions){this.regions = regions;}

}
