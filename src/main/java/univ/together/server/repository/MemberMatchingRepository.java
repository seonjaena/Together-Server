package univ.together.server.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.model.User;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepository {

	private final EntityManager em;
	
	public List<User> getUserList() {
		return em.createQuery("SELECT u FROM User u WHERE u.delete_flag = :delete_flag", User.class)
				.setParameter("delete_flag", "N")
				.getResultList();
	}
	
}
