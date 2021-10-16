package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.CreateCardDto;
import univ.together.server.dto.Pair;
import univ.together.server.dto.ProjectCardDto;
import univ.together.server.dto.TeamMatchingDto;
import univ.together.server.dto.TeamSearchingDto;
import univ.together.server.model.Project;
import univ.together.server.model.UserHobbyList;
import univ.together.server.repository.MainRepository;
import univ.together.server.repository.ProjectRepository;
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
	
	public List<String> CreateProjectCardMain(Long user_idx){
		return teammatchingRepository.findSearchNotAvailableProject(user_idx);
	}
	
	public Project getProjectInfo(String project_name) {
		return teammatchingRepository.getProjectInfo(project_name);
	};
	
	public void completeCreateCard(CreateCardDto ccd) {
		teammatchingRepository.completeCreateCard(ccd);
	}
	
	
	public List<ProjectCardDto> teamMatchingMain(Long user_idx) {
		List<Long>project_idx_list = new ArrayList<>();
		List<ProjectCardDto> card_list = new ArrayList<>();
		project_idx_list.addAll(teammatchingRepository.findSearchAvailableProject(user_idx));
		System.out.println(project_idx_list);
		for (Long idx : project_idx_list) {
			
			card_list.add(teammatchingRepository.getTeamMatchingInfo(idx));
		}
		
		return card_list;
	}
}

