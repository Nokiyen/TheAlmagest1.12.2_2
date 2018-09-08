package noki.almagest.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import noki.almagest.AlmagestCore;
import noki.almagest.item.ItemAlmagest;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.item.ItemMissingStar;
import noki.almagest.saveddata.gamedata.IItemData;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**********
 * @class HelperConstellation
 *
 * @description 星と星座のデータを保持し、取得するためのクラスです。
 * また、ItemStackへのNBT操作を行うメソッドも持ちます。
 */
public class HelperConstellation {
	
	//******************************//
	// define member variables.
	//******************************//
	public static HashMap<Integer, StarData> starList = new HashMap<Integer, StarData>();
	public static ListMultimap<Constellation, Integer> constStar = ArrayListMultimap.create();
	public static ListMultimap<Constellation, LineData> lineList = ArrayListMultimap.create();
	public static HashMap<Constellation, ArrayList<OtherStarData>> otherStarList = new HashMap<HelperConstellation.Constellation, ArrayList<OtherStarData>>();
	
	public static final int constSize = 88;
	
	
	//******************************//
	// define member methods.
	//******************************//
	//--------------------
	// Static Methods for Getting Star Data.
	//--------------------
	public static StarData getStar(int starNumber) {
		
		return starList.get(starNumber);
		
	}
	
	public static ArrayList<StarData> getStars(int constNumber) {
		
		return getStars(Constellation.getConstFromNumber(constNumber));
		
	}
	
	public static ArrayList<StarData> getStars(Constellation constName) {
		
		ArrayList<StarData> list = new ArrayList<StarData>();
		
		List<Integer> gotList = constStar.get(constName);
		for(Integer each: gotList) {
			list.add(starList.get(each));
		}
		
		return list;
		
	}
	
	public static List<LineData> getLines(int constNumber) {
		
		return getLines(Constellation.getConstFromNumber(constNumber));
		
	}
	
	public static List<LineData> getLines(Constellation constName) {
		
		return lineList.get(constName);
		
	}
	
	public static ArrayList<OtherStarData> getOtherStars(int constNumber) {
		
		return otherStarList.get(Constellation.getConstFromNumber(constNumber));
		
	}
	

	//--------------------
	// Static Methods about Stack and NBT.
	//--------------------
	public static ItemStack getConstStack(int constNumber, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constNumber, stackSize);
		
	}
	
	public static ItemStack getConstStack(Constellation constellation, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constellation.getId(), stackSize);

	}
	
	public static ItemStack getConstStack(int constNumber, int[] missingStars, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constNumber, missingStars, stackSize);
		
	}
	
	public static ItemStack getIncompleteConstStack(Constellation constellation, int stackSize) {
		
		return ItemBlockConstellation.getIncompleteConstStack(constellation, stackSize);
		
	}

	public static int getConstNumber(ItemStack stack) {
		
		return ItemBlockConstellation.getConstNumber(stack);
		
	}
	
	public static int[] getMissingStars(ItemStack stack) {
		
		return ItemBlockConstellation.getMissingStars(stack);
		
	}
	
	public static int getMissingStarNumber(ItemStack stack) {
		
		return ItemMissingStar.getMissingStarNumber(stack);
		
	}
	
	public static ItemStack setConstFlagToAlmagest(ItemStack stack, int constNumber, int flag) {
		
		return ItemAlmagest.setConstFlag(stack, constNumber, flag);
		
	}
	
	public static int getConstFlagFromAlmagest(ItemStack stack, int constNumber) {
		
		return ItemAlmagest.getConstFlag(stack, constNumber);
		
	}
	
	public static int getConstMetadata(int constNumber) {
		
		return Constellation.getMetadataFromNumber(constNumber);
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public static class StarData {
		
		//*****define member variables.*//
		public int hip;
		public String name;
		public int longitudeH;
		public int longitudeM;
		public double longitudeS;
		public int latitudeD;
		public int latitudeM;
		public double latitudeS;
		public double magnitude;
		public Spectrum spectrum;
		
		//*****define member methods.***//
		public StarData(int hip, String name,
				int longitudeH, int longitudeM, double longitudeS, int latitudeD, int latitudeM, double latitudeS,
				double magnitude, Spectrum spectrum) {
			this.hip = hip;
			this.name = name;
			this.longitudeH = longitudeH;
			this.longitudeM = longitudeM;
			this.longitudeS = longitudeS;
			this.latitudeD = latitudeD;
			this.latitudeM = latitudeM;
			this.latitudeS = latitudeS;
			this.magnitude = magnitude;
			this.spectrum = spectrum;
		}
		
		public double getCalculatedLong() {
			double value = (double)this.longitudeH*15D + (double)this.longitudeM*(15D/60D) + this.longitudeS*(15D/(60D*60D));
			return value;
		}
		
		public double getCalculatedLat() {
			double value = (double)this.latitudeD + (double)this.latitudeM*(1D/60D) + this.latitudeS*(1D/(60D*60D));
			return value;
		}
		
	}
	
	public static class LineData {
		
		//*****define member variables.*//
		public Constellation constName;
		public StarData star1;
		public StarData star2;
		
		//*****define member methods.***//
		public LineData(Constellation constName, int hip1, int hip2) {
			this.constName = constName;
			this.star1 = starList.get(hip1);
			this.star2 = starList.get(hip2);
		}
		
	}
	
	
	//--------------------
	// Enum for Constellations.
	//--------------------
	public enum Spectrum {
		
		//*****define enums.************//
		W("W", 0),	// most blue.
		O("O", 1),
		B("B", 2),
		A("A", 3),	// almost white.
		F("F", 4),
		G("G", 5),
		K("K", 6),
		M("M", 7);	// most red.

		
		//*****define member variables.*//
		private String name;
		private int metadata;
		
		//*****define member methods.***//
		private Spectrum(String name, int metadata) {
			this.name = name;
			this.metadata = metadata;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getMetadata() {
			return this.metadata;
		}
		
		//-----static methods.----------//
		public static Spectrum getSpectrumFromName(String name) {
			Spectrum[] spectrums = Spectrum.values();
			for(Spectrum each: spectrums) {
				if(each.getName().equals(name)) {
					return each;
				}
			}
			return null;
		}
		
	}
	
	public enum Constellation implements IItemData {
		
		//*****define enums.************//
		And("And",	1,	ConstFamily.Perseus),			//Andromeda-アンドロメダ
		Ant("Ant",	2,	ConstFamily.La_Caille),			//Antlia-ポンプ
		Aps("Aps",	3,	ConstFamily.Bayer),				//Apus-ふうちょう
		Aql("Aql",	4,	ConstFamily.Hercules,
				MissingStar.Altair),					//Aquila-わし
		Aqr("Aqr",	5,	ConstFamily.Zodiac),			//Aquarius-みずがめ
		Ara("Ara",	6,	ConstFamily.Hercules),			//Ara-さいだん
		Ari("Ari",	7,	ConstFamily.Zodiac),			//Aries-おひつじ
		Aur("Aur",	8,	ConstFamily.Perseus,
				MissingStar.Capella),					//Auriga-ぎょしゃ
		Boo("Boo",	9,	ConstFamily.Ursa_Major,
				MissingStar.Arcturus),					//Bootes-うしかい
		Cae("Cae",	10,	ConstFamily.La_Caille),			//Caelum-ちょうこくぐ
		Cam("Cam",	11,	ConstFamily.Ursa_Major),		//Camelopardalis-きりん
		Cap("Cap",	12,	ConstFamily.Zodiac),			//Capricornus-やぎ
		Car("Car",	13,	ConstFamily.Heavenly_Waters,
				MissingStar.Canopus),					//Carina-りゅうこつ
		Cas("Cas",	14,	ConstFamily.Perseus),			//Cassiopeia-カシオペヤ
		Cen("Cen",	15,	ConstFamily.Hercules,
				MissingStar.RigilKent,
				MissingStar.Hadar),						//Centaurus-ケンタウルス
		Cep("Cep",	16,	ConstFamily.Perseus),			//Cepheus-ケフェウス
		Cet("Cet",	17,	ConstFamily.Perseus,
				MissingStar.Mira),						//Cetus-くじら
		Cha("Cha",	18,	ConstFamily.Bayer),				//Chamaeleon-カメレオン
		Cir("Cir",	19,	ConstFamily.La_Caille),			//Circinus-コンパス
		CMa("CMa",	20,	ConstFamily.Orion,
				MissingStar.Sirius),					//Canis Major-おおいぬ
		CMi("CMi",	21,	ConstFamily.Orion,
				MissingStar.Procyon),					//Canis Minor-こいぬ
		Cnc("Cnc",	22,	ConstFamily.Zodiac),			//Cancer-かに
		Col("Col",	23,	ConstFamily.Heavenly_Waters),	//Columba-はと
		Com("Com",	24,	ConstFamily.Ursa_Major),		//Coma Berenices-かみのけ
		CrA("CrA",	25,	ConstFamily.Hercules),			//Corona Australis-みなみのかんむり
		CrB("CrB",	26,	ConstFamily.Ursa_Major),		//Corona Borealis-かんむり
		Crt("Crt",	27,	ConstFamily.Hercules),			//Crater-コップ
		Cru("Cru",	28,	ConstFamily.Hercules,
				MissingStar.Acrux,
				MissingStar.Becrux),					//Crux-みなみじゅうじ
		Crv("Crv",	29,	ConstFamily.Hercules),			//Corvus-からす
		CVn("CVn",	30,	ConstFamily.Ursa_Major),		//Canes Venatici-りょうけん
		Cyg("Cyg",	31,	ConstFamily.Hercules,
				MissingStar.Deneb),						//Cygnus-はくちょう
		Del("Del",	32,	ConstFamily.Heavenly_Waters),	//Delphinus-いるか
		Dor("Dor",	33,	ConstFamily.Bayer),				//Dorado-かじき
		Dra("Dra",	34,	ConstFamily.Ursa_Major),		//Draco-りゅう
		Equ("Equ",	35,	ConstFamily.Heavenly_Waters),	//Equuleus-こうま
		Eri("Eri",	36,	ConstFamily.Heavenly_Waters,
				MissingStar.Achernar),					//Eridanus-エリダヌス
		For("For",	37,	ConstFamily.La_Caille),			//Fornax-ろ
		Gem("Gem",	38,	ConstFamily.Zodiac,
				MissingStar.Pollux),					//Gemini-ふたご
		Gru("Gru",	39,	ConstFamily.Bayer),				//Grus-つる
		Her("Her",	40,	ConstFamily.Hercules),			//Hercules-ヘルクレス
		Hor("Hor",	41,	ConstFamily.La_Caille),			//Horologium-とけい
		Hya("Hya",	42,	ConstFamily.Hercules),			//Hydra-うみへび
		Hyi("Hyi",	43,	ConstFamily.Bayer),				//Hydrus-みずへび
		Ind("Ind",	44,	ConstFamily.Bayer),				//Indus-インディアン
		Lac("Lac",	45,	ConstFamily.Perseus),			//Lacerta-とかげ
		Leo("Leo",	46,	ConstFamily.Zodiac,
				MissingStar.Regulus),					//Leo-しし
		Lep("Lep",	47,	ConstFamily.Orion),				//Lepus-うさぎ
		Lib("Lib",	48,	ConstFamily.Zodiac),			//Libra-てんびん
		LMi("LMi",	49,	ConstFamily.Ursa_Major),		//Leo Minor-こじし
		Lup("Lup",	50,	ConstFamily.Hercules),			//Lupus-おおかみ
		Lyn("Lyn",	51,	ConstFamily.Ursa_Major),		//Lynx-やまねこ
		Lyr("Lyr",	52,	ConstFamily.Hercules,
				MissingStar.Vega),						//Lyra-こと
		Men("Men",	53,	ConstFamily.La_Caille),			//Mensa-テーブルさん
		Mic("Mic",	54,	ConstFamily.La_Caille),			//Microscopium-けんびきょう
		Mon("Mon",	55,	ConstFamily.Orion),				//Monoceros-いっかくじゅう
		Mus("Mus",	56,	ConstFamily.Bayer),				//Musca-はえ
		Nor("Nor",	57,	ConstFamily.La_Caille),			//Norma-じょうぎ
		Oct("Oct",	58,	ConstFamily.La_Caille),			//Octans-はちぶんぎ
		Oph("Oph",	59,	ConstFamily.Hercules),			//Ophiuchus-へびつかい
		Ori("Ori",	60,	ConstFamily.Orion,
				MissingStar.Betelgeuse,
				MissingStar.Rigel),						//Orion-オリオン
		Pav("Pav",	61,	ConstFamily.Bayer),				//Pavo-くじゃく
		Peg("Peg",	62,	ConstFamily.Perseus),			//Pegasus-ペガスス
		Per("Per",	63,	ConstFamily.Perseus),			//Perseus-ペルセウス
		Phe("Phe",	64,	ConstFamily.Bayer),				//Phoenix-ほうおう
		Pic("Pic",	65,	ConstFamily.La_Caille),			//Pictor-がか
		PsA("PsA",	66,	ConstFamily.Heavenly_Waters,
				MissingStar.Fomalhaut),					//Piscis Austrinus-みなみのうお
		Psc("Psc",	67,	ConstFamily.Zodiac),			//Pisces-うお
		Pup("Pup",	68,	ConstFamily.Heavenly_Waters),	//Puppis-とも
		Pyx("Pyx",	69,	ConstFamily.Heavenly_Waters),	//Pyxis-らしんばん
		Ret("Ret",	70,	ConstFamily.La_Caille),			//Reticulum-レチクル
		Scl("Scl",	71,	ConstFamily.La_Caille),			//Sculptor-ちょうこくしつ
		Sco("Sco",	72,	ConstFamily.Zodiac,
				MissingStar.Antares),					//Scorpius-さそり
		Sct("Sct",	73,	ConstFamily.Hercules),			//Scutum-たて
		Ser("Ser",	74,	ConstFamily.Hercules),			//Serpens(Cauda)-へび(尾)
		Sex("Sex",	75,	ConstFamily.Hercules),			//Sextans-ろくぶんぎ
		Sge("Sge",	76,	ConstFamily.Hercules),			//Sagitta-や
		Sgr("Sgr",	77,	ConstFamily.Zodiac),			//Sagittarius-いて
		Tau("Tau",	78,	ConstFamily.Zodiac,
				MissingStar.Aldebaran),					//Taurus-おうし
		Tel("Tel",	79,	ConstFamily.La_Caille),			//Telescopium-ぼうえんきょう
		TrA("TrA",	80,	ConstFamily.Hercules),			//Triangulum Australe-みなみのさんかく
		Tri("Tri",	81,	ConstFamily.Perseus),			//Triangulum-さんかく
		Tuc("Tuc",	82,	ConstFamily.Bayer),				//Tucana-きょしちょう
		UMa("UMa",	83,	ConstFamily.Ursa_Major),		//Ursa Major-おおぐま
		UMi("UMi",	84,	ConstFamily.Ursa_Major,
				MissingStar.Polaris),					//Ursa Minor-こぐま
		Vel("Vel",	85,	ConstFamily.Heavenly_Waters),	//Vela-ほ
		Vir("Vir",	86,	ConstFamily.Zodiac,
				MissingStar.Spica),						//Virgo-おとめ
		Vol("Vol",	87,	ConstFamily.Bayer),				//Volans-とびうお
		Vul("Vul",	88,	ConstFamily.Hercules);			//Vulpecula-こぎつね
		
		
		//*****define member variables.*//
		private String name;
		private int id;
//		private boolean ecliptical;	//whether it is an ecliptical constellation(黄道十二星座) or not.
		private boolean incomplete;	//whether it can be incomplete or not.
		private MissingStar[] missingStars;
		private ConstFamily family;
		
		private int raH;
		private int raM;
		private double ra;
		private double declination;
		
		//*****define member methods.***//
		private Constellation(String name, int id, ConstFamily family, MissingStar... missingStars) {
			this.name = name;
			this.id = id;
//			this.ecliptical = ecliptical;
			this.incomplete = missingStars.length == 0 ? false : true;
			this.family = family;
			this.missingStars = missingStars;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getId() {
			return this.id;
		}
		
		public boolean isEcliptical() {
			return this.family == ConstFamily.Zodiac;
		}
		
		public boolean isIncomplete() {
			return this.incomplete;
		}
		
		public MissingStar[] getMissingStars() {
			return this.missingStars;
		}
		
		public ConstFamily getFamily() {
			return this.family;
		}
		
		public void setRa(int raH, int raM) {
			this.raH = raH;
			this.raM = raM;
			this.ra = (double)this.raH*15D + (double)this.raM*(15D/60D);
		}
		
		public void setDec(int declination) {
			this.declination = (double)declination;
		}
		
		public double getRa() {
			return this.ra;
		}
		
		public double getDec() {
			return this.declination;
		}
		
		@Override
		public String toString() {
			return this.getName();
		}
		
		@Override
		public String getDisplay() {
			return "almagest.gui.constellation."+String.format("%02d", this.getId())+".name";
		}
		
		//-----static methods.----------//
		public static Constellation getConstFromNumber(int constNumber) {
			constNumber = MathHelper.clamp(constNumber, 1, Constellation.values().length) - 1;
			return Constellation.values()[constNumber];
		}
		
		public static Constellation getConstFromString(String constName) {
			Constellation[] constellations = Constellation.values();
			for(Constellation each: constellations) {
				if(each.getName().equals(constName)) {
					return each;
				}
			}
			return null;
		}
		
		public static int getMetadataFromNumber(int constNumber) {
			return getConstFromNumber(constNumber).isEcliptical() == true ? 1 : 0;
		}
		
	}
	
	public enum ConstFamily {
		
		//*****define enums.************//
		Zodiac,
		La_Caille,
		Bayer,
		Heavenly_Waters,
		Hercules,
		Orion,
		Perseus,
		Ursa_Major;
		
		private ArrayList<Constellation> familyMember;
		
		public ArrayList<Constellation> getMembers() {
			if(this.familyMember == null) {
				this.familyMember = new ArrayList<Constellation>();
			}
			if(this.familyMember.size() == 0) {
				for(Constellation each: Constellation.values()) {
					if(each.getFamily() == this) {
						this.familyMember.add(each);
					}
				}
			}
			return this.familyMember;
		}
		
	}
	
	public enum MissingStar {
		
		//*****define enums.************//
		Altair		("Altair",		97649,	Constellation.Aql),
		Capella		("Capella",		24608,	Constellation.Aur),
		Arcturus	("Arcturus",	69673,	Constellation.Boo),
		Sirius		("Sirius",		32349,	Constellation.CMa),
		Procyon		("Procyon",		37279,	Constellation.CMi),
		Canopus		("Canopus",		30438,	Constellation.Car),
		RigilKent	("RigilKent",	71683,	Constellation.Cen),
		Hadar		("Hadar",		68702,	Constellation.Cen),
		Acrux		("Acrux",		60718,	Constellation.Cru),
		Becrux		("Becrux",		62434,	Constellation.Cru),
		Deneb		("Deneb",		102098,	Constellation.Cyg),
		Achernar	("Achernar",	7588,	Constellation.Eri),
		Pollux		("Pollux",		37826,	Constellation.Gem),
		Regulus		("Regulus",		49669,	Constellation.Leo),
		Vega		("Vega",		91262,	Constellation.Lyr),
		Betelgeuse	("Betelgeuse",	27989,	Constellation.Ori),
		Rigel		("Rigel",		24436,	Constellation.Ori),
		Fomalhaut	("Fomalhaut",	113368,	Constellation.PsA),
		Antares		("Antares",		80763,	Constellation.Sco),
		Aldebaran	("Aldebaran",	21421,	Constellation.Tau),
		Spica		("Spica",		65474,	Constellation.Vir),
		Mira		("Mira",		10826,	Constellation.Cet),
		Polaris		("Polaris",		11767,	Constellation.UMi);
		
		
		//*****define member variables.*//
		private String name;
		private int hip;
		private Constellation constellation;
		
		//*****define member methods.***//
		private MissingStar(String name, int hip, Constellation constellation) {
			this.name = name;
			this.hip = hip;
			this.constellation = constellation;
		}
		
		public String getName() {
			return this.name;
		}

		public int getStarNumber() {
			return this.hip;
		}
		
		public Constellation getConst() {
			return this.constellation;
		}
		
	}
	
	public static class OtherStarData {
		
		//*****define member variables.*//
		public int hip;
		public double ra;
		public double dec;
		public double magnitude;
		public Constellation constellation;
		public Spectrum spectrum;
		
		//*****define member methods.***//
		public OtherStarData(int hip, double ra, double dec, double magnitude, Constellation constellation, Spectrum spectrum) {
			this.hip = hip;
			this.ra = ra*360D/24D;
			this.dec = dec;
			this.magnitude = magnitude;
			this.constellation = constellation;
			this.spectrum = spectrum;
		}
		
	}
	
	//--------------------
	// Method for Registering Star Data.
	//--------------------
	public static void registerStarData(FMLPreInitializationEvent event) {
		
		File file = event.getSourceFile();	// get the mod's jar file.
		try {
			JarFile jar = new JarFile(file);
			InputStream inputStream;
			BufferedReader reader;
			String container = "";	// local variable for containing csv's line.
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_star.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] stars = new String[10];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					stars[i] = tokens.nextToken();
				}
				StarData star = new StarData(
						Integer.valueOf(stars[0]),
						stars[1] == "null" ? null : stars[1],
						Integer.valueOf(stars[2]), Integer.valueOf(stars[3]), Double.valueOf(stars[4]),
						Integer.valueOf(stars[5]), Integer.valueOf(stars[6]), Double.valueOf(stars[7]),
						Double.valueOf(stars[8]), Spectrum.getSpectrumFromName(stars[9]));
				starList.put(Integer.valueOf(stars[0]), star);
			}
			reader.close();
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_composition.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] compositions = new String[2];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					compositions[i] = tokens.nextToken();
				}
				constStar.put(Constellation.getConstFromString(compositions[0]), Integer.valueOf(compositions[1]));
			}
			reader.close();
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_line.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] lines = new String[3];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					lines[i] = tokens.nextToken();
				}
				LineData line = new LineData(Constellation.getConstFromString(lines[0]),
						Integer.valueOf(lines[1]), Integer.valueOf(lines[2]));
				lineList.put(Constellation.getConstFromString(lines[0]), line);
			}
			reader.close();
			
			//	resolve constellation_others.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_others.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			stars = new String[6];
			
			for(Constellation each: Constellation.values()) {
				otherStarList.put(each, new ArrayList<OtherStarData>());
			}
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					stars[i] = tokens.nextToken();
				}
				Constellation targetConst = Constellation.getConstFromString(stars[4]);
				OtherStarData otherStar = new OtherStarData(
						Integer.valueOf(stars[0]), Double.valueOf(stars[1]),
						Double.valueOf(stars[2]), Double.valueOf(stars[3]), targetConst, Spectrum.getSpectrumFromName(stars[5]));
				otherStarList.get(targetConst).add(otherStar);
			}
			reader.close();
			
			//	resolve constellation_pos.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_pos.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			stars = new String[4];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					stars[i] = tokens.nextToken();
				}
				Constellation targetConst = Constellation.getConstFromNumber(Integer.valueOf(stars[0]));
				targetConst.setRa(Integer.valueOf(stars[1]), Integer.valueOf(stars[2]));
				targetConst.setDec(Integer.valueOf(stars[3]));
			}
			reader.close();
			
			jar.close();
		}
		catch(IOException e) {
			AlmagestCore.log(e.getMessage());
		}		
	
	}

}
