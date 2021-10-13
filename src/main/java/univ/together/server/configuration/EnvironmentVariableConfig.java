package univ.together.server.configuration;

public class EnvironmentVariableConfig {

	private static final String photo_url = "http://101.101.216.93:8080/images/";
	
	private EnvironmentVariableConfig() {}
	
	public static String getPhotoUrl() {
		return photo_url;
	}
	
}
