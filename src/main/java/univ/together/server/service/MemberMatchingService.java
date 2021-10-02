package univ.together.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberProfileInfoDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.RegisterSearchMemberProfileCardDto;
import univ.together.server.model.User;
import univ.together.server.repository.MemberMatchingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberMatchingService {
	
	private final MemberMatchingRepository memberMatchingRepository;
	
	// user의 프로필 카드를 가져옴
	public SearchMemberProfileCardDto getUserProfileCard(Long userIdx) {
		try {
			return new SearchMemberProfileCardDto(memberMatchingRepository.getUserProfileCard(userIdx));
		}catch(Exception e) {
			return null;
		}
	}
	
	// user의 프로필 카드가 없는 경우, user의 정보를 가져온다.
	public MemberProfileInfoDto getUserProfileInfo(Long userIdx) {
		return new MemberProfileInfoDto(memberMatchingRepository.getUserProfileInfo(userIdx));
	}
	
	// 카드를 등록/업데이트 한다.
	@Transactional
	public void registerUpdateProfileCard(Long userIdx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		try {
			memberMatchingRepository.getUserProfileCard(userIdx);
			memberMatchingRepository.updateUserProfileCard(userIdx, registerSearchMemberProfileCardDto);
		}catch(Exception e) {
			memberMatchingRepository.registerUserProfileCard(userIdx, registerSearchMemberProfileCardDto);
		}
	}
	
	public List<User> getUserList(Long project_idx) {
		return memberMatchingRepository.getUserList();
	}
	
	

}
