package com.HWDTEMPT.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaveLet {
	/**
	 * ����Ϊ�ο�matlab��С���任��д��java���򣬹�����ʵ����ɢ���ݵ�С���任�������ֽ�wavedec���ع�appcoef����Ϊϵ������������db6С��ϵ��������Ŀǰֻ������db6С�����б任
	 * ��Ҫ��ʱ�����������С���ı任ϵ����
	 * ʹ��ʱ������֤��Ҫ����С���任������Ϊ2�Ķ�η�������2��4�η��������������δ֪����
	 * @author mick
	 */
	
	
	//db6С���任�ֽ������ع�����
	private double[] Lo_D_dec = { -0.00107730108499558, 0.00477725751101065,
			0.000553842200993802, -0.0315820393180312, 0.0275228655300163,
			0.0975016055870794, -0.129766867567096, -0.226264693965169,
			0.315250351709243, 0.751133908021578, 0.494623890398385,
			0.111540743350080 };
	private double[] Lo_R_coef = { 0.111540743350080, 0.494623890398385,
			0.751133908021578, 0.315250351709243, -0.226264693965169,
			-0.129766867567096, 0.0975016055870794, 0.0275228655300163,
			-0.0315820393180312, 0.000553842200993802, 0.00477725751101065,
			-0.00107730108499558 };

	private double[] Hi_D_dec = { -0.111540743350080, 0.494623890398385,
			-0.751133908021578, 0.315250351709243, 0.226264693965169,
			-0.129766867567096, -0.0975016055870794, 0.0275228655300163,
			0.0315820393180312, 0.000553842200993802, -0.00477725751101065,
			-0.00107730108499558 };

	private double[] Hi_R_coef = { -0.00107730108499558, -0.00477725751101065,
			0.000553842200993802, 0.0315820393180312, 0.0275228655300163,
			-0.0975016055870794, -0.129766867567096, 0.226264693965169,
			0.315250351709243, -0.751133908021578, 0.494623890398385,
			-0.111540743350080 };
	
/**
 * db6С���ֽ����
 * @param data ��Ҫ�ֽ����ɢ���ݣ�������֤Ϊ2�Ķ�η�������ǰ����ݶ�����1000�����ϣ���Ϊ�˴�ʹ�õ���int���ͣ�������С����
 * @param scale Ҫ���зֽ�ĳ߶�
 * @param wavelet ��Ҫʹ�õ�С�����˴�ֻ��ѡ��db6С������ʵ�˲����ں�����û��ʹ��
 * @return  ����һ��map�а����������飬һ���Ƿֽ�֮���ϵ�����飬һ����ϵ����������
 */
	public Map<String, Object> Wavedec(int[] data, int scale, String wavelet) {
		List<Integer> Cpoint = new ArrayList<Integer>();//���淵�ص�ϵ������
		Map<String, Object> map = new HashMap<String, Object>();//�ֽ⺯�����ص�map
		
		int []Data=data;
		int s = Data.length;
		 int []L = new int[scale + 2];//���صĳ߶�ϵ��
		L[scale + 1] = s;
		for (int i = 0; i <scale; i++) {
			Map<String, Object> DwtMap = Dwt(Data, Lo_D_dec, Hi_D_dec);//DWT����������������
			Data=(int[]) DwtMap.get("a");
	    	int []D=(int[]) DwtMap.get("D");
			for (int j = D.length - 1; j >= 0; j--) {
				Cpoint.add(0, D[j]);
			}
			L[scale - i] = D.length;
		}

		for (int j = Data.length - 1; j >= 0; j--) {
			Cpoint.add(0, Data[j]);
		}
		L[0] = Data.length;
		
		//����Ҫ���ص�����װ��map��
		map.put("list1",Cpoint);
		map.put("list2",L);
		
		return map;
	}

	
	/**
	 * �ع��������������Ѿ��ֽ����ϵ����ϵ�����������ع�������
	 * @param data ��Ҫ�����ع������ݣ�����ǰ����ݶ�����1000�����ϣ���Ϊ�˴�ʹ�õ���int���ͣ�������С����
	 * @param L  ��Ҫ�����ع���ϵ����������
	 * @return  �����ع�֮�������
	 */
	public int[] appcoef(List<Integer> data,int []L) {
	
		int rmax = L.length;
		int nmax = rmax - 2;
		int[] a = new int [L[0]];
		int []Data=new int [data.size()];
		
		for(int i=0;i<data.size();i++)
		{
			Data[i]=data.get(i);
		}
		
		for(int i=0;i<L[0];i++)
		{
			a[i]=data.get(i);
		}
		
		int imax = rmax+1;
		
		for(int p = nmax;p>0;p--)
		{
		    int []d = detcoef(Data,L,p); 
		    a=idwt_upsconv1(a,Lo_R_coef,L[imax-p-1]);
		    int a2[]=idwt_upsconv1(d,Hi_R_coef,L[imax-p-1]);
		   
		    for(int i=0;i<a.length &&i<a2.length;i++)
		    {
		    	a[i]=a[i]+a2[i];
		    }
		   
		}
		return a;
	}
		

	

	private int[] idwt_upsconv1(int[] x, double[] f, int s) {
		// TODO Auto-generated method stub
		int []y = x;
		int []dyadup=new int[y.length*2-1];
		for(int i=0;i<dyadup.length-1;i++)
		{
			if(i%2==0)
			{
			dyadup[i]=y[i/2];
			}else
			{
				dyadup[i]=0;
			}
		}
		dyadup[dyadup.length-1]=y[y.length-1];
		  y = Wconv2(dyadup,f,"full");
		    y = wkeep1(y,s);
	return y;
	
	}

	
	
	private int[] wkeep1(int[] y, int len) {
		// TODO Auto-generated method stub
	int [] tempy=y;
	
	int sx = y.length;
	 int d=0, first=0, last=0;;
	if((sx-len)%2==0)
	{
	 d = (sx-len)/2;
	 first=1+d;
	last = (sx-d);
	}else{
		d=(sx-len)/2;
		first=1+d;
		last=(sx-(d+1));
	}
	
	   int []TemPY=new int[last-first+1];
	  for(int i=first-1;i<last;i++)
	  {
		  TemPY[i-first+1]=tempy[i]; 
	  }
	return TemPY;
	
	}

	
	
	private int[] detcoef(int[] coefs, int[] longs, int levels) {
		// TODO Auto-generated method stub
	
		int[] firsttemp = new int[longs.length];
		for (int i = 0; i < longs.length; i++) 
		{
			firsttemp[i] = longs[i] + 1;
			if(i>0)
			{
				firsttemp[i]=firsttemp[i]+firsttemp[i-1]-1;
			}
		}
		int[] first = new int[firsttemp.length - 2];
		for (int i = 0; i<first.length; i++) {
			first[i] = firsttemp[first.length -1- i];
		}

		int[] longst = new int[longs.length -2];
		for (int i = 1; i <= longst.length; i++) {
			longst[i-1] = longs[longs.length-1 - i];
		}

		int[] last = new int[longst.length];
		for (int i = 0; i < longst.length; i++) {
			last[i] = first[i] + longst[i] - 1;
		}
		int tmp[] = new int[last[levels-1] - first[levels-1]+1];
		for (int i = first[levels-1]-1; i < last[levels-1]; i++) {
			tmp[i+1 - first[levels-1]] = coefs[i];
		}

		return tmp;

	}

	
	public Map<String, Object> Dwt(int[] x, double[] LO_D, double[] Hi_D) {
		Map<String, Object> map = new HashMap<String, Object>();
		int Lf = LO_D.length;
		int lenEXT = Lf - 1;
		int[] y = Wextend(x, lenEXT);
		int[] z = Wconvl(y, LO_D, "valid");
		int[] a = new int[z.length / 2];
		for (int i = 0; i < a.length; i++) {
			a[i] = z[2 * i + 1];
		}

		int[] z1 = Wconvl(y, Hi_D, "valid");

		int []D = new int[z1.length / 2];
		for (int i = 0; i < D.length; i++) {
			D[i] =z1[2 * i + 1];
		}
		
		map.put("a",a);
		map.put("D",D);
		return map;
	}

	private int[] Wconvl(int[] y, double[] hi_D2, String string) {
		// TODO Auto-generated method stub
		int[] tt = new int[y.length - hi_D2.length + 1];
		for (int i = 0; i < tt.length; i++) {
			double temp = 0;
			for (int m =0; m<hi_D2.length; m++)
			{
				temp += y[i+m] * hi_D2[hi_D2.length-m-1];	
			}
				
			tt[i] = (int) temp;

		}
		return tt;
	}
	
	private int[] Wconv2(int[] y, double[] hi_D2, String string) {
		// TODO Auto-generated method stub
		int[] tt= new int[y.length + hi_D2.length - 1];
		for (int i = 0; i < tt.length; i++) {
			
			double temp = 0;
			if(i<hi_D2.length)
			{
			for (int m =0; m<=i; m++)
			{
				temp += y[m] * hi_D2[i-m];	
			}	
			}
			if(i>=hi_D2.length &&y.length-i>0)
			{
				for (int m =i-hi_D2.length+1,j=1; m<=i; m++)
				{
					temp += y[m] * hi_D2[hi_D2.length-j];	
					j++;
				}
			}
			if(i>=y.length)
			{
				for (int m =i-hi_D2.length+1,j=1; m<y.length; m++)
				{
					temp += y[m] * hi_D2[hi_D2.length-j];
					j++;
				}
			}
				
			tt[i] = (int) temp;

		}
		return tt;
	}


	private int[] Wextend(int[] x2, int lenEXT) {
		// TODO Auto-generated method stub
		int[] y = new int[2 * lenEXT + x2.length];

		for (int i = 0; i < lenEXT; i++) {
			y[i] = x2[lenEXT-1 - i];
		}

		for (int i = lenEXT; i < lenEXT + x2.length; i++) {
			y[i] = x2[i - lenEXT];
		}

		for (int i = lenEXT + x2.length; i < 2 * lenEXT + x2.length; i++) {
			y[i] = x2[x2.length-1 - (i - lenEXT - x2.length)];
		}

		return y;
	}

}
