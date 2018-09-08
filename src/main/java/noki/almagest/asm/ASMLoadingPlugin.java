package noki.almagest.asm;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;


/**********
 * @class ASMLoadingPlugin
 *
 * @description CoreModの本体です。
 * このクラスで、CoreModとしての情報を格納するクラスと、実際に改変を行うクラスを指定します。
 * ほぼおまじない。
 * 
 * このクラスを、MANIFEST.MFファイル内で指定する必要があります。
 * 通常のModとCoreModをひとつのMod(ひとつのjar)として配布するならば、
 * あわせて"FMLCorePluginContainsFMLMod: true"を追記する必要があります。
 * 
 * ~~~.jar/META-INF/MANIFEST.MF
 * 		Manifest-Version: 1.0
 * 		FMLCorePlugin: noki.almagest.asm.ASMLoadingPlugin
 * 		FMLCorePluginContainsFMLMod: true
 * 
 * @see ASMModContainer, ASMClassTransformer
 */
public class ASMLoadingPlugin implements IFMLLoadingPlugin {
	
	//******************************//
	// define member variables.
	//******************************//
	public static  Logger LOGGER = LogManager.getLogger("almagesttransform");

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public String[] getASMTransformerClass() {
		
		return new String[] {"noki.almagest.asm.ASMClassTransformer"};
		
	}
	
	@Override
	public String getModContainerClass() {
		
		return "noki.almagest.asm.ASMModContainer";
		
	}
	
	@Override
	public String getSetupClass() {
		
		return null;
		
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		
		return null;
		
	}
	
	public static void log(String message, Object... data) {
		
		LOGGER.info(message, data);
		
	}
	
}
