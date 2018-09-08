package noki.almagest.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class TsuchinokoModel extends ModelBase {
	
	// モデルの直方体を代入する変数
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer tail;
	
 
	public TsuchinokoModel() {
		
		super();
		
		// テクスチャの縦と横のサイズ
		this.textureWidth = 64;
		this.textureHeight = 64;
		
		// モデルの形を作る
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-5F, -4.5F, -10F, 10, 8, 10);
		this.head.setRotationPoint(0F, 16F*1.5F-2.5F, 0F);
//		this.head.setRotationPoint(0F, 16F*1.5F-3F, 0F);
		
/*		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4F, -4F, -4F, 8, 8, 8);
		this.head.setRotationPoint(0F, 0F, 0F);*/
		
		
		
		this.body = new ModelRenderer(this, 0, 18);
		this.body.addBox(-4F, -2.5F, 0F, 8, 5, 10);
		this.body.setRotationPoint(0F, 16F*1.5F-2.5F, 0F);

		this.tail = new ModelRenderer(this, 44, 8);
		this.tail.addBox(-2F, -2F, 0F, 4, 4, 6);
		this.tail.setRotationPoint(0F, 16F*1.5F-2F, 10F);
//		this.tail.setRotationPoint(0F, 18F, 11F);
		
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
		
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
//        this.body.rotateAngleX = ((float)Math.PI / 2F);
//		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
//        this.body.rotateAngleY = (netHeadYaw / (180F / (float) Math.PI));
		this.tail.rotateAngleY = MathHelper.cos(limbSwing*0.3F) * limbSwingAmount;

	}
 
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		// 描画
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.head.render(scale);
		this.body.render(scale);
		this.tail.render(scale);
		
	}
	
}
