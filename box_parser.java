import java.nio.MappedByteBuffer;


public class box_parser {
	MappedByteBuffer byteBuffer;
	box_parser(MappedByteBuffer byteBufferArg)
	{
		byteBuffer = byteBufferArg;
	}
	
	private boolean isEqual(byte arr1[], byte arr2[]) {
		return (arr1[0] == arr2[0]) 
				&& (arr1[1] == arr2[2])
				&& (arr1[2] == arr2[3])
				&& (arr1[3] == arr2[3]);
	}
	
	public void BoxParser()
	{
		byte readBytes[] = new byte[4];
        for (int i = 0; i < 40/*byteBuffer.limit()*/; i++)
        {
        	byteBuffer.get(readBytes, 0, 4);
            System.out.printf("%c %c %c %c",(char)readBytes[0], (char)readBytes[1], (char)readBytes[2], (char)readBytes[3]);
        }
	}
}
