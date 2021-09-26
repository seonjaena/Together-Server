package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchingDto {

	private Integer age;
	private String license1;
	private String license2;
	private String license3;
	private Integer mbti;
	
}
