import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

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
		for (int i = 0; i < count; i++) {
			System.out.printf(" ");
		}
	}
	
	void print()
	{
		printSpaces(spaceCount);
		System.out.printf("%s\n", name);
		//System.out.printf("%s %d AbsPos: %d\n", name, size, absPosition);
	}
}

public class box_parser {
	MappedByteBuffer byteBuffer;
	box_parser(MappedByteBuffer byteBufferArg)
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
	}
	
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
	
	/*pointer is not at the starting of box, it is after type*/
	private void skipBox()
	{
		int size = extractBoxSize();
		int currPosition = byteBuffer.position();
		byteBuffer.position(currPosition + size - 8);
	}
	
	/*pointer is at the starting of box*/
	private void skipBoxAbs()
	{
		int size = extractBoxSizeAbs();
		int currPosition = byteBuffer.position();
		byteBuffer.position(currPosition + size);
	}
	
	public void setPositionAtftypBox()
	{
		Box boxObj = new Box();
		int currBoxSize;
		int skipLen;
		byte readBytes[] = new byte[4];
		
		for (int i = 0; i < 50; i++)
        {
        	byteBuffer.get(readBytes, 0, 4);
        	String boxName = new String(readBytes);
        	if (-1 != boxObj.isBoxName(boxName)) {
        		String boxType = new String(readBytes);
        		if (boxType.equals("ftyp")) {
              		//set pointer at starting of box.
              		byteBuffer.position(byteBuffer.position() - 8);
              		break;
        		}
          	}
        }
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
			extractBoxHeader(headerObj);
			headerObj.print();
			int ret = Box.isContainer(headerObj.name);
			if (1 == ret) {
				parseContainerBoxAbs(headerObj.absPosition, headerObj.size);
			}else if (0 == ret){
				seekRel(headerObj.size);
			} else {
				System.out.println("Not a box: Parsing Error\n");
				break;
			}
		}
		BoxHeader.delSpace();
		//Reset back to old position.
		//byteBuffer.position(currPositionBackup);
	}
	
	public void BoxParser()
	{
		Box boxObj = new Box();
		int currBoxSize;
		BoxHeader currBoxHeader = new BoxHeader();
		int skipLen;
		byte readBytes[] = new byte[4];
		
		//setPositionAtftypBox();
		
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
				break;
			}
		}
	}
}
