package noki.almagest.asm;

import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import noki.almagest.asm.event.ItemSmeltingEvent;


public class VisitorFurnaceRecipes implements IVisitorProvider, Opcodes {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME = "net.minecraft.item.crafting.FurnaceRecipes";
	
	
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
		
		private static final String TARGET_METHOD_NAME_OBF = "func_151395_a";
		private static final String TARGET_METHOD_DESC = "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;";
		private static final String TARGET_METHOD_NAME = "getSmeltingResult";
		
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
			super.visitVarInsn(ALOAD, 1);
			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorFurnaceRecipes", "getSmeltingResultAlt",
					"(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false);
			super.visitInsn(ARETURN);
			super.visitEnd();
		}
		
	}
	
	public static ItemStack getSmeltingResultAlt(ItemStack stack) {
		
		if(FurnaceRecipes.instance() == null) {
			return ItemStack.EMPTY;
		}
		
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		ItemStack target = ItemStack.EMPTY;
		for(Entry<ItemStack, ItemStack> entry : smeltingList.entrySet()) {
			if(compareItemStacks(stack, entry.getKey())) {
				target = entry.getValue();
				break;
			}
		}
		
		return ItemSmeltingEvent.postEvent(stack, target);
		
	}
	
	private static boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
		
	}
	
}
