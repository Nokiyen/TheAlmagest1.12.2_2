package noki.almagest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class VisitorEntityLivingBase implements IVisitorProvider, Opcodes {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME = "net.minecraft.entity.EntityLivingBase";
	
	
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
		
		private static final String TARGET_METHOD_NAME_OBF = "func_70687_e";
		private static final String TARGET_METHOD_DESC = "(Lnet/minecraft/potion/PotionEffect;)Z";
		private static final String TARGET_METHOD_NAME = "isPotionApplicable";
		private static final String TARGET_METHOD_NAME_OBF2 = "func_70097_a";
		private static final String TARGET_METHOD_DESC2 = "(Lnet/minecraft/util/DamageSource;F)Z";
		private static final String TARGET_METHOD_NAME2 = "attackEntityFrom";		
		
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
			else if(ASMHelper.checkMethodName(this.owner, name, desc, TARGET_METHOD_NAME2, TARGET_METHOD_NAME_OBF2, TARGET_METHOD_DESC2)) {
				ASMLoadingPlugin.LOGGER.info("enter method / {}.", TARGET_METHOD_NAME2);
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor2(this.api, this.owner, access, name, desc, mv);
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
			super.visitVarInsn(ALOAD, 1);
			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/event/PotionApplyEvent", "postEvent", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/potion/PotionEffect;)Z", false);
//			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/event/PotionApplyEvent", "postEvent", "(Lnet/minecraft/potion/PotionEffect;)Z", false);
//			super.visitInsn(POP);
			Label label = new Label();
//			super.visitFrame(F_NEW, 0, null, 0, null); //必要なし
			super.visitJumpInsn(IFEQ, label);
//			super.visitJumpInsn(GOTO, label);
			super.visitInsn(ICONST_0);
			super.visitInsn(IRETURN);
			super.visitLabel(label);
//			super.visitFrame(F_SAME, 0, null, 0, null);
//			super.visitFrame(F_SAME1, 0, null, 0, null); */
		}
		
	}
	
	private class CustomMethodVisitor2 extends MethodVisitor {
		//*****define member variables.*//
		private static final String NAME = "onLivingAttack";
		private static final String DESC = "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/DamageSource;F)Z";
		
		
		//*****define member methods.***//
		protected CustomMethodVisitor2(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
			if(opcode == INVOKESTATIC && name.equals(NAME) && desc.equals(DESC)) {
				super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/event/LivingAttackPostEvent", "postEvent",
						"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/DamageSource;F)Z", false);
				return;
			}
			super.visitMethodInsn(opcode, owner, name, desc, itf);
	    }
		
	}
	
}
