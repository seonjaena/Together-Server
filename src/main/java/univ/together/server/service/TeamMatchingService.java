package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.repository.TeamMatchingRepository;
import univ.together.server.repository.UserRepository;
import univ.together.server.dto.Pair;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMatchingService {
	
	private final TeamMatchingRepository teammatchingRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public List<String> teamMatching(TeamMatchingDto teammatchingdto) {
		ArrayList<Pair> tag_idx = new ArrayList<Pair>();
		for(int i=0; i< teammatchingdto.getTag_num(); i++) {

			try {
				tag_idx.add(new Pair(teammatchingRepository.getTagIdx(teammatchingdto.getTag()[i], teammatchingdto.getDetail()[i]), 0));
			}catch(Exception e) {
				tag_idx.add(new Pair(0, teammatchingRepository.getTagSearchIdx(teammatchingdto.getTag()[i], teammatchingdto.getDetail()[i])));
			}
		}
		
		System.out.println("asd");
		return teammatchingRepository.teamMatching(teammatchingdto, tag_idx);
	}
	
}

