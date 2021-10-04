package univ.together.server.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.model.RegisterSearchMemberProfileCardDto;
import univ.together.server.model.SearchMember;
import univ.together.server.model.User;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepository {

	private final EntityManager em;
	
	// user의 프로필 카드를 가져옴
	public SearchMember getUserProfileCard(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm JOIN FETCH sm.user_idx " + 
							  "WHERE sm.user_idx.user_idx = :user_idx AND sm.user_idx.delete_flag = :delete_flag", SearchMember.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// user의 프로필 카드가 없는 경우, user의 정보를 가져온다.
	public User getUserProfileInfo(Long user_idx) {
		return em.createQuery("SELECT u FROM User u JOIN FETCH u.user_mbti " + 
							  "WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", User.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// user의 프로필 카드를 업데이트
	public void updateUserProfileCard(Long user_idx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		em.createQuery("UPDATE SearchMember sm SET sm.resume = :resume, sm.comment = :comment " + 
					   "WHERE sm.user_idx.user_idx = :user_idx")
				.setParameter("resume", registerSearchMemberProfileCardDto.getResume())
				.setParameter("comment", registerSearchMemberProfileCardDto.getComment())
				.setParameter("user_idx", user_idx)
				.executeUpdate();
	}
	
	// user의 프로필 카드를 등록
	public void registerUserProfileCard(Long user_idx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		em.createNativeQuery("INSERT INTO search_member (user_idx, resume, comment) " + 
							 "VALUES(:user_idx, :resume, :comment)")
				.setParameter("user_idx", user_idx)
				.setParameter("resume", registerSearchMemberProfileCardDto.getResume())
				.setParameter("comment", registerSearchMemberProfileCardDto.getComment())
				.executeUpdate();
	}
	
	// user들의 프로필 카드 리스트(최근 등록자 top5)
	public List<SearchMember> getProfileCardList(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm " + 
							  "WHERE sm.user_idx.delete_flag = :delete_flag AND " + 
							  "sm.user_idx.user_idx != :user_idx " + 
							  "ORDER BY sm.search_member_idx DESC", SearchMember.class)
				.setParameter("delete_flag", "N")
				.setParameter("user_idx", user_idx)
				.setFirstResult(0)
				.setMaxResults(5)
				.getResultList();
	}
	
	// user들의 프로필 카드 리스트(모든 등록자)
	public List<SearchMember> getAllProfileCardList(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm " + 
							  "WHERE sm.user_idx.delete_flag = :delete_flag AND " + 
							  "sm.user_idx.user_idx != :user_idx", SearchMember.class)
				.setParameter("delete_flag", "N")
				.setParameter("user_idx", user_idx)
				.getResultList();
	}
	
}
