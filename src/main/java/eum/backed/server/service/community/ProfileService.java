package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.auth.user.Role;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.auth.user.UsersRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;

    /**
     * 프로필 생성
     * @param createProfile
     * @param userId
     * @return
     */
    @Transactional
    public APIResponse<ProfileResponseDTO.ProfileResponse> create(ProfileRequestDTO.CreateProfile createProfile, Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        if (profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 프로필이 있는 회원");

        validateNickname(createProfile.getNickname());

        Profile profile = Profile.toEntity(createProfile,getUser);
        Profile savedProfile = profileRepository.save(profile);

        Role role = (getUser.getRole() == Role.ROLE_ORGANIZATION) ? Role.ROLE_ORGANIZATION : Role.ROLE_UNPASSWORD_USER;
        getUser.updateRole(role);

        ProfileResponseDTO.ProfileResponse createProfileResponse = ProfileResponseDTO.toProfileResponse(savedProfile);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,createProfileResponse);

    }

    /**
     * 프로필 조회
     * @param userId
     * @return
     */
    public APIResponse<ProfileResponseDTO.ProfileResponse> getMyProfile(Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (!profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("프로필이 없는 유저");
        ProfileResponseDTO.ProfileResponse profileResponseDTO = ProfileResponseDTO.toProfileResponse( getUser.getProfile());
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
     * @param userId
     * @return
     */
    public APIResponse updateMyProfile(ProfileRequestDTO.UpdateProfile updateProfile,Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        Profile getProfile = profileRepository.findByUser(getUser).orElseThrow(() -> new NullPointerException("프로필이 없습니다"));


        validateNickname(updateProfile.getNickname());
        getProfile.updateNickName(updateProfile.getNickname());
        profileRepository.save(getProfile);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }


}
