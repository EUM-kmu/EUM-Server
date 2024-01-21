package eum.backed.server.domain.community.marketpost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.DTO.request.MarketPostRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
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
    private String content;
    private Long pay;
    private String location;
    private int volunteerTime;
    private int maxNumOfPeople;
    private int currentAcceptedPeople;
    private Date startDate;
    private boolean isDeleted;

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

    @OneToMany(mappedBy = "marketPost",orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();


    @OneToMany(mappedBy = "marketPost",orphanRemoval = true)
    private List<MarketComment> marketComments = new ArrayList<>();

    @OneToMany(mappedBy = "marketPost")
    private List<ChatRoom> chatRooms  = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String contents) {
        this.content = contents;
    }
    public void addCurrentAcceptedPeople(){
        this.currentAcceptedPeople += 1;
    }
    public void subCurrentAcceptedPeople(){
        this.currentAcceptedPeople -= 1;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
    public void updateStartDate(Date startDate) {this.startDate = startDate;}
    public void updateSlot(Slot slot) {this.slot = slot;}
    public  void updateLocation(String location) {this.location = location;}

    public void updateVolunteerTime(int volunteerTime) {
        this.volunteerTime = volunteerTime;
    }

    public void updateMaxNumOfPeople(int maxNumOfPeople) {
        this.maxNumOfPeople = maxNumOfPeople;
    }

    public void updatePay(Long pay) {
        this.pay = pay;
    }

    public void updateDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public static MarketPost toEntity(MarketPostRequestDTO.MarketCreate marketCreate,Long pay, Users user,MarketCategory marketCategory) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN);
        return MarketPost.builder()
                .title(marketCreate.getTitle())
                .content(marketCreate.getContent())
                .startDate(simpleDateFormat.parse(marketCreate.getStartTime()))
                .slot(marketCreate.getSlot())
                .pay(pay)
                .isDeleted(false)
                .location(marketCreate.getLocation())
                .volunteerTime(marketCreate.getVolunteerTime())
                .marketType(marketCreate.getMarketType())
                .maxNumOfPeople(marketCreate.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .marketCategory(marketCategory)
                .build();
    }
}
