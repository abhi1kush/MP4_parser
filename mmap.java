import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
	
public class mmap {
	 
    public void MapFile(String filename) throws Exception
    {
    	File fileObj = new File(filename);
    	long fileLen = fileObj.length();
   
        try(RandomAccessFile file = new RandomAccessFile(filename, "r"))
        {
            MappedByteBuffer byteBuffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileLen);
            byte byteArr[] = byteBuffer.array();
            System.out.printf("%x %x %x %x", byteArr[0], byteArr[1], byteArr[2], byteArr[3]);
        }
    }
}
