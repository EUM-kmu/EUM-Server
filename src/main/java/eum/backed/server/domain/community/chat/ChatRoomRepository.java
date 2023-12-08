package eum.backed.server.domain.community.chat;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<List<ChatRoom>> findByPostWriter(Users user);

    Optional<List<ChatRoom>> findByApplicant(Users user);

    Optional<List<ChatRoom>> findByPostWriterOrApplicant(Users user, Users users);

    Optional<ChatRoom> findByMarketPostAndApplicant(MarketPost marketPost, Users applicant);

}
