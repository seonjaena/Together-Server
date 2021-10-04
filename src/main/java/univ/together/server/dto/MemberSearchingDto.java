package univ.together.server.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchingDto {

	private Integer min_age;
	private Integer max_age;
	private List<String> license = new ArrayList<String>();
	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private List<String> hobby_small_idx = new ArrayList<String>();
	
}
