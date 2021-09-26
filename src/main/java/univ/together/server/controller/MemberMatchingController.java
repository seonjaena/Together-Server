package univ.together.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.service.MemberMatchingService;

@RestController
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberMatchingController {

	private final MemberMatchingService memberMatchingService;
	
	// 팀원 검색
	@GetMapping(value = "/search/{projectIdx}")
	public void memberSearching(@ModelAttribute MemberSearchingDto memberSearchingDto, 
								@PathVariable(name = "projectIdx") Long projectIdx) {
		memberMatchingService.getUserList(projectIdx);
	}
	
	// 팀원 매칭
	@GetMapping(value = "/matching")
	public void memberMatching() {
		
	}
	
}
