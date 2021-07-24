package univ.together.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.service.TeamMatchingService;

@RestController(value="/matching")
@RequiredArgsConstructor
public class TeamMatchingController { // 매칭에 필요한 정보 : 프로젝트 전문성, 프로젝트 유형, 시작기간,종료기간, mbti정보(팀장만 확인? 팀원 모두 확인? 명확하지않음)
	private final TeamMatchingService matchingService;
	
	@GetMapping(value="/team")
	public List<String> teamMatching(@ModelAttribute TeamMatchingDto teammatchingdto) {
		return matchingService.teamMatching(teammatchingdto);
	} // user_idx 를 가져와서 user에 대한 정보와 매칭되는 프로젝트 네잉을 리턴하는거? x
	// 원하는 project에 대한 정보를 입력 하면 dto로 전달, 해당 dto와 비교
	
}
