package univ.together.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddProjectScheduleDto;
import univ.together.server.dto.ModifyProjectInfoDto;
import univ.together.server.dto.ProjectDetailScheduleDto;
import univ.together.server.dto.ProjectDto;
import univ.together.server.dto.ProjectInformationDto;
import univ.together.server.dto.ProjectScheduleDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.TagList;
import univ.together.server.service.ProjectService;

@RestController
@RequestMapping(value = "/project")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectService projectService;
	private final List<String> Nlist = new ArrayList<>();
	
	@GetMapping(value = "/main")
	public List<ProjectScheduleDto> main(@RequestParam(name = "project_idx") Long project_idx) {
		return projectService.getProjectScheduleList(project_idx);
	}
	
	@GetMapping(value = "/detailSchedule")
	public ProjectDetailScheduleDto detailSchedule(@RequestParam(name = "schedule_idx") Long schedule_idx) {
		return projectService.getDetailSchedule(schedule_idx);
	}
	
	@PostMapping(value = "/searchMember")
	public List<String> searchMember(@RequestBody Map<String, Long> user_idx){
		return projectService.searchMember(user_idx.get("user_idx"));
	}

	@GetMapping(value = "/inviteMember/{user_nickname}")
	public String addInvitationList(@PathVariable(name= "user_nickname") String user_nickname) {
		
		return projectService.addInvitation(user_nickname);
	}
	
	@PostMapping(value = "/createProject")
	public String finishCreating(@Valid @RequestBody ProjectDto projectdto,  BindingResult result) {
		if(result.hasErrors()) {
			return "failed";
		}
		
		projectService.createProject(projectdto);
			return "success";
	}
	
	@GetMapping(value = "/UserInfo/{user_nickname}")
	public Object askUserInfo(@PathVariable("user_nickname") String user_nickname ) {
		return projectService.askUserInfo(user_nickname);
	}
	
	@PostMapping(value = "/addSchedule")
	public String addSchedule(@RequestBody AddProjectScheduleDto addProjectScheduleDto) {
		return projectService.addSchedule(addProjectScheduleDto);
	}
	
	@GetMapping(value = "/inviteUser")
	public String inviteUser(@RequestParam(name = "project_idx") Long project_idx, @RequestParam(name = "user_idx") Long user_idx) {
		return projectService.inviteUser(project_idx, user_idx);
	}
	
	@GetMapping(value = "/removeUser")
	public String removeUser(@RequestParam(name = "project_idx") Long project_idx, 
							 @RequestParam(name = "user_idx") Long user_idx, 
							 @RequestParam(name = "logined_user_idx") Long logined_user_idx) {
		return projectService.removeUser(project_idx, user_idx, logined_user_idx);
	}
	
	@GetMapping(value = "/getTagList")
	public List<TagList> getTagList() {
		return projectService.getTagList();
	}
	
	
	// ===================== 나중에 삭제 =====================
//	@GetMapping(value = "/getInfo")
//	public ProjectInfoDto getProjectInfo(@RequestParam(name = "project_idx") Long project_idx) {
//		return projectService.getProjectInfo(project_idx);
//	}
//	
//	@PostMapping(value = "/modiifyProjectInfo")
//	public String modifyProjectInfo(@RequestBody ModifyProjectDetailInfoDto modifyProjectDetailInfoDto) {
//		return projectService.modifyProjectInfo(modifyProjectDetailInfoDto);
//	}
	// ====================================================
	
	// ===================== 프로젝트 정보 얻기 =====================
	@GetMapping(value = "/getInfo/{project_idx}")
	public ProjectInformationDto getProjectInfo(@PathVariable(name = "project_idx") Long project_idx) {
		return projectService.getProjectInfo(project_idx);
	}
	// =========================================================
	
	// ===================== 프로젝트 정보 수정 =====================
	@PostMapping(value = "/modifyInfo/{project_idx}")
	public String modifyProjectInfo(@PathVariable(name = "project_idx") Long project_idx, 
								  @RequestBody ModifyProjectInfoDto modifyProjectInfoDto) {
		return projectService.modifyProjectInfo(modifyProjectInfoDto, project_idx);
	}
	// =========================================================
	
	// ===================== 팀원 관리 main =====================
	@GetMapping(value = "/members/{project_idx}")
	public List<SearchMemberProfileCardDto> manageMemberMain(@PathVariable(name = "project_idx") Long project_idx) {
		return projectService.manageMemberMain(project_idx);
	}
	// ========================================================
	
	// ===================== 팀원 추방 =====================
	@PostMapping(value = "/removeMember/{user_idx}/{project_idx}")
	public String removeMember(@PathVariable(name = "user_idx") Long user_idx, @PathVariable(name = "project_idx") Long project_idx) {
		return projectService.removeMember(user_idx, project_idx);
	}
	// ========================================================
	
	// ===================== 팀원 수정 =====================
	@PostMapping(value = "/modifyMember/{user_idx}/{project_idx}")
	public String modifyMember(@PathVariable(name = "user_idx") Long user_idx, 
							   @PathVariable(name = "project_idx") Long project_idx, 
							   @RequestBody Map<String, String> member_right) {
		return projectService.modifyMember(user_idx, project_idx, member_right.get("member_right"));
	}
	// ========================================================
	
}
