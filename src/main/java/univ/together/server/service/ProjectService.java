package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddProjectScheduleDto;
import univ.together.server.dto.ModifyProjectInfoDto;
import univ.together.server.dto.ProjectDetailScheduleDto;
import univ.together.server.dto.ProjectDto;
import univ.together.server.dto.ProjectInformationDto;
import univ.together.server.dto.ProjectScheduleDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.Project;
import univ.together.server.model.SearchMember;
import univ.together.server.model.TagList;
import univ.together.server.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	public final List<String>invitationList;
	
	public List<ProjectScheduleDto> getProjectScheduleList(Long project_idx) {
		List<ProjectScheduleDto> projectScheduleDto = null;
		try {
			return projectRepository.getProjectScheduleList(project_idx).stream().map(p -> new ProjectScheduleDto(p)).collect(Collectors.toList());
		}catch(Exception e) {
			projectScheduleDto = new ArrayList<ProjectScheduleDto>();
			return projectScheduleDto;
		}
	}
	
	public ProjectDetailScheduleDto getDetailSchedule(Long schedule_idx) {
		return new ProjectDetailScheduleDto(projectRepository.getDetailSchedule(schedule_idx));
	}
	
	
	public String addInvitation(String user_nickname) {
		if (invitationList.contains(user_nickname))
			return "existed";
		invitationList.add(user_nickname);
		return "success";
	}
	
	@Transactional // 프로젝트 생성
	public void createProject( ProjectDto projectdto) {
		Project project = Project.builder()
				.project_name(projectdto.getProject_name())
				.project_status("A")
				.project_exp(projectdto.getProject_exp())
				.start_date(projectdto.getStart_date())
				.end_date(projectdto.getEnd_date())
				.professionality(projectdto.getProfessionality())
				.project_type(projectdto.getProject_type())
				.open_flag("Y")
				.build();
		
		Long pid = projectRepository.CreateProject(project);
		
		
		inviteMember(pid, invitationList);
		
		// 멤버를 INSERT하는 기능(나선재 작성)
		projectRepository.saveMember(projectdto.getUser_idx(), pid);
		for(int i=0; i<projectdto.getTag_num(); i++)
			projectRepository.insertProjectTag(pid, projectdto.getTag_name()[i],projectdto.getDetail_name()[i]);
		
	}
	
	//멤버 초대
	public void inviteMember(Long pid, List<String> invitationList) {
		projectRepository.inviteMember(pid, invitationList);
		invitationList.clear();
	}


	public List<String> searchMember( Long user_idx) {
		return projectRepository.SearchMember(user_idx);
	}
	
	public Object askUserInfo(String user_nickname){
		return projectRepository.askUserInfo(user_nickname);
	}
	
	@Transactional
	public String addSchedule(AddProjectScheduleDto addProjectScheduleDto) {
		String code = "";
		
		try {
			int changedRowNum = projectRepository.addSchedule(addProjectScheduleDto);
			if(changedRowNum == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		
		return code;
	}
	
	@Transactional
	public String inviteUser(Long project_idx, Long user_idx) {
		String code = "fail";
		try {
			Long piRowNum = projectRepository.checkUserProjectInvitation(project_idx, user_idx);
			Long pRowNum = projectRepository.checkUserProject(project_idx, user_idx);
			if(piRowNum == 0 && pRowNum == 0) {
				int rowNum = projectRepository.inviteUser(project_idx, user_idx);
				if(rowNum == 1) code = "success";
				else code = "fail";
			}
		}catch(Exception e) {
			e.printStackTrace();
			code = "fail";
		}
		return code;
	}
	
	@Transactional
	public String removeUser(Long project_idx, Long user_idx, Long logined_user_idx) {
		String code = "";
		try {
			String member_right = projectRepository.getRightUser(project_idx, logined_user_idx);
			if(!member_right.equals("Leader")) throw new Exception();
			int rowNum = projectRepository.removeUser(project_idx, user_idx);
			if(rowNum == 1) rowNum = projectRepository.subProjectMemberNum(project_idx);
			else throw new Exception();
			if(rowNum == 1) code = "success";
			else throw new Exception();
		}catch(Exception e) {
			code = "fail";
		}
		return code;
	}
	
	public List<TagList> getTagList() {
		return projectRepository.getTagList();
	}
	
	// ===================== 나중에 삭제 =====================
	/*
	public ProjectInfoDto getProjectInfo(Long project_idx) {
		ProjectInfoDto projectInfoDto = null;
		try {
			Project projectInfos = projectRepository.getProjectInfo(project_idx);
			projectInfoDto = new ProjectInfoDto(projectInfos);
			projectInfoDto.setCode("success");
			return projectInfoDto;
		}catch(Exception e) {
			projectInfoDto = new ProjectInfoDto();
			projectInfoDto.setCode("error");
			return projectInfoDto;
		}
	}
	
	@Transactional
	public String modifyProjectInfo(ModifyProjectDetailInfoDto modifyProjectDetailInfoDto) {
		String code = "";
		try {
			int rowNum = projectRepository.modifyProjectInfo(modifyProjectDetailInfoDto);
			if(rowNum == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		return code;
	}
	*/
	
	// ===================== 프로젝트 정보 얻기 =====================
	public ProjectInformationDto getProjectInfo(Long project_idx) {
		try {
			return new ProjectInformationDto(projectRepository.getProjectInfo(project_idx), projectRepository.getProjectTagList(project_idx));
		}catch(Exception e) {
			return null;
		}
	}
	// =========================================================
	
	// ===================== 프로젝트 정보 수정 =====================
	@Transactional
	public String modifyProjectInfo(ModifyProjectInfoDto modifyProjectInfoDto, Long project_idx) {
		try {
			int num = projectRepository.modifyProjectInfo(modifyProjectInfoDto, project_idx);
			if(num == 1) return "success";
			else throw new Exception();
		}catch(Exception e) {
			return "fail";
		}
	}
	// =========================================================
	
	// ===================== 팀원 관리 main =====================
	public List<SearchMemberProfileCardDto> manageMemberMain(Long project_idx) {
		List<SearchMemberProfileCardDto> smpcds = new ArrayList<SearchMemberProfileCardDto>();
		List<SearchMember> sms = projectRepository.getUserIdxByProjectIdx(project_idx);
		if(sms.size() >= 1) {
			for(SearchMember sm : sms) {
				smpcds.add(new SearchMemberProfileCardDto(sm));
			}
			return smpcds;
		}else return null;
	}
	// ========================================================
	
	// ===================== 팀원 추방 =====================
	@Transactional
	public String removeMember(Long user_idx, Long project_idx) {
		try {
			int num = projectRepository.removeMember(user_idx, project_idx);
			if(num == 1) { num = projectRepository.subProjectMemberNum(project_idx); }
			else { throw new Exception(); }
			if(num ==1) { return "success"; }
			else { throw new Exception(); }
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
		
	// ===================== 팀원 수정 =====================
	@Transactional
	public String modifyMember(Long user_idx, Long project_idx, String member_right) {
		try {
			return projectRepository.modifyMember(user_idx, project_idx, member_right) == 1? "success" : "fail";
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
	
}
