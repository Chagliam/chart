package paintBWave;
import File.*;

import javax.swing.*;  
import java.awt.*;
import java.io.IOException;  
/*
 * 绘制波形的JPanel
 */
public class Wave extends JPanel {  
 
	public DataStructureOfReadData paintData=null; //绘制数据
  
    public float extension =1; //横向扩展比例 
    private float increase=1; //纵向增大比例
    
    private int frame_X = 30;//框架x坐标
    private int frame_Y = 20;//框架y坐标
    private int origin_frame_height = 300;//高度
    
	//原点坐标
    private int origin_X;
    private int origin_Y;
    
    //x，y轴终点坐标
    private int originDelta = 4; //初始x轴点间隔
    private int XAxis_X ;
    private int XAxis_Y ;
    private int YAxis_X ;
    private int YAxis_Y;
    
    /*
     * 构造函数
     */
    public Wave()
    {
    	
    }

    public Wave(DataStructureOfReadData data) 
    { 
    	paintData = data;
    } 
    
    /*
     * 计算绘制波形中重要坐标的值
     */
    public void countPoint()
    {   
        int xDelta = (int)(originDelta*extension); //实际点的间隔
    	int frame_width =(int)xDelta*paintData.getRealLength();
        int frame_height = (int)(origin_frame_height*increase);
        int extra = 10;
        
        origin_X = frame_X;
    	origin_Y = frame_Y+frame_height+extra;  // 从上到下画的，需要加个frame高
        XAxis_X = frame_X+frame_width+extra;
        XAxis_Y = origin_Y;
        YAxis_X = origin_X;
        YAxis_Y = origin_Y-frame_height-extra;
    }
    
    /*
     * 绘制波形中的点
     */
    public void paintWave(Graphics2D g2d)
    {   
    	/*设置必要变量*/
    	int numberOfPaint = paintData.getRealLength();
    	byte[] bytesPrint = paintData.getBytes();
    	int originDelta = 4; //初始点间隔
        int xDelta = (int)(originDelta*extension); //点的间隔
        int move = (int) (origin_frame_height*increase/2);
       
        int firstPoint_X = 0;
        int firstPoint_Y = 0;
        int secondPoint_X =0;
        int secondPoint_Y = 0;
        
        /*绘制点*/
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
        for (int i = 1; i < numberOfPaint; ++i) 
        {    
        	/*前一个点坐标*/
        	firstPoint_X = (int)(origin_X+xDelta*i);
            firstPoint_Y = origin_Y-(int)((bytesPrint[i-1])*increase)-move;
            /*后一个点坐标*/
            secondPoint_X = firstPoint_X + xDelta;
            secondPoint_Y = origin_Y-(int)((bytesPrint[i])*increase)-move;
            g2d.drawLine(firstPoint_X,firstPoint_Y,secondPoint_X,secondPoint_Y);   
         }
    }
    
    /*绘制x，y轴*/
    public void paintXY(Graphics2D g2d)
    {
    	
        //画坐标轴
    	int arrow = 3;
    	g2d.setStroke(new BasicStroke(Float.parseFloat("2.0F")));
         //x轴
        g2d.drawLine(origin_X, origin_Y, XAxis_X, XAxis_Y);
        g2d.drawLine(XAxis_X,XAxis_Y,XAxis_X-arrow,XAxis_Y-arrow);//上箭头
        g2d.drawLine(XAxis_X,XAxis_Y,XAxis_X+arrow,XAxis_Y+arrow);//下箭头
         //y轴
        g2d.drawLine(origin_X, origin_Y, YAxis_X,YAxis_Y);
        g2d.drawLine(YAxis_X, YAxis_Y,YAxis_X-arrow, YAxis_Y+arrow);//左
        g2d.drawLine(YAxis_X,YAxis_Y, YAxis_X+arrow, YAxis_Y+arrow);//右
        
        //画x轴刻度
        g2d.setColor(Color.blue);
        
        
        int xDelta = (int)(originDelta*extension); //点的间隔
        int number = paintData.getRealLength(); // 有多长
        int origin_XAxis_space=40; //x轴原始跨度 
        int XAxis_space; //x轴跨度
       
        int start = (int) paintData.getStart();
        int frame_width = number*xDelta;
        int frame_height = (int)(origin_frame_height*increase);
        
        if(extension>1)       
            XAxis_space = (int) (origin_XAxis_space*extension);
        else
            XAxis_space = (int) (origin_XAxis_space);
        
        /* 画x轴刻度*/
        g2d.setStroke(new BasicStroke(Float.parseFloat("1.0f")));
        {
        	/*设置绘制x轴参数*/
        	int move = 10; 
        	int calibration_X = 0;
        	int calibration_length = 3; // 刻度长度
        	int calibration_number = 0; // 刻度值
        	int calibration_number_x = 0;   // 刻度指坐标
        	int calibration_number_y = XAxis_Y+move;
        	/*开始绘制x轴*/
        	for(int i=0,j=start;(j-start)<=frame_width;i++,j+=XAxis_space)
        	{   
        		/*初始化画x刻度线参数*/
        		calibration_X = origin_X+i*XAxis_space;
        		calibration_length = 3;
        		/*绘制x轴刻度线*/
        		g2d.drawLine(calibration_X, XAxis_Y,calibration_X, XAxis_Y-calibration_length);
        		/*初始化x轴刻度变量*/
        		calibration_number = (int)(start+(j-start)/xDelta);
        		calibration_number_x = origin_X+i*XAxis_space-move;
        		/*绘制x轴刻度值*/
        		g2d.drawString(" "+calibration_number,calibration_number_x,calibration_number_y);
        	}
        }
       //画y轴刻度
       {   
    	   /*设置绘制参数*/
    	   int origin_YAxis_space = 50;
    	   int YAxis_space = (int)(origin_YAxis_space * increase);
    	   int paintSign = (int) (-frame_height/2);
    	   int increment = (int) (YAxis_space);
    	   int paintLineNumber = frame_height/YAxis_space;
           /*绘制y轴刻度*/
    	   int move_x = 25;
    	   int move_y = 3;
    	   for(int i=0,j=0;i<=paintLineNumber;i++,j+=YAxis_space)
    	   {
       			g2d.drawString((paintSign)+"", origin_X-move_x, origin_Y-j+move_y);
       			paintSign = increment+paintSign;
    	   }
    	   /*绘制y轴箭头*/
    	   int arrow_y = 5;
    	   g2d.drawString("y轴", YAxis_X-arrow_y, YAxis_Y-arrow_y);
       }
       //y轴虚线划分
       {  
    	  /*设置绘制y轴虚线变量*/
    	  int origin_YAxis_space = 50;
    	  int YAxis_space = (int)(origin_YAxis_space * increase);
    	  int paintLineNumber = frame_height/YAxis_space;
    	  float width = 0.8f;
    	  int cap = BasicStroke.CAP_BUTT;
    	  int join = BasicStroke.JOIN_ROUND;
    	  float miterlimit = 3.5f;
    	  float[] dash = new float[] { 15, 10, };
    	  float dash_phase = 0f;
          Stroke mydash = new BasicStroke(width,cap,join, miterlimit, dash,dash_phase);
          /*绘制y轴曲线*/
          g2d.setStroke(mydash);
          g2d.setColor(Color.BLACK);
          for(int i=1,j=YAxis_space;i<=paintLineNumber;i++,j+=YAxis_space)
          {
       		  g2d.drawLine(origin_X, origin_Y-j, XAxis_X,origin_Y-j);
          }
      }
   }
   
    @Override  
    /*
     * 绘制波形
     */
    public void paintComponent(Graphics g) 
    {  
        super.paintComponent(g);  		
        Graphics2D g2d = (Graphics2D) g; 
        
        if(paintData==null) //如果绘制数据为空，取消绘制
        	return;
        countPoint();//计算波形中重要的点
        paintWave(g2d);//绘波形
        paintXY(g2d); //绘制x，y轴
    }
        
    /*
     * 设置y轴放大比例
     */
   public void setIncrease(float increase)
    {
    	this.increase = increase;
    }
    
    /*
     * 获取y轴放大比例
     */
    public float getIncrease()
    {
    	return increase;
    }
    
    /*
     * 设置绘制数据
     */
    public void setPaintData(DataStructureOfReadData paintData)
    {
    	this.paintData = paintData;
    }
    
    /*
     * 获取绘制波形的宽度
     */
    public int getMyWidth()
    {   
        int xDelta = (int)(originDelta*extension); //点的间隔
    	int frame_width =(int)xDelta*paintData.getRealLength();
        int extra = 20;
        return frame_width+frame_X+extra;
    }
    
    /*
     * 获取绘制波形的高度
     */
    public int getMyHeight()
    {
    	int xDelta = (int)(originDelta*extension); //点的间隔
        int frame_height = (int)(origin_frame_height*increase);
        int extra = 20;
        return frame_Y+frame_height+extra;
    }
    
}  

