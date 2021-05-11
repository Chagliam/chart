package File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * 用于读取文件的类
 */
public class MyFile {
    
	private RandomAccessFile randAccessfile=null; //保存打开文件后的文件指针
	private long fileBytes=0; //文件总大小
	
	public long getFileBytes() {
		return fileBytes;
	}
	
	public void BackFilePointer(long start) //将文件指针向后移动
	{
		try {
			long temp = start;
			if (temp>1000)
			{
				temp = temp-1000;
			    randAccessfile.seek(temp);
			}
			else
			{
				temp = 0;
			    randAccessfile.seek(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public MyFile(String path) throws IOException {
		// TODO Auto-generated constructor stub
		randAccessfile = new RandomAccessFile(path,"r"); //打开文件
		fileBytes = randAccessfile.length(); //获取文件长度
	}
	
	public DataStructureOfReadData getBytes (long position,int length) throws IOException //读取数据并封装为DataStructureOfReadData
	{    
		DataStructureOfReadData tempdata = new DataStructureOfReadData();
		
		byte[] bytes= new byte[length];
		int templength=0;
		
		randAccessfile.seek(position);
		templength=randAccessfile.read(bytes);
		tempdata.setBytes(bytes);
		tempdata.setRealLength(templength);
		tempdata.setStart(randAccessfile.getFilePointer()-templength);
		
		return tempdata;
	}
	
	public DataStructureOfReadData getBytes (int length) throws IOException   //读取数据并封装为DataStructureOfReadData
	{  
	   DataStructureOfReadData tempData = new DataStructureOfReadData();
	   byte[] bytes = new byte[length];
	   int templength=0;
	   
	   templength=randAccessfile.read(bytes);
		System.out.println(templength);
	   tempData.setBytes(bytes);
	   tempData.setRealLength(templength);
	   tempData.setStart(randAccessfile.getFilePointer()-templength);
	   
	   return tempData;
	}
	
	public void gc() throws IOException
	{
		randAccessfile.close();
	}

}
