package univ.together.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberProfileInfoDto;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.RegisterSearchMemberProfileCardDto;
import univ.together.server.service.MemberMatchingService;

@RestController
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberMatchingController {

	private final MemberMatchingService memberMatchingService;
	
	// 팀원 검색 main page
	@GetMapping(value = "/search/main/{userIdx}")
	public SearchMemberProfileCardDto mainPage(@PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getUserProfileCard(userIdx);
	}
	
	// 카드 등록하는 페이지로 이동
	@GetMapping(value = "/search/register/{userIdx}")
	public MemberProfileInfoDto registerCardPage(@PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getUserProfileInfo(userIdx);
	}
	
	// 카드 등록
	@PostMapping(value = "/search/register/{userIdx}")
	public void registerCard(@PathVariable(name = "userIdx") Long userIdx, 
											 @RequestBody RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		memberMatchingService.registerUpdateProfileCard(userIdx, registerSearchMemberProfileCardDto);
	}
	
	
	// 팀원 검색
	@GetMapping(value = "/search/{projectIdx}")
	public void memberSearching(@ModelAttribute MemberSearchingDto memberSearchingDto, 
								@PathVariable(name = "projectIdx") Long projectIdx) {
		memberMatchingService.getUserList(projectIdx);
	}
	
}
