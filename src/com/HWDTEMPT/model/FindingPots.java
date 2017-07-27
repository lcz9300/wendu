package com.HWDTEMPT.model;

import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author mick
 *         此方法用来找心电信号的特征点，包括数据滤波处理，特征点提取两部分，其中包含去除野值点，小波变换，中值滤波，找R，P,T,Q,S,以及Q波起点和S波终点
 *         ，使用的时候可以采用其中的一部分后者多部分
 * 版本日期：2016.10.25；王浩任修改，心动过速问题解决；
 *         haorenwang@sjtu.edu.cn
 */
public class FindingPots {
    private int fs;// 采样频率

    int RRrate=0;
    private int[] RRfinally,QQt,SSt,Q1point,S2point,Tpoint,Ppoint;
    public FindingPots(int fs) {
    this.fs = fs;
//UserInfo user=new UserInfo();
//String age=user.getUserAge();
    }

    /**
     * 去掉野值点
     * 
     * @param eCGDataListA
     *            输入要去除野值的数组
     * @return datareturn 返回的去除野值点的数组
     */
    public int[] Remove_Outliers(int[] eCGDataListA) {
        // TODO Auto-generated method stub
        int[] datareturn = new int[eCGDataListA.length];
        System.arraycopy(eCGDataListA, 0, datareturn, 0, eCGDataListA.length);
        int max[] = new int[5];
        int min[] = new int[5];
        int[] data = new int[400];

        if (eCGDataListA.length > 2000) {
            for (int i = 0; i < 5; i++) {
                for (int k = 0; k < 400; k++) {
                    data[k] = eCGDataListA[k + i * 400];    
                }

                Arrays.sort(data);
                max[i] = data[399];
                min[i] = data[0];
            }
            // 此处为设定的去除野值阈值，此阈值可根据实际情况调整，但是不宜过高，因为会改变波形
            double th = 0.2 * ((max[0] + max[1] + max[2] + max[3] + max[4] - min[0] - min[1] - min[2] - min[3] - min[4]) / 5);

            int k = 1;
            for (int i = 4; i < eCGDataListA.length; i++) {
                while (Math.abs(eCGDataListA[i] - eCGDataListA[i - 1]) > th && i + k < eCGDataListA.length) {
                    datareturn[i] = (eCGDataListA[i - 1] + eCGDataListA[i + k]) / 2;
                    k = k + 1;
                }

            }
        }

        return datareturn;
    }

    /**
     * 中值滤波方法，输入数据为int数组，输出也为int数组，需要输入数据乘以1000倍
     * 
     * @param data
     *            输入的数组
     * @return datareturn 中值滤波之后的数组
     */
    public int[] Middle_Filter(int[] data) {

        int[] datareturn = new int[data.length];
        System.arraycopy(data, 0, datareturn, 0, data.length);
        int Length = 0;// 中值滤波的长度，取决于采样频率，一般认为是采样频率的80%，为奇数；
        if ((fs * 0.8) % 2 == 0) {
            Length = (int) (fs * 0.8);
        } else {
            Length = (int) (fs * 0.8 - 1);
        }
        for (int i = Length / 2; i < data.length - Length / 2; i++) {
            int[] M = new int[Length + 1];
            for (int j = i - Length / 2; j <= i + Length / 2; j++) {
                M[j - i + Length / 2] = data[j];
            }
            Arrays.sort(M);
            datareturn[i] = data[i] - M[Length / 2];
        }

        return datareturn;
    }

    /**
     * 寻找R点的方法，此方法为双正交小波变换方法，具体参考matlab程序
     * 
     * @param data
     *            输入的心电数据，最好是原始数据放大1000倍
     * @return RRfinally 输出为R点所在位置的数组
     */
    public int[] Find_Rpoints(int[] data) {
        
        List<Integer> Rpoints = new ArrayList<Integer>();// 用一个动态数据保存R点，最后再转换为固定数组，方便之后的操作
        int[] swa = new int[data.length];// 定义一个数组，存放双正交小波变换的中间值
        int[] swd = new int[data.length];// 定义一个数组，存放双正交变换后数据的值
        for (int i = 0; i < data.length - 3; i++) {
            swa[i + 3] = data[i + 3] / 4 + data[i + 3 - 1] * 3 / 4 + data[i + 3 - 2] * 3 / 4 + data[i + 3 - 3] / 4;
        }  

        for (int i = 0; i < data.length - 6; i++) {
            swd[i + 6] = -swa[i + 6 - 1] / 4 - swa[i + 6 - 2] * 3 / 4 + swa[i + 6 - 4] * 3 / 4 + swa[i + 6 - 6] / 4;

        }
        
        Map<String, Object> map = Arraylistdata.Ask_Jida_Jixiao(swd);// 求数组中极大极小所在位置，返回的数组中极大极小位置为1，其他为0

        int[] jida = (int[]) map.get("int[]1");// 极大值所在的数组
        int[] jixiao = (int[]) map.get("int[]2");// 极小值所在的数组

        
        int[]pddw=new int[jida.length];
        // 求极大值中在swd中也为正值的数组
        for (int i = 0; i < jida.length; i++) {
            if (swd[i] * jida[i] > 0) {
                pddw[i] = 1;
            } else {
                pddw[i] = 0;
            }
        }

        
        int[]nddw=new int[jixiao.length];
        // 求极小值中在swd中也为负值的点
        for (int i = 0; i < nddw.length; i++) {
            if (swd[i] * jixiao[i] < 0) {
                nddw[i] = 1;
            } else {
                nddw[i] = 0;
            }
        }

        int[] MJ2 = new int[data.length];// MJ2为包含极大极小值的swd数组，其他位置为0，这样做可以减少很多不必要的计算量

        MJ2[0] = swd[0];
        for (int i = 1; i < data.length-1; i++) {
            if (pddw[i] > 0 || nddw[i] > 0) {
                MJ2[i] = swd[i];
            } else {
                MJ2[i] = 0;
            }
        }
MJ2[data.length-1]=swd[data.length-1];


        // 此处开始寻找R点，posi为求阈值的数组
        int possi[] = new int[1000];
        System.arraycopy(MJ2, 0, possi, 0, possi.length);
        Arrays.sort(possi);
        int E1 = (int) (0.5 * possi[998]); // 正极大值阈值
        
int []nega=new int[MJ2.length];
for(int i=0;i<nega.length;i++)
{
    if(MJ2[i]<0)
    {
        nega[i]=MJ2[i];
    }else{
        nega[i]=0;
    }   
}


        int e1 = (int) (0.3 *Arraylistdata.Find_min(nega, 0, nega.length));// 负极大值阈值

        // 开始求R点
        int j2 = 0;

        for (int i = 5; i < data.length-10; i++) {
            if (MJ2[i] > E1) {
                
                Log.e("i", i+"");
                Rpoints.add(i);

                // 判断两者是否在一个心拍里

                if ((j2 >= 1) && ((Rpoints.get(j2) - Rpoints.get(j2 - 1)) < fs / 5))// 不应期为0.2倍的fs，当fs=400的时候，不应期为80
                {
                    if (MJ2[Rpoints.get(j2)] > MJ2[Rpoints.get(j2 - 1)])// 取最大的R点
                    {
                        Rpoints.remove(j2 - 1);
                    } else {
                        Rpoints.remove(j2);
                    }
                    j2 = j2 - 1;
                }

                // 多检检查,当检测到的点与前一个点距离小于平均值的一半，认为多检，进行排查
                if ((j2 > 2) && ((Rpoints.get(j2) - Rpoints.get(j2 - 1))) < 0.4 * ((Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))) {
                    if (MJ2[Rpoints.get(j2)] > MJ2[Rpoints.get(j2 - 1)])// %取最大的R点
                    {
                        Rpoints.remove(j2 - 1);
                    } else {
                        Rpoints.remove(j2);
                    }
                    j2 = j2 - 1;
                }

                // 多检检查,当检测到的点与前一个点的幅值比大于2的时候，认为多检，进行排查
                if (j2 > 1 && Rpoints.get(j2 - 1) - 0.15 * fs > 0) {
                      Log.e("j2", j2+"");
                    if ((MJ2[Rpoints.get(j2)] >= 2 * MJ2[Rpoints.get(j2 - 1)]) || (MJ2[Rpoints.get(j2 - 1)] >= 2 * MJ2[Rpoints.get(j2)])) {
                        if ((Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2 - 1) - 0.15 * fs), (Rpoints.get(j2 - 1) - 3)) > e1) && (Arraylistdata.Find_min(nega, (Rpoints.get(j2 - 1) + 3), (int) Math.min(Rpoints.get(j2 - 1) + 0.15 * fs, data.length)) > e1))// 如果第j2-1点检测的极小值绝对值没有超过极小值阈值，那么更新e1时候采用j2的点进行更新
                        {
                            Rpoints.remove(j2 - 1);
                            j2 = j2 - 1;
                            int a1 = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2) - 0.15 * fs), (Rpoints.get(j2) - 3));
                            int a2 = Arraylistdata.Find_min(nega, (Rpoints.get(j2) + 3), (int) Math.min(Rpoints.get(j2) + 0.15 * fs, data.length));
                            int min0 = Math.min(a1, a2);
                            e1 = (int) (0.875 * e1 + 0.125 * 0.3 * min0);
                            
                        } else if ((Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2) - 0.15 * fs), (Rpoints.get(j2) - 3)) > e1) && (Arraylistdata.Find_min(nega, (Rpoints.get(j2) + 3), (int) Math.min(Rpoints.get(j2) + 0.15 * fs, data.length)) > e1))// 如果第j2-1点检测的极小值绝对值没有超过极小值阈值，那么更新e1时候采用j2的点进行更新
                        {
                            Rpoints.remove(j2);
                            j2 = j2 - 1;
                            int a1 = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2-1) - 0.15 * fs), (Rpoints.get(j2-1) - 3));
                            int a2 = Arraylistdata.Find_min(nega, (Rpoints.get(j2-1) + 3), (int) Math.min(Rpoints.get(j2-1) + 0.15 * fs, data.length));
                            int min0 = Math.min(a1, a2);
                            e1 = (int) (0.875 * e1 + 0.125 * 0.3 * min0);
                           
                        } else {
                            int a1 = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2 - 1) - 0.15 * fs), (Rpoints.get(j2 - 1) - 3));
                            int a2 = Arraylistdata.Find_min(nega, (Rpoints.get(j2-1) + 3), (int) Math.min(Rpoints.get(j2-1) + 0.15 * fs, data.length));
                            int min0 = Math.min(a1, a2);
                            int a3 = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2) - 0.15 * fs), (Rpoints.get(j2) - 3));
                            int a4 = Arraylistdata.Find_min(nega, (Rpoints.get(j2) + 3), (int) Math.min(Rpoints.get(j2) + 0.15 * fs, data.length));
                            int min1 = Math.min(a3, a4);
                            int MIN = min0 + min1;
                            e1 = (int) (0.875 * e1 + 0.125 * 0.3 * MIN / 2);
                            E1 = (int) (0.925 * E1 + 0.075 * 0.4 * MJ2[Rpoints.get(j2 - 1)]);
                        }
                    }

                    else {
                        E1 = (int) (0.875 * E1 + 0.125 * 0.4 * MJ2[Rpoints.get(j2 - 1)]);
                    }
                }
                j2 = j2 + 1;
            }

            // 漏检检查，当检测的点比之前平均RR1.5倍还大时，认为是漏检了
            if (j2 > 1 && i - Rpoints.get(j2 - 1) > 1.5 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1)) {
                Log.e("Rpoint", ""+Rpoints.size());
                int maxRR = Arraylistdata.Find_max(MJ2, (int) (Rpoints.get(j2 - 1) + (0.8 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))), (int) (Rpoints.get(j2 - 1) + (1.2 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))));
                int maxR = Arraylistdata.Find_max_location(MJ2, (int) (Rpoints.get(j2 - 1) + (0.8 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))), (int) (Rpoints.get(j2 - 1) + (1.2 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))));
                int minRR = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2 - 1) + (0.8 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))), (int) (Rpoints.get(j2 - 1) + (1.2 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))));
                int minR = Arraylistdata.Find_min_location(nega, (int) (Rpoints.get(j2 - 1) + (0.8 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))), (int) (Rpoints.get(j2 - 1) + (1.2 * (Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))));

                if (maxRR > 0.5 * E1 && ((Arraylistdata.Find_min(nega, (int) (maxR - 0.15 * fs), (maxR - 3)) < 0.5 * e1) || (Arraylistdata.Find_min(nega, (maxR + 3), (int) Math.min(maxR + 0.15 * fs, data.length))) < 0.5 * e1)) {
                    Rpoints.add(maxR);
                    j2 = j2 + 1;
                    i = (int) (maxR + 0.2 * fs);
                } else if (minRR < 0.5 * e1 && ((Arraylistdata.Find_max(MJ2, (int) (minR - 0.15 * fs), (minR - 3)) > 0.5 * E1) || (Arraylistdata.Find_max(MJ2, (minR + 3), (int) Math.min(minR + 0.15 * fs, data.length))) > 0.5 * E1)) {
                    Rpoints.add(minR);
                    j2 = j2 + 1;
                    i = (int) (minR + 0.2 * fs);
                }

            }

        }

        // 判断R波方向是向上还是向下
        int[] direction = new int[Rpoints.size()];// 默认的0为极大值R波向上，若为1为极小值R波向下
        for (int k = 0; k < Rpoints.size(); k++) {
            if (Arraylistdata.Find_min(nega, Math.max(Rpoints.get(k) - 55, 1), (Rpoints.get(k) - 3)) > Arraylistdata.Find_min(nega, (Rpoints.get(k) + 3), Math.min(Rpoints.get(k) + 55, data.length))) {
                direction[k] = 1;// R波向下
                // 找区间中的最小值
                int index0 = Rpoints.get(k) + 3;
                for (int i = Rpoints.get(k) + 3; i < Math.min(Rpoints.get(k) + 55, data.length); i++) {
                    if (MJ2[i] < MJ2[index0]) {
                        index0 = i;
                    }
                }
                Rpoints.set(k, index0 + 2);
            }
        }

        // 回归到原始心电信号找R点
        RRfinally = new int[Rpoints.size()];// 实际的R点集合

        for (int k = 0; k < Rpoints.size(); k++) {
            int maxer = 0;
            // 判断是向上还是向下
            if (direction[k] == 0) {
                maxer = Arraylistdata.Find_max(data, (int) Math.max(1, Rpoints.get(k) - 0.1 * fs), Rpoints.get(k));// 向上
            } else {
                maxer = Arraylistdata.Find_min(data, (int) Math.max(1, Rpoints.get(k) - 0.1 * fs), Rpoints.get(k));// 向下
            }

            for (int i = (int) Math.max(1, Rpoints.get(k) - 0.1 * fs); i < Rpoints.get(k); i++) {
                if (data[i] == maxer) {
                    RRfinally[k] = i;
                }
            }
        }

        return RRfinally;

    }

    /**
     * 寻找Q点的方法，具体参考matlab程序
     * 
     * @param data
     *            输入的心电数据，最好是原始数据放大1000倍
     * @param Rpoints
     *            输入的R点位置
     * @return QQt 输出为Q点所在位置的数组
     */
    public int[] Finding_Qpoints(int[] data, int[] Rpoint) {

        QQt = new int[Rpoint.length];

        if (Rpoint.length > 2) {

            for (int j = 1; j < Rpoint.length - 1; j++) {
                QQt[j] = Rpoint[j];
                if (data[Rpoint[j]] > 0) {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if ((data[Rpoint[j] - jj + 1] - data[Rpoint[j] - jj]) * fs < 1.2 && data[Rpoint[j] - jj] < 0.2 * data[Rpoint[j]]) {
                            QQt[j] = Rpoint[j] - jj + 1;
                            break;
                        }
                    }
                } else {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if ((data[Rpoint[j] - jj + 1] - data[Rpoint[j] - jj]) * fs > -1.2 && Math.abs(data[Rpoint[j] - jj]) < 0.2 * Math.abs(data[Rpoint[j]])) {
                            QQt[j] = Rpoint[j] - jj + 1;
                            break;
                        }
                    }
                }
                if (QQt[j] == Rpoint[j]) {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if (data[Rpoint[j] - jj] * data[Rpoint[j]] < 0) {
                            QQt[j] = Rpoint[j] - jj + 1;
                            break;
                        }
                    }
                }
            }
        }

        return QQt;
    }

    /**
     * 寻找S点的方法，具体参考matlab程序
     * 
     * @param data
     *            输入的心电数据，最好是原始数据放大1000倍
     * @param Rpoints
     *            输入的R点位置
     * @return SSt 输出为S点所在位置的数组
     */
    public int[] Finding_Spoints(int[] data, int[] Rpoint) {

        SSt = new int[Rpoint.length];

        if (Rpoint.length > 2) {

            for (int j = 1; j < Rpoint.length - 1; j++) {
                SSt[j] = Rpoint[j];
                if (data[Rpoint[j]] > 0) {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if ((data[Rpoint[j] + jj + 1] - data[Rpoint[j] + jj]) * fs > -1.2 && data[Rpoint[j] - jj] < 0.2 * data[Rpoint[j]]) {
                            SSt[j] = Rpoint[j] + jj - 1;
                            break;
                        }
                    }
                } else {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if ((data[Rpoint[j] + jj + 1] - data[Rpoint[j] + jj - 1]) * fs < 1.2 && Math.abs(data[Rpoint[j] + jj]) < 0.2 * Math.abs(data[Rpoint[j]])) {
                            SSt[j] = Rpoint[j] + jj - 1;
                            break;
                        }
                    }
                }
                if (SSt[j] == Rpoint[j]) {
                    for (int jj = 4; jj < 0.4 * (Rpoint[j] - Rpoint[j - 1]); jj++) {
                        if (data[Rpoint[j] + jj] * data[Rpoint[j]] < 0) {
                            SSt[j] = Rpoint[j] + jj - 1;
                            break;
                        }
                    }
                }
            }
        }

        return SSt;
    }

    /**
     * 
     * @param data
     *            输入的心电数据，最好是原始数据放大1000倍
     * @param QQt
     *            找到的Q点位置
     * @return Q1point 返回Q1点位置
     */
    public int[] Finding_Q1points(int[] data, int[] QQt) {
        Q1point = new int[QQt.length];
        for (int j = 1; j < QQt.length - 1; j++) {
            Q1point[j] = QQt[j];
            if (data[QQt[j]] <= 0) {
                for (int jj = 2; jj < 0.3 * (QQt[j] - QQt[j - 1]); jj++) {
                    if (data[QQt[j] - jj] >= 0) {
                        Q1point[j] = QQt[j] - jj;
                        break;
                    }
                    if ((data[QQt[j] - jj + 1] - data[QQt[j] - jj]) * fs > -1.5) {
                        Q1point[j] = QQt[j] - jj;
                        break;
                    }
                }
            } else {
                for (int jj = 2; jj < 0.3 * (QQt[j] - QQt[j - 1]); jj++) {
                    if (data[QQt[j] - jj] <= 0) {
                        Q1point[j] = QQt[j] - jj;
                        break;
                    }
                    if ((data[QQt[j] - jj + 1] - data[QQt[j] - jj]) * fs < 1.5) {
                        Q1point[j] = QQt[j] - jj;
                        break;
                    }
                }
            }
            if (Math.abs(data[Q1point[j] + 2] - data[Q1point[j] + 1]) < 10 && Math.abs(data[Q1point[j] + 1] - data[Q1point[j]]) < 10) {
                Q1point[j] = Q1point[j] + 2;
            }
        }

        return Q1point;
    }

    /**
     * 
     * @param data
     *            输入的心电数据，最好是原始数据放大1000倍
     * @param SSt
     *            找到的S点位置
     * @return S2point 返回S2点位置
     */
    public int[] Finding_S2points(int[] data, int[] SSt) {
        S2point = new int[SSt.length];

        for (int j = 1; j < SSt.length - 1; j++) {
            S2point[j] = SSt[j];
            if (data[SSt[j]] <= 0) {
                for (int jj = 2; jj < 0.3 * (SSt[j] - SSt[j - 1]); jj++) {
                    if (data[SSt[j] + jj] >= 0) {
                        S2point[j] = SSt[j] + jj;
                        break;
                    }
                    if ((data[SSt[j] + jj + 1] - data[SSt[j] + jj]) * fs < 1.5) {
                        S2point[j] = SSt[j] + jj;
                        break;
                    }
                }
            } else {
                for (int jj = 2; jj < 0.3 * (SSt[j] - SSt[j - 1]); jj++) {
                    if (data[SSt[j] + jj] <= 0) {
                        S2point[j] = SSt[j] + jj;
                        break;
                    }
                    if ((data[SSt[j] + jj + 1] - data[SSt[j] + jj]) * fs > -1.5) {
                        S2point[j] = SSt[j] + jj;
                        break;
                    }
                }
            }
            if (Math.abs(data[S2point[j] - 2] - data[S2point[j] - 1]) < 10 && Math.abs(data[S2point[j] - 1] - data[S2point[j]]) < 10) {
                S2point[j] = S2point[j] - 2;
            }
        }

        return S2point;
    }

    /**
     * 
     * @param data
     *            输入的原始数据，要求1000倍
     * @param S2point
     *            输入之前找到的S2点，如果算法中不加入S2的话，输入的S2可以换成S点位置
     * @return Tpoint 找到的T点位置
     */
    public int[] Finding_Tpoints(int[] data, int[] S2point) {
        /* 求P点位置和T波位置 */
        Tpoint = new int[S2point.length];
        for (int j = 1; j < S2point.length - 1; j++) {
            Tpoint[j] = S2point[j];
            int TT1point = S2point[j];
            int jj = 0;
            while (S2point[j] + jj < data.length - 1 && data[S2point[j] + jj] <= 0) {
                jj = jj + 1;
            }
            int TT2point = S2point[j] + jj;

            for (int k = 4; k < 0.7 * (S2point[j + 1] - S2point[j]); k++) {
                if (data[S2point[j] + k] < 0) {
                    if (data[S2point[j] + k] < data[TT1point]) {
                        TT1point = S2point[j] + k;
                    }
                } else {
                    if (data[S2point[j] + k] > data[TT2point]) {
                        TT2point = S2point[j] + k;
                    }
                }
            }
            if ((data[S2point[j]] - data[TT1point]) < data[TT2point]) {
                Tpoint[j] = TT2point;
            } else {
                Tpoint[j] = TT1point;
            }
        }

        return Tpoint;
    }

    /**
     * 
     * @param data
     *            输入的原始数据，要求1000倍
     * @param Q1point
     *            输入的Q1点位置，如果算法中不加入Q1的话,使用Q点位置代替
     * @return Ppoint 算法找到的P点位置
     */
    public int[] Findg_Ppoints(int[] data, int[] Q1point) {
        // 找P波
        Ppoint = new int[Q1point.length];
        for (int j = 1; j < Q1point.length - 1; j++) {
            Ppoint[j] = Q1point[j];
            int PP1point = Q1point[j];
            int jj = 1;
            while (Q1point[j] - jj > 0 && data[Q1point[j] - jj] < 0) {
                jj++;
            }
            int PP2point = Q1point[j] - jj;

            for (int k = 4; k < 0.7 * (Q1point[j] - Q1point[j - 1]); k++) {
                if (data[Q1point[j] - k] < 0) {
                    if (data[Q1point[j] - k] < data[PP1point]) {
                        PP1point = Q1point[j] - k;
                    }
                } else {
                    if (data[Q1point[j] - k] > data[PP2point]) {
                        PP2point = Q1point[j] - k;
                    }
                }
            }
            if ((data[Q1point[j]] - data[PP1point]) < data[PP2point]) {
                Ppoint[j] = PP2point;
            } else {
                Ppoint[j] = PP1point;
            }

        }

        return Ppoint;
    }

    /**
     * 参数求值
     * 
     * @param data
     *            原始数据，要求扩大1000倍
     * @param Rpoint
     *            算法找到的R点
     * @param Q1point
     *            算法找到的Q点起点，如果算法中不计算Q1，此处可以用Q点代替
     * @param S2point
     *            算法找到的S点终点，如果算法中不计算S2，此处可以用S点代替
     * @param Ppoint
     *            算法中计算的P点位置
     * @param Tpoint
     *            算法中计算的T点位置
     * 
     */
    public int[] Find_Parameters_and_Result(int[] data, int[] Rpoint, int[] Q1point, int[] S2point, int[] Ppoint, int[] Tpoint) 
    {
       
        
        double Model[][]=new double[Rpoint.length][11];//10.19 revised
        int[] RRtime = new int[Rpoint.length];// RR间期
        float[] ARtime = new float[Rpoint.length];// RR间期
        float[] HRV = new float[Rpoint.length];// RR间期变异
        float[] QRSwidth = new float[Rpoint.length];// QRS宽度
        float[] PRtime = new float[Rpoint.length];// PR之间的距离
        float[] Pheight = new float[Rpoint.length];
        float[] EST = new float[Rpoint.length];// T波是否与R波方向相反

        for (int j = 2; j < Rpoint.length - 1; j++) {
            RRtime[j] = Rpoint[j] - Rpoint[j - 1];
        }

        for (int i=9;i<Rpoint.length;i++)
        {
                ARtime[i]=(Rpoint[i-1]-Rpoint[i-9])/(8);
        }
        
        
        for (int j = 2; j < Rpoint.length - 1; j++) {
            HRV[j] = (float) Math.abs(RRtime[j] - RRtime[j - 1]);
        }

        for (int j = 1; j < Rpoint.length - 1; j++) {
            QRSwidth[j] = S2point[j] - Q1point[j];
            Log.e("7777777", "gggggggggg");
            System.out.println("23334443333333333333");
           System.out.println( QRSwidth[j]); 
       //     System.out.print( QRSwidth[j]);
            
        }

        for (int j = 1; j < Rpoint.length - 1; j++) {
            if (Tpoint[j] == Rpoint[j]) {
                EST[j] = (float) 0;
            } else {
                EST[j] = (data[Tpoint[j]] - data[S2point[j]]) / 1000;// 计算T点和S2点之间的距离，此处最重要的是正负号问题
            }
        }

        for (int j = 1; j < Rpoint.length - 1; j++) {
            if (Ppoint[j] == Q1point[j]) {
                PRtime[j] = (float) 0;
                Pheight[j] = (float) 0;
            } else {
                PRtime[j] = Rpoint[j] - Ppoint[j];
                Pheight[j] = (data[Ppoint[j]]);
                Log.e("55555", "eeeeeeeeee");  
               System.out.println( PRtime[j]); 
            }

        }
        
        
        if(Rpoint.length>1)
        {
            RRrate=Rpoint[Rpoint.length-1]-Rpoint[0];
            RRrate = RRrate / (Rpoint.length - 1);
            RRrate = (60 * StaticValue.Fs) / RRrate;
        }
        
        //1.离散化RRrate
        double rate = 0;
        if (RRrate < 40)
        {
            rate = 0;
        } else if (40 <= RRrate && RRrate < 60)
        {
            rate = 3.116*Math.pow(10, -10)*Math.pow(RRrate, 5.692)-0.3426;
        } else if (60 <= RRrate && RRrate < 90)
        {
            rate = Double.POSITIVE_INFINITY;
        } else if (90 <= RRrate && RRrate < 120)
        {
            rate = 4.553*Math.pow(10,11)*Math.pow(RRrate, -5.625)-0.8622;
        } else if (120 <= RRrate && RRrate<=85)
        {
            rate = 0;
        } else if (85<=RRrate && RRrate<90)
        {
            rate= 0.5108-0.0719*(RRrate-85);
        } else if (90<=RRrate && RRrate<100)
        {
            rate = 0.2231-0.02422*(RRrate-90);
        } else if (100<=RRrate && RRrate<120)
        {
            rate = 0.051-0.00255*(RRrate-100);
        } else if (120<=RRrate)
        {
            rate = 0;
        }
        
        for (int i=0;i<Rpoint.length;i++)
        {
                Model[i][0]=rate;
        }
        
        
        
        
        // 2.离散化QRSwidth
        for (int i = 1; i < Rpoint.length - 1; i++)
        {

            if (QRSwidth[i] < 0.1)
            {
                Model[i][1] = 0;
            } else if (0.1 <= QRSwidth[i] && QRSwidth[i] < 0.12)
            {
                Model[i][1] = 10 * (QRSwidth[i] - 0.1);
            } else if (0.12 <= QRSwidth[i] && QRSwidth[i] < 0.14)
            {
                Model[i][1] = 0.2 + 20 * (QRSwidth[i] - 0.1);
            } else if (0.14 <= QRSwidth[i])
            {
                Model[i][1] = 1;
            }
        }
    
        
        
        
        // 3.离散化 HRV
        for (int i = 4; i < Rpoint.length - 4; i++)
        {
            if (HRV[i - 1] < 0.12 && HRV[i] < 0.12 && HRV[i + 1] < 0.12)
            {
                Model[i][2] = 0-0.8*HRV[i];
            } else if (HRV[i - 1] < 0.12 && HRV[i] >= 0.12 && HRV[i + 1] < 0.12)
            {
                Model[i][2] = 1-0.8*HRV[i];
            } else if (HRV[i - 1] >= 0.12 && HRV[i] >= 0.12 && HRV[i + 1] < 0.12)
            {
                Model[i][2] = 2-0.8*HRV[i];
            } else if (HRV[i - 1] < 0.12 && HRV[i] >= 0.12 && HRV[i + 1] >= 0.12)
            {
                Model[i][2] = 2-0.8*HRV[i];
            } else if (HRV[i - 1] >= 0.12 && HRV[i] >= 0.12 && HRV[i + 1] >= 0.12)
            {
                Model[i][2] = 3-0.8*HRV[i];
            }
        }

        
        
        //4.离散化RRtime

        for (int i=2;i<Rpoint.length;i++)
        {
           if (RRtime[i-1]<1.5 && RRtime[i]<1.5 && RRtime[i+1]<1.5)
           {
            Model[i][3]=0;
           }else if (RRtime[i-1]<1.5 && RRtime[i]>=1.5 && RRtime[i+1]<1.5)
           {
                Model[i][3]=1;  
           } else if (RRtime[i-1]<1.5 && RRtime[i]>=1.5 && RRtime[i+1]>=1.5)
           {
                Model[i][3]=2;
           } else if (RRtime[i-1]>=1.5 && RRtime[i]>=1.5 && RRtime[i+1]<1.5)
           {
                Model[i][3]=2;
           } else if (RRtime[i-1]>=1.5 && RRtime[i]>=1.5 && RRtime[i+1]>=1.5)
           {
                Model[i][3]=3;
           }
        }
           
        
        
        //5.离散化RRtime和ARtime

        for(int i=9;i<Rpoint.length-1;i++)
        {
            if (RRtime[i]<0.85*ARtime[i])
            {
                Model[i][4]=0;
            }else if (RRtime[i]<ARtime[i] &&RRtime[i]>=0.85*ARtime[i])
            {
                    Model[i][4]=20/3*(RRtime[i]/ARtime[i]-0.85);
            }else if (RRtime[i]>=ARtime[i])
            {
                        Model[i][4]=1;
            }
        }


        //6.离散化ECG[Rpoints[i]]
        for (int i=0;i<Rpoint.length;i++)
        {
            if (data[Rpoint[i]]>0)
            {
                Model[i][5]=0;
            }else{
                Model[i][5]=1;
            }
        }
   

        //7.离散化ECG[Rpoints[i]]*EST[i]
        for (int i=1;i<Rpoint.length-1;i++)
        {
            if (data[Rpoint[i]]*EST[i]>0)
            {
                Model[i][6]=0;
            }else {
                Model[i][6]=1;
            }
        }

        //8.离散化RRtime[i]+RRtime[i+1]>=2*ARtime[i]
        for (int i=1;i<Rpoint.length-1;i++)
        {
            if (RRtime[i]+RRtime[i+1]<1.8*ARtime[i] ||RRtime[i]+RRtime[i+1]>2.2*ARtime[i])
            {
                Model[i][7]=0;
            } else if (RRtime[i]+RRtime[i+1]<=2.2*ARtime[i] && RRtime[i]+RRtime[i+1]>=1.8*ARtime[i])
            {
                Model[i][7]=1;
            }
        }

        //9.离散化Rpoints与Tpoints值

        for (int i=1;i<Rpoint.length-1;i++)
        {
            if (data[Tpoint[i]]>data[Rpoint[i]])
            {
                Model[i][8]=0;
            }else if (data[Tpoint[i]]<data[Rpoint[i]] &&data[Tpoint[i]]>0)
            {
                    Model[i][8]=1;
            }else if(data[Tpoint[i]]>-0.5*data[Rpoint[i]] &&data[Tpoint[i]]<0)
            {
                        Model[i][8]=2;
            }else if (data[Tpoint[i]]<-0.5*data[Rpoint[i]])
            {
                            Model[i][8]=3;
            }
        }

        //10.离散化PRtime
        for (int i=2;i<Rpoint.length-1;i++)
        {
            if (PRtime[i]>=0.18)
            {
                Model[i][9]=1;
            }else if (PRtime[i]<0.18 &&PRtime[i]>0.16)
            {
                    Model[i][9]=0.5+25*(PRtime[i]-0.16);
            }else if (PRtime[i]<=0.16 &&PRtime[i]>0.12)
                {
                        Model[i][9]=0.5;
                }else if (PRtime[i]<=0.12 &&PRtime[i]>0.1)
                    {
                            Model[i][9]=25*(PRtime[i]-0.1);
                    }else if (PRtime[i]<=0.1)
                    {
                                Model[i][9]=0;
                    }
        }
        
        //11.求出HRV的最大值 revise 10.19
        float maxHRV=HRV[3];
        for (int i=3;i<Rpoint.length-1;i++)
        {
            if(HRV[i]>maxHRV)
            {
                maxHRV=HRV[i];
            }
        }
        for (int i=2;i<Rpoint.length-1;i++)
        {
            Model[i][10]=maxHRV;
        }
 
        int result[] =analyse3(Model,Rpoint.length);

        return result;

    }
    
    public int[] analyse3(double[][] Model,int length)
    {
        int result[][]=new int[Model.length][Model[0].length];
        double d1=0;
        
        
        //1.心动过速
        for (int i=2;i<length-1;i++)
        {
            if (RRrate>85)
            {
            d1=Model[i][0];
            result[i][0]=(int) (Math.pow(2.7318, -d1)*100);
            }
            else
            {
                result[i][0]=0;
            }
        }
            
        //2.室性心动过速
        for (int i=1;i<length-1;i++)
        {
            if (RRrate>150)
            {
            d1=2*(1-Model[i][0])+(1-Model[i][1]);
            result[i][1]=(int) (Math.pow(2.7318, -d1)*100);
            }
            else
            {
                result[i][1]=0;
            }
         
        }
    

        //3.心动过缓
        for(int i=1;i<length-1;i++)
        {
            if (RRrate<60)
            {
            d1=Model[i][0];
            result[i][2]=(int) (Math.pow(2.7318, -d1)*100);
            }else 
                {result[i][2]=0;}
        }

        //4.室性逸博
        for (int i=1;i<length-1;i++)
        {
             if (RRrate<60)
             {
            d1=1.8*Model[i][0]+1.7*(1-Model[i][1]);
            result[i][3]=(int) (Math.pow(2.7318, -d1)*100);
             }
             else
             {
                 result[i][3]=0;
             }

        }

        //5.房性逸博
        for (int i=1;i<length-1;i++)
        {
            if (RRrate<60)
            {
            d1=2*Model[i][0]+2*(1-Model[i][1])+(1-Model[i][9]);
            result[i][4]=(int) (Math.pow(2.7318, -d1)*100);
            }else
                {
                result[i][4]=0;
                }
        }


        //6.心律不齐 revised 10.19
        for (int i=3;i<length-1;i++)
        {
            d1=Math.abs(Model[i][10]);
            if (d1<40)
                result[i][5]=0;
            else if (d1<200)
                result[i][5]=(int)(5*d1/8-25);
            else 
                result[i][5]=100;
        }
        
        


        //7.室性早搏revised 10.21
        for (int i=9;i<length-1;i++)
        {
            if(RRrate>60)
            {
                if ( Model[i][5]==1)
                {
                    d1=Model[i][4]+(1-Model[i][1]);
                } 
                else if ( Model[i][4]<1)
                {
                   d1=(1-Model[i][1])+Model[i][4]+0.1*(3-Model[i][2])+0.1*(1-Model[i][6])+(1-Model[i][7]);
                }
                   result[i][6]=(int) (Math.pow(2.7318, -d1)*100);
            }
                   else
                       result[i][6]=0;
        }
        
        //8.房性早搏revised 10.21
        for (int i=9;i<length-1;i++)
        {
            if(RRrate>=60)
            {
               if( Model[i][1]<=0.5 || Model[i][4]<=1)
               {
                   d1=(Model[i][4]+(3-Model[i][2])+(Model[i][7])+Math.abs(Model[i][9]-0.5));
                   result[i][7]=(int) (Math.pow(2.7318, -d1)*100);
               }
            }
            else
                result[i][7]=0;
            
        }

        //9.交界性早搏
        for (int i=9;i<length-1;i++)
        {
            if(RRrate>=60)
            {
                d1=Math.abs(Model[i][1]-1)+Math.abs(Model[i][9]-2)+Math.abs(Model[i][4]);
                result[i][8]=(int) (Math.pow(2.7318, -d1)*100);
            }
            else
                result[i][8]=0;
        }
        
        
        //10房颤
        for (int i=0;i<length-1;i++)
        {
            if(RRrate>=60)
            {
                d1=Math.abs(Model[i][0])+Math.abs(Model[i][2]-3)+Math.abs(Model[i][1])+(Model[i][7]);
                result[i][9]=(int) (Math.pow(2.7318, -d1)*100);
            }
            else
                result[i][9]=0;
        }
        
        
        
        int resultt[]=new int[result[0].length];
        for(int i=0;i<result[0].length;i++)
        {
            
            int temp[]=new int[result.length];
            for(int j=0;j<result.length;j++)
            {
                temp[j]=result[j][i];
            }
            Arrays.sort(temp);
            resultt[i]=(temp[temp.length-1]+temp[temp.length-2]+temp[temp.length-3]+temp[temp.length-4]+temp[temp.length-5])/5;
        }
        
        
        double plus=0;
        for(int i=0;i<result.length;i++)
        {
            plus+=result[i][5];
        }

        //resultt[5]=(int)(5*(plus/result.length)-362);revide
        resultt[6]=(int)resultt[6]-36;//revised 10.21
        resultt[7]=(int)resultt[7]+5;//revised 10.21
        resultt[8]=(int)resultt[8]-8;
        for(int i=0;i<10;i++)
        {
            if(resultt[i]>100)
            {
                resultt[i]=100;
            }
            else if(resultt[i]<0)
            {
                resultt[i]=0;
            }
        }
        
        return resultt;
                
        
    }
    

}

