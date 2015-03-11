package com.example.glass;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.google.android.glass.view.WindowUtils;
import com.kenkoooo.AsyncCommentTask;
import com.kenkoooo.ProblemUpdater;

public class MarqueActivity extends Activity implements OnClickListener {
	private Handler handler = new Handler();

	private Runnable updateComment;

	private final int delay = 10000;// 更新間隔ミリ秒

	private TextView tView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

		setContentView(R.layout.marque);
		this.tView = (TextView) findViewById(R.id.textView1);
		this.tView.setOnClickListener(this);
		// ---ここから
		// this.tView.setSingleLine(); // 文字列を1行で表示.
		// // これがないと複数行に渡って表示されてしまうので、スクロールできない
		// this.tView.setFocusableInTouchMode(true); //
		// Touchモード時にViewがフォーカスを取得可能か設定をします。
		// this.tView.setEllipsize(TruncateAt.MARQUEE);
		// ---ここまで

		// コメントを取る
		this.updateComment = new Runnable() {
			@Override
			public void run() {
				AsyncCommentTask task = new AsyncCommentTask();
				task.execute();
				try {

					String aaaString = "";
					for (String comment : task.get()) {
						aaaString = aaaString + comment + "　";
					}
					Log.i(aaaString, "");
					tView.setText(aaaString);

				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				MarqueActivity.this.handler
						.removeCallbacks(MarqueActivity.this.updateComment);
				MarqueActivity.this.handler.postDelayed(
						MarqueActivity.this.updateComment,
						MarqueActivity.this.delay);
			}
		};
		this.handler.postDelayed(this.updateComment, 0);
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
		Intent intent = new Intent(this, ChartActivity.class);
		// 遷移先のアクティビティを起動させる
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS
				|| featureId == Window.FEATURE_OPTIONS_PANEL) {
			getMenuInflater().inflate(R.menu.menu, menu);
			// Log.d("wgp",""+menu);
			return true;
		}
		// Pass through to super to setup touch menu.
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
			ProblemUpdater updater = new ProblemUpdater();
			String quiz;
			switch (item.getItemId()) {
			case R.id.show_graph_item:
				// handle top-level cats menu item
				Log.d("wgp", "show_graph_item");

				// 遷移先のActivityを指定して、Intentを作成する
				Intent intent = new Intent(MarqueActivity.this,
						ChartActivity.class);
				// 遷移先のアクティビティを起動させる
				startActivity(intent);

				break;
			case R.id.qize_list_item:
				// handle top-level cats menu item
				Log.d("wgp", "show_quiz_item");
				break;
			case R.id.qize_1:
				// handle second-level cats menu item
				Log.d("wgp", "quiz_1");
				quiz = "quiz1_test";
				updater.execute(quiz);
				break;
			case R.id.qize_2:
				// handle second-level cats menu item
				Log.d("wgp", "quiz_2");
				quiz = "quiz2_test";
				updater.execute(quiz);
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