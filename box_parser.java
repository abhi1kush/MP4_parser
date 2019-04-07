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
	
	private int extractBoxSize()
	{
		return byteBuffer.getInt((byteBuffer.position()-8));
	}
	
	public void BoxParser()
	{
		Box boxObj = new Box();
		int currBoxSize;
		int skipLen;
		byte readBytes[] = new byte[4];
		
        for (int i = 0; i < 500 /*byteBuffer.limit()*/; i++)
        {
        	byteBuffer.get(readBytes, 0, 4);
        	String boxName = new String(readBytes);
        	if (-1 != boxObj.isBoxName(boxName)) {
        		String boxType = new String(readBytes);
        		if (boxType.equals("ftyp")) {
              		System.out.printf("%c%c%c%c: size %d\n",(char)readBytes[0], 
            				(char)readBytes[1], (char)readBytes[2], 
            				(char)readBytes[3], extractBoxSize());
              		skipLen = extractBoxSize() - 8;
              		
        		}
          	}
        	
        }
        System.out.printf("\n--- END ---");
	}
}
