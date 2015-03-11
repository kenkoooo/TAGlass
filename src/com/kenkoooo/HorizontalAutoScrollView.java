package com.kenkoooo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class HorizontalAutoScrollView extends HorizontalScrollView {

	private final static String STR_ATTR_FRAME_INTERVAL = "frameInterval"; // フレーム間の時間
	private final static String STR_ATTR_FRAME_DELTA = "frameDelta"; // フレーム間の移動量
	private final static String STR_ATTR_LAPEL_INTERVAL = "lapelInterval"; // 折り返す時のフレーム間隔

	private Handler _handlerAnimation = null; // アニメーション用
	private int _frameInterval = 100; // フレーム間の時間
	private int _frameDelta = 2; // フレーム間の移動量
	private int _lapelInterval = 500; // 折り返す時のフレーム間隔
	private boolean _isDerectionLeft = true; // 左へ動いているか
	private int _prev_x = 0; // 前回の場所

	/**
	 * 描画スレッド
	 */
	private final Runnable _runAnimationThread = new Runnable() {
		public void run() {

			updateAutoScroll();

		}
	};

	/**
	 * アニメーション用のハンドラ
	 * 
	 * @return
	 */
	private Handler getHandlerAnimation() {
		if (_handlerAnimation == null) {
			_handlerAnimation = new Handler();
		}
		return _handlerAnimation;
	}

	/**
	 * 横スクロールするテキストビューのコンストラクタ
	 * 
	 * @param context
	 * @param attrs
	 */
	public HorizontalAutoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		String temp = null;

		// フレーム間の時間
		temp = attrs.getAttributeValue(null, STR_ATTR_FRAME_INTERVAL);
		if (temp != null) {
			_frameInterval = Integer.valueOf(temp);
		}
		// フレーム間の移動量
		temp = attrs.getAttributeValue(null, STR_ATTR_FRAME_DELTA);
		if (temp != null) {
			_frameDelta = Integer.valueOf(temp);
		}
		// 折り返し時のフレーム間隔
		temp = attrs.getAttributeValue(null, STR_ATTR_LAPEL_INTERVAL);
		if (temp != null) {
			_lapelInterval = Integer.valueOf(temp);
		}

	}

	/**
	 * 表示状態が変わった
	 */
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);

		if (visibility == View.VISIBLE) {
			startAutoScroll();
		} else {
			stopAutoScroll();
		}
	}

	/**
	 * 自動スクロールを開始する
	 */
	public void startAutoScroll() {
		// 監視を開始
		getHandlerAnimation().postDelayed(_runAnimationThread, _frameInterval);
	}

	/**
	 * 自動スクロールを止める
	 */
	public void stopAutoScroll() {
		// 停止する
		getHandlerAnimation().removeCallbacks(_runAnimationThread);
		// 位置を戻す
		scrollTo(0, getScrollY());
	}

	/**
	 * 自動スクロールの状態更新
	 */
	public void updateAutoScroll() {
		int next_interval = _frameInterval;
		int x = getScrollX();

		if (getChildAt(0) == null) {
		} else if (getChildAt(0).getWidth() <= getWidth()) {
			// はみ出てない
			next_interval *= 2; // スクロールの必要が無いので間隔を広げる

			_isDerectionLeft = true;
			_prev_x = 0;
			x = 0;

		} else {
			// はみ出てる

			// 位置を計算
			if (_isDerectionLeft) {
				// 左へ
				x += _frameDelta;
			} else {
				// 右へ
				x -= _frameDelta;
			}
			// 向きを変える
			if (x == _prev_x) {
				_isDerectionLeft = !_isDerectionLeft;
				next_interval = _lapelInterval;
			}
			_prev_x = x;

		}

		// 移動
		scrollTo(x, getScrollY());

		// 次のセット
		getHandlerAnimation().postDelayed(_runAnimationThread, next_interval);
	}

}