package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class bloodknowledge extends Activity {
    long exitTime=0;
    private View view1,ViewScree,view29,view30,view31,view32;
    Button bt31,bt32,bt33;
    private ImageView mImageView;
    private int screenWidth,screenHeight;
    public static boolean fanhuixy2=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodjieshi);
 
/*     mImageView =(ImageView)findViewById(R.id.mingci_imageview_gohome1);
        
        mImageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            
                fanhuixy2=true;
                if (screenWidth > 1500) {
                    finish();
                   // System.exit(0);
                }else {
                    finish();
                   // System.exit(0);
                }
            }
        });   */ 
        
        Log.i("width/height", screenWidth +"::"+ screenHeight + "");

  /*   
        bt31 =(Button) findViewById(R.id.Button_xueya);
        bt32=(Button) findViewById(R.id.buttonxueyajieshi);
        bt33=(Button) findViewById(R.id.Buttonxueyashuoming);
        
     
        bt31.setOnClickListener(new mButtonListenser());
        bt32.setOnClickListener(new mButtonListenser());
        bt33.setOnClickListener(new mButtonListenser());*/
}
    class mButtonListenser implements OnClickListener{
       
        public void onClick(View v){
            
            switch (v.getId()) {
       /*
                case R.id.Button_xueya:
                    setContentView(R.layout.bloodlayout);
                    back1();
                    break; 
                case R.id.buttonxueyajieshi:
                    setContentView(R.layout.xueyachangshi);
                    back1();
                    break; 
                case R.id.Buttonxueyashuoming:
                    setContentView(R.layout.xueyayibangzhu);
                    back1();
                    break; 
                default:
                    break;*/
            }
        }
        
    }

   
/*    public void back1()
    {
        mImageView =(ImageView)findViewById(R.id.mingci_imageview_gohome1);
        
        mImageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (screenWidth > 1500) {
                    finish();
                    System.exit(0);
                }else {
                    finish();
                    System.exit(0);
                }
            }
        });
       
    }
   */
   
    
    
    
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
    
           if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
           {  
               if((System.currentTimeMillis()-exitTime) > 2000){  
                   Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
                   exitTime = System.currentTimeMillis();  
               } else {  
                 fanhuixy2=true;
                   finish();
                 //   System.exit(0);
               }  
               return true;  
           }  
           return super.onKeyDown(keyCode, event);  
        }

}