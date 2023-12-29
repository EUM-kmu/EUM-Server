package eum.backed.server.service.community;

import eum.backed.server.domain.community.block.Block;
import eum.backed.server.domain.community.block.BlockRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final UsersRepository usersRepository;

    public Boolean blockedAction(Users blocker,Users blocked){
        if(blockRepository.existsByBlockerAndBlocked(blocker,blocked)){
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
