package com.example.test_utility_library;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathVariableResolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;

public class AltimetricView extends View {
	private Paint axis = new Paint();
	private Paint xPoint = new Paint();
	private Paint ySpacingLinesPaint = new Paint();
	private Path altitudeValuesPath = new Path();
	// private int parentWidth;
	// private int parentHeight;
	private int thickness;
	private int boundScaleX;
	private List<Double> altimetryValues;
	private List<Float> xyPointValuesPixel;
	private float yScaleStep;
	private int scaleMax;
	private int k;
	float progressInPixel;
	private int ySpacingLines;

	public AltimetricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		thickness = 5;

		axis.setColor(Color.BLACK);
		axis.setStrokeWidth(thickness);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float pointX;
		float pointY;
		//float[] points = new float[xyPointValuesPixel.size()];
		for (int i = 0; i < xyPointValuesPixel.size(); i+=2){
			//points[i] = xyPointValuesPixel.get(i);
			pointX = xyPointValuesPixel.get(i);
			pointY = xyPointValuesPixel.get(i+1);
			if(i == 0)
				altitudeValuesPath.moveTo(pointX, pointY);
			else {
				altitudeValuesPath.lineTo(pointX, pointY);
			}
		}
		
		

		canvas.drawLine(0, 0, 0, getHeight(), axis);
		canvas.drawLine(0, getHeight(), getWidth(), getHeight(), axis);
		xPoint.setColor(Color.BLACK);
		xPoint.setStyle(Paint.Style.STROKE);
		xPoint.setStrokeWidth(2);
		drawLineScaleY(canvas);
		
		//canvas.drawPoints(points, xPoint);
		
		canvas.drawPath(altitudeValuesPath, xPoint);
	}

	public void setAltitudeValues(List<Double> values) {
		this.altimetryValues = values;
		this.yScaleStep = this.spacingY();
		this.xyPointValuesPixel = this.calculateAltitudePointCoordinates();
	}

	private List<Float> calculateAltitudePointCoordinates() {
		List<Float> altitudesCoordinatesXYPixel = new ArrayList<Float>();
		int dimensionScaleViewX = getWidth() - 40;
		int allValues = altimetryValues.size();
		//intero che indica il fattore di campionamento dei valori in funzione dei pixel a 
		//disposizione e della risoluzione
		boundScaleX = (int) (allValues / (float) dimensionScaleViewX);
		int j = 1;
		for (int i = 0; i < altimetryValues.size(); i = i + boundScaleX) {
			altitudesCoordinatesXYPixel.add((float) (thickness + j));
			altitudesCoordinatesXYPixel.add((float) (getHeight()
					- (altimetryValues.get(i) / yScaleStep) - 20));
			j++;
		}
		return altitudesCoordinatesXYPixel;
	}
//
//	public void drawAltimetricCursor(int progress){
//		progressInPixel = getWidth()
//				
//		progress = 
//	}
	
	// ci permette di aggiornare la dimensione degli elementi in base al fattore di riduzione
	public int getBoundScaleX(){
		return boundScaleX;
	}

	private void drawLineScaleY(Canvas canvas) {
		if (k == 1)
			ySpacingLines = ySpacingLines * 10;
		ySpacingLinesPaint.setAlpha(128);
		ySpacingLinesPaint.setColor(Color.DKGRAY);
		ySpacingLinesPaint.setStrokeWidth(1);
		float lineSpacing = getHeight() / (float) ySpacingLines;
		for (int i = 1; i <= ySpacingLines; i++) {

			canvas.drawLine(0, getHeight() - (i * lineSpacing), getWidth(),
					getHeight() - (i * lineSpacing), ySpacingLinesPaint);
		}
		// invalidate();
	}

	private float spacingY() {
		/*
		 * nella vista abbiamo settato un Margin che dobbiamo recuperare
		 * dinamicamente
		 */

		Double max = 0.0;
		scaleMax = 0;
		ySpacingLines = 0;
		float scaleStep = 0;
		for (Double d : this.altimetryValues) {
			if (max < d)
				max = d;
		}
		Integer maxInt = Integer.valueOf(max.intValue());
		int digitsNumber = maxInt.toString().length();
		k = 0; // indica quando passiamo i 1000m di altitudineMax
		switch (digitsNumber) {
		case 1:
			ySpacingLines = 1;
			scaleMax = 10;
			break;

		case 2:
			ySpacingLines = ((int) ((maxInt + 10) / 10f));
			scaleMax = ySpacingLines * 10;
			break;

		case 3:
			ySpacingLines = ((int) ((maxInt + 100) / 100f));
			scaleMax = ySpacingLines * 100;
			break;
		case 4:
			ySpacingLines = ((int) ((maxInt + 1000) / 1000f));
			k = 1;
			scaleMax = ySpacingLines * 1000;
		}
		scaleStep = scaleMax / (float) (getHeight() - 40);
		Log.i("ALTITUDE_VIEW", "ScaleMax:" + scaleMax + " ScaleStep:"
				+ scaleStep + " Height:" + getHeight());

		return scaleStep;
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	// parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	// this.setMeasuredDimension(parentWidth, parentHeight);
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// }

}
