package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwDto {

	private String user_pw;
	private String user_pw2;
	private Long user_idx;
	
}
