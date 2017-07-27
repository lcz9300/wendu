package com.HWDTEMPT.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.HWDTEMPT.hwdtempt.MainActivity;

public class Analyse {
	private float[] ARtime, PRtime, EST, HRV, QRSwidth, Pheight;
	private int[] QQt, SSt, Q1point, S2point, Tpoint, Ppoint, RRtime;
	private int totalECG,sfreq ;
	public int   ecgtemp = 0;
	public double th1 = 0;
	private double th2 = 0;
	public String ecgg = "";
	public int [] eCGsingnal,ECGpoint;
	float [] d,e;
	List<Integer> Rpoint = new ArrayList<Integer>();
	public Analyse(int sfreq)
	{
		this.sfreq=sfreq;
		
		Rpoint = new ArrayList<Integer>();
	}

	// 心跳算法
	public int rateanalyse(List<Integer> eCGData_rate) {
		// TODO Auto-generated method stub
		int total = eCGData_rate.size();
		double th11 = 0;
		Integer[] ECGsing = new Integer[total];
		for (int i = 0; i < total; i++) {
			ECGsing[i] = eCGData_rate.get(i);
		}
		// ECGsing=eCGData_rate.toArray(ECGsing);
		int ECGrate = 0;
		int length = total / sfreq - 1;
		Log.e("analyse", String.valueOf(length));// 打印读取的数据
		if (length > 8) {
			Integer a[] = new Integer[sfreq];
			Integer b[] = new Integer[5];
			for (int i = 0; i < 5; i++) {
				System.arraycopy(ECGsing, (i + 3) * sfreq, a, 0, sfreq);
				Arrays.sort(a);
				b[i] = a[sfreq - 1];
			}
			for (int i = 0; i < 5; i++) {
				th11 = th11 + b[i];
			}
			th11 = 0.7 * th11 / 5;

			List<Integer> Rpoint = new ArrayList<Integer>();
			int j2 = 0;
			int i = 200;
			int k = 3;
			while (i < total - 2 && th11 != 0) {
				if (ECGsing[i] >= th11 && ECGsing[i] >= ECGsing[i + 1] && ECGsing[i] >= ECGsing[i - 1]) {
					Rpoint.add(i);
					j2 = Rpoint.size() - 1;
					// 不应期检查
					if (j2 >= 1 && Rpoint.get(j2) - Rpoint.get(j2 - 1) < 0.4 * sfreq) {
						if (ECGsing[Rpoint.get(j2)] < ECGsing[Rpoint.get(j2 - 1)]) {
							Rpoint.remove(j2 - 1);
						} else {
							Rpoint.remove(j2);
						}
					}
				}

				// 更新阈值
				while (k < j2 && (i - Rpoint.get(Rpoint.size() - 1)) > 0.3 * sfreq) {
					th11 = 0.9 * th11 + 0.07 * ECGsing[Rpoint.get(Rpoint.size() - 1)];
					k = k + 1;
				}
				i = i + 1;
			}

			if (Rpoint.size() > 1) {
				for (int j = 1; j < Rpoint.size(); j++) {
					ECGrate = ECGrate + (Rpoint.get(j) - Rpoint.get(j - 1));
				}
				ECGrate = ECGrate / (Rpoint.size() - 1);
				ECGrate = (60 * sfreq) / ECGrate;
				if (ecgtemp != 0) {
					ECGrate = (ECGrate + ecgtemp) / 2;
				}
				Log.w("analyseresult", ECGrate + "");
			} else {
				if (ecgtemp != 0) {
					ECGrate = ecgtemp;
				}
			}
			total = 0;
			ecgtemp = ECGrate;
		}
		return ECGrate;
	}
	
	
	
	
	
	
	
	

	/* 具体分析 */
/*
 * 参数求值
 * Ppoints 
 * Rpoints
 * Tpoints
 * ARtime 1.AR（8个RR）的平均时间，此处取前一个到前九个的平均，为了消除最近一个带来的误差
 * HRV 2.RR间期差别
 * QRSwidth  3.QRS宽度
 * EST   4.ST段抬高还是降低
 * PRtime   5.PR间期,P波宽度，P波高度
 * Pwidth
 * Pheight
 * aaaa（Rpoints）
 */
	public void analyse1(int[] eCGsingnal) {
		
		this.eCGsingnal=eCGsingnal;
		totalECG=eCGsingnal.length;
		d = new float[totalECG];
		e = new float[totalECG];

		// 对信号求一阶差分，二阶差分，并对二阶差分进行平滑
		for (int i = 0; i < totalECG - 1; i++)// 求一阶差分，一阶差分为d,d的大小为总数total-1
		{
			d[i] = (eCGsingnal[i + 1] - eCGsingnal[i]);
		}

		for (int i = 0; i < d.length - 1; i++) // 求二阶差分,二阶差分为e
		{
			e[i] = d[i + 1] - d[i];
		}
		//e = Arraylistdata.eightpin(e);// 对数据进行八点平滑滤波
		
	}

	
	public int analyse2()
	{
		
		 int RRrate=0;
		/* 求阈值th1，th2 */

		float a[] = new float[sfreq];
		float b[] = new float[5];
		for (int i = 0; i < 5; i++) {
			System.arraycopy(e, i * sfreq + 200, a, 0, sfreq);
			Arrays.sort(a);
			b[i] = a[0];
		}
		th1 = 0.6 * ((b[0] + b[1] + b[2] + b[3] + b[4]) / 5);

		int c[] = new int[sfreq];
		int f[] = new int[5];
		for (int i = 0; i < 5; i++) {
			System.arraycopy(eCGsingnal, i * sfreq + 200, c, 0, sfreq);
			Arrays.sort(c);
			f[i] = c[sfreq - 1];
		}
		th2 = 0.8 * ((f[0] + f[1] + f[2] + f[3] + f[4]) / 5);

		/* 求R点，并且进行不应期检查 */
		
		int j2 = 0;
		int w= 2;
		int kk=4;
		while (w < totalECG) {
			if (Math.abs(e[w]) > Math.abs(th1) && Math.abs(e[w]) >= Math.abs(e[w + 1]) && Math.abs(e[w]) >= Math.abs(e[w- 1])) 
			{
				if (Math.abs(eCGsingnal[w]) > 0.5 * th2) {
					Rpoint.add(w);
					j2 = Rpoint.size() - 1;
					// 不应期检查
					if (j2 > 0 && Rpoint.get(j2) - Rpoint.get(j2 - 1) < 0.2 * sfreq) 
					{
						if ((Math.abs(e[Rpoint.get(j2)]) < Math.abs(e[Rpoint.get(j2 - 1)]))&&(Math.abs(eCGsingnal[Rpoint.get(j2)]) > Math.abs(eCGsingnal[Rpoint.get(j2 - 1)]))) {
							Rpoint.remove(j2 - 1);
							j2 = Rpoint.size() - 1;
						} else if(Math.abs(eCGsingnal[Rpoint.get(j2)]) > Math.abs(eCGsingnal[Rpoint.get(j2 - 1)]))
						{
							Rpoint.remove(j2-1);
							j2 = Rpoint.size() - 1;
						}else
						{
							Rpoint.remove(j2);
							j2 = Rpoint.size() - 1;
						}
					}

					// 漏检检查
					if (j2 >2 && (Rpoint.get(j2) - Rpoint.get(j2 - 1) > 1.5 * (Rpoint.get(j2 - 1) - Rpoint.get(0)) / (j2 - 1))) {

						List<Integer> maxRR = new ArrayList<Integer>();
						for (int n = (int) (0.2 * sfreq); n < 1.5 * ((Rpoint.get(j2 - 1) - Rpoint.get(0)) / (j2 - 1) - 0.2 * sfreq); n++) 
						{
							if (Math.abs(eCGsingnal[Rpoint.get(j2 - 1) + n]) > 0.3 * th2 && Math.abs(e[Rpoint.get(j2 - 1) + n]) > 0.5 * Math.abs(th1)
									&& Math.abs(e[Rpoint.get(j2 - 1) + n]) >= Math.abs(e[Rpoint.get(j2 - 1) + n + 1])
									&& Math.abs(e[Rpoint.get(j2 - 1) + n]) >= Math.abs(e[Rpoint.get(j2 - 1) + n - 1])) {
								maxRR.add(Rpoint.get(j2 - 1) + n);
							}
						}
						
						if(maxRR.size()==0)
						{
							for (int n = (int) (0.2 * sfreq); n <(Rpoint.get(j2) - Rpoint.get(j2-1) - 0.2 * sfreq); n++) 
							{
								if (Math.abs(eCGsingnal[Rpoint.get(j2 - 1) + n]) > 0.3 * th2 && Math.abs(e[Rpoint.get(j2 - 1) + n]) > 0.3 * Math.abs(th1)
										&& Math.abs(e[Rpoint.get(j2 - 1) + n]) >= Math.abs(e[Rpoint.get(j2 - 1) + n + 1])
										&& Math.abs(e[Rpoint.get(j2 - 1) + n]) >= Math.abs(e[Rpoint.get(j2 - 1) + n - 1])) 
								{
									maxRR.add(Rpoint.get(j2 - 1) + n);
								}
							}	
						}
						if (maxRR.size() != 0) 
						{
							int maxR = 0;
							maxR = maxRR.get(0);
							for (int j = 1; j < maxRR.size(); j++) 
							{
								if (Math.abs(e[maxRR.get(j)]) > Math.abs(e[maxR])) 
								{
									maxR = maxRR.get(j);
								}

							}
							Rpoint.set(j2, maxR);
							w= (int) (maxR + 0.2 * sfreq);
						}
					}
				}
			}
			if(kk<j2-1 && (w-Rpoint.get(j2-1))>(0.2*sfreq))
			{
				th1=0.85*th1+0.09*(-Math.abs(e[Rpoint.get(j2-1)]));
				kk++;
			}
			
			w = w + 1;
		}

		/* 调整R波位置 */
		int maxRR = 0;
		for (int RR = 1; RR < Rpoint.size() - 1; RR++) {
			maxRR = Rpoint.get(RR);
			if (eCGsingnal[Rpoint.get(RR)] > 0) {
				for (int j = -5; j < 5; j++) {
					if (eCGsingnal[Rpoint.get(RR) + j] > eCGsingnal[maxRR]) {
						maxRR = Rpoint.get(RR) + j;
					}
				}
			} else {
				for (int j = -5; j < 5; j++) {
					if (eCGsingnal[Rpoint.get(RR) + j] < eCGsingnal[maxRR]) {
						maxRR = Rpoint.get(RR) + j;
					}
				}
			}
			Rpoint.set(RR, maxRR);
		}
		if(Rpoint.size()>1)
		{
		RRrate=Rpoint.get(Rpoint.size()-1)-Rpoint.get(0);
		RRrate = RRrate / (Rpoint.size() - 1);
		RRrate = (60 * sfreq) / RRrate;
		}
	
		/* 求RR间期和AR */
		 RRtime = new int[Rpoint.size()];
		 ARtime = new float[Rpoint.size()];
	
		for (int j = 1; j < Rpoint.size(); j++) 
		{
			RRtime[j] = ((Rpoint.get(j) - Rpoint.get(j-1)));
		}
		for (int j = 2; j < Rpoint.size(); j++) 
		{
			ARtime[j] = (Rpoint.get(j - 1) - Rpoint.get(0))/(float)(j-1);
		}

		/* 求Q点和S点 */
		QQt = new int[Rpoint.size()];
		SSt = new int[Rpoint.size()];
		
		if (Rpoint.size() > 2) {
			
			for (int j = 1; j < Rpoint.size() - 1; j++) {
				QQt[j] = Rpoint.get(j);
				if (eCGsingnal[Rpoint.get(j)] > 0) {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j) - Rpoint.get(j - 1)); jj++) {
						if (d[Rpoint.get(j) - jj] * sfreq < 1.2 && eCGsingnal[Rpoint.get(j) - jj] < 0.2 * eCGsingnal[Rpoint.get(j)]) {
							QQt[j] = Rpoint.get(j) - jj+1;
							break;
						}
					}
				} else {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j) - Rpoint.get(j - 1)); jj++) {
						if (d[Rpoint.get(j) - jj] * sfreq > -1.2 && Math.abs(eCGsingnal[Rpoint.get(j) - jj]) < 0.2 * Math.abs(eCGsingnal[Rpoint.get(j)])) {
							QQt[j] = Rpoint.get(j) - jj+1;
							break;
						}
					}
				}
				if (QQt[j] == Rpoint.get(j)) {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j) - Rpoint.get(j - 1)); jj++) {
						if (eCGsingnal[Rpoint.get(j) - jj] * eCGsingnal[Rpoint.get(j)] < 0) {
							QQt[j] = Rpoint.get(j) - jj+1;
							break;
						}
					}
				}
			}

			for (int j = 1; j < Rpoint.size() - 1; j++) {
				SSt[j] = Rpoint.get(j);
				if (eCGsingnal[Rpoint.get(j)] > 0) {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j+1) - Rpoint.get(j)); jj++) {
						if (d[Rpoint.get(j) + jj] * sfreq > -1.2 && eCGsingnal[Rpoint.get(j) + jj] < 0.2 * eCGsingnal[Rpoint.get(j)]) {
							SSt[j] = Rpoint.get(j) + jj-1;
							break;
						}
					}
				} else {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j+1) - Rpoint.get(j)); jj++) {
						if (d[Rpoint.get(j) + jj] * sfreq < 1.2 && Math.abs(eCGsingnal[Rpoint.get(j) + jj]) < 0.2 * Math.abs(eCGsingnal[Rpoint.get(j)])) {
							SSt[j] = Rpoint.get(j) + jj-1;
							break;
						}
					}
				}
				if (SSt[j] == Rpoint.get(j)) {
					for (int jj = 4; jj < 0.4 * (Rpoint.get(j+1) - Rpoint.get(j)); jj++) {
						if (eCGsingnal[Rpoint.get(j) + jj] * eCGsingnal[Rpoint.get(j)] <= 0) {
							SSt[j] = Rpoint.get(j) + jj-1;
							break;
						}
					}
				}
			}
		}

		/* 求QS波的起点和终点 */
		Q1point = new int[Rpoint.size()];
		S2point = new int[Rpoint.size()];
		
		
		for (int j = 1; j < Rpoint.size() - 1; j++) {
			Q1point[j] = QQt[j];
			if (eCGsingnal[QQt[j]] <= 0)
			{
				for (int jj = 2; jj < 0.3 * (Rpoint.get(j) - Rpoint.get(j - 1)) - (Rpoint.get(j) - QQt[j]); jj++) 
				{
					if (eCGsingnal[QQt[j] - jj] >= 0) {
						Q1point[j] = QQt[j] - jj;
						break;
					}
					if (d[QQt[j] - jj] * sfreq > -1.5) {
						Q1point[j] = QQt[j] - jj;
						break;
					}
				}
			} else 
			{
				for (int jj = 2; jj < 0.3 * ((Rpoint.get(j) - Rpoint.get(j - 1)) - (Rpoint.get(j) - QQt[j])); jj++) {
					if (eCGsingnal[QQt[j] - jj] <= 0) 
					{
						Q1point[j] = QQt[j] - jj;
						break;
					}
					if (d[QQt[j] - jj] * sfreq < 1.5) 
					{
						Q1point[j] = QQt[j] - jj;
						break;
					}
				}
			}
			if (Math.abs(eCGsingnal[Q1point[j] + 2] - eCGsingnal[Q1point[j] + 1]) < 10
					&& Math.abs(eCGsingnal[Q1point[j] + 1] - eCGsingnal[Q1point[j]]) < 10) 
			{
				Q1point[j] = Q1point[j] + 2;
			}
		}

		for (int j = 1; j < Rpoint.size() - 1; j++) {
			S2point[j] = SSt[j];
			if (eCGsingnal[SSt[j]] <= 0) {
				for (int jj = 2; jj < 0.3 * ((Rpoint.get(j) - Rpoint.get(j - 1)) - (SSt[j] - Rpoint.get(j))); jj++) {
					if (eCGsingnal[SSt[j] + jj] >= 0) {
						S2point[j] = SSt[j] + jj;
						break;
					}
					if (d[SSt[j] + jj] * sfreq < 1.5) {
						S2point[j] = SSt[j] + jj;
						break;
					}
				}
			} else {
				for (int jj = 2; jj < 0.3 * (Rpoint.get(j) - Rpoint.get(j - 1)) - (SSt[j] - Rpoint.get(j)); jj++) {
					if (eCGsingnal[SSt[j] + jj] <= 0) {
						S2point[j] = SSt[j] + jj;
						break;
					}
					if (d[SSt[j] + jj] * sfreq > -1.5) {
						S2point[j] = SSt[j] + jj;
						break;
					}
				}
			}
			if (Math.abs(eCGsingnal[S2point[j] - 2] - eCGsingnal[S2point[j] - 1]) <10
					&& Math.abs(eCGsingnal[S2point[j] - 1] - eCGsingnal[S2point[j]]) <10) {
				S2point[j] = S2point[j] - 2;
			}
		}

		/* 求P点位置和T波位置 */
		Tpoint = new int[Rpoint.size()];
		Ppoint = new int[Rpoint.size()];
		for (int j = 1; j < Rpoint.size() - 1; j++) 
		{
			Tpoint[j] = S2point[j];
			int TT1point =S2point[j]; 
			int jj=0;
			while(eCGsingnal[S2point[j]+jj]<=0 && S2point[j]+jj<totalECG)
			{
				jj=jj+1;
			}
			int TT2point=S2point[j]+jj;
			
			
			for (int k = 4; k < 0.7*(Rpoint.get(j + 1)-Rpoint.get(j))-(S2point[j]-Rpoint.get(j)); k++) 
			{
				if (eCGsingnal[S2point[j] + k]<0) 
				{
					if(eCGsingnal[S2point[j] + k]<eCGsingnal[TT1point])
					{
						TT1point=S2point[j] + k;
					}
				}
				else
				{
					if(eCGsingnal[S2point[j] + k]>eCGsingnal[TT2point])
					{
						TT2point=S2point[j] + k;
					}	
				}
			 }
			if((eCGsingnal[S2point[j]]-eCGsingnal[TT1point]) <eCGsingnal[TT2point])
			{
				Tpoint[j]=TT2point;
			}else
			{
				Tpoint[j]=TT1point;
			}
		}

		// 找P波
		for (int j = 1; j < Rpoint.size()-1; j++) {
			Ppoint[j]=Q1point[j];
			int PP1point=Q1point[j];
			int jj=1;
			while(Q1point[j]-jj>0 && eCGsingnal[Q1point[j]-jj]<0)
			{
				jj++;
			}
			int PP2point = Q1point[j] - jj;
			
			for (int k = 4; k < 0.7*(Rpoint.get(j)-Rpoint.get(j-1))-(Rpoint.get(j)-Q1point[j]); k++) 
			{
				if (eCGsingnal[Q1point[j] - k]<0) 
				{
					if(eCGsingnal[Q1point[j]-k]<eCGsingnal[PP1point])
					{
						PP1point=Q1point[j]-k;
					}
				}
				else
				{
					if(eCGsingnal[Q1point[j]-k]>eCGsingnal[PP2point])
					{
						PP2point=Q1point[j]-k;
					}	
				}
			 }
			if((eCGsingnal[Q1point[j]]-eCGsingnal[PP1point]) <eCGsingnal[PP2point])
			{
				Ppoint[j]=PP2point;
			}else
			{
				Ppoint[j]=PP1point;
			}

		}

		/* * 参数求值 */

		HRV = new float[Rpoint.size()];
		QRSwidth = new float[Rpoint.size()];
		EST = new float[Rpoint.size()];
		PRtime = new float[Rpoint.size()];
		Pheight = new float[Rpoint.size()];
		
		for (int j = 2; j < Rpoint.size() - 1; j++) 
		{
			HRV[j] = (float) Math.abs(RRtime[j] - RRtime[j-1]);
		}

		for (int j = 1; j < Rpoint.size() - 1; j++) 
		{
			QRSwidth[j] =S2point[j] - Q1point[j];
		}

		for (int j = 1; j < Rpoint.size() - 1; j++) 
		{
			if (Tpoint[j] == Rpoint.get(j)) {
				EST[j] = (float) 0;
			} else {
				EST[j] = eCGsingnal[Tpoint[j]] - eCGsingnal[S2point[j]];
			}

		}

		for (int j = 1; j < Rpoint.size() - 1; j++) 
		{
			if (Ppoint[j] == Q1point[j]) 
			{
				PRtime[j] = (float) 0;
				Pheight[j] = (float) 0;
			} else {
				PRtime[j] =Rpoint.get(j)-Ppoint[j];
				Pheight[j] = (eCGsingnal[Ppoint[j]]);
			}

		}
		ECGpoint=new int[(Rpoint.size()-2)*5];
		for(int i=0;i<Rpoint.size()-2;i++)
		{
			ECGpoint[i*5]=Ppoint[i+1];
			ECGpoint[i*5+1]=QQt[i+1];
			ECGpoint[i*5+2]=Rpoint.get(i+1);
			ECGpoint[i*5+3]=SSt[i+1];
			ECGpoint[i*5+4]=Tpoint[i+1];
		}
		
		return RRrate;
	}

	public String analyse3()
	{
		// 心跳检验
				int []result = new int[Rpoint.size()];

				// 窦性心律
				for (int j = 2; j < Rpoint.size()-3; j++) 
				{
					
					if (eCGsingnal[Ppoint[j]] > eCGsingnal[Q1point[j]] && Ppoint[j] != Q1point[j]) {
						if (QRSwidth[j] < 0.12*sfreq) {
							if (RRtime[j-1] > 1.5*sfreq && RRtime[j] > 1.5*sfreq && RRtime[j + 1] > 1.5*sfreq) {
								if (RRtime[j] > 1.9 * ARtime[j] && RRtime[j] > RRtime[j + 1] && RRtime[j] > RRtime[j - 1] &&RRtime[j - 1]>1.5*sfreq)
								{
									result[j] = result[j] + 10000;// 窦性停博
								} else {
									result[j] = result[j] + 1000;// 窦性心动过缓
								}
							} else {
								if (RRtime[j - 1] < 0.5*sfreq && RRtime[j] < 0.5*sfreq && RRtime[j + 1] < 0.5*sfreq) {
									result[j] = result[j] + 100;// 窦性心动过速
								} else {
									if (j >= 2 && HRV[j - 2] > 0.18*sfreq && HRV[j - 1] > 0.18*sfreq && HRV[j] > 0.18*sfreq && HRV[j + 1] > 0.18*sfreq && HRV[j + 2] > 0.18*sfreq) {
										result[j] = result[j] + 100000;// 窦性心律不齐
									}
								}
							}
						}
					}
				  }
				

				
				
				
				
				// 早搏
				for (int j = 3; j < Rpoint.size() - 4; j++) 
				{
					if ((RRtime[j] < ARtime[j] || eCGsingnal[Rpoint.get(j)] < 0 || eCGsingnal[Rpoint.get(j)] <= eCGsingnal[Tpoint[j]]) && QRSwidth[j] > 0.1*sfreq) 
					{
						int check[] = new int[7];
						int sumc = 0;
						if (RRtime[j] < 0.85 * ARtime[j]) 
						{
							check[0] = 1;
						}
						if (QRSwidth[j] > 0.12*sfreq) 
						{
							check[1] = 1;
						}
						if (QRSwidth[j] > 0.18*sfreq) 
						{
							check[1] = 2;
						}
						if ( HRV[j] > 0.12*sfreq && HRV[j + 1] > 0.12*sfreq) 
						{
							check[2] = 1;
						}
						
						if (Pheight[j] <= 0.05) {
							check[3] = 1;
						}
						if (eCGsingnal[Rpoint.get(j)] * EST[j] < 0) {
							check[4] = 1;
						}
						if (eCGsingnal[Tpoint[j]] < -0.5 * eCGsingnal[Rpoint.get(j)]) {
							check[4] = 2;
						}
						if (eCGsingnal[Rpoint.get(j)] < 0) {
							check[5] = 3;
						}
						if (Math.abs(eCGsingnal[Rpoint.get(j)]) > 0.4 * (Math.abs(eCGsingnal[Rpoint.get(j - 1)]) + Math.abs(eCGsingnal[Rpoint.get(j - 2)]) + Math
								.abs(eCGsingnal[Rpoint.get(j - 3)])) || (eCGsingnal[Rpoint.get(j)] <= eCGsingnal[Tpoint[j]])) {
							check[6] = 1;
						}

						for (int n = 0; n < check.length; n++) 
						{
							sumc = sumc + check[n];
						}
						
						switch (sumc) {
						case 4:
							result[j] = result[j] + 500;
							break;
						case 5:
							result[j] = result[j] + 50;
							break;
						case 6:
							result[j] = result[j] + 5;
							break;
						case 7:
							result[j] = result[j] + 5;
							break;
						case 8:
							result[j] = result[j] + 5;
							break;
						case 9:
							result[j] = result[j] + 5;
							break;
						case 10:
							result[j] = result[j] + 5;
							break;
						}
					}

					// 房性早搏
					if (RRtime[j] < ARtime[j] && QRSwidth[j] < 0.14*sfreq && PRtime[j]>0.12*sfreq) {
						int checkc[] = new int[5];
						int sumcn = 0;
						if (RRtime[j] < 0.9 * ARtime[j]) 
						{
							checkc[0] = 2;
						}
						
						if (RRtime[j] < 0.85 * ARtime[j]) 
						{
							checkc[0] = 2;
						}
						if (Math.abs(Ppoint[j] - Tpoint[j - 1]) < 5) 
						{
							checkc[1] = 1;
						}
						if ( HRV[j] > 0.12*sfreq && HRV[j + 1] > 0.12*sfreq ) 
						{
							checkc[2] = 2;
						}
						if (RRtime[j] + RRtime[j + 1] >= 2 * ARtime[j]) 
						{
							checkc[3] = 1;
						}
						
						if (PRtime[j] > 0.13*sfreq) 
						{
							checkc[4] = 1;
						}

						for (int n = 0; n < checkc.length; n++) 
						{
							sumcn = sumcn + checkc[n];
						}
						
						switch (sumcn) {
						case 4:
							result[j] = result[j] + 80;
							break;
						case 5:
							result[j] = result[j] + 8;
							break;
						case 6:
							result[j] = result[j] + 8;
							break;
						case 7:
							result[j] = result[j] + 8;
							break;

						}
					}

					// 交界性早搏
					if (QRSwidth[j] < 0.12*sfreq && (eCGsingnal[Ppoint[j]] - eCGsingnal[Q1point[j]]) < 0 && PRtime[j] < 0.12*sfreq && RRtime[j] < 0.85 * ARtime[j]) {
						result[j] = result[j] + 7; // 交界性期早搏
					}
				}
				
				String resultstring="";
				
//				for(int j=0;j<result.length;j++)
//				{
//					resultstring+=","+result[j];
//				}
				for(int j=0;j<result.length;j++)
				{
			     switch (result[j]%10)
			     {
			     case 0:
			    	 if(resultstring=="") {
			    		 resultstring+="正常";
			    	 }else{
			    		 resultstring+=",正常";
			    	 }
			    	 break;		   
			     case 5:
			    	 if(resultstring=="") {
			    		 resultstring+="室性早搏";
			    	 }else{
			    		 resultstring+=",室性早搏";
			    	 }
			    	 break;
			     case 7:
			    	 if(resultstring=="") {
			    		 resultstring+="交界性期早搏";
			    	 }else{
			    		 resultstring+=",交界性期早搏";
			    	 }
			    	 break;
			     case 8:
			    	 if(resultstring=="") {
			    		 resultstring+="房性早搏";
			    	 }else{
			    		 resultstring+=",房性早搏";
			    	 }
			    	 break;
		    	 
			     }
			     
			     
			     switch (result[j]/10)
			     {
			     case 5:
			    	 if(resultstring=="") {
			    		 resultstring+="室性早搏";
			    	 }else{
			    		 resultstring+=",室性早搏";
			    	 }
			    	 break;
			     case 8:
			    	 if(resultstring=="") {
			    		 resultstring+="房性早搏";
			    	 }else{
			    		 resultstring+=",房性早搏";
			    	 }
			    	 break; 
			     
			     }
			     
			     switch (result[j]/100)
			     {
			     
			     case 1:
			    	 if(resultstring=="") {
			    		 resultstring+="窦性心动过速";
			    	 }else{
			    		 resultstring+=",窦性心动过速";
			    	 }
			    	 break;
			    	 
			     case 5:
			    	 if(resultstring=="") {
			    		 resultstring+="室性早搏";
			    	 }else{
			    		 resultstring+=",室性早搏";
			    	 }
			    	 break;
			     case 10:
			    	 if(resultstring=="") {
			    		 resultstring+="窦性心动过缓";
			    	 }else{
			    		 resultstring+=",窦性心动过缓";
			    	 }
			    	 break;
			    	 
			     case 100:
			    	 if(resultstring=="") {
			    		 resultstring+="窦性停博";
			    	 }else{
			    		 resultstring+=",窦性停博";
			    	 }
			    	 break;
			    	 
			     case 1000:
			    	 if(resultstring=="") {
			    		 resultstring+="窦性心律不齐";
			    	 }else{
			    		 resultstring+=",窦性心律不齐";
			    	 }
			    	 break;
			     
			     }
				}
				if(resultstring=="")
				{
					resultstring="正常";
				}
			    


				return resultstring;
	}
	public static void putsimple(String s, String name) {
		try {
			File destDir = new File(Environment.getExternalStorageDirectory() + "/" + "ECG" + "/" + "ECGsimple");
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			FileOutputStream outStream = new FileOutputStream(destDir + "/" + name + ".txt", true);
			OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
			writer.write(s);
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
			Log.e("m", "file write finish");
		} catch (Exception e) {
			Log.e("m", "file write error");
		}
	}

	public static void put(String s, String name) {
		try {
			File destDir = new File(Environment.getExternalStorageDirectory() + "//" + "BMDBT"+"//"+MainActivity.NAME);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			FileOutputStream outStream = new FileOutputStream(destDir + "/" + name + ".txt", true);
			OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
			writer.write(s);
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
			Log.e("m", "file write finish");
		} catch (Exception e) {
			Log.e("m", "file write error");
		}
	}

    public static void putgn(String s, String name) {
        try {
            File destDir = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            FileOutputStream outStream = new FileOutputStream(destDir + "/" + name + ".txt", false);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
            writer.write(s);
            writer.flush();
            writer.close();// 记得关闭
            outStream.close();
            Log.e("m", "file write finish");
        } catch (Exception e) {
            Log.e("m", "file write error");
        }
    }
    public static void putgnhis(String s, String name) {
        try {
            File destDir = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            FileOutputStream outStream = new FileOutputStream(destDir + "/" + name + ".txt", false);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
            writer.write(s);
            writer.flush();
            writer.close();// 记得关闭
            outStream.close();
            Log.e("m", "file write finish");
        } catch (Exception e) {
            Log.e("m", "file write error");
        }
    }
}
