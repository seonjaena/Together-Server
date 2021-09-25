package univ.together.server.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamSearchingDto {
	private Long user_idx;
	private int tag_num;
	private String tag[] = new String[tag_num];
	private String detail[] = new String[tag_num];
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private int member_num;
	private String mbti;
	
}
