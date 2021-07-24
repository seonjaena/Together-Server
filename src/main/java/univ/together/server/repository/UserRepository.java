package univ.together.server.repository;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddPrivateScheduleDto;
import univ.together.server.model.PrivateSchedule;
import univ.together.server.model.ProjectInvitation;
import univ.together.server.model.User;
import univ.together.server.model.UserHobbyCatSmall;
import univ.together.server.model.UserHobbyList;
import univ.together.server.model.UserValidation;

@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final EntityManager em;
	
	public void changeLastLoginedDatetime(Long user_idx) {
		em.createQuery("UPDATE User u SET u.last_logined_datetime = :last_logined_datetime WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("last_logined_datetime", LocalDateTime.now())
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public String getPwByEmail(String user_email) {
		return em.createQuery("SELECT u.user_pw FROM User u WHERE u.user_email = :user_email AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_email", user_email)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public Long getUserIdxByEmail(String user_email) {
		return em.createQuery("SELECT u.user_idx FROM User u WHERE u.user_email = :user_email AND u.delete_flag = :delete_flag", Long.class)
				.setParameter("user_email", user_email)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserNaeByIdx(Long user_idx) {
		return em.createQuery("SELECT u.user_name FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserProfilePhoto(Long user_idx) {
		return em.createQuery("SELECT u.user_profile_photo FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserNameByIdx(Long user_idx) {
		return em.createQuery("SELECT u.user_name FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public void joinUser(User user) {
		em.persist(user);
	}
	
	public String checkEmail(String user_email) {
		return em.createQuery("SELECT u.user_email FROM User u WHERE u.user_email = :user_email", String.class)
				.setParameter("user_email", user_email)
				.getSingleResult();
	}
	
	public String checkPhone(String user_phone) {
		return em.createQuery("SELECT u.user_phone FROM User u WHERE u.user_phone = :user_phone", String.class)
				.setParameter("user_phone", user_phone)
				.getSingleResult();
	}
	
	public String checkNickname(String user_nickname) {
		return em.createQuery("SELECT u.user_nickname FROM User u WHERE u.user_nickname = :user_nickname", String.class)
				.setParameter("user_nickname", user_nickname)
				.getSingleResult();
	}
	
	public void saveValidation(UserValidation validation) {
		em.persist(validation);
	}
	
	public String checkDeviceValidation(String code_type, String user_device) {
		return em.createQuery("SELECT uv.validation_value FROM UserValidation uv " + 
							"WHERE uv.validation_flag = :validation_flag " + 
							"AND uv.user_device = :user_device " + 
							"ORDER BY uv.validation_idx DESC", String.class)
				.setParameter("validation_flag", code_type)
				.setParameter("user_device", user_device)
				.setFirstResult(0)
				.setMaxResults(1)
				.getSingleResult();
	}
	
	public void deleteHobby(Long user_hobby_idxes) {
		em.createQuery("DELETE FROM UserHobbyList uhl " + 
							"WHERE uhl.user_hobby_idx = :user_hobby_idx")
				.setParameter("user_hobby_idx", user_hobby_idxes)
				.executeUpdate();
	}
	
	public List<UserHobbyCatSmall> getHobbyList() {
		return em.createQuery("SELECT uhcs FROM UserHobbyCatSmall uhcs JOIN FETCH uhcs.user_hobby_cat_big_idx " + 
							"ORDER BY uhcs.user_hobby_cat_big_idx.user_hobby_cat_big_idx, uhcs.user_hobby_cat_small_idx", UserHobbyCatSmall.class)
				.getResultList();
	}
	
	public void deleteDeviceValidation(String code_type, String user_device) {
		em.createQuery("DELETE FROM UserValidation uv " + 
				"WHERE uv.validation_flag = :validation_flag AND uv.user_device = :user_device")
				.setParameter("validation_flag", code_type)
				.setParameter("user_device", user_device)
				.executeUpdate();
	}
	
	public User getUserInfo(Long user_idx) {
		return em.createQuery("SELECT DISTINCT u FROM User u LEFT OUTER JOIN FETCH u.user_mbti WHERE u.user_idx = :user_idx", User.class)
				.setParameter("user_idx", user_idx)
				.getSingleResult();
	}
	
	public List<ProjectInvitation> getInvitationList(Long user_idx) {
		return em.createQuery("SELECT pi FROM ProjectInvitation pi " + 
							"JOIN FETCH pi.project_idx " + 
							"WHERE pi.user_idx.user_idx = :user_idx " + 
							"ORDER BY pi.project_invitation_idx DESC", ProjectInvitation.class)
				.setParameter("user_idx", user_idx)
				.getResultList();
	}
	
	public int changeUserPw(String user_pw, Long user_idx) {
		return em.createQuery("UPDATE User u SET u.user_pw = :user_pw " + 
							"WHERE u.user_idx = :user_idx " + 
							"AND u.delete_flag = :delete_flag")
				.setParameter("user_pw", user_pw)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public int changePhoto(String user_profile_photo, Long user_idx) {
		return em.createQuery("UPDATE User u SET u.user_profile_photo = :user_profile_photo WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("user_profile_photo", user_profile_photo)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public int addSchedule(AddPrivateScheduleDto addPrivateScheduleDto) {
		return em.createNativeQuery("INSERT INTO private_schedule (title, user_idx, body, start_datetime, end_datetime) " + 
							"VALUES (:title, :user_idx, :body, :start_datetime, :end_datetime)")
				.setParameter("title", addPrivateScheduleDto.getSchedule_name())
				.setParameter("user_idx", addPrivateScheduleDto.getWriter_idx())
				.setParameter("body", addPrivateScheduleDto.getSchedule_content())
				.setParameter("start_datetime", addPrivateScheduleDto.getSchedule_start_datetime())
				.setParameter("end_datetime", addPrivateScheduleDto.getSchedule_end_datetime())
				.executeUpdate();
	}
	
	public List<PrivateSchedule> getUserSchedules(Long user_idx) {
		return em.createQuery("SELECT ps FROM PrivateSchedule ps " + 
							"WHERE ps.user_idx.user_idx = :user_idx " + 
							"AND ps.user_idx.delete_flag = :delete_flag " + 
							"ORDER BY ps.private_schedule_idx DESC", PrivateSchedule.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getResultList();
	}
	
	// ====================================== 초대 수락/거부 기능 START ======================================
	
	public int deleteProjectInvitation(Long user_idx, Long project_idx) {
		return em.createQuery("DELETE FROM ProjectInvitation pi WHERE pi.user_idx.user_idx = :user_idx AND pi.project_idx.project_idx = :project_idx")
				.setParameter("user_idx", user_idx)
				.setParameter("project_idx", project_idx)
				.executeUpdate();
	}
	
	public int addProjectMember(Long project_idx, Long user_idx) {
		return em.createNativeQuery("INSERT INTO member (project_idx, user_idx, member_right) VALUES (:project_idx, :user_idx, :member_right)")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.setParameter("member_right", "Member")
				.executeUpdate();
	}
	
	public int addProjectMemberNum(Long project_idx) {
		return em.createQuery("UPDATE Project p SET p.member_num = p.member_num + :plus_num WHERE p.project_idx = :project_idx")
				.setParameter("plus_num", 1)
				.setParameter("project_idx", project_idx)
				.executeUpdate();
	}
	
	// ====================================== 초대 수락/거부 기능 END ======================================
	
	
	// ====================================== 서비스 최근 이용 날짜가 190일이 넘은 유저를 비활성화 한다. ======================================
	public List<User> getNotDeletedUsers() {
		return em.createQuery("SELECT u FROM User u WHERE u.delete_flag = :delete_flag", User.class)
				.setParameter("delete_flag", "N")
				.getResultList();
	}
	
	public void updateDeleteUsers(List<Long> users) {
		em.createQuery("UPDATE User u SET (u.delete_flag, u.deleted_datetime) = (:delete_flag, :deleted_datetime) WHERE u.user_idx IN (:user_idx)")
				.setParameter("delete_flag", "Y")
				.setParameter("deleted_datetime", LocalDateTime.now())
				.setParameter("user_idx", users)
				.executeUpdate();
	}
	// ==========================================================================================================================
	
}
