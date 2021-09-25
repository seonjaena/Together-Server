package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.Pair;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.dto.TeamSearchingDto;
import univ.together.server.model.UserHobbyList;
import univ.together.server.repository.TeamMatchingRepository;
import univ.together.server.repository.UserRepository;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMatchingService {
	
	private final TeamMatchingRepository teammatchingRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public List<String> teamSearching(TeamSearchingDto teamSearchingdto) {
		ArrayList<Pair> tag_idx = new ArrayList<Pair>();
		for(int i=0; i< teamSearchingdto.getTag_num(); i++) {

			try {
				tag_idx.add(new Pair(teammatchingRepository.getTagIdx(teamSearchingdto.getTag()[i], teamSearchingdto.getDetail()[i]), 0));
			}catch(Exception e) {
				tag_idx.add(new Pair(0, teammatchingRepository.getTagSearchIdx(teamSearchingdto.getTag()[i], teamSearchingdto.getDetail()[i])));
			}
		}
		
		System.out.println("asd");
		return teammatchingRepository.teamSearching(teamSearchingdto, tag_idx);
	}
	
	
	@Transactional
	public void teamMatching(TeamMatchingDto teamMatchingDto) {
		List<UserHobbyList> hobby_list=userRepository.getAddHobbyReturnValue(teamMatchingDto.getUser_idx());
		String big="", small="";
		teammatchingRepository.teamMatching(big, small);
	}
}

