import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Mp4Parser {
	MappedByteBuffer byteBuffer;
	Mp4Parser(MappedByteBuffer byteBufferArg)
	{
		byteBuffer = byteBufferArg;
	}
		
	/*Expects curr position at after box_type (name)*/
	private int extractBoxSize()
	{
		return byteBuffer.getInt((byteBuffer.position()-8));
	}
	
	/*Expects curr position at starting of box*/
	private int extractBoxSizeAbs()
	{
		return byteBuffer.getInt();
	}
	
	/*Expects curr position at starting of Box */
	private void extractBoxHeaderMdf(BoxHeader obj) 
	{
		byte[] readName = new byte[4];
		obj.absPosition = byteBuffer.position();
		obj.size = byteBuffer.getInt();
		byteBuffer.get(readName, 0, 4);
		String boxName = new String(readName);
		obj.name = boxName;
		if (1 == Box.isFullBox(boxName)) {
			seekRel(4);
		}
	}
	
	/*
	 * aligned(8) class Box (unsigned int(32) boxtype, optional unsigned int(8)[16] extended_type) 
	 * {  unsigned int(32) size;  
	 * 	  unsigned int(32) type = boxtype;  
	 *    if (size==1) {   unsigned int(64) largesize;  } 
	 *    else if (size==0) {   // box extends to end of file  }  
	 *    if (boxtype==‘uuid’) {   unsigned int(8)[16] usertype = extended_type;  } 
	 * }
	 * */
	private void extractBoxHeader(BoxHeader obj) 
	{
		byte[] readName = new byte[4];
		int positionBackUp = byteBuffer.position();
		obj.absPosition = positionBackUp;
		obj.size = byteBuffer.getInt();
		byteBuffer.get(readName, 0, 4);
		String boxName = new String(readName);
		obj.name = boxName;
		byteBuffer.position(positionBackUp);
	}
	
	public void stringMatchBoxes()
	{
		byte readBytes[] = new byte[4];
		
		for (int i = 0; i < byteBuffer.limit() - 4; i++)
        {
			int pos = byteBuffer.position();
        	byteBuffer.get(readBytes, 0, 4);
        	byteBuffer.position(pos + 1);
        	String boxName = new String(readBytes);
        	if (-1 != Box.isBoxName(boxName)) {
        		System.out.printf("%s %d\n", boxName, byteBuffer.getInt(pos - 4));
          	}
        }
		System.out.printf("%d\n", byteBuffer.position());
		bufferReset();
	}
		
	private void seekRel(int pos)
	{
		byteBuffer.position(byteBuffer.position() + pos);
	}
	
	public void parseContainerBoxAbs(int boxPosition, int boxSize)
	{
		int currPositionBackup = byteBuffer.position();
		BoxHeader headerObj = new BoxHeader();
		
		byteBuffer.position(boxPosition);
		extractBoxHeaderMdf(headerObj);
		BoxHeader.addSpace();
		while (byteBuffer.position() < currPositionBackup + boxSize) {
			int startPos = byteBuffer.position();
			extractBoxHeader(headerObj);
			headerObj.print();
			int ret = Box.isContainer(headerObj.name);
			if (1 == ret) {
				parseContainerBoxAbs(headerObj.absPosition, headerObj.size);
			}else if (0 == ret){
				seekRel(headerObj.size);
			} else {
				System.out.println("Not a box: Parsing Error{");
				headerObj.print();
				System.out.println("}");
				seekRel(headerObj.size);
				//break;
			}
			if (startPos == byteBuffer.position()) {
				//break infinite loop.
				System.out.println("Breaking Infinite loop.");
				break;
			}
		}
		BoxHeader.delSpace();
		//Reset back to old position.
		//byteBuffer.position(currPositionBackup);
	}
	
	public void printHexBox()
	{
		BoxHeader boxObj = new BoxHeader();
		extractBoxHeader(boxObj);
		int lasti;
		for (int i = 0; i < byteBuffer.capacity() && i < boxObj.size; i +=8) {
			byte[] readBytes = new byte[8];
			byteBuffer.get(readBytes, 0, 8);
			String strBytes = new String(readBytes);
			System.out.printf("%s %x%x%x%x%x%x%x%x",strBytes, 
					readBytes[0],
					readBytes[1],
					readBytes[2],
					readBytes[3],
					readBytes[4],
					readBytes[5],
					readBytes[6],
					readBytes[7]);
			lasti = i;
		}
	}
	
	public void BoxParser()
	{
		BoxHeader currBoxHeader = new BoxHeader();
		
		while (byteBuffer.position() < byteBuffer.capacity()) {
			int startPos = byteBuffer.position();
			int ret;
			extractBoxHeader(currBoxHeader);
			ret = Box.isContainer(currBoxHeader.name); 
			if (-1 != ret) {
				currBoxHeader.print();
			}
			
			if (1 == ret) {
				parseContainerBoxAbs(currBoxHeader.absPosition, currBoxHeader.size);
			} else if (0 == ret) {
				seekRel(currBoxHeader.size);
			} else {
				System.out.println("Not a box");
			}
			if (startPos == byteBuffer.position()) {
				//break infinite loop.
				System.out.println("Breaking Infinite loop.");
				break;
			}
		}
	}
	
	public void bufferReset() 
	{
		byteBuffer.position(0);
	}
	
}


class BoxHeader {
	int size;
	String name;
	int absPosition;
	static int spaceCount = 0;
	
	static void addSpace()
	{
		spaceCount += 4;
	}
	
	static void delSpace()
	{
		spaceCount -= 4;
	}
	
	private void printSpaces(int count)
	{
		for (int i = 0; i < count; i +=4) {
			System.out.printf("    ");
		}
	}
	
	void print()
	{
		printSpaces(spaceCount);
		//System.out.printf("%s\n", name);
		System.out.printf("%s %d AbsPos: %d\n", name, size, absPosition);
	}
}

class FullBoxHeader extends BoxHeader {
	byte versionAndFlag[] = new byte[4];
}