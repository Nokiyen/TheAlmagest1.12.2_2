package noki.almagest.asm;

import org.objectweb.asm.Opcodes;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ASMHelper {
	
	public static boolean checkMethodName(String owner, String givenName, String givenDesc, String name, String obfName, String desc) {
		
		boolean obfNameFlag = obfName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, givenName, givenDesc));
		boolean descFlag = desc.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(givenDesc));
		if(obfNameFlag && descFlag) {
			return true;
		}
		
		boolean nameFlag = name.equals(givenName);
		if(nameFlag && descFlag) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean checkFieldName(int opcode, String owner, String givenName, String givenDesc, String name, String obfName) {
		
		boolean obfNameFlag = obfName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, givenName, givenDesc));
		if(opcode == Opcodes.GETFIELD && obfNameFlag) {
			return true;
		}
		
		boolean nameFlag = name.equals(givenName);
		if(opcode == Opcodes.GETFIELD && nameFlag) {
			return true;
		}
		
		return false;
		
	}

}
