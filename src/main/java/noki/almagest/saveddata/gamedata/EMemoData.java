package noki.almagest.saveddata.gamedata;

import net.minecraft.util.math.MathHelper;

public enum EMemoData implements IItemData {
	
	MEMO001(1),
	MEMO002(2),
	MEMO003(3),
	MEMO004(4),
	MEMO005(5),
	MEMO006(6),
	MEMO007(7),
	MEMO008(8),
	MEMO009(9),
	MEMO0010(10),
	MEMO0011(11),
	MEMO0012(12),
	MEMO0013(13),
	MEMO0014(14),
	MEMO0015(15),
	MEMO0016(16),
	MEMO0017(17),
	MEMO0018(18),
	MEMO0019(19),
	MEMO0020(20);

	private int id;
	
	private EMemoData(int id) {
		
		this.id = id;
		
	}
	
	@Override
	public String getDisplay() {
		
		return "almagest.gui.almagest.memo."+String.format("%02d",this.id);
		
	}
	
	public int getId() {
		
		return this.id;
		
	}
	
	public static EMemoData getMemoData(int id) {
		
		id = MathHelper.clamp(id, 1, EMemoData.values().length);
		
		for(EMemoData each: EMemoData.values()) {
			if(each.getId() == id) {
				return each;
			}
		}
		
		return null;
	}
	
}
