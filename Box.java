import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

class BoxInfo
{
	String name;
	boolean isContainer;
	boolean isFullBox;
	
	BoxInfo(String name, boolean isContainer) {
		this.name = name;
		this.isContainer = isContainer;
		this.isFullBox = false;
	}
	
	BoxInfo(String name, boolean isContainer, boolean isFullBox) {
		this.name = name;
		this.isContainer = isContainer;
		this.isFullBox = isFullBox;
	}
}

public class Box {
	private static boolean isIntialised = false;
	private static BoxInfo[] boxList = {
		new BoxInfo("ftyp", false),
		new BoxInfo("pdin", false, true), 
		new BoxInfo("moov", true), //container
		new BoxInfo("mvhd", false, true),
		new BoxInfo("trak", true), //container
		new BoxInfo("tkhd", false, true),
		new BoxInfo("tref", false),
		new BoxInfo("edts", true), //container
		new BoxInfo("elst", false, true),
		new BoxInfo("mdia", true), //container
		new BoxInfo("mdhd", false, true),
		new BoxInfo("hdlr", false, true),
		new BoxInfo("minf", true), //container
		new BoxInfo("vmhd", false, true),
		new BoxInfo("smhd", false, true),
		new BoxInfo("hmhd", false, true),
		new BoxInfo("nmhd", false, true),
		new BoxInfo("dinf", true), //container
		new BoxInfo("dref", false, true),
		new BoxInfo("stbl", true), //container
		new BoxInfo("stsd", false, true),
		new BoxInfo("stts", false, true),
		new BoxInfo("stsc", false, true),
		new BoxInfo("ctts", false, true),
		new BoxInfo("stsz", false, true),
		new BoxInfo("stz2", false, true),
		new BoxInfo("stco", false, true),
		new BoxInfo("co64", false, true),
		new BoxInfo("stss", false, true),
		new BoxInfo("stsh", false, true),
		new BoxInfo("padb", false, true),
		new BoxInfo("stdp", false, true),
		new BoxInfo("sbgp", false, true),
		new BoxInfo("sgpd", false, true),
		new BoxInfo("subs", false),
		new BoxInfo("mvex", true), //container
		new BoxInfo("mehd", false, true),
		new BoxInfo("trex", false, true),
		new BoxInfo("ipmc", false),
		new BoxInfo("moof", true), //container
		new BoxInfo("mfhd", false, true),
		new BoxInfo("traf", true), //container
		new BoxInfo("tfhd", false, true),
		new BoxInfo("trun", false, true),
		new BoxInfo("sdtp", false, true),
		new BoxInfo("sdgp", false),
		new BoxInfo("subs", false, true),
		new BoxInfo("mfra", true), //container
		new BoxInfo("tfra", false, true),
		new BoxInfo("mfro", false, true),
		new BoxInfo("mdat", false),
		new BoxInfo("free", false),
		new BoxInfo("skip", true), //container
		new BoxInfo("udta", true), //container
		new BoxInfo("cprt", false, true),
		new BoxInfo("meta", true, true), //container 
		new BoxInfo("hdlr", false),
		new BoxInfo("ilst", true), //container
		new BoxInfo("gsst", false),
		new BoxInfo("gstd", false),
		new BoxInfo("ipmc", false, true),
		new BoxInfo("iloc", false, true),
		new BoxInfo("ipro", true, true), //container
		new BoxInfo("sinf", true), //container
		new BoxInfo("frma", false),
		new BoxInfo("imif", false, true),
		new BoxInfo("schm", false, true),
		new BoxInfo("schi", false),
		new BoxInfo("iinf", false, true),
		//new BoxInfo("xml",  false, true), /*only box name with 3 length, not required now/
		new BoxInfo("bxml", false, true),
		new BoxInfo("pitm", false, true)	
		//srpp fullbox
	};
	
	private static HashMap<String, BoxInfo> boxMap = new HashMap<>();
	
	public static void initMap()
	{
		for (int i = 0; i < boxList.length; i++) {
			boxMap.put(boxList[i].name, boxList[i]);
		}
		isIntialised = true;
	}
	
	static int isContainer(String str)
	{   
		if (false == isIntialised) {
			initMap();
		}
		if (boxMap.containsKey(str)) {
			return boxMap.get(str).isContainer ? 1 : 0;
		}
		return -1;
	}
	
	static int isBoxName(String str)
	{
		if (false == isIntialised) {
			initMap();
		}
		if(boxMap.containsKey(str)) {
			return 1;
		}
		return -1;
	}
	
	static int isFullBox(String str)
	{
		if (false == isIntialised) {
			initMap();
		}
		if(boxMap.containsKey(str)) {
			return boxMap.get(str).isFullBox ? 1 : 0;
		}
		return -1;
	}
	
	public static void printMap()
	{
		if (false == isIntialised) {
			initMap();
		}
		for (HashMap.Entry<String, BoxInfo> entry : boxMap.entrySet()) {
		    System.out.println(entry.getKey()+" : "+entry.getValue());
		}
	}
}
