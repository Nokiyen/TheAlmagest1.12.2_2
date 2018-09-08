package noki.almagest.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMira extends ModelBase {
	
	// モデルの直方体を代入する変数
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer bottom;
	ModelRenderer armRight;
	ModelRenderer armLeft;
	ModelRenderer legRight;
	ModelRenderer legLeft;
	
 
	public ModelMira() {
		
		super();
		
		// テクスチャの縦と横のサイズ
		this.textureWidth = 64;
		this.textureHeight = 32;
		
		// モデルの形を作る
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4F, -7.5F, -3.5F, 8, 7, 7);
		this.head.setRotationPoint(0.0F, 11F, 0.0F);
		
		this.body = new ModelRenderer(this, 32, 0);
		this.body.addBox(-3F, 10F, -2F, 6, 7, 4);
		
		this.bottom = new ModelRenderer(this, 0, 14);
		this.bottom.addBox(-4.5F, 15F, -3.5F, 9, 7, 7);
		
		this.armRight = new ModelRenderer(this, 32, 11);
		this.armRight.addBox(-3F, 0F, -1F, 2, 6, 2);
		this.armRight.setRotationPoint(-1F, 11F, 0F);
		
		this.armLeft = new ModelRenderer(this, 32, 11);
		this.armLeft.mirror = true;
		this.armLeft.addBox(1F, 0F, -1F, 2, 6, 2);
		this.armLeft.setRotationPoint(1F, 11F, 0F);
		
		this.legRight = new ModelRenderer(this, 40, 11);
		this.legRight.addBox(-1.5F, 0F, -1F, 3, 2, 2);
		this.legRight.setRotationPoint(-1.5F, 22F, 0F);
		
		this.legLeft = new ModelRenderer(this, 40, 11);
		this.legLeft.mirror = true;
		this.legLeft.addBox(-1.5F, 0F, -1F, 3, 2, 2);
		this.legLeft.setRotationPoint(1.5F, 22F, 0F);
		
	}
	
	@Override
	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entity) {

		this.head.rotateAngleY = f4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = f5 / (180F / (float)Math.PI);

		this.legRight.rotateAngleX = MathHelper.cos(f1 * 0.6662F) * 1.4F * f2;
		this.legLeft.rotateAngleX = MathHelper.cos(f1 * 0.6662F + (float)Math.PI) * 1.4F * f2;
		
		this.armRight.rotateAngleX = MathHelper.cos(f1* 0.6662F + (float)Math.PI) * 2.0F * f2 * 0.5F;
		this.armLeft.rotateAngleX = MathHelper.cos(f1 * 0.6662F) * 2.0F * f2 * 0.5F;
		this.armRight.rotateAngleZ = 0.4F;
		this.armLeft.rotateAngleZ = -0.4F;
		this.armRight.rotateAngleY = 0.0F;
		this.armLeft.rotateAngleY = 0.0F;

	}
 
	@Override
	public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6) {
		
		// 描画
		this.setRotationAngles(f1, f2, f3, f4, f5, f6, entity);
		this.head.render(f6);
		this.body.render(f6);
		this.bottom.render(f6);
		this.legRight.render(f6);
		this.legLeft.render(f6);
		this.armRight.render(f6);
		this.armLeft.render(f6);
		
	}
	
}
