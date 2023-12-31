package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.community.avatar.*;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.region.RegionsRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;
    private final RegionsRepository regionsRepository;
    private final AvatarRepository avatarRepository;
    private final StandardRepository standardRepository;
    private final LevelService levelService;
    public APIResponse<ProfileResponseDTO.ProfileResponse> create(ProfileRequestDTO.CreateProfile createProfile, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 프로필이 있는 회원");
        Regions getRegions = regionsRepository.findById(createProfile.getRegionId()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));
        if(getRegions.getRegionType() != RegionType.DONG) throw new IllegalArgumentException("행정동으로만 설정가능");
        Standard initialLevel = standardRepository.findById(1L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        Avatar getAvatar = avatarRepository.findByAvatarNameAndStandard(createProfile.getAvatarName(),initialLevel).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());

        Profile profile = Profile.toEntity(createProfile, getRegions, getAvatar,getUser);
        Profile savedProfile = profileRepository.save(profile);

        Role role = (getUser.getRole() == Role.ROLE_ORGANIZATION) ? Role.ROLE_ORGANIZATION : Role.ROLE_UNPASSWORD_USER;
        getUser.updateRole(role);
        Users updatedUser= userRepository.save(getUser);


        int getNextStandard = standardRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("초기데이터 설정오류")).getStandard();

        ProfileResponseDTO.ProfileResponse createProfileResponse = ProfileResponseDTO.toProfileResponse(updatedUser, savedProfile,getNextStandard);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,createProfileResponse);

    }

    public APIResponse<ProfileResponseDTO.ProfileResponse> getMyProfile(String email, int nextStandard) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (!profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("프로필이 없는 유저");
        ProfileResponseDTO.ProfileResponse profileResponseDTO = ProfileResponseDTO.toProfileResponse(getUser, getUser.getProfile(),nextStandard);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, profileResponseDTO);
    }
    private void validateNickname(String nickname){
        if(profileRepository.existsByNickname(nickname)) throw new IllegalArgumentException("이미 있는 닉네임");
    }

    public APIResponse updateMyProfile(ProfileRequestDTO.UpdateProfile updateProfile,String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Profile getProfile = profileRepository.findByUser(getUser).orElseThrow(() -> new NullPointerException("프로필이 없습니다"));
        Regions getRegions = regionsRepository.findById(updateProfile.getRegionId()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));


        Standard currentLevel = getProfile.getAvatar().getStandard();
        Avatar getAvatar = (getUser.getRole() == Role.ROLE_ORGANIZATION)
                ? avatarRepository.findByAvatarName(AvatarName.ORGANIZATION).orElseThrow(()->new IllegalArgumentException("초기데이터 미세팅"))
                :avatarRepository.findByAvatarNameAndStandard(updateProfile.getAvatarName(),currentLevel).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));

        validateNickname(updateProfile.getNickname());
        getProfile.updateNickName(updateProfile.getNickname());
        getProfile.updateRegions(getRegions);
        getProfile.upDateAvatar(getAvatar);
        getProfile.updateInstroduction(updateProfile.getIntroduction());
        profileRepository.save(getProfile);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }

    public void updateTotalSunrise(Profile profile, Long amount){
        if(profile.getAvatar().getAvatarName() != AvatarName.ORGANIZATION){
            int sunrise = amount.intValue();
            profile.addTotalSunrisePay(sunrise);
            Profile updatedProfile=profileRepository.save(profile);

            levelService.levelUp(updatedProfile);
        }
    }

}
