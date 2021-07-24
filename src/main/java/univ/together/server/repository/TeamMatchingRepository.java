package univ.together.server.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.TeamMatchingDto;

@Repository
@RequiredArgsConstructor
public class TeamMatchingRepository {
	
	private final EntityManager em;
	
	
	//user 정보 가져옴
//	public User getUserInfo(Long user_idx) {
//		return em.createQuery("SELECT u FROM user u WHERE u.user_idx = : user_idx",User.class)
//				.setParameter("user_idx", user_idx)
//				.getSingleResult();
//	}
//	
	
	//팀 매칭
	public List<String> teamMatching(TeamMatchingDto teammatchingdto) {
		//상관없음 추가해야함 + 프로젝트 태그 관련
		return em.createQuery("SELECT p.project_name FROM Project p JOIN FETCH ProjectTag t WHERE p.professionality =:professionality AND "
				+ "p.project_type =: project_type AND p.project_start_date >= :project_start_date AND p.project_end_date"
				+ "<= :project_end_date",String.class)
		.setParameter("professionality", teammatchingdto.getProfessionality())
		.setParameter("project_type", teammatchingdto.getProject_type())
		.setParameter("project_start_date", teammatchingdto.getStart_date())
		.setParameter("project_end_date", teammatchingdto.getEnd_date())
		.getResultList();
	}
}
