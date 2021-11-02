package univ.together.server.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import univ.together.server.model.TeamApplication;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamApplicationDto {
	private Long team_application_idx;
	private Long user_idx;
	private Long project_idx;
	
	public TeamApplicationDto(TeamApplication t){
		this.team_application_idx=t.getTeam_application_idx();
		this.project_idx=t.getProject_idx().getProject_idx();
		this.user_idx=t.getUser_idx().getUser_idx();
	}
}
