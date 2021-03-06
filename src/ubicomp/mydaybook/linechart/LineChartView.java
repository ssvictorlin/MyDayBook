package ubicomp.mydaybook.linechart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ubicomp.mydaybook.MainActivity;
import ubicomp.mydaybook.R;
import ubicomp.mydaybook.data.DummyData;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class LineChartView extends View {

    private static final int LINES = 7;
    private static int offsetY = 90;
    private static int offsetX = 60;
    private static int range;
    public  List<DummyData> datapoints = new ArrayList<DummyData>();
    
    private Paint paint = new Paint();
    private Bitmap mBitmap;

    private Bitmap d1 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color1);
    private Bitmap d2 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color2);
    private Bitmap d3 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color3);
    private Bitmap d4 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color4);
    private Bitmap d5 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color5);
    private Bitmap d6 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color6);
    private Bitmap d7 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color7);
    private Bitmap d8 = BitmapFactory.decodeResource(getResources(), R.drawable.dot_color8);
    private Bitmap[] dotArray= {d1, d2, d3, d4, d5, d6, d7, d8};
    
    private Bitmap cursorImg = BitmapFactory.decodeResource(getResources(), R.drawable.linechart_cursor);
    private Bitmap legendImg = BitmapFactory.decodeResource(getResources(), R.drawable.linechart_legend);
    
    private Bitmap rectBarBg = BitmapFactory.decodeResource(getResources(), R.drawable.gray_underbar);
    private Bitmap passBarBg = BitmapFactory.decodeResource(getResources(), R.drawable.pass_rect);
    private Bitmap noPassBarBg = BitmapFactory.decodeResource(getResources(), R.drawable.nopass_rect);
    
    private GestureDetector gestureDetector; 
    
    private static int NONE = 0;
    private static int ZOOM1 = 1;
    private static int ZOOM2 = 2;
    
    private int mode = 0;  
    private int cursorLinePos = 5;
    private int initHeight;

	public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());
    	dummyDataGenerator();

    }

	
	public void dummyDataGenerator() {
		for (int i = 0; i < 30 ; i++) {
			Random r1 = new Random();
			Random r2 = new Random();
			int activityType;
			int who = r1.nextInt(3 - 1) + 1;
			
			if (who == 1) {
				activityType = r2.nextInt(6 - 1) + 1;
				
			}
			else {
				activityType = r2.nextInt(9 - 6) + 6;
			}
			
			Bitmap bmp = dotArray[activityType-1];
			
			Random r3 = new Random();
			int score = r3.nextInt(4 - (-3)) + (-3);
			Random r4 = new Random();
			boolean passTest;
			int pass = r4.nextInt(3 - 1) + 1;
			
			if (pass == 1) {passTest = true;}
			else {passTest = false;}
			
			DummyData singleData = new DummyData(who, score, activityType, bmp , 6, i+1, passTest);
			datapoints.add(singleData);	
		}
		/*Log.i("OMG", "type:" + MainActivity.getChartType());
		datapoints = new ArrayList<DummyData>();
		if (MainActivity.getChartType() == 1) {
			// Test 1
			for (int i = 0; i < 15 ; i++) {
				DummyData singleData;
				if ( i == 4 || i == 5 || i == 7) {
					singleData = new DummyData(2, -2, 7, d7 , 6, i+1, false);
				}
				else if ( i < 4 || i ==9 || i==10  ) {
					singleData = new DummyData(1, 3, 3, d3 , 6, i+1, true);
				}
				else {
					singleData = new DummyData(1, 0, 1, d1 , 6, i+1, true);
					
				}
				
				datapoints.add(singleData);
			}
		}
		else {
			// Test 2
			for (int i = 0; i < 15 ; i++) {
				DummyData singleData;
				if ( i < 7) {
					Random r1 = new Random();
					Random r2 = new Random();
					int activityType;
					int who = 1;
					
					if (who == 1) {
						activityType = r2.nextInt(6 - 3) + 3;
						
					}
					else {
						activityType = r2.nextInt(7 - 6) + 6;
					}
					
					Bitmap bmp = dotArray[activityType-1];
					
					Random r3 = new Random();
					int score = r3.nextInt(4 - 1) + 1;
					Random r4 = new Random();
					boolean passTest;
					int pass = r4.nextInt(3 - 1) + 1;
					
					passTest = true;
					
					
					singleData = new DummyData(who, score, activityType, bmp , 6, i+1, passTest);
				}
				else if ( i == 7) {
					singleData = new DummyData(1, -3, 2, d2 , 6, i+1, true);
				}
				else if ( i == 8) {
					singleData = new DummyData(1, -3, 2, d2 , 6, i+1, false);
				}
				else  {
					Random r1 = new Random();
					Random r2 = new Random();
					int activityType;
					int who = r1.nextInt(2 - 1) + 1;
					
					if (who == 1) {
						activityType = r2.nextInt(3 - 1) + 1;
						
					}
					else {
						activityType = r2.nextInt(9 - 6) + 6;
					}
					
					Bitmap bmp = dotArray[activityType-1];
					
					Random r3 = new Random();
					int score = r3.nextInt((-1) - (-3)) + (-3);
					Random r4 = new Random();
					boolean passTest;
					int pass = r4.nextInt(3 - 1) + 1;
					
					passTest = false;
					
					singleData = new DummyData(who, score, activityType, bmp , 6, i+1, passTest);
				}
				
				
				datapoints.add(singleData);
			}
		}*/
		

	}

    
 // override onSizeChanged
 	@Override
 	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
 		super.onSizeChanged(w, h, oldw, oldh);

 		// your Canvas will draw onto the defined Bitmap
 		mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
 	}
    
    @Override 
    public boolean onTouchEvent(MotionEvent event) {
    	gestureDetector.onTouchEvent(event);
        return true;    	
    }
    
    public void setZoomMode() {
    	if (mode == NONE) {
    		mode = ZOOM1;
    	}
    	else if (mode == ZOOM1) {
    		mode = ZOOM2;
    	}
    	else if (mode == ZOOM2) {
    		mode = NONE;
    	}    	
    }
    
    public int getZoomMode() {
    	return mode;
    }
    
    private void setCanvasWidth() {
    	this.requestLayout();    	
    	switch (mode) {
    	case 0:  
    		this.getLayoutParams().width = 2200;
    		break;
    	case 1:
    		this.getLayoutParams().width = 1600;
    		break;
    	case 2:
    		this.getLayoutParams().width = 1100;
    		break;
   	
    	}
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);   	
    	canvas.save();

    	if  (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { offsetY = 90; }
    	else { offsetY = 30; }
        drawBackground(canvas);
        drawDate(canvas);
        drawCursor(canvas);
        drawLineChart(canvas);
        drawRectBar(canvas);
        
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {
        range = getLineDistance();

        paint.setStyle(Style.FILL);
        paint.setColor(getResources().getColor(R.color.linechart_bgline_color));
        paint.setTextAlign(Align.LEFT);
        paint.setTextSize(35);
        paint.setStrokeWidth(1);
        Paint line_paint = paint;
        line_paint.setColor(getResources().getColor(R.color.linechart_score_color));
        for (int y = 1; y <= 7; y++) {
            final float yPos = y*range + offsetY;
            
            if (y == 1 || y == 4 || y == 7) {
            	canvas.drawLine(0, yPos, getWidth(), yPos, paint);        	
            	canvas.drawText(String.valueOf((-1*y)+4), getPaddingLeft(), yPos, line_paint);
            }
        }
    }

    
    private void drawDate(Canvas canvas) {
    	int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    	int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    	
    	int numOfDays = datapoints.size();
    	for (int i = 0; i < numOfDays ; i ++) {
    		if (mode == ZOOM2) {
    			if (i%5 != 0) continue;
    		}
		
    		paint.setStyle(Style.FILL);
    	    paint.setColor(getResources().getColor(R.color.linechart_date_color));
    	    paint.setTextAlign(Align.CENTER);
    		paint.setTextSize(35);
            canvas.drawText(String.valueOf(currentDate+i), getXPos(i), getHeight() - 120, paint);
    	}
    }

	private int getLineDistance() {
        int distance;
        
        distance = getHeight() - getPaddingTop() - getPaddingBottom();
        distance = (distance/8)*5/LINES;

        return distance;
    }
	

    private void drawLineChart(Canvas canvas) {
        Path path = new Path();
        Path path_self = new Path();
        Path path_other = new Path();
        Paint p = new Paint();
        
    	int startPoint = 0;
        for (int i = 0; i < datapoints.size(); i++) {
        	if (datapoints.get(i).activityType < 6) {
        	path_self.moveTo(getXPos(i), getYPos(datapoints.get(i).score));
        	startPoint = i;
        	break;
        	}
        }
        for (int i = startPoint; i < datapoints.size(); i++) {
        	if (datapoints.get(i).activityType < 6) {
            path_self.lineTo(getXPos(i), getYPos(datapoints.get(i).score));
        	}
        	
        }
        startPoint = 0;
        for (int i = 0; i < datapoints.size(); i++) {
        	if (datapoints.get(i).activityType > 5) {
        	path_other.moveTo(getXPos(i), getYPos(datapoints.get(i).score));
        	startPoint = i;
        	break;
        	}
        }
        for (int i = startPoint; i < datapoints.size(); i++) {
        	if (datapoints.get(i).activityType > 5) {
            path_other.lineTo(getXPos(i), getYPos(datapoints.get(i).score));
        	}
        	
        }
        /*
        if (checkLineChartType() <2 ) {
	        path.moveTo(getXPos(0), getYPos(datapoints.get(0).score));
	        for (int i = 1; i < datapoints.size(); i++) {
	            path.lineTo(getXPos(i), getYPos(datapoints.get(i).score));
	        }
	
	        paint.setStyle(Style.STROKE);
	        paint.setStrokeWidth(4);
	        paint.setColor(getResources().getColor(R.color.path_normal));
	        paint.setAntiAlias(true);
	        paint.setShadowLayer(4, 2, 2, 0x80000000);
	        canvas.drawPath(path, paint);
	        paint.setShadowLayer(0, 0, 0, 0);
        }
        else {
        
            paint.setStyle(Style.STROKE);
	        paint.setStrokeWidth(4);
	        paint.setColor(getResources().getColor(R.color.path_other_color));
	        paint.setAntiAlias(true);
	        paint.setShadowLayer(4, 2, 2, 0x80000000);
	        canvas.drawPath(path_other, paint);
	        
	        Paint paint_self = paint;
	        paint_self.setColor(getResources().getColor(R.color.path_self_color));
	        canvas.drawPath(path_self, paint_self);
	        
	        paint.setShadowLayer(0, 0, 0, 0);
	        paint_self.setShadowLayer(0, 0, 0, 0);
        }
        */
        switch (checkLineChartType()) {
        
        case 0: {
        	 paint.setStyle(Style.STROKE);
 	         paint.setStrokeWidth(4);
 	         paint.setColor(getResources().getColor(R.color.path_normal));
 	         paint.setAntiAlias(true);
 	         paint.setShadowLayer(4, 2, 2, 0x80000000);
 	         canvas.drawPath(path_self, paint);
 	         paint.setShadowLayer(0, 0, 0, 0);
        	
        	 for (int i = 0; i < datapoints.size(); i++) {
        		if (datapoints.get(i).activityType > 5) continue;
 	        	if (drawTheDotOrNot(datapoints.get(i).activityType)) {
 	        	Bitmap bmp = datapoints.get(i).dotColor;
 	            bmp = getResizedBitmap(bmp, 25, 25);
 	            canvas.drawBitmap(bmp, getXPos(i)-12, getYPos(datapoints.get(i).score)-10, p);
 	        	}
 	        }
        	break;
        }
        case 1: {
        	 paint.setStyle(Style.STROKE);
 	         paint.setStrokeWidth(4);
 	         paint.setColor(getResources().getColor(R.color.path_normal));
 	         paint.setAntiAlias(true);
 	         paint.setShadowLayer(4, 2, 2, 0x80000000);
 	         canvas.drawPath(path_other, paint);
 	         paint.setShadowLayer(0, 0, 0, 0);
        	
        	 for (int i = 0; i < datapoints.size(); i++) {
         		if (datapoints.get(i).activityType < 6) continue;
  	        	if (drawTheDotOrNot(datapoints.get(i).activityType)) {
  	        	Bitmap bmp = datapoints.get(i).dotColor;
  	            bmp = getResizedBitmap(bmp, 25, 25);
  	            canvas.drawBitmap(bmp, getXPos(i)-12, getYPos(datapoints.get(i).score)-10, p);
  	        	}
  	        }
        	break;
        }
        
        case 2: {
        	paint.setStyle(Style.STROKE);
	        paint.setStrokeWidth(4);
	        paint.setColor(getResources().getColor(R.color.path_other_color));
	        paint.setAntiAlias(true);
	        paint.setShadowLayer(4, 2, 2, 0x80000000);
	        canvas.drawPath(path_other, paint);
	        
	        Paint paint_self = paint;
	        paint_self.setColor(getResources().getColor(R.color.path_self_color));
	        canvas.drawPath(path_self, paint_self);
	        
	        paint.setShadowLayer(0, 0, 0, 0);
	        paint_self.setShadowLayer(0, 0, 0, 0);
        	Bitmap legend = getResizedBitmap(legendImg, 40, 350);
        	canvas.drawBitmap(legend, getPaddingLeft(), getPaddingTop(), p);
        	break;
        }       
      }
    }
    
    private int checkLineChartType () {
    	return MainActivity.getChartType();
    }
    
    private boolean drawTheDotOrNot (int typeOfActivity) {
    	if (MainActivity.filterButtonIsPressed[0]) return true;
    	else return MainActivity.filterButtonIsPressed[typeOfActivity]; 
    }
    
    private void drawRectBar(Canvas canvas) {
    	Paint p = new Paint();
    	rectBarBg = getResizedBitmap(rectBarBg, 30, rectBarBg.getWidth());
    	canvas.drawBitmap(rectBarBg , 0 , getHeight() - offsetY , p);
    	
    	for (int i = 0; i < datapoints.size(); i++) {
    		if (datapoints.get(i).passTest) {
    			passBarBg = getResizedBitmap(passBarBg, 28, 60);
	            canvas.drawBitmap(passBarBg, getXPos(i)-25, getHeight()-offsetY, p);
    		}
    		else {
    			noPassBarBg = getResizedBitmap(noPassBarBg, 28, 60);
	            canvas.drawBitmap(noPassBarBg, getXPos(i)-25, getHeight()-offsetY, p);
    		}   			
        }    	
    }
    
    private void drawCursor(Canvas canvas) {
    	// TODO : only draw above dates
    	
    	Paint p = new Paint();
    	//p.setColor(Color.RED);
    	//p.setStyle(Style.STROKE);
    	//p.setStrokeWidth(5);
    	//p.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
    	Bitmap cursor = getResizedBitmap(cursorImg, getHeight() - 2*getPaddingTop() - 2*getPaddingBottom() , 25);
    	canvas.drawBitmap(cursor, getXPos(cursorLinePos) - 13, 0, p);
    	
    }

    private float getYPos(float value) {
      return range*(-1*value+4) + offsetY;
    }

    private float getXPos(float value) {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float maxValue = datapoints.size() - 1;

        // scale it to the view size
        value = (value / maxValue) * width;

        // offset it to adjust for padding
        value += getPaddingLeft();

        return value + offsetX;
    }
    
    
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
      	
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    
    public int getCursorPos(float x) {
    	float xWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / datapoints.size();
    	int temp = (int) Math.round(x/xWidth); 
    	return temp;
    }
    
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            setZoomMode();
        	setCanvasWidth();
    		//invalidate();
            return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
        	float x = event.getX();
            float y = event.getY();
            cursorLinePos = getCursorPos(x) - 2;
            if (cursorLinePos < 1) { return false;}
            invalidate();
            return true;
        }
    }
    

}

