package univ.together.server.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectTag;

@Data
public class ProjectCardDto {
	
	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private int member_num;
	private String comment;
	 
	private List<Pair> tag= new ArrayList<>();
	
	
	public ProjectCardDto(Project p, String comment){
		this.project_name=p.getProject_name();
		this.project_exp=p.getProject_exp();
		this.start_date=p.getStart_date();
		this.end_date=p.getEnd_date();
		this.professionality=p.getProfessionality();
		this.project_type=p.getProject_type();
		this.member_num=p.getMember_num();
		this.comment=comment;
		
		for (ProjectTag t : p.getTags()) {
			if(t.getTag_search_idx().getTag_search_idx()==0) {
				tag.add(new Pair(t.getTag_idx().getTag_name(),t.getTag_idx().getTag_detail_name()));
			}
			else {
				tag.add(new Pair(t.getTag_search_idx().getSearch_name(), t.getTag_search_idx().getSearch_detail_name()));
			}
		}
		
	}


	
}
