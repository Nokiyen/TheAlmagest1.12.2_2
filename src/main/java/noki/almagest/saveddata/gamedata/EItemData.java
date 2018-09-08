package noki.almagest.saveddata.gamedata;

public enum EItemData implements IItemData {
	
	ALMAGEST("almagest.gui.item.almagest.display"),
	PLANISPHERE("almagest.gui.item.planisphere.display"),
	STAR_DUST("almagest.gui.item.start_dust.display"),
	STAR_MEMO("almagest.gui.item.star_memo.display"),
	MISSING_STAR_ALTAIR("almagest.gui.item.star_altair.display"),
	MISSING_STAR_CAPELLA("almagest.gui.item.star_capella.display"),
	MISSING_STAR_ARCTURUS("almagest.gui.item.star_arcturus.display"),
	MISSING_STAR_SIRIUS("almagest.gui.item.star_sirius.display"),
	MISSING_STAR_PROCYON("almagest.gui.item.star_procyon.display"),
	MISSING_STAR_CANOPUS("almagest.gui.item.star_canopus.display"),
	MISSING_STAR_RIGIL_KENT("almagest.gui.item.star_rigil_kent.display"),
	MISSING_STAR_HADAR("almagest.gui.item.star_hadar.display"),
	MISSING_STAR_ACRUX("almagest.gui.item.star_acrux.display"),
	MISSING_STAR_BECRUX("almagest.gui.item.star_becrux.display"),
	MISSING_STAR_DENEB("almagest.gui.item.star_deneb.display"),
	MISSING_STAR_ACHERNAR("almagest.gui.item.star_achernar.display"),
	MISSING_STAR_POLLUX("almagest.gui.item.star_pollux.display"),
	MISSING_STAR_REGULUS("almagest.gui.item.star_regulus.display"),
	MISSING_STAR_VEGA("almagest.gui.item.star_vega.display"),
	MISSING_STAR_BETELGEUSE("almagest.gui.item.star_betelgeuse.display"),
	MISSING_STAR_RIGEL("almagest.gui.item.star_rigel.display"),
	MISSING_STAR_FOMALHAUT("almagest.gui.item.star_fomalhaut.display"),
	MISSING_STAR_ANTARES("almagest.gui.item.star_antares.display"),
	MISSING_STAR_ALDEBARAN("almagest.gui.item.star_aldebaran.display"),
	MISSING_STAR_SPICA("almagest.gui.item.star_spica.display"),
	MISSING_STAR_MIRA("almagest.gui.item.star_mira.display"),
	MISSING_STAR_POLARIS("almagest.gui.item.star_polaris.display"),
	TAROT_CARDS("almagest.gui.item.tarot_cards.display"),
	HOROSCOPE("almagest.gui.item.horoscope.display"),
	DIFF_GRASS("almagest.gui.item.diff_grass.display"),
	BINOCULARS("almagest.gui.item.binoculars.display");
	
	private EItemData(String display) {
		this.displayString = display;
	}
	
	private String displayString;
	@Override
	public String getDisplay() {
		return this.displayString;
	}

}
