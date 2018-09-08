package noki.almagest.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import noki.almagest.ModInfo;
import noki.almagest.entity.EntityTsuchinoko;

public class TsuchinokoRenderFactory implements IRenderFactory<EntityTsuchinoko> {

	@Override
	public Render<? super EntityTsuchinoko> createRenderFor(RenderManager manager) {
		
		return new TsuchinokoRenderEntity(manager);
		
	}
	
	private static class TsuchinokoRenderEntity extends RenderLiving<EntityTsuchinoko> {
		
		public static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/entity/tsuchinoko.png");
		
		public TsuchinokoRenderEntity(RenderManager manager) {
			super(manager, new TsuchinokoModel(), 0.6f);
		}
		
		@Override
		protected ResourceLocation getEntityTexture(EntityTsuchinoko entity) {
			return texture;
		}

	}

}
