import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

class BoxHeader {
	int size;
	String name;
	int absPosition;
}

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
	
	/*Expects curr position at after box_type (name)*/
	private int extractBoxSize()
	{
		return byteBuffer.getInt((byteBuffer.position()-8));
	}
	
	/*Expects curr position at starting of Box */
	private void extractBoxHeader(BoxHeader obj) 
	{
		byte[] readName = new byte[4];
		obj.absPosition = 
		obj.size = byteBuffer.getInt();
		byteBuffer.get(readName, 0, 4);
		String boxName = new String(readName);
		obj.name = boxName;
	}
	
	/*pointer is not at the starting of box, it is after type*/
	private void skipBox()
	{
		int size = extractBoxSize();
		int currPosition = byteBuffer.position();
		byteBuffer.position(currPosition + size - 8);
	}
	
	public void setPositionAtftypBox()
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
              		//set pointer at starting of box.
              		byteBuffer.position(byteBuffer.position() - 8);
              		break;
        		}
          	}
        }
        System.out.printf("\n--- END ---");
	}
	
	private void printSpaces(int count)
	{
		for (int i = 0; i < count; i++) {
			System.out.printf(" ");
		}
	}
	
	public void parseContainerBox(int boxPosition, int boxSize)
	{
		int spaceCount = 0;
		BoxHeader headerObj = new BoxHeader();
		int currPositionBackup = byteBuffer.position();
		byteBuffer.position(boxPosition);
		extractBoxHeader(headerObj);
		System.out.printf("%s\n", headerObj.name);
		spaceCount += 8;
		for (int i = 8; i < boxSize;) {
			extractBoxHeader(headerObj);
			if (0 == Box.isContainer(headerObj.name))
			skipBox();
		}
		
		//Reset back to old position.
		byteBuffer.position(currPositionBackup);
	}
	
	public void BoxParser()
	{
		Box boxObj = new Box();
		int currBoxSize;
		BoxHeader currBoxHeader = new BoxHeader();
		int skipLen;
		byte readBytes[] = new byte[4];
		Deque<BoxHeader> dfsStack = new ArrayDeque<BoxHeader>();
		
		setPositionAtftypBox();
		extractBoxHeader(currBoxHeader);
		System.out.printf("\nBoxParser: size %d type %s\n", currBoxHeader.size, currBoxHeader.name);
		if (1 == Box.isContainer(currBoxHeader.name)) {
			//dfsStack.addLast();
		}
	}
}
