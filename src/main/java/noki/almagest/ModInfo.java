package noki.almagest;


/**********
 * @class ModInfo
 *
 * @description このModのごく基本的な情報を格納します。
 * @description_en Contains the Mod's basic information.
 */
public class ModInfo {

	//*****
	//define member variables.
	//*****	
	public static final String ID = "almagest";
	public static final String NAME = "The Almagest";
	public static final String VERSION = "1.0.0";
	public static final String CHANNEL = ID;
	public static final String DEPENDENCIES = "required-after:forge@[1.12.2-14.23.0.2486,);";
	public static final String MC_VERSIONS = "[1.12.2,)";
	public static final String PROXY_LOCATION = "noki.almagest.proxy.";
	public static final String UPDATE_JSON = "https://dl.dropboxusercontent.com/s/b1seya3jpoczkig/almagest2.json?dl=1";

}
