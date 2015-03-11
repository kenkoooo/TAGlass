package com.example.glass;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.glass.view.WindowUtils;
import com.kenkoooo.AsyncJSONTask;
import com.kenkoooo.PieChartView;
import com.kenkoooo.ProblemUpdater;

public class ChartActivity extends Activity implements OnClickListener {
	private Handler handler = new Handler();
	private Runnable updateChart;
	private final int delay = 10000;// 更新間隔ミリ秒
	private int p_num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

		updateChart = new Runnable() {
			public void run() {
				AsyncJSONTask jsonTask = new AsyncJSONTask();
				jsonTask.execute();
				HashMap<String, Integer> hashMap = null;
				try {
					hashMap = jsonTask.get();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * HashMapを作って投げるとViewを返してくれます。
				 */
				PieChartView piecv = new PieChartView(getApplicationContext(),
						null);
				piecv.setChart(hashMap);// 表示させたいHashMapをsetする。
				piecv.setOnClickListener(ChartActivity.this);
				setContentView(piecv);

				handler.removeCallbacks(updateChart);
				handler.postDelayed(updateChart, delay);
			}
		};
		handler.postDelayed(updateChart, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// タッチイベント
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			finish();
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		String[] strings = { "たかしくんは毎月いくらもらえますか？", "たかしくんは明日も暮らせますか？",
				"たかしくんは就活をしますか？", "たかしくんはデモで動かせますか？", "たかしくんはもういない。" };
		p_num++;
		p_num = p_num % 5;
		ProblemUpdater updater = new ProblemUpdater();
		updater.execute(strings[p_num]);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {

			switch (item.getItemId()) {
			case R.id.show_comment_item:
				// handle top-level dogs menu item
				Log.d("wgp", "show_comment_item");
				finish();
				break;

			case R.id.qize_list_item:
				// handle top-level cats menu item
				Log.d("wgp", "show_qize_item");
				break;

			case R.id.finish_application:
				// handle second-level cats menu item
				Log.d("wgp", "finish");
				finish();
				break;

			default:
				return true;
			}
			return true;
		}
		// Good practice to pass through to super if not handled
		return super.onMenuItemSelected(featureId, item);
	}
}
