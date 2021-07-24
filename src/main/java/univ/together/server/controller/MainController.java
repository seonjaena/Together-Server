package univ.together.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.ProjectListDto;
import univ.together.server.service.MainService;

@RestController
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;
	
	@GetMapping(value = "/main")
	public List<ProjectListDto> main(@RequestParam(name = "user_idx") Long user_idx) {
		return mainService.findProjectByUserIdx(user_idx);
	}
	
}
