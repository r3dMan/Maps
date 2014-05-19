package com.example.test_utility_library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ScaleBar extends View {
	private Paint line = new Paint();
	private Paint textPaint = new Paint();
	private float lengthX;
	private float thickness;
	private String text;
	private String scale;
	private float marginL;
	

	public ScaleBar(Context cont) {
		super(cont);
		thickness = 5 ;
		text = "scale";
		scale = "";
		line.setColor(Color.BLACK);
		line.setStrokeWidth(thickness);
		marginL =15; 
		lengthX = 150;
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(30);
	}

	public void setScale(String scale) {
		this.scale = scale;
		invalidate();
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawText(text+": "+scale+"m", 20, 30, textPaint);
		canvas.drawLine(marginL, 50, lengthX+marginL, 50, line);
		
	}

	public void setLengthX(float lengthX) {
		this.lengthX = lengthX;

	}
}
