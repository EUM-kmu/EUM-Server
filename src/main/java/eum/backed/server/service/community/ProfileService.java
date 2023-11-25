package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.controller.community.dto.response.ProfileResponseDTO;
import eum.backed.server.domain.community.avatar.*;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.region.DONG.TownshipRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;
    private final TownshipRepository townShipRepository;
    private final AvatarRepository avatarRepository;
    private final BankAccountService bankAccountService;
    private final StandardRepository standardRepository;
    private final LevelService levelService;
    public APIResponse create(ProfileRequestDTO.CreateProfile createProfile, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 프로필이 있는 회원");
        Township getTownship = townShipRepository.findByName(createProfile.getTownShip()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));
        Standard initialLevel = standardRepository.findById(1L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        Avatar getAvatar = avatarRepository.findByAvatarNameAndStandard(createProfile.getAvatarName(),initialLevel).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());

        Profile profile = Profile.t0Entity(createProfile, getTownship, getAvatar,getUser);
        profileRepository.save(profile);

        getUser.updateRole(Role.ROLE_USER);
        userRepository.save(getUser);
        bankAccountService.createUserBankAccount(createProfile.getNickname(), createProfile.getAccountPassword(),getUser);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);

    }

    public APIResponse<ProfileResponseDTO.AllProfile> getMyProfile(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (!profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("프로필이 없는 유저");
        ProfileResponseDTO.AllProfile profileResponseDTO = ProfileResponseDTO.toNewProfileResponseDTO(getUser, getUser.getProfile());
//        final APIResponse successResponse = successResponsecessResponse.of(SuccessCode.SELECT_SUCCESS, profileResponseDTO);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, profileResponseDTO);
    }
    private void validateNickname(String nickname){
        if(profileRepository.existsByNickname(nickname)) throw new IllegalArgumentException("이미 있는 닉네임");
    }

    public APIResponse updateMyProfile(ProfileRequestDTO.UpdateProfile updateProfile,String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Profile getProfile = profileRepository.findByUser(getUser).orElseThrow(() -> new NullPointerException("프로필이 없습니다"));
        Township getTownship = townShipRepository.findByName(updateProfile.getTownShip()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));

        Standard currentLevel = getProfile.getAvatar().getStandard();
        Avatar getAvatar = avatarRepository.findByAvatarNameAndStandard(updateProfile.getAvatarName(),currentLevel).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));

        validateNickname(updateProfile.getNickname());
        getProfile.updateNickName(updateProfile.getNickname());
        getProfile.updateTownship(getTownship);
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
