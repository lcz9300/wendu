
package com.HWDTEMPT.hwdtempt;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Face;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.model.UserInfo;
import com.HWDTEMPT.view.BloodOx_view;
import com.huicheng.ui.BluetoothLeService;
import com.HWDTEMPT.widget.CircularImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ChoiceHome extends Activity implements android.view.View.OnClickListener {
    private long exitTime = 0;
    private ImageView ecgimg, bloodimg, xueyangimg, xuetangimg, imagpop,btn_quit;
    private TextView nametxt, photograph, albums;
    private TextView xuyatxtview, xueyangtxtview, xuetangtxtview, ecgtxtView;
    private LinearLayout cancel;
  //  private Button btn_quit;
    private View _homeView,BighomeView;
    private LayoutInflater layoutInflater;
    PopupMenu popupMenu = null;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile = null;
    private int screenWidth,screenHeight;
    private CircularImage cover_user_photo;  

    private SharedPreferences spPreferences;
    private Bitmap bitmap = null;
    private PopupWindow popWindow;

    public static boolean mflagCH;
    public static String NAMECH;
    public static boolean xindiankai1 = false;
    public static boolean xueyakai1 = false;
    // 文件保存目录
    private File saveCatalog;
    // 保存的文件
    private File saveFile;
    // 文件保存目录字符串
    private String path = Environment.getExternalStorageDirectory().getPath() + "/BMDBT" + "/IMG";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = this.getLayoutInflater();
        _homeView = inflater.inflate(R.layout.shouye, null);
        BighomeView=inflater.inflate(R.layout.bigshouye, null);
    /*    
        setContentView(_homeView);
        setContentView(BighomeView);*/
        
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        StaticValue.surfaceviewWidth = localDisplayMetrics.widthPixels;
        screenWidth = StaticValue.surfaceviewWidth;   
        if (screenWidth > 1500) {
            setContentView(BighomeView);
        }else {
            setContentView(_homeView);
        }
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
         * Intent intentshou = getIntent(); NAMECH =
         * intentshou.getStringExtra("username");
         */
        NAMECH = MainActivity.NAME;

        // mflagCH = intent.getBooleanExtra("flag", false);
        nametxt = (TextView) findViewById(R.id.relative_shouye_txt);
        nametxt.setText("用户:" + NAMECH);

        // 创建文件保存目录
        saveCatalog = new File(path);
        if (!saveCatalog.exists()) {
            // 创建目录
            saveCatalog.mkdirs();
        }
        // ---------------------------------------------------
        cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
        File pathfile = new File(Environment.getExternalStorageDirectory() + "//"
                + "BMDBT" + "//" + "IMG" + "//" + "image.jpg");

        if (pathfile.exists()) {
            Bitmap _bitma = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    + "//"
                    + "BMDBT" + "//" + "IMG" + "//" + "image.jpg");

            cover_user_photo.setImageBitmap(_bitma);
            cover_user_photo.setOnClickListener(this);

        } else {
            cover_user_photo.setImageResource(R.drawable.face);
            cover_user_photo.setOnClickListener(this);
        }

        // ---------------------------------------------------------
        inition();

    }

    public void gallery(View view) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(ChoiceHome.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {

                bitmap = data.getParcelableExtra("data");

                // this.mFace.setImageBitmap(bitmap);
                saveFile = new File(saveCatalog, "image.jpg");
                // 创建保存图片文件File
                if (saveFile.exists()) {
                    saveFile.delete();
                }
                if (bitmap != null) {
                    // 压缩图片
                    try {
                        // 创建FileOutputStream对象
                        FileOutputStream fos = new FileOutputStream(saveFile);
                        // 开始压缩图片
                        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                            fos.flush();
                            // 关闭流对象
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Message message = new Message();
                message.what = 1;
                mainHandler.sendMessage(message);

                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪切图片
     * 
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void inition() {
    //    ecgimg = (ImageView) findViewById(R.id.relative_xindian_img);
        bloodimg = (ImageView) findViewById(R.id.releative_xueyaxindian_img);
        // xuetangimg = (ImageView)findViewById(R.id.xuetang_img);
        // xueyangimg = (ImageView)findViewById(R.id.xueyang_img);

        xuyatxtview = (TextView) this.findViewById(R.id.releative_xueyaxindian_txt);
        // xueyangtxtview = (TextView)this.findViewById(R.id.xueyang_txt);
        // xuetangtxtview = (TextView)this.findViewById(R.id.xuetang_txt);
   //     ecgtxtView = (TextView) this.findViewById(R.id.relative_xindian_txt);
/*
        btn_quit = (ImageView) this.findViewById(R.id.quit_btn_shouye);
        btn_quit.setOnClickListener(this);*/
        imagpop = (ImageView) findViewById(R.id.imag_pop);
        imagpop.setOnClickListener(this);

    //    ecgimg.setOnClickListener(this);
        bloodimg.setOnClickListener(this);
        // xueyangimg.setOnClickListener(this);
        // xueyangimg.setBackground(getResources().getDrawable(R.drawable.xueyang_hui));
        // xueyangtxtview.setTextColor(getResources().getColor(R.color.table_background));

        // xuetangimg.setBackground(getResources().getDrawable(R.drawable.xuetang_hui));
        // xuetangtxtview.setTextColor(getResources().getColor(R.color.table_background));
        // 设置四个按钮的可选功能开通-----------------------------
        /*
         * spPreferences = getSharedPreferences("hwdinfo", MODE_PRIVATE); String
         * type = spPreferences.getString("type", ""); if
         * (type.equals("HWD-OB01")) { ecgimg.setOnClickListener(this);
         * bloodimg.
         * setBackground(getResources().getDrawable(R.drawable.xueya_hui));
         * xuetangimg
         * .setBackground(getResources().getDrawable(R.drawable.xuetang_hui));
         * xueyangimg
         * .setBackground(getResources().getDrawable(R.drawable.xueyang_hui));
         * xuyatxtview
         * .setTextColor(getResources().getColor(R.color.table_background));
         * xueyangtxtview
         * .setTextColor(getResources().getColor(R.color.table_background));
         * xuetangtxtview
         * .setTextColor(getResources().getColor(R.color.table_background));
         * }else if (type.equals("HWD-BPM01")) {
         * bloodimg.setOnClickListener(this); ecgimg.setOnClickListener(this);
         * xuetangimg
         * .setBackground(getResources().getDrawable(R.drawable.xuetang_hui));
         * xueyangimg
         * .setBackground(getResources().getDrawable(R.drawable.xueyang_hui));
         * xueyangtxtview
         * .setTextColor(getResources().getColor(R.color.table_background));
         * xuetangtxtview
         * .setTextColor(getResources().getColor(R.color.table_background));
         * }else if (type.equals("HWD-MBSS01")) {
         * ecgimg.setOnClickListener(this); bloodimg.setOnClickListener(this);
         * xuetangimg.setOnClickListener(this);
         * xueyangimg.setOnClickListener(this); }
         */
        /*
         * bloodimg.setOnClickListener(this);
         * xuetangimg.setOnClickListener(this);
         * xueyangimg.setOnClickListener(this);
         */
        // ---------------------------------------------
    }

    @SuppressLint("HandlerLeak")
    private Handler mainHandler = new Handler()
    {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (bitmap != null) {
                        cover_user_photo.setImageBitmap(bitmap);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.quit_btn_shouye:
                this.finish();
                System.exit(0);
                break;*/
        /*    case R.id.relative_xindian_img:// 心电
                Intent inttent = new Intent();
                inttent.setClass(ChoiceHome.this, bleactivty.class);
                inttent.putExtra("flag", true);
                inttent.putExtra("username", NAMECH);
                startActivity(inttent);
                xindiankai1 = true;
                Log.e("xindiankaishi1111111111", "bbbbbbbbbbbbbb");
                // this.finish();
                break;*/
            case R.id.releative_xueyaxindian_img:// 血压心电
                Intent intentxy = new Intent();
                intentxy.setClass(ChoiceHome.this, bloodactivity.class);
                // intent.putExtra("flag", true);
                intentxy.putExtra("username", NAMECH);
                startActivity(intentxy);
                xueyakai1 = true;
                // this.finish();
                break;
         /*   case R.id.xuetang_img:// 血糖
                break;*/
            /*
             * case R.id.xueyang_img://血氧 Intent intentxueyang = new Intent();
             * intentxueyang.setClass(ChoiceHome.this,xueyangActivity.class);
             * //intentxueyang.putExtra("flag", true);
             * intentxueyang.putExtra("username", NAMECH);
             * startActivity(intentxueyang); //this.finish(); break;
             */
            case R.id.cover_user_photo:// 激活系统图库，选择一张图片
                showPopupWindow(cover_user_photo);
                /*
                 * Intent intentp = new Intent(Intent.ACTION_PICK);
                 * intentp.setType("image/*"); startActivityForResult(intentp,
                 * PHOTO_REQUEST_GALLERY);
                 */
                break;
            case R.id.albums:
                popWindow.dismiss();
                Intent intentp1 = new Intent(Intent.ACTION_PICK);
                intentp1.setType("image/*");
                startActivityForResult(intentp1, PHOTO_REQUEST_GALLERY);
                break;
            case R.id.photograph:
                popWindow.dismiss();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                }
                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
                break;
            case R.id.cancel:
                popWindow.dismiss();
                break;
            case R.id.imag_pop:
                pop_menu(imagpop);
                break;

            case R.id.btn_poto2:

                // 从相机获取

                Intent intentpp = new Intent("android.media.action.IMAGE_CAPTURE");
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    intentpp.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                }
                startActivityForResult(intentpp, PHOTO_REQUEST_CAMERA);
                break;
            default:
                break;
        }

    }

    private void showPopupWindow(CircularImage parent) {
        // TODO Auto-generated method stub
        if (popWindow == null) {
            View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                    true);
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void initPop(View view) {
        photograph = (TextView) view.findViewById(R.id.photograph);// 拍照
        albums = (TextView) view.findViewById(R.id.albums);// 相册
        cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消
        photograph.setOnClickListener(this);
        albums.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    private void pop_menu(View imgView) {
        popupMenu = new PopupMenu(this, imgView);
        getMenuInflater().inflate(R.menu.popup_lishi_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.per:
                        Intent intent2 = new Intent();
                        intent2.putExtra("username", NAMECH);
                        intent2.setClass(ChoiceHome.this, PersonInfo.class);
                        startActivity(intent2);
                        // finish();
                        break;
                    case R.id.buy:
                        Intent intentbuy = new Intent();
                        intentbuy.putExtra("username", NAMECH);
                        intentbuy.putExtra("flag", "flag");
                        intentbuy.setClass(ChoiceHome.this, BuyActivity.class);
                        startActivity(intentbuy); 
                        // finish();
                        break;
     /*               case R.id.shebeitype:
                        Intent intentshebei = new Intent();
                        intentshebei.putExtra("username", NAMECH);
                        intentshebei.putExtra("flag", "ch");
                        intentshebei.setClass(ChoiceHome.this, bandbluedev.class);
                        startActivity(intentshebei);
                        finish();
                        break;*/
                    case R.id.site:
                        Intent intentsite = new Intent();
                        intentsite.putExtra("username", NAMECH);
                        intentsite.setClass(ChoiceHome.this, BuyActivity.class);
                        startActivity(intentsite);
                        // finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT)
                        .show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
