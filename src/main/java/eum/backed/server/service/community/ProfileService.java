package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.Data;
import eum.backed.server.common.DTO.Relationships;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.controller.community.DTO.response.UsersResponseDTO;
import eum.backed.server.domain.community.avatar.*;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.auth.user.Role;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.auth.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;
    private final AvatarRepository avatarRepository;

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


        Avatar getAvatar = avatarRepository.findByAvatarId(createProfile.getAvatarId()).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());

        Profile profile = Profile.toEntity(createProfile, getAvatar,getUser);
        Profile savedProfile = profileRepository.save(profile);

        Role role = (getUser.getRole() == Role.ROLE_ORGANIZATION) ? Role.ROLE_ORGANIZATION : Role.ROLE_UNPASSWORD_USER;
        getUser.updateRole(role);

        ProfileResponseDTO.ProfileResponse createProfileResponse = ProfileResponseDTO.toProfileResponse(savedProfile);
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
        ProfileResponseDTO.ProfileResponse profileResponseDTO = ProfileResponseDTO.toProfileResponse( getUser.getProfile());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, profileResponseDTO);
    }
    public Response<ProfileResponseDTO.ProfileResponse> get(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (!profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("프로필이 없는 유저");
        Profile getProfile = getUser.getProfile();

        ProfileResponseDTO.ProfileResponse profileResponseDTO = ProfileResponseDTO.toProfileResponse(getProfile);
        UsersResponseDTO.UserRole userRole = UsersResponseDTO.UserRole.builder().role(getUser.getRole()).build();

        Relationships.RelationshipData user = Relationships.RelationshipData.builder().id(getUser.getUserId()).type("user").build();
        List<Relationships.RelationshipData> relationshipData = new ArrayList<>();
        relationshipData.add(user);
        Relationships relationships = Relationships.of(relationshipData);
        HashMap<String,Relationships> relationshipsHashMap  = new HashMap<>();
        relationshipsHashMap.put("user", relationships);

        List<Data> userDataList = new ArrayList<>();
        Data userData = Data.of("user",getUser.getUserId(), userRole);
        userDataList.add(userData);

        Data profileData = Data.of("profile", getProfile.getProfileId(), profileResponseDTO, relationshipsHashMap);

        return Response.of(profileData,userDataList) ;
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
