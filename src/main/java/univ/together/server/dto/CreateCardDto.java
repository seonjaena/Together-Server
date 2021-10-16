package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardDto {
	private Long project_idx;
	private String comment;
}
