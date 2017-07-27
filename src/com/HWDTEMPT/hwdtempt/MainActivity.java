package com.HWDTEMPT.hwdtempt;


import com.HWDTEMPT.hwdtempt.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;




@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

	private TabHost tabHost;
	public static boolean mflag;
	public static String NAME;
	public static String xuexaVa = "";

	private Class<?> activitys[] = { ChoiceHome.class, App2.class,bloodknowledge.class,App5.class };
	private String title[] = { "检测", "历史", "指导", "设置" };
    private int image[] = {R.drawable.course_selctor,R.drawable.found_selector,R.drawable.yiliao_selector,R.drawable.setting_selected,};
	@SuppressWarnings("deprecation")
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		
		Intent intent = getIntent();

		mflag = intent.getBooleanExtra("flag", false);
		NAME = intent.getStringExtra("username");	
		xuexaVa = intent.getStringExtra("xueyast");//血压值
		initTabView();
	}

	@SuppressWarnings("deprecation")
    private void initTabView() {
		
	
		// 添加tabhost
		this.tabHost = (TabHost) findViewById(R.id.mytabhost);
	
		tabHost.setup(this.getLocalActivityManager());

		
		for (int i = 0; i < activitys.length; i++) {

			
			View view = View.inflate(this, R.layout.tab_layout, null);

			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			imageView.setImageDrawable(getResources().getDrawable(image[i]));			
			TextView textView = (TextView) view.findViewById(R.id.title);
			textView.setText(title[i]);
			textView.setTextSize(18);
			
			Intent intent = new Intent(this, activitys[i]);
			
			TabSpec spec = tabHost.newTabSpec(title[i]).setIndicator(view).setContent(intent);
			tabHost.addTab(spec);
		}

	}

}