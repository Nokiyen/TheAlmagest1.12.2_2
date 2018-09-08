package noki.almagest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public interface IVisitorProvider {
	
	public boolean match(String transformedName);
	public ClassVisitor provideVisitor(String name, ClassWriter writer);

}
