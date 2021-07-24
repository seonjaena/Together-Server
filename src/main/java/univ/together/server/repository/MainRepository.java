package univ.together.server.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.model.Project;

@Repository
@RequiredArgsConstructor
public class MainRepository {

	private final EntityManager em;
	
	public List<Project> findProjectByUserIdx(Long user_idx) {
		return em.createQuery("SELECT m.project_idx FROM Member m " + 
						"WHERE m.user_idx.user_idx = :user_idx", Project.class)
			.setParameter("user_idx", user_idx)
			.getResultList();
	}

}
