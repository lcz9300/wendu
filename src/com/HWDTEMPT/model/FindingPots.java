package com.HWDTEMPT.model;

import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author mick
 *         �˷����������ĵ��źŵ������㣬���������˲�������������ȡ�����֣����а���ȥ��Ұֵ�㣬С���任����ֵ�˲�����R��P,T,Q,S,�Լ�Q������S���յ�
 *         ��ʹ�õ�ʱ����Բ������е�һ���ֺ��߶ಿ��
 * �汾���ڣ�2016.10.25���������޸ģ��Ķ�������������
 *         haorenwang@sjtu.edu.cn
 */
public class FindingPots {
    private int fs;// ����Ƶ��

    int RRrate=0;
    private int[] RRfinally,QQt,SSt,Q1point,S2point,Tpoint,Ppoint;
    public FindingPots(int fs) {
    this.fs = fs;
//UserInfo user=new UserInfo();
//String age=user.getUserAge();
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

    /**
     * Ѱ��Q��ķ���������ο�matlab����
     * 
     * @param data
     *            ������ĵ����ݣ������ԭʼ���ݷŴ�1000��
     * @param Rpoints
     *            �����R��λ��
     * @return QQt ���ΪQ������λ�õ�����
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
     * Ѱ��S��ķ���������ο�matlab����
     * 
     * @param data
     *            ������ĵ����ݣ������ԭʼ���ݷŴ�1000��
     * @param Rpoints
     *            �����R��λ��
     * @return SSt ���ΪS������λ�õ�����
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
     *            ������ĵ����ݣ������ԭʼ���ݷŴ�1000��
     * @param QQt
     *            �ҵ���Q��λ��
     * @return Q1point ����Q1��λ��
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
     *            ������ĵ����ݣ������ԭʼ���ݷŴ�1000��
     * @param SSt
     *            �ҵ���S��λ��
     * @return S2point ����S2��λ��
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
     *            �����ԭʼ���ݣ�Ҫ��1000��
     * @param S2point
     *            ����֮ǰ�ҵ���S2�㣬����㷨�в�����S2�Ļ��������S2���Ի���S��λ��
     * @return Tpoint �ҵ���T��λ��
     */
    public int[] Finding_Tpoints(int[] data, int[] S2point) {
        /* ��P��λ�ú�T��λ�� */
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
     *            �����ԭʼ���ݣ�Ҫ��1000��
     * @param Q1point
     *            �����Q1��λ�ã�����㷨�в�����Q1�Ļ�,ʹ��Q��λ�ô���
     * @return Ppoint �㷨�ҵ���P��λ��
     */
    public int[] Findg_Ppoints(int[] data, int[] Q1point) {
        // ��P��
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
     * ������ֵ
     * 
     * @param data
     *            ԭʼ���ݣ�Ҫ������1000��
     * @param Rpoint
     *            �㷨�ҵ���R��
     * @param Q1point
     *            �㷨�ҵ���Q����㣬����㷨�в�����Q1���˴�������Q�����
     * @param S2point
     *            �㷨�ҵ���S���յ㣬����㷨�в�����S2���˴�������S�����
     * @param Ppoint
     *            �㷨�м����P��λ��
     * @param Tpoint
     *            �㷨�м����T��λ��
     * 
     */
    public int[] Find_Parameters_and_Result(int[] data, int[] Rpoint, int[] Q1point, int[] S2point, int[] Ppoint, int[] Tpoint) 
    {
       
        
        double Model[][]=new double[Rpoint.length][11];//10.19 revised
        int[] RRtime = new int[Rpoint.length];// RR����
        float[] ARtime = new float[Rpoint.length];// RR����
        float[] HRV = new float[Rpoint.length];// RR���ڱ���
        float[] QRSwidth = new float[Rpoint.length];// QRS���
        float[] PRtime = new float[Rpoint.length];// PR֮��ľ���
        float[] Pheight = new float[Rpoint.length];
        float[] EST = new float[Rpoint.length];// T���Ƿ���R�������෴

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
                EST[j] = (data[Tpoint[j]] - data[S2point[j]]) / 1000;// ����T���S2��֮��ľ��룬�˴�����Ҫ��������������
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
        
        //1.��ɢ��RRrate
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
        
        
        
        
        // 2.��ɢ��QRSwidth
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
    
        
        
        
        // 3.��ɢ�� HRV
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

        
        
        //4.��ɢ��RRtime

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
           
        
        
        //5.��ɢ��RRtime��ARtime

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


        //6.��ɢ��ECG[Rpoints[i]]
        for (int i=0;i<Rpoint.length;i++)
        {
            if (data[Rpoint[i]]>0)
            {
                Model[i][5]=0;
            }else{
                Model[i][5]=1;
            }
        }
   

        //7.��ɢ��ECG[Rpoints[i]]*EST[i]
        for (int i=1;i<Rpoint.length-1;i++)
        {
            if (data[Rpoint[i]]*EST[i]>0)
            {
                Model[i][6]=0;
            }else {
                Model[i][6]=1;
            }
        }

        //8.��ɢ��RRtime[i]+RRtime[i+1]>=2*ARtime[i]
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

        //9.��ɢ��Rpoints��Tpointsֵ

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

        //10.��ɢ��PRtime
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
        
        //11.���HRV�����ֵ revise 10.19
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
        
        
        //1.�Ķ�����
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
            
        //2.�����Ķ�����
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
    

        //3.�Ķ�����
        for(int i=1;i<length-1;i++)
        {
            if (RRrate<60)
            {
            d1=Model[i][0];
            result[i][2]=(int) (Math.pow(2.7318, -d1)*100);
            }else 
                {result[i][2]=0;}
        }

        //4.�����ݲ�
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

        //5.�����ݲ�
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


        //6.���ɲ��� revised 10.19
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
        
        


        //7.�����粫revised 10.21
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
        
        //8.�����粫revised 10.21
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

        //9.�������粫
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
        
        
        //10����
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

