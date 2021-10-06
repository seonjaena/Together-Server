package univ.together.server.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberProfileInfoDto;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.RegisterSearchMemberProfileCardDto;
import univ.together.server.model.SearchMember;
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
	
	// user들의 프로필 카드 리스트(최근 등록자 top5)
	public List<SearchMemberProfileCardDto> getProfileCardList(Long userIdx) {
		return memberMatchingRepository.getProfileCardList(userIdx).stream().map(sm -> new SearchMemberProfileCardDto(sm)).collect(Collectors.toList());
	}
	
	// 검색 결과(가장 맞는 user top5)
	public List<SearchMemberProfileCardDto> getSearchResult(Long userIdx, MemberSearchingDto memberSearchingDto) {
		return calResult(memberMatchingRepository.getAllProfileCardList(userIdx), memberSearchingDto);
	}
	
	// 검색 결과 연산
	public List<SearchMemberProfileCardDto> calResult(List<SearchMember> cardList, MemberSearchingDto memberSearchingDto) {
		int num = 0;
		List<SearchMemberProfileCardDto> resultList = new ArrayList<SearchMemberProfileCardDto>();
		List<SearchMemberProfileCardDto> list = new ArrayList<SearchMemberProfileCardDto>();
		for (SearchMember searchMember : cardList) {
			
			if(searchMember.getUser_idx().getUser_age() >= memberSearchingDto.getMin_age() && 
					searchMember.getUser_idx().getUser_age() <= memberSearchingDto.getMax_age()) {
				num += 1;
			}
			
			if(memberSearchingDto.getLicense().size() >= 1) {
				for(String license : memberSearchingDto.getLicense()) {
					if(license.equals(searchMember.getUser_idx().getLicense1()) || 
						license.equals(searchMember.getUser_idx().getLicense2()) || 
						license.equals(searchMember.getUser_idx().getLicense3())) {
						num += 1;
					}
				}
			}else {
				num += 1;
			}
			
			if(searchMember.getUser_idx().getAddress() != null && !memberSearchingDto.getMain_addr().equals("") && 
					!memberSearchingDto.getReference_addr().equals("") && !memberSearchingDto.getDetail_addr().equals("")) {
				if(searchMember.getUser_idx().getAddress().getMain_addr().equals(memberSearchingDto.getMain_addr())) {
					num += 1;
					if(searchMember.getUser_idx().getAddress().getReference_addr().equals(memberSearchingDto.getReference_addr())) {
						num += 1;
						if(searchMember.getUser_idx().getAddress().getDetail_addr().equals(memberSearchingDto.getDetail_addr())) {
							num += 1;
						}
					}
				}
			}else if(memberSearchingDto.getMain_addr().equals("") && 
					memberSearchingDto.getReference_addr().equals("") && 
					memberSearchingDto.getDetail_addr().equals("")) {
				num += 1;
			}
			
			if(memberSearchingDto.getHobby_small_idx().size() >= 1) {
				int hobby_filter_num = memberSearchingDto.getHobby_small_idx().size();
				int hobby_num = searchMember.getUser_idx().getUser_hobbies().size();
				for(int i = 0; i < hobby_num; i++) {
					for(int j = 0; j < hobby_filter_num; j++) {
						if(searchMember.getUser_idx().getUser_hobbies() != null && 
								searchMember.getUser_idx().getUser_hobbies().get(i).getHobby_idx() != null && 
								searchMember.getUser_idx().getUser_hobbies().get(i).getHobby_idx().getUser_hobby_cat_small_idx() == 
								Long.parseLong(memberSearchingDto.getHobby_small_idx().get(j))) {
							num += 1;
						}
					}
				}
			}else {
				num += 1;
			}
			
			SearchMemberProfileCardDto smpcd = new SearchMemberProfileCardDto(searchMember);
			smpcd.setNum(num);
			resultList.add(smpcd);
				
			num = 0;
		}
		
		resultList = resultList.stream().sorted(Comparator.comparing(SearchMemberProfileCardDto::getNum).reversed()).collect(Collectors.toList());
		
		if(resultList.size() >= 3) {
			for(int i = 0; i < 3; i++) list.add(resultList.get(i));
		}else {
			for(int i = 0; i < resultList.size(); i++) list.add(resultList.get(i));
		}
		
		return list;
	}
	
}