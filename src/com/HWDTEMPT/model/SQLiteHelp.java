package com.HWDTEMPT.model;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.HWDTEMPT.hwdtempt.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteHelp extends SQLiteOpenHelper {

    //数据库的名字
    private static final String DB_NAME="ECGBT.db";
    //创建三张表
    @SuppressWarnings("unused")
    private static final String TBL1_NAME="user";
    private static final String TBL2_NAME="Diagnosis";
    private static final String TBL3_NAME="Result";
    private static final String TBL4_NAME="Bloodpredb";
    private static final String TBL5_NAME="Bluetoothls";
    private static final String TBL6_NAME="wendu";
    
    private static final String CREATE_TBL1=" create table "+" user(_id integer primary key autoincrement,username,password,phone,Height,weight,age,sex,userNum)";
    
    private static final String CREATE_TBL2=" create table "+" Diagnosis(_id integer primary key autoincrement,username,time integer,Rpoint,Qpoint,Q1point,Spoint,S2point,Ppoint,Tpoint,Heartbeat integer)";
    
    private static final String CREATE_TBL3=" create table "+" Result(_id integer primary key autoincrement,username ,time integer ,Heartbeat integer,心动过速,室性心动过速,心动过缓,室性逸博,房性逸博,心律不齐,室性早搏,房性早搏,交界性早搏,房颤)";
    
    private static final String CREATE_TBL4=" create table "+" Bloodpredb(_id integer primary key autoincrement,username, time integer, Heartbeart,gaoya,diya)";
    private static final String CREATE_TBL5=" create table "+" wendu(_id integer primary key autoincrement,username, time integer,wenduvalue)";
  // public static HashMap<Long, Float> listswendu = new HashMap<Long, Float>();
    
    //private static final String CREATE_TBL5=" create table "+" Bluetoothls(_id integer primary key autoincrement,username,blueid)";
    public SQLiteHelp(Context context) {
        super(context, DB_NAME, null, 1);       
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        
        db.execSQL(CREATE_TBL1);
        db.execSQL(CREATE_TBL2);
        db.execSQL(CREATE_TBL3);
        db.execSQL(CREATE_TBL4);
        db.execSQL(CREATE_TBL5);
        
        //db.execSQL(CREATE_TBL5);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion) {
        // TODO Auto-generated method stub
    }
    
    public boolean addblue(String username,String bluetoothid)
    {
        SQLiteDatabase db=getWritableDatabase();
        String sql = "insert into Bluetoothls(usernmae,blueid) values(?,?)";
        Object obj[]={username,bluetoothid};
        db.execSQL(sql, obj);   
        db.close();
        return true;  
        
    }
    
   /* public List<String> selectbluetoothls(String username)
    {
        Log.i("22222222222", username);
        List<String> listblie = new ArrayList<String>();
        SQLiteDatabase dbblue = this.getReadableDatabase();
        String sql = "select * from Bluetoothls where username=?";
        Cursor cursorbl = dbblue.rawQuery(sql, new String[]{username});
        if (cursorbl.getCount() > 0) 
        {
            cursorbl.moveToNext();
            listblie.add(cursorbl.getString(cursorbl.getColumnIndex("blueid")));
            return listblie;
        }else {
            return null;
        }
        
       
    }*/
    
    /*public boolean delbluetooth(String username,String bluetoothid){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause =  "username=? and time=?";  
        String[] whereArgs = new String[] {username,bluetoothid};  
        db.delete(TBL5_NAME, whereClause, whereArgs); 
        db.close();
        return true;
        
    }*/
    //登陆验证
    public boolean login(String username,String password){
        SQLiteDatabase db=getWritableDatabase();
        String sql="select * from user where username=? and password=?";
        Cursor cursor=db.rawQuery(sql, new String[]{username,password});        
        if(cursor.moveToFirst()==true){
            cursor.close();   
            return true;
   
        }else
        {
            cursor.close();
        return false;
        }
        
    }
    
    
    //登陆注册
    public boolean dup(String username){
        SQLiteDatabase db=getWritableDatabase();
        String sql="select * from user where username=?";
        Cursor cursor=db.rawQuery(sql, new String[]{username});        
        if(cursor.moveToFirst()==true){
            cursor.close();   
            return true;
   
        }else
        {
            cursor.close();
        return false;
        }
        
    }

    //注册（向第一张表中添加数据）
    public boolean register(UserInfo user){
        SQLiteDatabase db=getWritableDatabase();
        String sql="insert into user(username,password,phone,Height,weight,age,sex,userNum) values(?,?,?,?,?,?,?,?)";
        Object obj[]={user.getUserName(),user.getUserPwd(),user.getPhone(),user.getUserHeight(),user.getUseWeight(),user.getUserAge(),user.getUserSex(),user.getUserNum()};
        db.execSQL(sql, obj);   
        db.close();
        return true;
    }
    
    //修改密码（修改第一张表中数据）
    public void pwdxiugai(String name, String pwd2)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cvupdate = new ContentValues();
        cvupdate.put("password", pwd2);
        db.update("user", cvupdate, "username=?", new String[] {name}); 
        db.close();
    }  
    //更新用户信息
    public void userinfoupdate(String name,String phone,String tall,String weight,String sex,String Identity,String age){
        //phone,Height,weight,age,sex,userNum
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cvupdate = new ContentValues();
        cvupdate.put("phone", phone);
        cvupdate.put("Height", tall);
        cvupdate.put("weight", weight);
        cvupdate.put("sex", sex);
        cvupdate.put("age", age);
        cvupdate.put("userNum", Identity);
        db.update("user", cvupdate, "username=?", new String[] {name}); 
        db.close();
    }
    //查找用户信息
    public  List<String> selectuserinfo(String username){
      //phone,Height,weight,age,sex,userNum
        List<String> inforeturn = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select phone,Height, weight,age,sex,userNum from user where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.getCount() > 0) 
        { 
        // 必须使用moveToFirst方法将记录指针移动到第1条记录的位置 
        cursor.moveToNext();
        
        inforeturn.add(cursor.getString(cursor.getColumnIndex("phone")));
        inforeturn.add(cursor.getString(cursor.getColumnIndex("Height")));
        inforeturn.add(cursor.getString(cursor.getColumnIndex("weight")));
        inforeturn.add(cursor.getString(cursor.getColumnIndex("age")));
        inforeturn.add(cursor.getString(cursor.getColumnIndex("sex")));
        inforeturn.add(cursor.getString(cursor.getColumnIndex("userNum")));
        }
        
        return inforeturn;
        
    }
    public void insert_Bloodpre(String name,long time,int heartbeat,int gaoya,int diya)
    {
        SQLiteDatabase db=getWritableDatabase();
        
        String sql = "insert into Bloodpredb(username,time,Heartbeart,gaoya,diya) values (?,?,?,?,?)";
        Object object[] = {name,time,heartbeat,gaoya,diya};
        db.execSQL(sql, object);   
        db.close();
        
    }
    public void insert_Bloodpre1(String name,long time,String wendu)
    {
        SQLiteDatabase db=getWritableDatabase();
        
        String sql = "insert into wendu(username,time,wenduvalue) values (?,?,?)";
        Object object[] = {name,time,wendu};
        Log.e("time", time+"");
        Log.e("2244444444444222222", wendu+"");
        db.execSQL(sql, object);   
        db.close();
        
    } 
    public String select_blood(String name,String time1){
        SQLiteDatabase db=getWritableDatabase();
        String gaoyaString = "";
        String diString = "";
        String sql = "select gaoya,diya from Bloodpredb where username=? and time=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{name,time1});
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            
            gaoyaString = cursor.getString(cursor.getColumnIndex("gaoya"));
            diString = cursor.getString(cursor.getColumnIndex("diya"));
            
            return gaoyaString+"/"+diString;
        }
        //return gaoyaString+"/"+diString;
        return " ";
    }
    public List<String> blood_sel(String name,String timeString){
   //     Log.e("tag1111111",  timeString);
        
        List<String> listbl = new ArrayList<String>();
        SQLiteDatabase dbl = this.getReadableDatabase();
        String sql = "select gaoya,diya,time from Bloodpredb where username=? and time like'"+timeString+"%'"
                + " ORDER BY time ASC" ;
        Cursor cursorbl = dbl.rawQuery(sql, new String[]{name});
        
        
       
        Log.e("tag2222222222",  timeString);
        if (cursorbl.getCount() > 0)
        {
            cursorbl.moveToFirst();
            
            listbl.add(cursorbl.getString(cursorbl.getColumnIndex("gaoya")));
            listbl.add(cursorbl.getString(cursorbl.getColumnIndex("diya")));
            listbl.add(cursorbl.getString(cursorbl.getColumnIndex("time")));
            Log.e("tag333333",  listbl.get(0)+"/"+listbl.get(1));
        }
        
        return listbl;
        
    }
    public List<String> wendu_sel(String name,String timeString){
        //     Log.e("tag1111111",  timeString);
             
             List<String> listbl = new ArrayList<String>();
             SQLiteDatabase dbl = this.getReadableDatabase();
             String sql = "select wenduvalue,time from wendu where username=? and time like'"+timeString+"%'"
                     + " ORDER BY time ASC" ;
             Cursor cursorbl = dbl.rawQuery(sql, new String[]{name});
             Log.e("tag5555555555",  timeString);
             if (cursorbl.getCount() > 0)
             {
                 cursorbl.moveToFirst();
                 
                 listbl.add(cursorbl.getString(cursorbl.getColumnIndex("wenduvalue")));
                 listbl.add(cursorbl.getString(cursorbl.getColumnIndex("time")));
                 Log.e("tag66666666666",  listbl.get(0));
             }
             return listbl;
             
         }  
    
//向第二张表中添加数据 
    public void insert_Diagnosis(String name,long time,String Rpoint ,String Qpoint,String Q1point,String Spoint,String S2point,String Ppoint,String Tpoint,int Heartbeat)
    {

        SQLiteDatabase db=getWritableDatabase();
        
        String sql="insert into Diagnosis(username,time,Rpoint,Qpoint,Q1point,Spoint,S2point,Ppoint,Tpoint,Heartbeat) values(?,?,?,?,?,?,?,?,?,?)";
        Object obj[]={name,time,Rpoint,Qpoint,Q1point,Spoint,S2point,Ppoint,Tpoint,Heartbeat};
        db.execSQL(sql, obj);   
        db.close();
    }
    
    
    //从第二张表中查找
    public Cursor select(String sql,String[] Condition){  

        SQLiteDatabase db = this.getReadableDatabase();
       
        Cursor cursor = db.rawQuery(sql, Condition);
        return cursor;  
    }
    
   
    
  //向第三张表中添加数据 
    public void insert_Result(String name,long time,int Heartbeat,int R1 ,int R2,int R3,int R4,int R5,int R6,int R7,int R8,int R9,int R10)
    {

        SQLiteDatabase db=getWritableDatabase();
        String sql="insert into Result(username,time,Heartbeat,心动过速,室性心动过速,心动过缓,室性逸博,房性逸博,心律不齐,室性早搏,房性早搏,交界性早搏,房颤) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object obj[]={name,time,Heartbeat,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10};
        db.execSQL(sql, obj);   
        db.close();
    }
    
    
    
    //删除第二张第三张表中数据
    public void deletevalue(String name,String time )
    {
        SQLiteDatabase db=getWritableDatabase();
        String whereClause =  "username=? and time=?";  
        String[] whereArgs = new String[] {name,time};  
        db.delete(TBL2_NAME, whereClause, whereArgs);  
        db.delete(TBL3_NAME, whereClause, whereArgs); 
        db.delete(TBL6_NAME, whereClause, whereArgs); 
        db.close();
    }


    public List<String> Findblood(String time[], String uname, int i){
        
        List<String> lsi = new ArrayList<String>();
      
        SQLiteDatabase db1 = this.getReadableDatabase();
        String sql = "select wenduvalue,time from wendu where username=? and time like'"+time[i]+"%'"
                + " ORDER BY time ASC" ;
       
        //String sql = "select gaoya,diya, from Bloodpredb where username=? and time like'"+time[i]+"%' and gaoya = (select MAX(gaoya) from Bloodpredb) ORDER BY time ASC";
        Cursor cursorsCu = db1.rawQuery(sql, new String[]{uname});
        System.out.println( time[i]); 
        if (cursorsCu.getCount() > 0) 
        {
            cursorsCu.moveToNext();
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue")));
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
       
           
            
            //System.out.println( "SQLADY:"+cursorsCu.getString(cursorsCu.getColumnIndex("gaoya"))+"/"+cursorsCu.getString(cursorsCu.getColumnIndex("diya"))+"/"+cursorsCu.getString(cursorsCu.getColumnIndex("time"))); 
        }
        
        //db1.close();
        return lsi;
        
    } 

    public List<String> Findwendu(String time, String uname){
     //   Log.e("time[i]222222222", time[i]+"");
        List<String> lsi = new ArrayList<String>();
      
        SQLiteDatabase db1 = this.getReadableDatabase();
      /*  String sql = "select wenduvalue,time from wendu where username=? and time like'"+time[i]+"%'"
                + " ORDER BY time ASC" ;*/
        String sql = "select wenduvalue,time from wendu where username=? and time like'"+time+"%'"
                + " ORDER BY time ASC" ;
        //String sql = "select gaoya,diya, from Bloodpredb where username=? and time like'"+time[i]+"%' and gaoya = (select MAX(gaoya) from Bloodpredb) ORDER BY time ASC";
        Cursor cursorsCu = db1.rawQuery(sql, new String[]{uname});
        Log.e("tag", lsi.size()+"");
    //   cursorsCu.getColumnCount();    
     //  System.out.println( cursorsCu.getColumnCount()); 
   //     System.out.println( time[i]+"nnnnnnnnnnn"); 
      if (cursorsCu.getCount() > 0) 
        {
            cursorsCu.moveToFirst(); 
         
        }while(cursorsCu.moveToNext())
        {
            
            
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue")));
            //  lsi.add(cursorsCu.getString(cursorsCu.getColumnIndexOrThrow("wendu")));
              lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
            
         /*     long date = Long.parseLong(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
              float WENDU = cursorsCu.getInt(cursorsCu.getColumnIndex("wenduvalue"));
              listswendu.put(date, WENDU);
              System.out.println(WENDU+"22222222222222222");*/
             System.out.println( "SQLADY:"+cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue"))); 
          //光标移动成功
          //把数据取出
          }
     /*   if(cursorsCu.moveToFirst()){
            
                         do{
                                 //遍历Cursor对象，取出数据并打印
                               String wendu=cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue"));
                               String time1=cursorsCu.getString(cursorsCu.getColumnIndex("time"));
                               Log.d("MainActivity11111","wendu is"+wendu);
                                Log.d("MainActivity11111","time is"+time1);
                             }while(cursorsCu.moveToNext());
                            }      */
   /*    String ssString= lsi.get(10);
       List<String> lsii = new ArrayList<String>();
      lsii.add(ssString);
       db1.close();
     */
        return lsi;
        
    } 
    public List<String> Findwendu2(String time,String uname){
        //   Log.e("time[i]222222222", time[i]+"");
           List<String> lsi = new ArrayList<String>();
         
           SQLiteDatabase db1 = this.getReadableDatabase();
         /*  String sql = "select wenduvalue,time from wendu where username=? and time like'"+time[i]+"%'"
                   + " ORDER BY time ASC" ;*/
           String sql = "select wenduvalue,time from wendu where username=? and time like'"+time+"%'"
                   + " ORDER BY time ASC" ;
           //String sql = "select gaoya,diya, from Bloodpredb where username=? and time like'"+time[i]+"%' and gaoya = (select MAX(gaoya) from Bloodpredb) ORDER BY time ASC";
           Cursor cursorsCu = db1.rawQuery(sql, new String[]{uname});
         //  Log.e("tag22222", lsi.size()+"");
         if (cursorsCu.getCount() > 0) 
           {
               cursorsCu.moveToFirst(); 
            
           }while(cursorsCu.moveToNext())
           {
               lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue")));
            //   lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
            /*     long date = Long.parseLong(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
                 float WENDU = cursorsCu.getInt(cursorsCu.getColumnIndex("wenduvalue"));
                 listswendu.put(date, WENDU);
                 System.out.println(WENDU+"22222222222222222");*/
                System.out.println( "WENDUZHI:"+cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue"))); 

             }
   
           return lsi;
           
       } 
    public List<String> Findwendu3(String time,String uname){
        //   Log.e("time[i]222222222", time[i]+"");
           List<String> lsi = new ArrayList<String>();
         
           SQLiteDatabase db1 = this.getReadableDatabase();
         /*  String sql = "select wenduvalue,time from wendu where username=? and time like'"+time[i]+"%'"
                   + " ORDER BY time ASC" ;*/
           String sql = "select wenduvalue,time from wendu where username=? and time like'"+time+"%'"
                   + " ORDER BY time ASC" ;
           //String sql = "select gaoya,diya, from Bloodpredb where username=? and time like'"+time[i]+"%' and gaoya = (select MAX(gaoya) from Bloodpredb) ORDER BY time ASC";
           Cursor cursorsCu = db1.rawQuery(sql, new String[]{uname});
         //  Log.e("tag22222", lsi.size()+"");
         if (cursorsCu.getCount() > 0) 
           {
               cursorsCu.moveToFirst(); 
            
           }while(cursorsCu.moveToNext())
           {
             //  lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue")));
               lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
            /*     long date = Long.parseLong(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
                 float WENDU = cursorsCu.getInt(cursorsCu.getColumnIndex("wenduvalue"));
                 listswendu.put(date, WENDU);
                 System.out.println(WENDU+"22222222222222222");*/
            //    System.out.println( "WENDUZHI:"+cursorsCu.getString(cursorsCu.getColumnIndex("wenduvalue"))); 

             }
   
           return lsi;
           
       } 
                 
    public List<String> getAllPoints(String time[], String uname, int i) {  
        String sql = "select wenduvalue,time from wendu where username=? and time like'"+time[i]+"%'"
                + " ORDER BY time ASC" ;
        SQLiteDatabase db = this.getWritableDatabase();  
        List<String> pointList = new ArrayList<String>();  
        Cursor cursor = db.rawQuery(sql, null);  
        while (cursor.moveToNext()) {  
            pointList.add(cursor.getString(cursor.getColumnIndex("wenduvalue")));
            //  lsi.add(cursorsCu.getString(cursorsCu.getColumnIndexOrThrow("wendu")));
            pointList.add(cursor.getString(cursor.getColumnIndex("time")));
             
        }  
        return pointList;  
    }  
    
 public List<String> Findblood1(String time[], String uname, int i){
        
        List<String> lsi = new ArrayList<String>();
      
        SQLiteDatabase db1 = this.getReadableDatabase();
        String sql = "select gaoya,diya,time from Bloodpredb where username=? and time like'"+time[i]+"%'"
                + " ORDER BY time ASC" ;
       
        //String sql = "select gaoya,diya, from Bloodpredb where username=? and time like'"+time[i]+"%' and gaoya = (select MAX(gaoya) from Bloodpredb) ORDER BY time ASC";
        Cursor cursorsCu = db1.rawQuery(sql, new String[]{uname});
        System.out.println( time[i]); 
        if (cursorsCu.getCount() > 0) 
        {
            cursorsCu.moveToNext();
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("gaoya")));
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("diya")));
            lsi.add(cursorsCu.getString(cursorsCu.getColumnIndex("time")));
           
            
            //System.out.println( "SQLADY:"+cursorsCu.getString(cursorsCu.getColumnIndex("gaoya"))+"/"+cursorsCu.getString(cursorsCu.getColumnIndex("diya"))+"/"+cursorsCu.getString(cursorsCu.getColumnIndex("time"))); 
        }
        
        //db1.close();
        return lsi;
        
    } 
    //从第三张表中取出结果
    public List<String> FindfromSQLite(String nAME, String time) {
        // TODO Auto-generated method stub
        List<String> resultreturn=new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select Heartbeat,心动过速,室性心动过速,心动过缓,室性逸博,房性逸博,心律不齐,室性早搏,房性早搏,交界性早搏,房颤 from Result where username=? and time=?"; 
        Cursor cursor = db.rawQuery(sql, new String[]{nAME,time});
        
        if (cursor.getCount() > 0) 
        { 
        // 必须使用moveToFirst方法将记录指针移动到第1条记录的位置 
        cursor.moveToFirst(); 
        resultreturn.add(cursor.getString(cursor.getColumnIndex("心动过速"))); 
        resultreturn.add(cursor.getString(cursor.getColumnIndex("室性心动过速")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("心动过缓")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("室性逸博")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("房性逸博")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("心律不齐")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("室性早搏")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("房性早搏")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("交界性早搏")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("房颤")));
        resultreturn.add(cursor.getString(cursor.getColumnIndex("Heartbeat")));
        }
        
        return resultreturn;
    }
}
