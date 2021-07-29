package univ.together.server.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddProjectScheduleDto;
import univ.together.server.dto.ModifyProjectDetailInfoDto;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectSchedule;
import univ.together.server.model.TagList;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

	private final EntityManager em;

	public List<ProjectSchedule> getProjectScheduleList(Long project_idx) {
		return em
				.createQuery(
						"SELECT ps FROM ProjectSchedule ps " + "JOIN FETCH ps.project_idx "
								+ "JOIN FETCH ps.writer_idx " + "WHERE ps.project_idx.project_idx = :project_idx",
						ProjectSchedule.class)
				.setParameter("project_idx", project_idx).getResultList();
	}
	
	public ProjectSchedule getDetailSchedule(Long schedule_idx) {
		return em.createQuery("SELECT ps FROM ProjectSchedule ps JOIN FETCH ps.writer_idx WHERE ps.schedule_idx = :schedule_idx", ProjectSchedule.class)
				.setParameter("schedule_idx", schedule_idx)
				.getSingleResult();
	}

	// 프로젝트 생성
	public Long CreateProject(Project project) {
		em.persist(project);

		return em.createQuery("SELECT p.project_idx FROM Project p ORDER BY p.project_idx DESC", Long.class)
				.setFirstResult(0).setMaxResults(1).getSingleResult();
	}

	// 멤버 초대(프로젝트 생성 당시)
	public void inviteMember(long project_idx, List<String> invitationList) {
		List<Long> userList = new ArrayList();

		for (String nickname : invitationList) {
			userList.add(em.createQuery("SELECT user_idx FROM User WHERE user_nickname = :invitationList", Long.class)
					.setParameter("invitationList", nickname).getSingleResult());
		}

		for (Long idx : userList) {
			em.createNativeQuery(
					"INSERT INTO project_invitation(project_idx, user_idx, invite_datetime) VALUES (:project_idx, :user_idx,:invite_time)")
					.setParameter("project_idx", project_idx).setParameter("user_idx", idx)
					.setParameter("invite_time", LocalDateTime.now()).executeUpdate();
		}
	}

	// 프로젝트의 멤버 INSERT ==> 프로젝트 리더 (나선재 작성)
	public void saveMember(Long user_idx, Long project_idx) {
		em.createNativeQuery("INSERT INTO member (project_idx, user_idx, member_right) VALUES (:project_idx, :user_idx, :member_right)")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.setParameter("member_right", "Leader")
				.executeUpdate();
	}

	public List<String> SearchMember(Long user_idx) {
		return em
				.createQuery("SELECT u.user_nickname FROM User u WHERE u.user_idx != :user_idx", String.class)
				.setParameter("user_idx", user_idx)
				.getResultList();
	}

	public Object askUserInfo(String search_nickname) {

		Query query = em.createQuery(
				"SELECT u.user_nickname, m.mbti_name, u.user_age, u.license1, u.license2, u.license3, u.user_profile_photo, u.address"
						+ " FROM User u INNER JOIN Mbti m ON u.user_mbti = m.mbti_idx WHERE u.user_nickname = :search_nickname")
				.setParameter("search_nickname", search_nickname);

		Object resultList = query.getResultList();
		return resultList;
	}

	// + 점수높은 유저 매칭

	public Project getProjectInfo(Long project_idx) {
		return em
				.createQuery("SELECT p FROM Project p " + "WHERE p.project_idx = :project_idx "
						+ "AND p.project_status = :project_status", Project.class)
				.setParameter("project_idx", project_idx).setParameter("project_status", "A").getSingleResult();
	}
	
	public int addSchedule(AddProjectScheduleDto addProjectScheduleDto) {
		return em.createNativeQuery("INSERT INTO project_schedule " + 
						"(project_idx, schedule_name, schedule_content, schedule_start_datetime, schedule_end_datetime, writer_idx) VALUES " + 
						"(:project_idx, :schedule_name, :schedule_content, :schedule_start_datetime, :schedule_end_datetime, :writer_idx)")
				.setParameter("project_idx", addProjectScheduleDto.getProject_idx())
				.setParameter("schedule_name", addProjectScheduleDto.getSchedule_name())
				.setParameter("schedule_content", addProjectScheduleDto.getSchedule_content())
				.setParameter("schedule_start_datetime", addProjectScheduleDto.getSchedule_start_datetime())
				.setParameter("schedule_end_datetime", addProjectScheduleDto.getSchedule_end_datetime())
				.setParameter("writer_idx", addProjectScheduleDto.getWriter_idx())
				.executeUpdate();
	}
	
	// ========================= 멤버 방출 =======================================
	
	// 해당 멤버의 권한을 검증한다.
	public String getRightUser(Long project_idx, Long logined_user_idx) {
		return em.createQuery("SELECT m.member_right FROM Member m " + 
						"WHERE m.project_idx.project_idx = :project_idx " + 
						"AND m.user_idx.user_idx = :logined_user_idx", String.class)
				.setParameter("project_idx", project_idx)
				.setParameter("logined_user_idx", logined_user_idx)
				.getSingleResult();
	}
	
	// 프로젝트의 멤버 수를 줄인다.
	public int subProjectMemberNum(Long project_idx) {
		return em.createQuery("UPDATE Project p SET p.member_num = p.member_num - :minus_num WHERE p.project_idx = :project_idx")
				.setParameter("minus_num", 1)
				.setParameter("project_idx", project_idx)
				.executeUpdate();
	}
		
	// 멤버를 방출한다.
	public int removeUser(Long project_idx, Long user_idx) {
		return em.createQuery("DELETE Member m WHERE m.project_idx.project_idx = :project_idx AND m.user_idx.user_idx = :user_idx")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.executeUpdate();
	}
	
	// =========================================================================
	
	
	// ========================= 멤버 초대(프로젝트 생성 후) =========================
	
	// 해당 유저가 초대했던 목록에 있는지 확인한다.
	public Long checkUserProjectInvitation(Long project_idx, Long user_idx) {
		return em.createQuery("SELECT COUNT(pi.project_invitation_idx) " + 
							"FROM ProjectInvitation pi " + 
							"WHERE pi.project_idx.project_idx = :project_idx " + 
							"AND pi.user_idx.user_idx = :user_idx ", Long.class)
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.getSingleResult();
	}
	
	// 해당 유저가 이미 프로젝트의 멤버인지 확인한다/
	public Long checkUserProject(Long project_idx, Long user_idx) {
		return em.createQuery("SELECT COUNT(m.member_idx) " + 
							"FROM Member m " + 
							"WHERE m.project_idx.project_idx = :project_idx " + 
							"AND m.user_idx.user_idx = :user_idx", Long.class)
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.getSingleResult();
	}
	
	// 멤버를 초대한다.
	public int inviteUser(Long project_idx, Long user_idx) {
		return em.createNativeQuery("INSERT INTO project_invitation (project_idx, user_idx, invite_datetime) VALUES (:project_idx, :user_idx, :invite_datetime)")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.setParameter("invite_datetime", LocalDateTime.now())
				.executeUpdate();
	}

	// =========================================================================

	public int modifyProjectInfo(ModifyProjectDetailInfoDto modifyProjectDetailInfoDto) {
		return em.createQuery("UPDATE Project p SET p.project_name = :project_name, " + 
							"p.project_exp = :project_exp, " + 
							"p.start_date = :start_date, " + 
							"p.end_date = :end_date, " + 
							"p.professionality = :professionality, " + 
							"p.project_type = :project_type " + 
							"WHERE p.project_idx = :project_idx")
				.setParameter("project_name", modifyProjectDetailInfoDto.getProject_name())
				.setParameter("project_exp", modifyProjectDetailInfoDto.getProject_exp())
				.setParameter("start_date", modifyProjectDetailInfoDto.getStart_date())
				.setParameter("end_date", modifyProjectDetailInfoDto.getEnd_date())
				.setParameter("professionality", modifyProjectDetailInfoDto.getProfessionality())
				.setParameter("project_type", modifyProjectDetailInfoDto.getProject_type())
				.setParameter("project_idx", modifyProjectDetailInfoDto.getProject_idx())
				.executeUpdate();
	}
	
	public List<TagList> getTagList() {
		return em.createQuery("SELECT t FROM TagList t",TagList.class).getResultList();
	}
	
	public void insertProjectTag(Long pid, String tag_name, String detail_name){
		Long tid;
		try {
		tid = em.createQuery("SELECT t.tag_idx FROM TagList t WHERE t.tag_name =:tag_name t.tag_detail_name =:detail_name",Long.class)
		.setParameter("detail_name", detail_name)
		.setParameter("tag_name", tag_name)
		.getSingleResult();
		}catch(Exception e) {
			tid = (long) 0;
		}
		
		if(tid == 0) {
			em.createNativeQuery("INSERT INTO tag_search VALUES(:search_name, :search_detail_name)")
			.setParameter("search_name", tag_name)
			.setParameter("search_detail_name", detail_name);
		}
		
		em.createNativeQuery("INSERT INTO project_tag VALUES(:pid, :tag_idx)").setParameter("pid", pid).setParameter("tag_idx", tid);
	}
	
		
}
