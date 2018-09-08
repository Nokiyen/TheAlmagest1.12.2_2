package noki.almagest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noki.almagest.asm.event.ArmorVisibilityEvent;


public class VisitorEntityPlayer implements IVisitorProvider, Opcodes {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME = "net.minecraft.entity.player.EntityPlayer";
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public boolean match(String transformedName) {
		
		return transformedName.equals(TARGET_CLASS_NAME);
		
	}
	
	@Override
	public ClassVisitor provideVisitor(String name, ClassWriter writer) {
		
		return new CustomClassVisitor(name, writer);
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class CustomClassVisitor extends ClassVisitor {
		
		//*****define member variables.*//
		private String owner;
		
		private static final String TARGET_METHOD_NAME_OBF = "func_82243_bO";
		private static final String TARGET_METHOD_DESC = "()F";
		private static final String TARGET_METHOD_NAME = "getArmorVisibility";
		
		//*****define member methods.***//
		public CustomClassVisitor(String owner, ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if(ASMHelper.checkMethodName(this.owner, name, desc, TARGET_METHOD_NAME, TARGET_METHOD_NAME_OBF, TARGET_METHOD_DESC)) {
				ASMLoadingPlugin.LOGGER.info("enter method / {}.", TARGET_METHOD_NAME);
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor(this.api, this.owner, access, name, desc, mv);
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
	}
	
	private class CustomMethodVisitor extends MethodVisitor {
		
		//*****define member variables.*//
		protected CustomMethodVisitor(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, mv);
		}
		
		//*****define member methods.***//
		@Override
		public void visitCode() {
			ASMLoadingPlugin.LOGGER.info("enter visitCode().");
			super.visitCode();
			super.visitVarInsn(ALOAD, 0);
			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorEntityPlayer", "getArmorVisibilityAlt",
					"(Lnet/minecraft/entity/player/EntityPlayer;)F", false);
			super.visitInsn(FRETURN);
			super.visitEnd();
		}
		
	}
	
	public static float getArmorVisibilityAlt(EntityPlayer player) {
		
		int level = 0;
		for(ItemStack itemstack : player.inventory.armorInventory) {
			level += ArmorVisibilityEvent.postEvent(player, itemstack);
		}
		
//		ASMLoadingPlugin.log("armor visibility / {}.", level);
		
		return (float)level / (float)player.inventory.armorInventory.size();
        
	}
	
}
