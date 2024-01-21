package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.community.avatar.*;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
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
    private final AvatarRepository avatarRepository;

    /**
     * 프로필 생성
     * @param createProfile
     * @param email
     * @return
     */
    public APIResponse<ProfileResponseDTO.ProfileResponse> create(ProfileRequestDTO.CreateProfile createProfile, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 프로필이 있는 회원");


        Avatar getAvatar = avatarRepository.findByAvatarId(createProfile.getAvatarId()).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());

        Profile profile = Profile.toEntity(createProfile, getAvatar,getUser);
        Profile savedProfile = profileRepository.save(profile);

        Role role = (getUser.getRole() == Role.ROLE_ORGANIZATION) ? Role.ROLE_ORGANIZATION : Role.ROLE_UNPASSWORD_USER;
        getUser.updateRole(role);
        Users updatedUser= userRepository.save(getUser);


        ProfileResponseDTO.ProfileResponse createProfileResponse = ProfileResponseDTO.toProfileResponse(updatedUser, savedProfile);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,createProfileResponse);

    }

    /**
     * 프로필 조회
     * @param email
     * @return
     */
    public APIResponse<ProfileResponseDTO.ProfileResponse> getMyProfile(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (!profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("프로필이 없는 유저");
        ProfileResponseDTO.ProfileResponse profileResponseDTO = ProfileResponseDTO.toProfileResponse(getUser, getUser.getProfile());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, profileResponseDTO);
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     */
    private void validateNickname(String nickname){
        if(profileRepository.existsByNickname(nickname)) throw new IllegalArgumentException("이미 있는 닉네임");
    }

    /**
     * 프로필 수정
     * @param updateProfile
     * @param email
     * @return
     */
    public APIResponse updateMyProfile(ProfileRequestDTO.UpdateProfile updateProfile,String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Profile getProfile = profileRepository.findByUser(getUser).orElseThrow(() -> new NullPointerException("프로필이 없습니다"));

        Avatar getAvatar = (getUser.getRole() == Role.ROLE_ORGANIZATION)
                ? avatarRepository.findById(13L).orElseThrow(()->new IllegalArgumentException("초기데이터 미세팅"))
                :avatarRepository.findByAvatarId(updateProfile.getAvatarId()).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));

        validateNickname(updateProfile.getNickname());
        getProfile.updateNickName(updateProfile.getNickname());
        getProfile.upDateAvatar(getAvatar);
        profileRepository.save(getProfile);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }


}
