package ubicomp.mydaybook.linechart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import ubicomp.mydaybook.MainActivity;
import ubicomp.mydaybook.R;
import ubicomp.mydaybook.linechart.App;

public class LineChartTitle extends View {

	private ChartCaller caller;
	private boolean chartTouchable = true;
	
	private int chart_type = 2;
	
	private int title_0 = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_0);
	private int title_1 = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_1);
	private int title_2 = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_2);
	
	private int title_0_r = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_0_r);
	private int title_1_r = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_1_r);
	private int title_2_r = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_2_r);
	
	private int t1, t2, t3;
	
	private int titleTop = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_title_top);
	
	private int textSize = App.getContext().getResources().getDimensionPixelSize(R.dimen.large_text_size);
	
	private String[] title_str = { "自己", "他人" ,"自己+他人" };
	private Paint text_paint_large = new Paint();
	private Paint text_paint_large_2 = new Paint();
	
	
	public LineChartTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		text_paint_large.setTextSize(textSize);
		text_paint_large.setTextAlign(Align.LEFT);
		text_paint_large.setTypeface(Typefaces.getWordTypefaceBold());
		text_paint_large_2.setColor(Color.BLUE);
		text_paint_large_2.setTextSize(textSize);
		text_paint_large_2.setTextAlign(Align.LEFT);
		text_paint_large_2.setTypeface(Typefaces.getWordTypefaceBold());
		
	
	}
	
	public void setting(ChartCaller caller) {
		this.caller = caller;
	}
	
	public void setTouchable(boolean touchable){
		chartTouchable = touchable;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!chartTouchable)
			return true;
		int x = (int) event.getX();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (x < t2) {
				chart_type = 0;
			} else if (x < t3) {
				chart_type = 1;
			} else {
				chart_type = 2;
			}
			caller.setChartType(chart_type);
			invalidate();
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			t1 = title_0;
			t2 = title_1;
			t3 = title_2;
		}
		else {
			t1 = title_0_r;
			t2 = title_1_r;
			t3 = title_2_r;
		}
		
		canvas.drawText(title_str[0], t1, titleTop, text_paint_large);
		canvas.drawText(title_str[1], t2, titleTop, text_paint_large);
		canvas.drawText(title_str[2], t3, titleTop, text_paint_large);
		switch (chart_type) {
		case 0:
			canvas.drawText(title_str[0], t1, titleTop, text_paint_large_2);
			break;
		case 1:
			canvas.drawText(title_str[1], t2, titleTop, text_paint_large_2);
			break;
		case 2:
			canvas.drawText(title_str[2], t3, titleTop, text_paint_large_2);
			break;
		}
	}
}
