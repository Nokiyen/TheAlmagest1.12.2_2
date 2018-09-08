package noki.almagest.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;
import noki.almagest.entity.EntityMira;

public class RenderEntityMira extends RenderLiving<EntityMira> {
	
	public static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/entity/mira.png");
	
	public RenderEntityMira(RenderManager manager) {
		
		// 引数:(ModelBase以降を継承したクラスのインスタンス、影の大きさ)
		super(manager, new ModelMira(), 0.6f);
		
	}
	
	@Override
	public ResourceLocation getEntityTexture(EntityMira entity) {
		
		return texture;
		
	}

}
