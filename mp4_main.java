import java.nio.MappedByteBuffer;

public class mp4_main {
	public static MappedByteBuffer byteBuffer;
	
	public static void main(String[] args) {
		mmap mmapObj = new mmap("C:\\Users\\Abhishek\\eclipse-workspace-java\\MP4_parser\\src\\prashant.mp4",0);
		try {
			byteBuffer = mmapObj.MapFileByteBuffer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		box_parser parser = new box_parser(byteBuffer);
		parser.BoxParser();
	}
}
