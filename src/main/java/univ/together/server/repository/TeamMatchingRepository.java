package univ.together.server.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.dto.Pair;

@Repository
@RequiredArgsConstructor
public class TeamMatchingRepository {

	private final EntityManager em;

	// user 정보 가져옴
//	public User getUserInfo(Long user_idx) {
//		return em.createQuery("SELECT u FROM user u WHERE u.user_idx = : user_idx",User.class)
//				.setParameter("user_idx", user_idx)
//				.getSingleResult();
//	}
//	

	// 팀 매칭
	// 검색할 태그들이 tag_list에 있는지확인, 있으면 idx 가져옴/ 없으면 tag_seach에서 찾고 없으면 -> tag_search에
	// 새로 대입 / 있으면 search_idx 값 가져옴
	public Long getTagIdx(String tag_name, String tag_detail_name) {

		return em
				.createQuery(
						"SELECT t.tag_idx FROM TagList t WHERE t.tag_name =: tag_name AND t.tag_detail_name =:detail_name",
						Long.class)
				.setParameter("detail_name", tag_detail_name).setParameter("tag_name", tag_name).getSingleResult();
	}

	public Long getTagSearchIdx(String tag_name, String tag_detail_name) {

		em.createNativeQuery("INSERT INTO tag_search(search_name, search_detail_name) VALUES(:search_name, :search_detail_name)")
				.setParameter("search_name", tag_name).setParameter("search_detail_name", tag_detail_name)
				.executeUpdate();

		return em.createQuery(
				"SELECT t.tag_search_idx FROM TagSearch t WHERE t.search_name =: tag_name AND t.search_detail_name=:"
						+ "tag_detail_name",
				Long.class).setParameter("tag_name", tag_name).setParameter("tag_detail_name", tag_detail_name)
				.setFirstResult(0).setMaxResults(1).getSingleResult();
	}

	public List<String> teamMatching(TeamMatchingDto teammatchingdto, ArrayList<Pair> tag_idx) {
		// 상관없음 추가해야함 + 프로젝트 태그 관련
		/*
		 * if (tag_idx.isEmpty() == true) { return em.createQuery(
		 * "SELECT p.project_name FROM Project p WHERE p.professionality Like :professionality AND "
		 * +
		 * "p.project_type Like: project_type AND p.project_start_date >= :project_start_date AND p.project_end_date"
		 * + "<= :project_end_date", String.class).setParameter("professionality", '%' +
		 * teammatchingdto.getProfessionality()) .setParameter("project_type", '%' +
		 * teammatchingdto.getProject_type()) .setParameter("project_start_date",
		 * teammatchingdto.getStart_date()) .setParameter("project_end_date",
		 * teammatchingdto.getEnd_date()).getResultList(); } else {
		 */
		String str = "SELECT p.project_name FROM Project p JOIN FETCH ProjectTag t WHERE p.professionality Like :professionality AND "
				+ "p.project_type Like: project_type AND p.project_start_date >= :project_start_date AND p.project_end_date"
				+ "<= :project_end_date";

		while (!tag_idx.isEmpty()) {
			if (tag_idx.get(0).getX() == 0) {
				str = str + " AND t.tag_search_idx.tag_search_idx =" + tag_idx.get(0).getY();
			} else {
				str = str + " AND t.tag_idx.tag_idx =" + tag_idx.get(0).getX();
			}
			tag_idx.remove(0);
		}

		return em.createQuery(str, String.class)
				.setParameter("professionality", '%' + teammatchingdto.getProfessionality())
				.setParameter("project_type", '%' + teammatchingdto.getProject_type())
				.setParameter("project_start_date", teammatchingdto.getStart_date())
				.setParameter("project_end_date", teammatchingdto.getEnd_date()).getResultList();
		// 값이 없으면 다른값으로 추가
	}
}
