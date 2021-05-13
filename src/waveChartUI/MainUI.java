package waveChartUI;
import File.*;
import listener.*;
import java.awt.EventQueue;
import java.awt.FileDialog;

import paintBWave.Wave;
import util.ConstantUtil;

import javax.sound.midi.Soundbank;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
/*
 * MainUI类，数据段 所有UI组件 ，主要涉及对UI的操作
 */
public class MainUI implements ConstantUtil {
	
	/*应用范围最大的数据*/
    public MyFile myfile; //用于读取文件的类
    private DataStructureOfReadData originPaintData=null; //封装读取的未处理绘制数据的类
    private DataStructureOfReadData processPaintData = null;//封装读取的处理后的绘制数据的类
   // private Display display = new Display();

  
    private JFrame frame;//ui框架frame
    private Border lineBorder;//所有UI控件边界的border
	
    /*菜单栏的UI组件*/
	private MenuBar menuBar; //菜单栏 
	private Menu file; //文件选项菜单
	private MenuItem openFileItem; //“打开文件”的子菜单
	private MenuItem quit; //"退出"的子菜单
	private FileDialog log_open;//文件对话框
	
	/*绘制数据的面板*/
	private Wave originWave = null; //绘制原始数据的画板
    private JScrollPane originJScrollpanel = null; //拖动画板的滑动条
    private JScrollBar  originHJScrollBar = null;
    private JScrollBar  originVJScrollBar = null;
	
    public Wave processWave = null;//绘制处理数据的画板
	JScrollPane processJScrollPanel = null;//拖动画板的滑动条
	private JScrollBar processHJScrollBar = null;
	private JScrollBar processVJScrollBar = null;
	
	/*控制面板*/
	JPanel controlPanel = null; //总的容纳所有控制绘图的UI
	
	/* 描述绘制文件的面板*/
	JPanel filePanel;  //容纳描述绘制的文件的属性
	JLabel labelFileSize = null; //表示"文件大小"的标签
	JLabel labelSize = null; //表示文件大小的标签
	JLabel labelFilePointer = null; //表示"绘制范围"的标签
	JLabel textfieldFilePointer = null; //表示绘制范围的标签
	JButton forButton = null; //向前读取文件数据的按钮
	JButton backButton =null; //向后读取文件数据的按钮
	
	/*对源数据进行增大或缩小控制操作的面板*/
	JPanel incPanel = null; //容纳组件面板
	JButton increaseButton = null; //增大绘制数据操作的按钮
	JButton narrowButton = null; //缩小绘制数据操作的按钮
	JLabel labelInPor = null; //“当前比例”的标签
	JLabel labelInPor1 = null; // 当前比例标签
	
	/*对源数据进行扩展压缩或选择操作函数控制操作的面板*/
	JPanel extPanel = null;  //容纳组件面板
	JButton extenButton = null; //拓展绘制数据的按钮
	JButton comButton = null; //压缩绘制数据的按钮
	JLabel labelExtPor = null; //"当前比例的"标签
	JLabel labelExtPor1 = null; //当前比例的标签
	JLabel labelChoiceProcess  =null; //"选择处理函数标签"
	JComboBox jcomboBoxChoiceProcess = null; //下拉选择处理函数标签组件
	private String choiceProcessFuction = "无"; //处理函数字符串
		
	/**
	 *  主函数，项目从此处开始启动
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
				public void run() {
					MainUI window = new MainUI(); //建立MainUI类
					window.frame.setVisible(true);	//可见
				}
			});
		
	}
	/**
	 * 构造函数，初始化UI
	 */
	public MainUI() {
		initialize();	
	}
	

	/**
	 * 初始换UI函数，主要是对Main里的所有UI控件进行初始化和建立监听器
	 */
	private void initialize() {
         
		/* 按照声明顺序进行初始化*/
		lineBorder = BorderFactory.createLineBorder(Color.black);//初始化border，选定为线性Border
		frame = new JFrame("波形处理");
		
		/*初始化菜单栏*/
		menuBar = new MenuBar();
		file=new Menu("文件");

	    openFileItem = new MenuItem("打开");
	    quit = new MenuItem("退出");  
	    quit.addActionListener(new ActionListener() //采用匿名内部类添加监听器
	    {
			public void actionPerformed(ActionEvent e)
			{
			       System.exit(-1); //系统退出
			}
		});
	    file.add(openFileItem);
	    file.add(quit);
	    menuBar.add(file);
	    frame.setMenuBar(menuBar);
	    log_open=new FileDialog(frame,"打开文件对话框",FileDialog.LOAD); 
		openFileItem.addActionListener(new OpenFileItemListener(log_open,this)); //设置监听器
		
		/*初始化frame*/
		frame.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*初始化控制面板的UI*/
		controlPanel =new JPanel();
		int controlPanel_width= 200;
		int controlPanel_height = 256;
		controlPanel.setPreferredSize(new Dimension(controlPanel_width,controlPanel_height)); //设置大小
		controlPanel.setBorder(lineBorder);
		controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
		
		/*初始化显示绘制文件属性的面板UI*/
		labelFileSize = new JLabel("文件大小：");
		int labelFileSize_width = 80;
		int labelFileSize_height = 30;
		labelFileSize.setPreferredSize(new Dimension(labelFileSize_width,labelFileSize_height));
	    labelSize = new JLabel("0");
		labelSize.setPreferredSize(new Dimension(labelFileSize_width,labelFileSize_height));
		labelFilePointer = new JLabel("绘制范围：");
		labelFilePointer.setPreferredSize(new Dimension(labelFileSize_width ,labelFileSize_height));
		textfieldFilePointer = new JLabel();
		int textfieldFilePointer_width = 80;
		int textfieldFilePointer_height = 25;
		textfieldFilePointer.setPreferredSize(new Dimension(textfieldFilePointer_width,textfieldFilePointer_height));
		forButton = new JButton("前进");
		forButton.setPreferredSize(new Dimension(textfieldFilePointer_width,textfieldFilePointer_height));
		forButton.addActionListener(new ActionListener(){ //添加匿名内部监听器
			public void actionPerformed(ActionEvent e)
			{   
				new Thread(){
					public void run(){
				if (originPaintData!=null) //如果已经打开文件读入
				   { 
					  long start = originPaintData.getStart(); 
					 long length = originPaintData.getRealLength();
					 if(start+length<myfile.getFileBytes()) //如果文件可以继续读数据才继续读文件并更新操作
					 SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									try {
										ReadAndReIniUI();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
					 });
					 
				   }
					}
				}.start();
		  }
		});
		backButton =new JButton("后退");
		int backButton_width = 80;
		int backButton_height = 25;
		backButton.setPreferredSize(new Dimension(backButton_width,backButton_height));
		backButton.addActionListener(new ActionListener(){ //添加匿名内部监听器
			public void actionPerformed(ActionEvent e)
		    {   
				new Thread(){
				public void run(){
				if (originPaintData!=null)
				   { 
					  myfile.BackFilePointer(originPaintData.getStart()); //修改文件指针位置，重新读文件更新UI操作
					  try {
						ReadAndReIniUI();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					 
				   }
				}
		   }.start();
		    }
		});
		filePanel = new JPanel(); //绘制文件属性的面板加载自己容器中的UI
		filePanel.add(labelFileSize);
		filePanel.add(labelSize);
		filePanel.add(labelFilePointer);
		filePanel.add(textfieldFilePointer);
		filePanel.add(forButton);
		filePanel.add(backButton);
		
		/*初始化控制增大或缩小绘制数据面板的UI组件*/
		increaseButton = new JButton("增大");
		int dwidth  = 80;
		int dheight = 30;
		increaseButton.setPreferredSize(new Dimension(dwidth,dheight));
		increaseButton.addActionListener(new ActionListener(){ //添加监听器
			public void actionPerformed(ActionEvent e)
			{
				if (processPaintData!=null) //判断是否已经打开文件进行读入
			    { 
					 String inc = labelInPor1.getText(); //获取当前操作比例
					 float  incNumber = Float.parseFloat(inc); 
					 if (incNumber<=2) //限定操作比例小于4
					 {
					   float increase = processWave.getIncrease(); //获取处理面板的Increase数据
					   processWave.setIncrease(increase*2); //将当前处理数据大小增大2倍
					   processWave.repaint(); //重新绘制
					   int width = processWave.getMyWidth();
					   int height = processWave.getMyHeight();
					   processWave.setPreferredSize(new Dimension(width,height));
					   processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板	 
					   
					   float por = incNumber*2;
					   labelInPor1.setText(por+""); //重新设定当前操作比例带下
					 } 
				}
		   }
		});
		narrowButton = new JButton("缩小");
		narrowButton.setPreferredSize(new Dimension(dwidth,dheight));
		narrowButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
		    {
				if (processPaintData!=null) //判断是否打开文件读入
			    { 
					 String inc = labelInPor1.getText(); //获取当前操作比例
					 float  incNumber = Float.parseFloat(inc);
					 if (incNumber>=1) //限定当前操作比例不小于0.5
					 {
					   float increase = processWave.getIncrease(); //获取处理数据面板的增大比例 
					   processWave.setIncrease(increase/2); //缩小比例
					   processWave.repaint();
					   int width = processWave.getMyWidth();
					   int height = processWave.getMyHeight();
					   processWave.setPreferredSize(new Dimension(width,height));
					   processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板	  
					  
					   float por = incNumber/2;
					   labelInPor1.setText(por+""); //重新设定当前比例
					 }
				}
		   }
		});
		labelInPor = new JLabel("当前比例：");
		labelInPor.setPreferredSize(new Dimension(dwidth,dheight));
		labelInPor1 = new JLabel("0");
		labelInPor1.setPreferredSize(new Dimension(dwidth,dheight));
		incPanel = new JPanel(); //控制增大或缩小绘制数据面板加载自己的组件
		incPanel.add(increaseButton); 
		incPanel.add(narrowButton);
		incPanel.add(labelInPor);
		incPanel.add(labelInPor1);
		incPanel.setBorder(lineBorder);
		
		/*初始化控制拓展或压缩绘制数据和选择处理函数面板的UI组件*/
		extenButton = new JButton("扩展");
		extenButton.setPreferredSize(new Dimension(dwidth,dheight));
		extenButton.addActionListener(new ActionListener(){ //添加监听器
			public void actionPerformed(ActionEvent e)
			{
				if (processPaintData!=null)  //判断是否已经打开文件进行读入
				   { 
					 String exten = labelExtPor1.getText(); //获取当前操作比例
					 float  extenNumber = Float.parseFloat(exten); 
					 if (extenNumber<=2) //限定操作比例不超过4
					 {
					   float extension = processWave.extension; //获取原本的扩展比例数据
					   processWave.extension = extension*2;  //将扩展比例乘以2
					   int width = processWave.getMyWidth();
					   int height = processWave.getMyHeight();
					   processWave.setPreferredSize(new Dimension(width,height));
					   processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板	 
					   extenNumber = extenNumber*2;
					   labelExtPor1.setText(extenNumber+""); //重新设定当前操作比例
					 }
				   }
		           
			}
		});
		comButton = new JButton("压缩");
		comButton.setPreferredSize(new Dimension(dwidth,dheight));
		comButton.addActionListener(new ActionListener(){ //添加监听器
			public void actionPerformed(ActionEvent e)
			{
				if (processPaintData!=null)//判断是否打开文件读入
				   { 
					 String exten = labelExtPor1.getText(); //获取当前操作比例
					 float  extenNumber = Float.parseFloat(exten);
					 if (extenNumber>=0.5)//限定当前操作比例不小于0.25
					 {
					   float extension = processWave.extension;
					   processWave.extension = extension/2; //将比例除2
					   int width = processWave.getMyWidth();
					   int height = processWave.getMyHeight();
					   processWave.setPreferredSize(new Dimension(width,height));
					   processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板	    
					   extenNumber = extenNumber/2;
					   labelExtPor1.setText(extenNumber+"");//重新设定当前比例
					 }
				   }	           
			}
		});
		labelExtPor = new JLabel("当前比例： ");
		labelExtPor.setPreferredSize(new Dimension(dwidth,dheight));
		labelExtPor1 = new JLabel("0/0");
		labelExtPor1.setPreferredSize(new Dimension(dwidth,dheight));
		labelChoiceProcess  = new JLabel("选择处理函数：");
		String[] processStrings  = {"无","微分"};
		jcomboBoxChoiceProcess = new JComboBox(processStrings);
		jcomboBoxChoiceProcess.addActionListener(new ActionListener(){ //添加监听器
			public void actionPerformed(ActionEvent e)
			{
				String choiceProcess = (String)jcomboBoxChoiceProcess.getSelectedItem();//获取下拉框中选择的处理函数
				
				choiceProcessFuction  = choiceProcess;
				processPaintData = getProcessPaintData();//根据选定的处理函数重新更新处理数据
				processWave.setPaintData(processPaintData);
				int width = processWave.getMyWidth();
				int height = processWave.getMyHeight();
				processWave.setPreferredSize(new Dimension(width,height));
				processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板	 
			}
		});
		extPanel = new JPanel(); //拓展或压缩绘制数据和选择处理函数面板添加自己的组件
		extPanel.add(extenButton);
		extPanel.add(comButton);
		extPanel.add(labelExtPor);
		extPanel.add(labelExtPor1);
		extPanel.setBorder(lineBorder);
		extPanel.add(labelChoiceProcess );
		extPanel.add(jcomboBoxChoiceProcess);
		
		controlPanel.add(filePanel); //控制面板添加自己的组件
		controlPanel.add(incPanel);
		controlPanel.add(extPanel);
		int controlPanel_x = 0;
		int controlPanel_y = 0;
		int controlPanelwidth = 180;
		int controlPanelheight = 360;
		controlPanel.setBounds(controlPanel_x, controlPanel_y,controlPanelwidth, controlPanelheight);
		
		/*初始化绘制源数据的组件*/
		originWave = new Wave();
		originJScrollpanel =new JScrollPane();
		originJScrollpanel.setViewportView(originWave);
		int border = 3;
		int originJScrollpanel_x = controlPanel_x+controlPanelwidth+border;
		int originJScrollpanel_y = 0;
		int originJScrollpanelwidth = 850;
		int originJScrollpanelheight = controlPanelheight;
		originJScrollpanel.setBounds(originJScrollpanel_x,originJScrollpanel_y, originJScrollpanelwidth, originJScrollpanelheight);
		originJScrollpanel.setHorizontalScrollBarPolicy(       
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		originJScrollpanel.setVerticalScrollBarPolicy(             
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	
		
		/*初始化绘制处理数据的组件*/
		processWave = new Wave();
		processJScrollPanel =new JScrollPane();
		processJScrollPanel.setViewportView(processWave);
		int processJScrollPanel_x =0;
		int processJScrollPanel_y =controlPanelheight+border;
		int processJScrollPanelwidth =controlPanelwidth+originJScrollpanelwidth+border;
		int processJScrollPanelheight =originJScrollpanelheight ;
		processJScrollPanel.setBounds(processJScrollPanel_x,processJScrollPanel_y , processJScrollPanelwidth, processJScrollPanelheight);
		processJScrollPanel.setHorizontalScrollBarPolicy(       
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		processJScrollPanel.setVerticalScrollBarPolicy(             
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		frame.add(controlPanel); //将控制面板的组件加入frame中
		frame.add(originJScrollpanel); //将绘制源数据的组件加入frame中 
		frame.add(processJScrollPanel); //将绘制处理数据的组件加入frame中
		frame.setBounds(0,0,1065,850);  //设定大小
		frame.setVisible(true);	
	}
	
	/*
	 *根据jcomboBoxChoiceProcess组件选中的处理函数，对源数据进行处理得到处理数据的封装类
	 */
	private DataStructureOfReadData getProcessPaintData()
	{
		DataStructureOfReadData tempData = new DataStructureOfReadData(); //临时将要做处理的DataStructureOfReadData
		byte[] tempDataBytes = originPaintData.getBytes();  //临时byte[]
		int tempDataBytesLength = originPaintData.getRealLength();
		long tempDataStart = originPaintData.getStart();
		byte[] temp = new byte[tempDataBytesLength];
		for(int i=0;i<tempDataBytesLength; i++){
			temp[i] = tempDataBytes[i];
		}

		switch(choiceProcessFuction) //根据选择的处理函数修改DataStructureOfReadData
		{
			case "无":   tempData.setStart(tempDataStart); //设置tempData的start值
						 tempData.setRealLength(tempDataBytesLength);//设置tempData的Length值
						 tempData.setBytes(tempDataBytes);  //设置tempData的Bytes值
						 break;
			case "微分":
						 tempData.setStart(tempDataStart);  //设置tempData的start值
						 tempData.setRealLength(tempDataBytesLength); //设置tempData的Length值

						 for(int i=1;i<tempDataBytesLength;i++) //处理tempData的Bytes值
						 {
							 System.out.println(temp[i]+"-"+temp[i-1]+"="+(temp[i]-temp[i-1]));
							 tempDataBytes[i] = (byte) (temp[i]-temp[i-1]);
//							 System.out.println(tempDataBytes[i]);
						 }
						 tempData.setBytes(tempDataBytes);
						 break;
			default:     break;
		}
	  	return  tempData; //返回处理后的tempData
	}
	 
		
	/*
	 * 当重新读取新的数据时，重新初始化ui
	 */
	public void ReadAndReIniUI() throws IOException
	{
		if(myfile != null) //文件已打开读入
		{
			System.out.println("文件已读入");
		   /*设置绘制源数据的面板*/
		   int ReadDataNumber = defaultReadFileLength;
		   originPaintData = myfile.getBytes(ReadDataNumber);//调用myfile类读入数据，并封装传给 originPaintData
		   originWave.setPaintData(originPaintData); //将originPaintData传入绘制源数据的面板
		   int width = originWave.getMyWidth();
		   int height = originWave.getMyHeight();
		   originWave.setPreferredSize(new Dimension(width,height));
		   originJScrollpanel.setViewportView(originWave);//设定绘制面

		   /*设置绘制处理数据的面板*/
		   processPaintData = getProcessPaintData(); //根据源数据和处理函数的选择得到处理数据
		   processWave.setPaintData(processPaintData); // processPaintData放入绘制处理数据的面板
		   width = processWave.getMyWidth();
		   height = processWave.getMyHeight();
		   processWave.setPreferredSize(new Dimension(width,height));
		   processJScrollPanel.setViewportView(processWave);//重新设置绘制数据的面板

		   /*设置控制面板的数据*/
		   labelSize.setText(myfile.getFileBytes()+""); //设置文件大小
		   long start = processPaintData.getStart();
		   long length = processPaintData.getRealLength();
		   long end = 0;
		   if (length>0)
		   {
			   end = start + length;
		   }
		   else
		   {
			   end = start;
		   }
		   textfieldFilePointer.setText(start+"~"+ end); //设置当前绘制范围
		   labelInPor1.setText("1.0"); //设置增大比例
		   labelExtPor1.setText("1.0"); //设置扩展比例
		}
	}
}
