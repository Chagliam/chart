package File;
/*
 * 封装从文件中读取的数据
 */
public class DataStructureOfReadData {
    private byte[] bytes; //储存读取的数据
    private int realLength; //读取的数据长度
    private long start; //读取的数据在文件中起始位置

    public DataStructureOfReadData(){
    	realLength = 0;
        start = 0;
    }

    public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

    public byte[] getBytes() {
		byte[] temp = new byte[realLength];
		for(int i=0;i<realLength;++i)
		{
			temp[i] = bytes[i];
		}
		return temp;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getRealLength() {
		return realLength;
	}

	public void setRealLength(int realLength) {
		this.realLength = realLength;
	}
	
}
