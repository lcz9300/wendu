package com.HWDTEMPT.hwdtempt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;




public class GuideActivity extends Activity {
	// 定义ViewPager对象
	private ViewPager viewPager;

	// 定义ViewPager适配�?
	private ViewPagerAdapter vpAdapter;

	// 定义�?个ArrayList来存放View
	private ArrayList<View> views;

	//定义各个界面View对象
	private View view1,view2,view3,view4;
	
	// 定义底部小点图片
	private ImageView pointImage0, pointImage1, pointImage2, pointImage3;

	//定义�?始按钮对�?
	private Button startBt;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);

		initView();

		initData();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		//实例化各个界面的布局对象 
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);
		view4 = mLi.inflate(R.layout.guide_view04, null);
				
		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		// 实例化ArrayList对象
		views = new ArrayList<View>();
		//将要分页显示的View装入数组	
				views.add(view1);
				views.add(view2);
				views.add(view3);
				views.add(view4);
		// 实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);

		// 实例化底部小点图片
		pointImage0 = (ImageView) findViewById(R.id.page0);
		pointImage1 = (ImageView) findViewById(R.id.page1);
		pointImage2 = (ImageView) findViewById(R.id.page2);
		pointImage3 = (ImageView) findViewById(R.id.page3);
	
		
		//实例化开始按钮
		startBt = (Button) view4.findViewById(R.id.startBtn);
	}

	/**
	 * 初始化数�?
	 */
	private void initData() {
		// 设置监听
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 设置适配器
		viewPager.setAdapter(vpAdapter);

		
	
		
						
		// 给开始按钮设置监听
		startBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    
			  
			    
				 startbutton();
				 
			}
		});

	}

	
	public class ViewPagerAdapter extends PagerAdapter {
		
		//界面列表
	    private ArrayList<View> views;
	    
	    public ViewPagerAdapter (ArrayList<View> views){
	        this.views = views;
	    }
	       
		/**
		 * 获得当前界面
		 */
		@Override
		public int getCount() {
			 if (views != null) {
	             return views.size();
	         }      
	         return 0;
		}

		/**
		 * 初始化position位置的界面
		 */
	    @Override
	    public Object instantiateItem(View view, int position) {
	       
	        ((ViewPager) view).addView(views.get(position), 0);
	       
	        return views.get(position);
	    }
	    
	    /**
		 * 判断是否由对象生成界面
		 */
		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return (view == arg1);
		}

		/**
		 * 销毁position位置的界面
		 */
	    @Override
	    public void destroyItem(View view, int position, Object arg2) {
	        ((ViewPager) view).removeView(views.get(position));       
	    }

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
	    
	}
	
	
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 1:
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 2:
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 3:
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));			
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
		
			
			}
		
		
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	
	/**
	 * 相应按钮点击事件
	 */
	private void startbutton() {  
      	Intent intent = new Intent();
		intent.setClass(GuideActivity.this,GuideViewDoor.class);
		startActivity(intent);
		this.finish();
      }
	
}
