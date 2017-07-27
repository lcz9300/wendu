package com.HWDTEMPT.tool;

import android.util.Log;

import com.HWDTEMPT.model.Arraylistdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FindR {
    private int fs;// ����Ƶ��

    int RRrate=0;
    private int[] RRfinally,QQt,SSt,Q1point,S2point,Tpoint,Ppoint;
    public FindR(int fs) {
        this.fs = fs;
    }
    /**
     * ȥ��Ұֵ��
     *
     * @param eCGDataListA
     *            ����Ҫȥ��Ұֵ������
     * @return datareturn ���ص�ȥ��Ұֵ�������
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
            // �˴�Ϊ�趨��ȥ��Ұֵ��ֵ������ֵ�ɸ���ʵ��������������ǲ��˹��ߣ���Ϊ��ı䲨��
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
     * ��ֵ�˲���������������Ϊint���飬���ҲΪint���飬��Ҫ�������ݳ���1000��
     *
     * @param data
     *            ���������
     * @return datareturn ��ֵ�˲�֮�������
     */
    public int[] Middle_Filter(int[] data) {

        int[] datareturn = new int[data.length];
        System.arraycopy(data, 0, datareturn, 0, data.length);
        int Length = 0;// ��ֵ�˲��ĳ��ȣ�ȡ���ڲ���Ƶ�ʣ�һ����Ϊ�ǲ���Ƶ�ʵ�80%��Ϊ������
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
     * Ѱ��R��ķ������˷���Ϊ˫����С���任����������ο�matlab����
     *
     * @param data
     *            ������ĵ����ݣ������ԭʼ���ݷŴ�1000��
     * @return RRfinally ���ΪR������λ�õ�����
     */
    public int[] Find_Rpoints(int[] data) {

        List<Integer> Rpoints = new ArrayList<Integer>();// ��һ����̬���ݱ���R�㣬�����ת��Ϊ�̶����飬����֮��Ĳ���
        int[] swa = new int[data.length];// ����һ�����飬���˫����С���任���м�ֵ
        int[] swd = new int[data.length];// ����һ�����飬���˫�����任�����ݵ�ֵ
        for (int i = 0; i < data.length - 3; i++) {
            swa[i + 3] = data[i + 3] / 4 + data[i + 3 - 1] * 3 / 4 + data[i + 3 - 2] * 3 / 4 + data[i + 3 - 3] / 4;
        }

        for (int i = 0; i < data.length - 6; i++) {
            swd[i + 6] = -swa[i + 6 - 1] / 4 - swa[i + 6 - 2] * 3 / 4 + swa[i + 6 - 4] * 3 / 4 + swa[i + 6 - 6] / 4;

        }

        Map<String, Object> map = Arraylistdata.Ask_Jida_Jixiao(swd);// �������м���С����λ�ã����ص������м���Сλ��Ϊ1������Ϊ0

        int[] jida = (int[]) map.get("int[]1");// ����ֵ���ڵ�����
        int[] jixiao = (int[]) map.get("int[]2");// ��Сֵ���ڵ�����


        int[]pddw=new int[jida.length];
        // �󼫴�ֵ����swd��ҲΪ��ֵ������
        for (int i = 0; i < jida.length; i++) {
            if (swd[i] * jida[i] > 0) {
                pddw[i] = 1;
            } else {
                pddw[i] = 0;
            }
        }


        int[]nddw=new int[jixiao.length];
        // ��Сֵ����swd��ҲΪ��ֵ�ĵ�
        for (int i = 0; i < nddw.length; i++) {
            if (swd[i] * jixiao[i] < 0) {
                nddw[i] = 1;
            } else {
                nddw[i] = 0;
            }
        }

        int[] MJ2 = new int[data.length];// MJ2Ϊ��������Сֵ��swd���飬����λ��Ϊ0�����������Լ��ٺܶ಻��Ҫ�ļ�����

        MJ2[0] = swd[0];
        for (int i = 1; i < data.length-1; i++) {
            if (pddw[i] > 0 || nddw[i] > 0) {
                MJ2[i] = swd[i];
            } else {
                MJ2[i] = 0;
            }
        }
        MJ2[data.length-1]=swd[data.length-1];


        // �˴���ʼѰ��R�㣬posiΪ����ֵ������
        int possi[] = new int[1000];
        System.arraycopy(MJ2, 0, possi, 0, possi.length);
        Arrays.sort(possi);
        int E1 = (int) (0.5 * possi[998]); // ������ֵ��ֵ

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


        int e1 = (int) (0.3 *Arraylistdata.Find_min(nega, 0, nega.length));// ������ֵ��ֵ

        // ��ʼ��R��
        int j2 = 0;

        for (int i = 5; i < data.length-10; i++) {
            if (MJ2[i] > E1) {

                Log.e("i", i+"");
                Rpoints.add(i);

                // �ж������Ƿ���һ��������

                if ((j2 >= 1) && ((Rpoints.get(j2) - Rpoints.get(j2 - 1)) < fs / 5))// ��Ӧ��Ϊ0.2����fs����fs=400��ʱ�򣬲�Ӧ��Ϊ80
                {
                    if (MJ2[Rpoints.get(j2)] > MJ2[Rpoints.get(j2 - 1)])// ȡ����R��
                    {
                        Rpoints.remove(j2 - 1);
                    } else {
                        Rpoints.remove(j2);
                    }
                    j2 = j2 - 1;
                }

                // �����,����⵽�ĵ���ǰһ�������С��ƽ��ֵ��һ�룬��Ϊ��죬�����Ų�
                if ((j2 > 2) && ((Rpoints.get(j2) - Rpoints.get(j2 - 1))) < 0.4 * ((Rpoints.get(j2 - 1) - Rpoints.get(0)) / (j2 - 1))) {
                    if (MJ2[Rpoints.get(j2)] > MJ2[Rpoints.get(j2 - 1)])// %ȡ����R��
                    {
                        Rpoints.remove(j2 - 1);
                    } else {
                        Rpoints.remove(j2);
                    }
                    j2 = j2 - 1;
                }

                // �����,����⵽�ĵ���ǰһ����ķ�ֵ�ȴ���2��ʱ����Ϊ��죬�����Ų�
                if (j2 > 1 && Rpoints.get(j2 - 1) - 0.15 * fs > 0) {
                    Log.e("j2", j2+"");
                    if ((MJ2[Rpoints.get(j2)] >= 2 * MJ2[Rpoints.get(j2 - 1)]) || (MJ2[Rpoints.get(j2 - 1)] >= 2 * MJ2[Rpoints.get(j2)])) {
                        if ((Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2 - 1) - 0.15 * fs), (Rpoints.get(j2 - 1) - 3)) > e1) && (Arraylistdata.Find_min(nega, (Rpoints.get(j2 - 1) + 3), (int) Math.min(Rpoints.get(j2 - 1) + 0.15 * fs, data.length)) > e1))// �����j2-1����ļ�Сֵ����ֵû�г�����Сֵ��ֵ����ô����e1ʱ�����j2�ĵ���и���
                        {
                            Rpoints.remove(j2 - 1);
                            j2 = j2 - 1;
                            int a1 = Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2) - 0.15 * fs), (Rpoints.get(j2) - 3));
                            int a2 = Arraylistdata.Find_min(nega, (Rpoints.get(j2) + 3), (int) Math.min(Rpoints.get(j2) + 0.15 * fs, data.length));
                            int min0 = Math.min(a1, a2);
                            e1 = (int) (0.875 * e1 + 0.125 * 0.3 * min0);

                        } else if ((Arraylistdata.Find_min(nega, (int) (Rpoints.get(j2) - 0.15 * fs), (Rpoints.get(j2) - 3)) > e1) && (Arraylistdata.Find_min(nega, (Rpoints.get(j2) + 3), (int) Math.min(Rpoints.get(j2) + 0.15 * fs, data.length)) > e1))// �����j2-1����ļ�Сֵ����ֵû�г�����Сֵ��ֵ����ô����e1ʱ�����j2�ĵ���и���
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

            // ©���飬�����ĵ��֮ǰƽ��RR1.5������ʱ����Ϊ��©����
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

        // �ж�R�����������ϻ�������
        int[] direction = new int[Rpoints.size()];// Ĭ�ϵ�0Ϊ����ֵR�����ϣ���Ϊ1Ϊ��СֵR������
        for (int k = 0; k < Rpoints.size(); k++) {
            if (Arraylistdata.Find_min(nega, Math.max(Rpoints.get(k) - 55, 1), (Rpoints.get(k) - 3)) > Arraylistdata.Find_min(nega, (Rpoints.get(k) + 3), Math.min(Rpoints.get(k) + 55, data.length))) {
                direction[k] = 1;// R������
                // �������е���Сֵ
                int index0 = Rpoints.get(k) + 3;
                for (int i = Rpoints.get(k) + 3; i < Math.min(Rpoints.get(k) + 55, data.length); i++) {
                    if (MJ2[i] < MJ2[index0]) {
                        index0 = i;
                    }
                }
                Rpoints.set(k, index0 + 2);
            }
        }

        // �ع鵽ԭʼ�ĵ��ź���R��
        RRfinally = new int[Rpoints.size()];// ʵ�ʵ�R�㼯��

        for (int k = 0; k < Rpoints.size(); k++) {
            int maxer = 0;
            // �ж������ϻ�������
            if (direction[k] == 0) {
                maxer = Arraylistdata.Find_max(data, (int) Math.max(1, Rpoints.get(k) - 0.1 * fs), Rpoints.get(k));// ����
            } else {
                maxer = Arraylistdata.Find_min(data, (int) Math.max(1, Rpoints.get(k) - 0.1 * fs), Rpoints.get(k));// ����
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
