package noki.almagest.event;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.ModInfo;


public class EventVersionCheck {
	
	private boolean notified = false;
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		
		if(this.notified) {
			return;
		}
		
		if(!event.getWorld().isRemote) {
			return;
		}
		
		if(event.getEntity() == null) {
			return;
		}
		
		if(!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		UUID targetID = ((EntityPlayer)event.getEntity()).getGameProfile().getId();
		UUID playerID = Minecraft.getMinecraft().player.getGameProfile().getId();
		if(!targetID.equals(playerID)) {
			return;
		}
		
		CheckResult result = ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(ModInfo.ID));
		if(result == null) {
			return;
		}
		
		if(result.status == Status.UP_TO_DATE) {
			return;
		}
		
		if(result.target == null) {
			return;
		}
		
		((EntityPlayerSP)event.getEntity()).sendMessage(
				new TextComponentTranslation(ModInfo.ID.toLowerCase()+".version.notify", result.target.toString()));
		this.notified = true;
					
	}

}
