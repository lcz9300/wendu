package com.HWDTEMPT.tool;

import android.util.Log;

import com.HWDTEMPT.model.Arraylistdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FindR {
    private int fs;// 采样频率

    int RRrate=0;
    private int[] RRfinally,QQt,SSt,Q1point,S2point,Tpoint,Ppoint;
    public FindR(int fs) {
        this.fs = fs;
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

}
