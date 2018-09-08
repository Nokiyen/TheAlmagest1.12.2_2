package noki.almagest.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import noki.almagest.entity.EntityMira;

public class RenderFactoryMira implements IRenderFactory<EntityMira> {

	@Override
	public Render<? super EntityMira> createRenderFor(RenderManager manager) {
		
		return new RenderEntityMira(manager);
		
	}

}
