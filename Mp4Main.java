import java.nio.MappedByteBuffer;

public class Mp4Main {
	public static MappedByteBuffer byteBuffer;
	
	public static void main(String[] args) {
		mmap mmapObj = new mmap("C:\\Users\\Abhishek\\eclipse-workspace-java\\MP4_parser\\src\\gameTrailer.mp4",0);
		try {
			byteBuffer = mmapObj.MapFileByteBuffer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Mp4Parser parser = new Mp4Parser(byteBuffer);
		//Box.printMap();
		//parser.stringMatchBoxes();
		//parser.printHexBox();
		parser.BoxParser();
	}
}
