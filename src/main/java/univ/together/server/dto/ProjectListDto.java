package univ.together.server.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.Project;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListDto {

	private Long project_idx;
	private String project_name;
	private int count;

	public ProjectListDto(Project project) {
		this.project_idx = project.getProject_idx();
		this.project_name = project.getProject_name();
		this.count = project.getMember_num();
	}

}
