import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
	
public class mmap {
	private int fileIdx = 0; 
	private String fileName;
	private FileChannel inChannel;
	private RandomAccessFile fileObj;
	public MappedByteBuffer byteBuffer;
	
	mmap(String fileNameArg, int fileIdxArg)
	{
		fileName = fileNameArg;
		fileIdx = fileIdxArg;
	}
	
    public MappedByteBuffer MapFileByteBuffer() throws Exception
    {   
        try
        {
        	fileObj = new RandomAccessFile(fileName, "r");
            inChannel = fileObj.getChannel();
            byteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            byteBuffer.load();
         }catch (FileNotFoundException e) {
        	 System.out.println("File Not Found");
         } catch (IOException e) {
             System.out.println("IOException");
         } catch (Exception e) {
             System.out.println("Some Other Exception");
         } finally{
         }
		return byteBuffer;
    }
    
    public void UnMap() {
        byteBuffer.clear(); 
        try {
			inChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			fileObj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

