package noki.almagest.saveddata.gamedata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noki.almagest.helper.HelperConstellation.Constellation;


public class GameDataConstellation extends GameData {
	
	private Constellation constellation;
	private boolean presented;
	
	protected static final String key_presented = "presented";
	
	
	public GameDataConstellation(Constellation constellation) {
		
		this.constellation = constellation;
		this.name = new ResourceLocation("almagest", "constellation."+this.constellation.getId());
		
	}
	
	public Constellation getConstellation() {
		
		return this.constellation;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.constellation.getDisplay();
		
	}
	
	public void setPresented(boolean flag) {
		
		this.presented = flag;
		
	}
	
	public boolean isPresented() {
		
		return this.presented;
		
	}
	
	@Override
	public void readFromNbt(NBTTagCompound nbt) {
		
		this.obtained = nbt.getBoolean(key_obtained);
		this.presented = nbt.getBoolean(key_presented);
		
	}
	
	@Override
	public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
		
		nbt.setBoolean(key_obtained, this.obtained);
		nbt.setBoolean(key_presented, this.presented);
		return nbt;
		
	}
	
	@Override
	public void reset() {
		
		this.presented = false;
		super.reset();
		
	}

}
