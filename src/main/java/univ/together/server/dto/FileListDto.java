package univ.together.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.File;

@Getter
@Setter
@NoArgsConstructor
public class FileListDto {
	public FileListDto(File f) {
		// TODO Auto-generated constructor stub
		this.file_idx = f.getFile_idx();
		this.file_extension = f.getFile_extension();
		this.file_origin_name = f.getFile_origin_name();
		this.file_type = f.getFile_type();
		this.file_sema_flag = f.getFile_sema_flag();
	}

	private Long file_idx;
	
	private String file_origin_name;
	
	private String file_extension;
	
	private String file_type;
	
	private String file_sema_flag;
	
}