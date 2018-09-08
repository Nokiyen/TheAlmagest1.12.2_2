package noki.almagest.client.render.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import noki.almagest.client.render.RenderResource;
import noki.almagest.helper.HelperConstellation.Constellation;


public class BakedModelConstellationAll  implements IBakedModel {
	
	public ItemOverrideList overrideList;
	public List<BakedQuad> generalQuads = new ArrayList<BakedQuad>();
	public Constellation constellation = Constellation.Ori;
	public boolean isMissing = false;
	public ItemCameraTransforms transforms;
	
	@SuppressWarnings("deprecation")
	public BakedModelConstellationAll() {
		
		List<ItemOverride> overrides = new ArrayList<ItemOverride>();
		for(Constellation constellation: Constellation.values()) {
			ModelResourceLocation location =
					new ModelResourceLocation("almagest", "constellation_"+constellation.getName().toLowerCase());

			HashMap<ResourceLocation, Float> map = new HashMap<ResourceLocation, Float>();
			map.put(new ModelResourceLocation("almagest", "constellation_type"), (float)constellation.getId());
			
			overrides.add(new ItemOverride(location, map));
			
			if(constellation.isIncomplete()) {
				location =
					new ModelResourceLocation("almagest", "constellation_"+constellation.getName().toLowerCase()+"_incomplete");
				map = new HashMap<ResourceLocation, Float>();
				map.put(new ModelResourceLocation("almagest", "constellation_type"), (float)constellation.getId()+0.5F);
				
				overrides.add(new ItemOverride(location, map));
				
			}
		}
		
		this.overrideList = new ItemOverrideList(overrides);
		
		
		//transforms.
		ItemTransformVec3f vecGui = new ItemTransformVec3f(new Vector3f(30,225,0), new Vector3f(0,0,0), new Vector3f(0.625F,0.625F,0.625F));
		ItemTransformVec3f vecGround = new ItemTransformVec3f(new Vector3f(0,0,0), new Vector3f(0,0.1F,0), new Vector3f(0.25F,0.25F,0.25F));
		ItemTransformVec3f vecFixed = new ItemTransformVec3f(new Vector3f(0,0,0), new Vector3f(0,0.1F,0), new Vector3f(0.5F,0.5F,0.5F));
		ItemTransformVec3f vecThirdLeft = new ItemTransformVec3f(new Vector3f(75,225,0), new Vector3f(0,0.1F,0), new Vector3f(0.375F,0.375F,0.375F));
		ItemTransformVec3f vecThirdRight = new ItemTransformVec3f(new Vector3f(75,45,0), new Vector3f(0,0.1F,0), new Vector3f(0.375F,0.375F,0.375F));
		ItemTransformVec3f vecFirstLeft = new ItemTransformVec3f(new Vector3f(0,225,0), new Vector3f(0,0,0), new Vector3f(0.4F,0.4F,0.4F));
		ItemTransformVec3f vecFirstRight = new ItemTransformVec3f(new Vector3f(0,45,0), new Vector3f(0,0,0), new Vector3f(0.4F,0.4F,0.4F));
		this.transforms = new ItemCameraTransforms(vecThirdLeft, vecThirdRight, vecFirstLeft, vecFirstRight,
				ItemCameraTransforms.DEFAULT.head, vecGui, vecGround, vecFixed);
		
		//frame.
		FaceBakery faceBakery = new FaceBakery();
		BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
		BlockPartFace partFace;
		BakedQuad quad;
		
		
		ResourceLocation frameTexture = RenderResource.textureFrame;
		if(this.constellation.isEcliptical()) {
			frameTexture = RenderResource.textureFrameE;
		}
		TextureAtlasSprite frameSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(frameTexture.toString());
		Vector3f from = new Vector3f(0, 0, 0);
		Vector3f to = new Vector3f(16, 16, 16);
		
		for(EnumFacing face: EnumFacing.VALUES) {
			partFace = new BlockPartFace(face, face.getIndex(), frameTexture.toString(), uv);
			quad = faceBakery.makeBakedQuad(from, to, partFace, frameSprite, face, ModelRotation.X0_Y0,
				null, true, true);
			this.generalQuads.add(quad);
		}
		
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		
		return false;
		
	}
	
	@Override
	public boolean isGui3d() {
		
		return true;
		
	}
	
	@Override
	public boolean isBuiltInRenderer() {
		
		return false;
		
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		
		return this.transforms;
		
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		
		return this.generalQuads;
		
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		
		return null;
		
	}

	@Override
	public ItemOverrideList getOverrides() {
		
		return this.overrideList;
		
	}
	
}
