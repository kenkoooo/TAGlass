package com.example.glass;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kenkoooo.AsyncCommentTask;

public class CopyOfMarqueActivity extends Activity implements OnClickListener {
	private Handler handler = new Handler();

	private Runnable updateComment;

	private final int delay = 10000;// 更新間隔ミリ秒

	private TextView tView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marque);
		this.tView = (TextView) findViewById(R.id.textView1);
		this.tView.setOnClickListener(this);
		// ---ここから
		this.tView.setSingleLine(); // 文字列を1行で表示.
									// これがないと複数行に渡って表示されてしまうので、スクロールできない
		this.tView.setFocusableInTouchMode(true); // Touchモード時にViewがフォーカスを取得可能か設定をします。
		this.tView.setEllipsize(TruncateAt.MARQUEE);
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

				CopyOfMarqueActivity.this.handler
						.removeCallbacks(CopyOfMarqueActivity.this.updateComment);
				CopyOfMarqueActivity.this.handler.postDelayed(
						CopyOfMarqueActivity.this.updateComment,
						CopyOfMarqueActivity.this.delay);
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
		Intent intent = new Intent(this, ChartActivity.class);
		// 遷移先のアクティビティを起動させる
		startActivity(intent);
	}

}