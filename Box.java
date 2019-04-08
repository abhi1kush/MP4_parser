import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;

class BoxInfo
{
	String name;
	boolean isContainer;
	
	BoxInfo(String name, boolean isContainer) {
		this.name = name;
		this.isContainer = isContainer;
	}
}

public class Box {
	private static BoxInfo[] boxList = {
		new BoxInfo("ftyp", false),
		new BoxInfo("pdin", false), 
		new BoxInfo("moov", true), //container
		new BoxInfo("mvhd", false),
		new BoxInfo("trak", true), //container
		new BoxInfo("tkhd", false),
		new BoxInfo("tref", false),
		new BoxInfo("edts", true), //container
		new BoxInfo("elst", false),
		new BoxInfo("mdia", true), //container
		new BoxInfo("mdhd", false),
		new BoxInfo("hdlr", false),
		new BoxInfo("minf", true), //container
		new BoxInfo("vmhd", false),
		new BoxInfo("smhd", false),
		new BoxInfo("hmhd", false),
		new BoxInfo("nmhd", false),
		new BoxInfo("dinf", true), //container
		new BoxInfo("dref", false),
		new BoxInfo("stbl", true), //container
		new BoxInfo("stsd", false),
		new BoxInfo("stts", false),
		new BoxInfo("stsc", false),
		new BoxInfo("ctts", false),
		new BoxInfo("stsz", false),
		new BoxInfo("stz2", false),
		new BoxInfo("stco", false),
		new BoxInfo("co64", false),
		new BoxInfo("stss", false),
		new BoxInfo("stsh", false),
		new BoxInfo("padb", false),
		new BoxInfo("stdp", false),
		new BoxInfo("sbgp", false),
		new BoxInfo("sgpd", false),
		new BoxInfo("subs", false),
		new BoxInfo("mvex", true), //container
		new BoxInfo("mehd", false),
		new BoxInfo("trex", false),
		new BoxInfo("ipmc", false),
		new BoxInfo("moof", true), //container
		new BoxInfo("mfhd", false),
		new BoxInfo("traf", true), //container
		new BoxInfo("tfhd", false),
		new BoxInfo("trun", false),
		new BoxInfo("sdtp", false),
		new BoxInfo("sdgp", false),
		new BoxInfo("subs", false),
		new BoxInfo("mfra", true), //container
		new BoxInfo("tfra", false),
		new BoxInfo("mfro", false),
		new BoxInfo("mdat", false),
		new BoxInfo("free", false),
		new BoxInfo("skip", true), //container
		new BoxInfo("udta", true), //container
		new BoxInfo("cprt", false),
		new BoxInfo("meta", false), //container //causing parsing error.
		new BoxInfo("hdlr", false),
		new BoxInfo("ipmc", false),
		new BoxInfo("iloc", false),
		new BoxInfo("ipro", true), //container
		new BoxInfo("sinf", true), //container
		new BoxInfo("frma", false),
		new BoxInfo("imif", false),
		new BoxInfo("schm", false),
		new BoxInfo("schi", false),
		new BoxInfo("iinf", false),
		//new BoxInfo("xml",  false), /*only box name with 3 length, not required now/
		new BoxInfo("bxml", false),
		new BoxInfo("pitm", false)	
	};
	
	private static HashMap<String, BoxInfo> boxMap = new HashMap<>();
	
	Box()
	{
		for (int i = 0; i < boxList.length; i++) {
			boxMap.put(boxList[i].name, boxList[i]);
		}
	}
	
	static int isContainer(String str)
	{
		if(boxMap.containsKey(str)) {
			return boxMap.get(str).isContainer ? 1 : 0;
		}
		return -1;
	}
	
	static int isBoxName(String str)
	{
		if(boxMap.containsKey(str)) {
			return boxMap.get(str).isContainer ? 1 : 0;
		}
		return -1;
	}
}
