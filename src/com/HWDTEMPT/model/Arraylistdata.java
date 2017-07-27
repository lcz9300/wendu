package com.HWDTEMPT.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arraylistdata {

	static List<Integer> ECGxinlv = new ArrayList<Integer>();
	static double th11=0.99529868;
	static double th22=0.99764934;
	
	
	//float信号的二阶差分的八点平滑滤波
		public static float[] eightpin(float[] e)
		{
			float [] ECGping = new float [e.length];
			if (e.length>8)
			{
		       for(int i=0;i<7;i++)
		           {
			         ECGping[i]=e[i];
		           }
			   for (int i=7;i<e.length;i++)
				  {
				   
					float lll= ((e[i]+e[i-1]+e[i-2]+e[i-3]+e[i-4]+e[i-5]+e[i-6]+e[i-7])/(8));
					ECGping[i]=lll;
				  }						
			}
			else
			{
				   for(int i=0;i<e.length;i++)
		           {
			         ECGping[i]=e[i];
		           }
			}
			
			return ECGping;
		}
	
	//int数组信号的八点滤波算法
	public static int [] eightping(int [] ECGData)
	{
		int [] ECGping = new int [ECGData.length];
		if (ECGData.length>8)
		{
	       for(int i=0;i<7;i++)
	           {
		         ECGping[i]=ECGData[i];
	           }
		   for (int i=7;i<ECGData.length;i++)
			  {
				int kkk= (int) ((ECGData[i]+ECGData[i-1]+ECGData[i-2]+ECGData[i-3]+ECGData[i-4]+ECGData[i-5]+ECGData[i-6]+ECGData[i-7])/(double)(8));
				ECGping[i]=kkk;
			  }						
		}
		else
		{
			   for(int i=0;i<ECGData.length;i++)
	           {
		         ECGping[i]=ECGData[i];
	           }
		}
		
		return ECGping;
	}
	
	//float动态数组信号的八点平滑
	public static ArrayList<Float> eightping(ArrayList<Float> e, int i, int size)
	{
			ArrayList<Float> ECGping = new ArrayList<Float>();
			
			if (size>8)
			{
		
				for (int mm=7;mm<size;mm++)
				{
					ECGping.add((e.get(mm)+e.get(mm-1)+e.get(mm-2)+e.get(mm-3)+e.get(mm-4)+e.get(mm-5)+e.get(mm-6)+e.get(mm-7))/8);
				}						
			}
			
			return ECGping;
	}
	
	//向动态数组中添加数据，保持动态数组的长度不变
	public static List<Integer> addxinlv(List<Integer>ECGData)
	{
		int Length=16000;
		
		for(int i=0;i<ECGData.size()-1;i++)
		{
		   if(ECGxinlv.size()<Length)
		    {
			   ECGxinlv.add(ECGData.get(i));
		    }
		   else
		   {
			   ECGxinlv.remove(0);
			   ECGxinlv.add(ECGData.get(i));
		   }
		}				
		return ECGxinlv;
	}
	
	
	
	//去除基线漂移，要求数组长度不能太短
	public static int [] jixianquchu(int [] ECGData)
	{
		int [] jixian= new int [ECGData.length];
		jixian[0]=ECGData[0];
	
		for(int i=1;i<jixian.length;i++)
		{
			int tmp=(int) (th11*(double)jixian[i-1]+th22*(double)(ECGData[i]-ECGData[i-1]));
			jixian[i]=tmp; 
		}
	
		return jixian;
	}
	
	
	//改进的Levkov滤波算法，去除基线漂移
	public static List<Integer> Levkov(List<Integer>ECGData)
	{

		int N=0;
		List<Integer> jixian= new ArrayList<Integer>();
		for(int i=0;i<ECGData.size();i++)
		{
			jixian.add(ECGData.get(i));
		}
		
		for(int i=10;i<jixian.size();i++)
		{
			int D1=ECGData.get(i-8)-ECGData.get(i);
			int D2=ECGData.get(i-9)-ECGData.get(i-1);
			if(Math.abs(D1-D2)<20)
			{
			jixian.set(i-4, (int) (ECGData.get(i)+ECGData.get(i-1)+ECGData.get(i-2)+ECGData.get(i-3)+ECGData.get(i-4)+ECGData.get(i-5)+ECGData.get(i-6)+ECGData.get(i-7))/8); 
		     N=ECGData.get(i-4)-jixian.get(i-4);
			}
			else
			{
				jixian.set(i-4, ECGData.get(i-4)-N);
			}
			
		}
	
		return jixian;
	}

//去掉数组的前面部分
	public static ArrayList<Integer> RemoveHead(ArrayList<Integer> eCGDataListC,int length) 
	{
		ArrayList<Integer> eCGData=eCGDataListC;
		// TODO Auto-generated method stub
		while(eCGData.size()>length)
		{
			eCGData.remove(0);
		}
		return eCGData;
		
	}



	
	public static Map<String, Object> Ask_Jida_Jixiao(int [] data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		
	    int []kk=new int [data.length];
		
		for(int i=0;i<data.length-1;i++)
		{
			kk[i]=data[i]-data[i+1];
		}
		
		int []kkpdw=new int[data.length];
		int []jida=new int[data.length];
		
		for(int i=0;i<kk.length;i++)
		{
			if(kk[i]<0)
			{
				kkpdw[i]=1;
			}
			else
			{
				kkpdw[i]=0;
			}
		}
		
		for(int i=0;i<kkpdw.length-1;i++)
		{
			if((kkpdw[i]-kkpdw[i+1])>0)
			{
				jida[i]=(kkpdw[i]-kkpdw[i+1]);
			}
			else
			{
				jida[i]=0;
			}
		}
		
		int []kkndw=new int[data.length];
		int []jixiao=new int[data.length];
		
		for(int i=0;i<kk.length;i++)
		{
			if(kk[i]>0)
			{
				kkndw[i]=1;
			}
			else
			{
				kkndw[i]=0;
			}
		}
		
		for(int i=0;i<kk.length-1;i++)
		{
			if((kkndw[i]-kkndw[i+1])>0)
			{
				jixiao[i]=(kkndw[i]-kkndw[i+1]);
			}
			else
			{
				jixiao[i]=0;
			}
		}
		
		//把需要返回的数据装到map中
				map.put("int[]1",jida);
				map.put("int[]2",jixiao);
		return map;
	}

	//求数组中最大值
	public static int Find_max(int []data,int start,int end)
	{
		int MAX=data[start];
		for(int i=start;i<end;i++)
		{
			if(data[i]>MAX)
			{
				MAX=data[i];
			}
		}
		
		return MAX;
	}
	
	
	//求数组中最小值
	public static int Find_min(int []data,int start,int end)
	{
		int MIN=data[start];
		for(int i=start;i<end;i++)
		{
			if(data[i]<MIN)
			{
				MIN=data[i];
			}
		}
		
		return MIN;
	}
	
	
	//求数组中最小值所在位置
		public static int Find_min_location(int []data,int start,int end)
		{
			int X=0;
			int MIN=data[start];
			for(int i=start;i<end;i++)
			{
				if(data[i]<MIN)
				{
					MIN=data[i];
					X=i;
				}
			}
			
			return X;
		}
	
	
		//求数组中最大值所在位置
				public static int Find_max_location(int []data,int start,int end)
				{
					int X=0;
					int MAX=data[start];
					for(int i=start;i<end;i++)
					{
						if(data[i]>MAX)
						{
							MAX=data[i];
							X=i;
						}
					}
					
					return X;
				}
	
	
	
	
	
	
	
	
	
	
}
