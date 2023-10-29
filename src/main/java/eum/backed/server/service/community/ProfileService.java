package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.domain.community.mylevel.MyLevel;
import eum.backed.server.domain.community.mylevel.MyLevelRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.region.DONG.DongRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;
    private final DongRepository dongRepository;
    private final MyLevelRepository myLevelRepository;
    private final BankAccountService bankAccountService;
    public DataResponse create(ProfileRequestDTO.CreateProfile createProfile, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Dong getDong = dongRepository.findByDong(createProfile.getDong()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));
        MyLevel getMyLevel = myLevelRepository.findById(1L).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());
        Profile profile = Profile.toEntiry(createProfile,getDong,getMyLevel,getUser);
        profileRepository.save(profile);
        bankAccountService.createUserBankAccount(createProfile.getNickname(), createProfile.getAccountPassword(),getUser);
        return new DataResponse<>().success("데이터 저장 성공");

    }
    private void validateNickname(String nickname){
        if(profileRepository.existsByNickname(nickname)) throw new IllegalArgumentException("이미 있는 닉네임");
    }
}
