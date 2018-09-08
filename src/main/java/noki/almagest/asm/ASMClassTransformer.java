package noki.almagest.asm;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import net.minecraft.launchwrapper.IClassTransformer;


/**********
 * @class ASMClassTransformer
 *
 * @description ASMによるバイトコード改変のエントリポイントです。
 */
public class ASMClassTransformer implements IClassTransformer {
	
	//******************************//
	// define member variables.
	//******************************//
	private List<IVisitorProvider> providers;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ASMClassTransformer() {
		
		this.providers = new ArrayList<IVisitorProvider>();
		
		this.providers.add(new VisitorRenderGlobal());
		this.providers.add(new VisitorEntityLivingBase());
		this.providers.add(new VisitorEntityPlayer());
		this.providers.add(new VisitorFurnaceRecipes());
		
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
//		ASMLoadingPlugin.LOGGER.info("enter ASMClassTransformer.");
		
		for(IVisitorProvider each: this.providers) {
			if(each.match(transformedName)) {
				try {
					ASMLoadingPlugin.LOGGER.info("enter transforming : {}.", transformedName);
					
					//**この辺りから、ASMの書き方ができます。**//
					
					//ClassReader, ClassWriter, ClassVisitorで3すくみになるように引数を与えることで処理を早める。
					ClassReader classReader = new ClassReader(basicClass);
					ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
					ClassVisitor classVisitor = each.provideVisitor(name, classWriter);
					classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
					return classWriter.toByteArray();
					
					//**ここまで**//
				} catch (Exception e) {
					throw new RuntimeException("asm, class transforming failed.", e);
				}
			}
		}
		
		return basicClass;
		
	}

}
