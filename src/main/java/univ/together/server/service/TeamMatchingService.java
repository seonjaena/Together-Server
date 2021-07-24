package univ.together.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.repository.TeamMatchingRepository;
import univ.together.server.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMatchingService {
	
	private final TeamMatchingRepository teammatchingRepository;
	private final UserRepository userRepository;
	
	public List<String> teamMatching(TeamMatchingDto teammatchingdto) {
		
		
		return teammatchingRepository.teamMatching(teammatchingdto);
	}
	
}
