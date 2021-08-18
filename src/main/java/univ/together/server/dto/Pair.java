package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair{
	private Long x;
	private Long y;
	
	public Pair(Long x, int y){
		this.x= x;
		this.y= (long) y;
	}
	public Pair(int x, Long y){
		this.x=(long)x;
		this.y=y;
	}
}
