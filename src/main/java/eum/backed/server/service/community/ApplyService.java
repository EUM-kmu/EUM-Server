package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.TransactionPostRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final UsersRepository usersRepository;
    private final TransactionPostRepository transactionPostRepository;
    private final ProfileRepository profileRepository;
    private final ApplyResponseDTO applyResponseDTO;


    public DataResponse doApply(ApplyRequestDTO.Apply applyRequest, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("Invalid email"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(applyRequest.getPostId()).orElseThrow(() -> new NullPointerException("Invalid postId"));
        if (applyRepository.existsByUserAndTransactionPost(getUser,getTransactionPost)) throw new IllegalArgumentException("이미 신청했음");
        Apply apply = Apply.toEntity(applyRequest.getIntroduction(), getUser, getTransactionPost);
        applyRepository.save(apply);
        return new DataResponse<>().success("지원 신청 완료");
    }

    public DataResponse<List<ApplyResponseDTO.ApplyListResponse>> getApplyList(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("Invalid email"));
        List<TransactionPost> transactionPosts = transactionPostRepository.findByUserOrderByCreateDateDesc(getUser).orElse(Collections.emptyList()); //로그인 유저가 작성한 게시글 목록 조회
        List<ApplyResponseDTO.ApplyListResponse> getAllApplicants = getAllApplicants(transactionPosts);
        return new DataResponse<>(getAllApplicants).success(getAllApplicants, "신청 목록 조회성공");

    }
    private List<ApplyResponseDTO.ApplyListResponse> getAllApplicants(List<TransactionPost> transactionPosts){
//      빈 신청 목록 리스트
        List<ApplyResponseDTO.ApplyListResponse> applyListResponses = new ArrayList<>();
        for (TransactionPost transactionPost : transactionPosts){
//            유저가 작성한 게시글에 매푕되는 신청 리스트 조회
            List<Apply> applies = applyRepository.findByTransactionPostOrderByCreateDateDesc(transactionPost).orElse(Collections.emptyList());
            for(Apply apply : applies){
//                해당 신청과 매핑되는 신청자 프로필 조회
                Profile getApplicantProfile = profileRepository.findByUser(apply.getUser()).orElseThrow(() -> new NullPointerException("프로필이 없는 유저"));
                ApplyResponseDTO.ApplyListResponse singleApplyResponseDTO = applyResponseDTO.newApplyListResponse(transactionPost, apply.getUser(), getApplicantProfile,apply);
                applyListResponses.add(singleApplyResponseDTO);
            }
        }
        return applyListResponses;
    }

    public DataResponse accept(Long applyId,String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("Invalid email"));
        Apply getApply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid id"));
        if (getApply.getTransactionPost().getUser() != getUser) throw new IllegalArgumentException("해당 게시글에 대한 권한이 없다");
        getApply.updateAccepted(true);
        applyRepository.save(getApply);
        return new DataResponse<>().success("선정성공");

    }
}
