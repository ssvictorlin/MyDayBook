package ubicomp.mydaybook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ubicomp.mydaybook.linechart.App;
import ubicomp.mydaybook.linechart.ChartCaller;
import ubicomp.mydaybook.linechart.LineChartTitle;
import ubicomp.mydaybook.linechart.LineChartView;
import ubicomp.mydaybook.data.Database;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ChartCaller {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private LinearLayout diaryList, boxesLayout, drawerContent;
	private RelativeLayout upperBarContent;
	private TextView titleText, backToTodayText;
	private View diaryItem;
	
	@SuppressWarnings("deprecation")
	private SlidingDrawer drawer;
	private ImageView toggle, toggle_linechart, linechartIcon, calendarIcon;
	private Context context;
	private Database myConstant;
		
	private static int sv_item_height;
	
	private int fragmentIdx;
	
	public int selectedDay, selectedMonth;
	
	private int chart_type = 2;
	private LinearLayout chartAreaLayout;
	private LineChartView lineChart;
	private LineChartTitle chartTitle;
	private ChartCaller caller;
	
	public View lineChartBar, lineChartView, lineChartFilter, calendarBar, calendarView, filterView;
	
	private ImageView filterAll, filter1, filter2, filter3, filter4, filter5, filter6, filter7, filter8;
	public ImageView lineChartFilterButton, calendarFilterButton, rotateLineChart;
	
	public static boolean[] filterButtonIsPressed = {true, false, false, false, false, false, false, false, false};
	
	private boolean isFilterIsOpen = false;
	private boolean isRotated = false;
	
	private int drawerHeight = App.getContext().getResources().getDimensionPixelSize(R.dimen.drawer_normal_height);
	private int drawerHeightWithFilter = App.getContext().getResources().getDimensionPixelSize(R.dimen.drawer_with_filter_height);
	private int filterHeight = App.getContext().getResources().getDimensionPixelSize(R.dimen.filter_normal_height);
	private int filterHeightLandscape = App.getContext().getResources().getDimensionPixelSize(R.dimen.filter_landscape_height);
	//public static List<Integer> filterList = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		caller = this;
		
		drawerContent = (LinearLayout) findViewById(R.id.drawer_content);
		upperBarContent = (RelativeLayout) findViewById(R.id.upper_bar);
		LayoutInflater inflater = LayoutInflater.from(context);
		calendarView = (View) inflater.inflate(R.layout.calendar_main, null, false);
		calendarBar = (View) inflater.inflate(R.layout.calendar_upperbar, null, false);
		
		drawerContent.addView(calendarView);
		upperBarContent.addView(calendarBar);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());  
		myConstant = new Database();
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);  
		mViewPager.setAdapter(mSectionsPagerAdapter);	
		
		// Initialize the selectedDay and selectedMonth
		selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		selectedMonth = Calendar.getInstance().get(Calendar.MONTH);
		
		backToTodayText = (TextView) findViewById(R.id.back_to_today);
		backToTodayText.setText(Integer.toString(selectedDay));
		
		titleText = (TextView) findViewById(R.id.month_text);	
		drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		toggle = (ImageView) findViewById(R.id.toggle);
		linechartIcon = (ImageView) findViewById(R.id.linechart_icon);
	
		lineChartBar = (View) inflater.inflate(R.layout.linechart_upperbar, null, false);
		lineChartView = (View) inflater.inflate(R.layout.linechart_main, null, false);
		lineChartFilter = (View) inflater.inflate(R.layout.linechart_filter, null, false);
		
		rotateLineChart = (ImageView) lineChartBar.findViewById(R.id.rotate_button);
		
	    calendarIcon = (ImageView) lineChartBar.findViewById(R.id.back_to_calendar);
	    toggle_linechart = (ImageView) lineChartBar.findViewById(R.id.toggle_linechart);
	    
	    filterAll = (ImageView) lineChartFilter.findViewById(R.id.filter_all);
	    filter1 = (ImageView) lineChartFilter.findViewById(R.id.filter_1);
	    filter2 = (ImageView) lineChartFilter.findViewById(R.id.filter_2);
	    filter3 = (ImageView) lineChartFilter.findViewById(R.id.filter_3);
	    filter4 = (ImageView) lineChartFilter.findViewById(R.id.filter_4);
	    filter5 = (ImageView) lineChartFilter.findViewById(R.id.filter_5);
	    filter6 = (ImageView) lineChartFilter.findViewById(R.id.filter_6);
	    filter7 = (ImageView) lineChartFilter.findViewById(R.id.filter_7);
	    filter8 = (ImageView) lineChartFilter.findViewById(R.id.filter_8);
	    
	    filterAll.setOnClickListener(new FilterListener());
	    filter1.setOnClickListener(new FilterListener());
	    filter2.setOnClickListener(new FilterListener());
	    filter3.setOnClickListener(new FilterListener());
	    filter4.setOnClickListener(new FilterListener());
	    filter5.setOnClickListener(new FilterListener());
	    filter6.setOnClickListener(new FilterListener());
	    filter7.setOnClickListener(new FilterListener());
	    filter8.setOnClickListener(new FilterListener());	    

		showDiary();
				
		drawer.toggle();
		
		setCurrentCalendarPage(selectedMonth + 1 - myConstant.START_MONTH);
		titleText.setText( (selectedMonth + 1)  + "月");
		
		toggle_linechart.setOnClickListener(new ToggleListener());
		toggle.setOnClickListener(new ToggleListener());
		titleText.setOnClickListener(new ToggleListener());
	
		linechartIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFilterIsOpen == false) {				
					drawerContent.removeAllViews();
					upperBarContent.removeAllViews();
					drawerContent.addView(lineChartView);
					upperBarContent.addView(lineChartBar);
				}
				else {
					drawerContent.removeAllViews();
					upperBarContent.removeAllViews();
					drawerContent.addView(lineChartFilter);
					setFilterSize();
					drawerContent.addView(lineChartView);
					upperBarContent.addView(lineChartBar);
				}
				if  (!drawer.isOpened()) { drawer.toggle();}
				
				lineChart = (LineChartView) findViewById(R.id.lineChart);
		        lineChart.setChartData(getRandomData());
		        lineChart.requestLayout();
		        lineChart.getLayoutParams().width = 2200;
		        
		        chartTitle = (LineChartTitle) findViewById(R.id.chart_title);
		        chartTitle.setting(caller);
		        setChartType(2);
		        
		        chartAreaLayout = (LinearLayout) findViewById(R.id.linechart_tabs);
		        chartAreaLayout.setBackgroundResource(R.drawable.linechart_bg);
				
			}
		});
		
		calendarIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("OMG", "hi");
				if (isFilterIsOpen == false) {
					drawerContent.removeAllViews();
					upperBarContent.removeAllViews();
					
					drawerContent.addView(calendarView);
					upperBarContent.addView(calendarBar);
					if  (!drawer.isOpened()) { drawer.toggle();}
				}
				else {
					drawerContent.removeAllViews();
					upperBarContent.removeAllViews();
					
					drawerContent.addView(lineChartFilter);
					setFilterSize();
					
					drawerContent.addView(calendarView);
					upperBarContent.addView(calendarBar);
					if  (!drawer.isOpened()) { drawer.toggle(); }
				}
				
			}
		});
						
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				toggle.setImageResource(R.drawable.dropup_arrow);
				toggle_linechart.setImageResource(R.drawable.dropup_arrow);
			}
		});
		
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				toggle.setImageResource(R.drawable.dropdown_arrow);
				toggle_linechart.setImageResource(R.drawable.dropdown_arrow);
				
			}
		});
			
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageSelected(int arg0) {
				fragmentIdx = arg0;
				titleText.setText( (myConstant.START_MONTH + fragmentIdx) + "月");
			}
			
		});
		 rotateLineChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isRotated) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					isRotated = false;
				}
				else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					isRotated = true;
				}
				
			}
		});
				
		lineChartFilterButton = (ImageView) lineChartBar.findViewById(R.id.line_chart_filter);
		calendarFilterButton = (ImageView) calendarBar.findViewById(R.id.calendar_filter);
		lineChartFilterButton.setOnClickListener(new FilterButtonListener());
		calendarFilterButton.setOnClickListener(new FilterButtonListener());
		
	}

	private float[] getRandomData() {
        return new float[] { 0, -3, 1, -2, -1, -3, 3, 2, 0, 1, -2, -1, 2, -2, 0, 1, -3, -1, 2, -1, 1, 3};
    }
	
	public void setChartType(int type) {
		chart_type = type;
		switch (chart_type) {
		case 0:
			chartTitle.setBackgroundResource(R.drawable.tab1_pressed);
			break;
		case 1:
			chartTitle.setBackgroundResource(R.drawable.tab2_pressed);
			break;
		case 2:
			chartTitle.setBackgroundResource(R.drawable.tab3_pressed);
			break;		
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    
	    LayoutParams lp = new LayoutParams(drawer.getLayoutParams());
    	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth(); 
    	int height = display.getHeight();

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {	    	
	    	calendarIcon.setVisibility(View.INVISIBLE);
	      	toggle_linechart.setVisibility(View.INVISIBLE);
	    	
	    	chartAreaLayout.setBackgroundResource(R.drawable.linechart_bg_landscape);
	    	
	    	lp.width = width;
	    	lp.height = height - upperBarContent.getHeight() + 200;	    	
	    } 
	    else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	    	calendarIcon.setVisibility(View.VISIBLE);
	      	toggle_linechart.setVisibility(View.VISIBLE);
	      	
	    	chartAreaLayout.setBackgroundResource(R.drawable.linechart_bg);
	    	
	    	if (!isFilterIsOpen) {		    	
		    	lp.width = width;
		    	lp.height = drawerHeight;
	    	}
	    	else {
	    	  	lp.width = width;
		    	lp.height = drawerHeightWithFilter;
	    	}
	    }
	    if (isFilterIsOpen) { setFilterSize(); }
	    lp.addRule(RelativeLayout.BELOW, lineChartBar.getId());
    	drawer.setLayoutParams(lp);
	}
	
	private void setFilterSize() {
		filterView = (View) findViewById(R.id.linechart_filter_area);
		filterView.requestLayout();
		if (isRotated) {
			filterView.getLayoutParams().height = filterHeightLandscape;
			filterView.setPadding(10, 10, 10, 10);
		}
		else {
			filterView.getLayoutParams().height = filterHeight;
			filterView.setPadding(30, 30, 30, 30);
		}
		
	}
	
	private void showDiary() {		
		diaryList = (LinearLayout) findViewById(R.id.item);
				
		for (int n = 1  ; n <=30 ; n++) {
			diaryItem = getLayoutInflater().inflate(R.layout.diary_item, null);
			
			//sv_item_height = diaryItem.getMeasuredHeight();
			TextView date_num = (TextView) diaryItem.findViewById(R.id.date);
			diaryList.addView(diaryItem);
			date_num.setText(Integer.toString(n) + "號");
			
			boxesLayout = (LinearLayout) findViewById(R.layout.diary_item);		
		}
	}
	
	public void setCurrentCalendarPage(int pageIdx){
		mViewPager.setCurrentItem(pageIdx);
	}
			
    private class ToggleListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			drawer.toggle();
		}
    }
    
    private class FilterButtonListener implements View.OnClickListener {
    	
    	@Override
    	public void onClick(View v) {
    		if (v.getId() == R.id.line_chart_filter) {
    			if (isFilterIsOpen == false) {				
					LayoutParams lp = new LayoutParams(drawer.getLayoutParams());
					//Log.i("OMG", "H: "+lp.height);
					lp.height = drawerHeightWithFilter;
					lp.addRule(RelativeLayout.BELOW, lineChartBar.getId());
					drawer.setLayoutParams(lp);
					
					drawerContent.removeAllViews();
					
					drawerContent.addView(lineChartFilter);
					setFilterSize();
					drawerContent.addView(lineChartView);
					isFilterIsOpen = true;
				}
				else {
					LayoutParams lp = new LayoutParams(drawer.getLayoutParams());
					lp.height = drawerHeight;
					lp.addRule(RelativeLayout.BELOW, lineChartBar.getId());
					drawer.setLayoutParams(lp);
					
					drawerContent.removeAllViews();
					
					drawerContent.addView(lineChartView);
					isFilterIsOpen = false;
				}
    			
    		} else {
    			if (isFilterIsOpen == false) {				
					LayoutParams lp = new LayoutParams(drawer.getLayoutParams());
					//Log.i("OMG", "H: "+lp.height);
					lp.height = drawerHeightWithFilter;
					lp.addRule(RelativeLayout.BELOW, calendarBar.getId());
					drawer.setLayoutParams(lp);
					
					drawerContent.removeAllViews();
					
					drawerContent.addView(lineChartFilter);
					setFilterSize();
					drawerContent.addView(calendarView);
					isFilterIsOpen = true;
				}
				else {
					LayoutParams lp = new LayoutParams(drawer.getLayoutParams());
					lp.height = drawerHeight;
					lp.addRule(RelativeLayout.BELOW, calendarBar.getId());
					drawer.setLayoutParams(lp);
					
					drawerContent.removeAllViews();
					
					drawerContent.addView(calendarView);
					isFilterIsOpen = false;
				}
    		} 		
    	}    	
    }
    
    private class FilterListener implements View.OnClickListener {
    	
    	@Override
    	public void onClick(View v) {
    		switch (v.getId()) {
    		case (R.id.filter_all): {
    			Log.i("OMG", "KKK");
    			
    			if (filterButtonIsPressed[0]) { filterButtonIsPressed[0] = false; }
    			else {filterButtonIsPressed[0] = true; }
    			
    			break;
    		}
    		case (R.id.filter_1): {
    			if (filterButtonIsPressed[1]) { filterButtonIsPressed[1] = false; }
    			else {filterButtonIsPressed[1] = true; }
    			break;	
    		}
    		
    		case (R.id.filter_2): {
    			Log.i("OMG", "mememe");
    			
    			if (filterButtonIsPressed[2]) { filterButtonIsPressed[2] = false; }
    			else {filterButtonIsPressed[2] = true; }
    			break;
    			
    		}
    		
    		case (R.id.filter_3): {
    			if (filterButtonIsPressed[3]) { filterButtonIsPressed[3] = false; }
    			else {filterButtonIsPressed[3] = true; }
    			break;
    			
    		}
    		
    		case (R.id.filter_4): {
    			if (filterButtonIsPressed[4]) { filterButtonIsPressed[4] = false; }
    			else {filterButtonIsPressed[4] = true; }
    			break;
    			
    		}
    		case (R.id.filter_5): {
    			if (filterButtonIsPressed[5]) { filterButtonIsPressed[5] = false; }
    			else {filterButtonIsPressed[5] = true; }
    			break;    			
    		}
    		case (R.id.filter_6): {
    			if (filterButtonIsPressed[6]) { filterButtonIsPressed[6] = false; }
    			else {filterButtonIsPressed[6] = true; }
    			break;
    		}
    		case (R.id.filter_7): {
    			if (filterButtonIsPressed[7]) { filterButtonIsPressed[7] = false; }
    			else {filterButtonIsPressed[7] = true; }
    			break;
    		}
    		case (R.id.filter_8): {
    			if (filterButtonIsPressed[8]) { filterButtonIsPressed[8] = false; }
    			else {filterButtonIsPressed[8] = true; }
    			break;
    		}
    		
    		
    	}
    }
    }
		
}
