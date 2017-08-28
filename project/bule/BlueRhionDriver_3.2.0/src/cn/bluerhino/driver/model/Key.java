package cn.bluerhino.driver.model;

public interface Key {
	
	// -----Intent being----
	
	public static final String EXTRA_BRLOCATION = "BRLocation";	
	
	// -----Intent end----

	// -----SharedPreferences begin-----

	public static final String PREFERENCES_NAME = "Configuration";

	public static final String KEY_ISIGONRE_UPDATE = "isIgonreUpdate";
	
	public static final String KEY_UPDATE_LOG = "updateLog";

	// ------SharedPreferences end-----
}
