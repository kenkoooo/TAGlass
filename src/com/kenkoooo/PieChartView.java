package com.kenkoooo;

import java.util.HashMap;
import java.util.Map;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PiePlot;
import org.afree.data.general.DefaultPieDataset;
import org.afree.graphics.geom.Font;
import org.afree.graphics.geom.RectShape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
	private AFreeChart chart;
	private final double rect_x = 0;
	private final double rect_y = 0;
	private final double width = 600.0;
	private final double height = 400.0;
	private final int fontsize = 30;// フォントサイズ

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		RectShape chartArea = new RectShape(rect_x, rect_y, width, height);
		this.chart.draw(canvas, chartArea);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) width, (int) height);
	}

	public void setChart(HashMap<String, Integer> hash) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Map.Entry entry : hash.entrySet()) {
			dataset.setValue((String) entry.getKey(), (int) entry.getValue());

		}
		AFreeChart chart = ChartFactory.createPieChart("", dataset, true,
				false, false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, fontsize));
		chart.removeLegend();
		this.chart = chart;
	}

}