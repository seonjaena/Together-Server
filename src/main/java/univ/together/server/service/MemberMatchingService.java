package univ.together.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.model.User;
import univ.together.server.repository.MemberMatchingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberMatchingService {
	
	private final MemberMatchingRepository memberMatchingRepository;
	
	public List<User> getUserList(Long project_idx) {
		return memberMatchingRepository.getUserList();
	}
	
	

}
