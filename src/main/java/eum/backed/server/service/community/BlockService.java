package eum.backed.server.service.community;

import eum.backed.server.domain.community.block.Block;
import eum.backed.server.domain.community.block.BlockRepository;
import eum.backed.server.domain.auth.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;

    /**
     * 차단하기
     * @param blocker 차단하는 유저
     * @param blocked 처덩덩헌 유저
     * @return
     */
    public Boolean blockedAction(Users blocker,Users blocked){
        if(blockRepository.existsByBlockerAndBlocked(blocker,blocked)){ //차단 / 해제
            Block block = blockRepository.findByBlockerAndBlocked(blocker, blocked).get();
            blockRepository.delete(block);
            return false;
        }
        Block block = Block.toEntity(blocker, blocked);
        blockRepository.save(block);
        return true;

    }
    public List<Users> getBlockedUser(Users getUser){
        List<Users> blockedUsers = new ArrayList<>();
//        내가 차단한 사람들
        List<Block> blockers = blockRepository.findByBlocker(getUser).orElse(Collections.emptyList());
        for (Block block:blockers){
            blockedUsers.add(block.getBlocked());
        }
//        날 차단힌 시림들
        List<Block> bloccked = blockRepository.findByBlocked(getUser).orElse(Collections.emptyList());
        for(Block block:bloccked){
            blockedUsers.add(block.getBlocker());
        }
        return blockedUsers;
    }
}
