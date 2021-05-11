package listener;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import File.DataStructureOfReadData;
import File.MyFile;
import waveChartUI.MainUI;

/*
 * 打开文件的监听器
 */
public class OpenFileItemListener implements ActionListener {
    
	private MainUI ui;
	private FileDialog fileDialog;
	public OpenFileItemListener(FileDialog open,MainUI ui) {
		// TODO Auto-generated constructor stub
		this.ui = ui;
		this.fileDialog = open;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 fileDialog.setVisible(true);  
		 
		 if(fileDialog.getFile()!=null)  //判断是否文件对话框获得文件路径
         {  
             String path = fileDialog.getDirectory()+fileDialog.getFile(); //根据文件对话框获得的文件路径打开文件
             try {  
                 if(ui.myfile!=null)//如果已有打开文件，关闭该文件
            	   ui.myfile.gc();
                 
                 ui.myfile = new MyFile(path); 
                 ui.ReadAndReIniUI(); //回调方法
             } catch (IOException exception) {  
                 // TODO Auto-generated catch block  
                 exception.printStackTrace();  
             }  
         }  
	}

}
