package eum.backed.server.service.community;

import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.community.avatar.Avatar;
import eum.backed.server.domain.community.avatar.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final ModelMapper modelMapper;

    public List<ProfileResponseDTO.AvatarInfo> getAvatars (){
        List<Avatar> avatars = avatarRepository.findAllByAvatarIdNot(13L);
        List<ProfileResponseDTO.AvatarInfo> avatarInfos = new ArrayList<>();
        for(Avatar avatar : avatars){
            ProfileResponseDTO.AvatarInfo avatarInfo = modelMapper.map(avatar, ProfileResponseDTO.AvatarInfo.class);
            avatarInfos.add(avatarInfo);
        }
        return avatarInfos;
    }
}
