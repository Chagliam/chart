package test;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 * @author Xuan Luo
 * @date 2021/5/6 15:24
 */
public class WriteFIle {
    public static void main(String[] args) {
        String fileName="E:\\Users\\LEGION\\Desktop\\2.dat";
//        int value0=255;
        int value1= 20;
        int value2= -20;
        int value3= 60;
        try
        {
            //将DataOutputStream与FileOutputStream连接可输出不同类型的数据
            //FileOutputStream类的构造函数负责打开文件kuka.dat，如果文件不存在，
            //则创建一个新的文件，如果文件已存在则用新创建的文件代替。然后FileOutputStream
            //类的对象与一个DataOutputStream对象连接，DataOutputStream类具有写
            //各种数据类型的方法。
            DataOutputStream out=new DataOutputStream(new FileOutputStream(fileName));
            for(int i=0; i<20; i++){
                out.writeByte(value1);
                out.writeByte(value3);
//                out.writeByte(value1);
                out.writeByte(value2);
            }
            char i = 100;
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
