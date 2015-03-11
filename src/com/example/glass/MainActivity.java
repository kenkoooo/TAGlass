package com.example.glass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.kenkoooo.AsyncCommentTask;

public class MainActivity extends Activity implements OnClickListener {
	private Handler handler = new Handler();

	private Runnable updateComment;

	private final int delay = 10000;// 更新間隔ミリ秒

	private ViewFlipper tView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.tView = (ViewFlipper) findViewById(R.id.viewFlipper);

		// コメントを取る
		this.updateComment = new Runnable() {
			@Override
			public void run() {
				AsyncCommentTask task = new AsyncCommentTask();
				task.execute();
				try {
					for (String comment : task.get()) {
						initScrollTextView(MainActivity.this.tView, comment);
					}
				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				MainActivity.this.handler
						.removeCallbacks(MainActivity.this.updateComment);
				MainActivity.this.handler.postDelayed(
						MainActivity.this.updateComment,
						MainActivity.this.delay);
			}
		};
		this.handler.postDelayed(this.updateComment, 0);
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
			// The touchpad was tapped
			// 遷移先のActivityを指定して、Intentを作成する
			Intent intent = new Intent(this, ChartActivity.class);
			// 遷移先のアクティビティを起動させる
			startActivity(intent);
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
		this.handler.postDelayed(this.updateComment, 0);

	}

	private void initScrollTextView(final ViewFlipper viewFlipper,
			String comment) {
		viewFlipper.setInAnimation(inFromUpAnimation(3000));
		LinearLayout linearLayout = null;
		Thread trd = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					viewFlipper.showNext();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		});
		for (String iterable_element : splitAt(10, comment)) {
			linearLayout = new LinearLayout(this);
			TextView textView = new TextView(this);
			textView.setText(iterable_element);
			linearLayout.addView(textView);
			trd.start();
		}
	}

	public static List<String> splitAt(int n, String s) {
		final char[] cs = s.toCharArray();
		List<String> ss = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < cs.length; i++) {
			sb.append(cs[i]);
			if (((i + 1) % n) != 0) {
				continue;
			}
			ss.add(sb.toString());
			sb.delete(0, sb.length());
		}
		ss.add(sb.toString());

		return ss;
	}

	public static Animation inFromUpAnimation(int duration) {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(duration);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public static Animation outToDownAnimation(int duration) {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f);
		outtoLeft.setDuration(duration);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
}