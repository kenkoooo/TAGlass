package com.kenkoooo;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

/*
 * AsyncTask<型1, 型2,型3>
 *
 *   型1 … Activityからスレッド処理へ渡したい変数の型
 *          ※ Activityから呼び出すexecute()の引数の型
 *          ※ doInBackground()の引数の型
 *
 *   型2 … 進捗度合を表示する時に利用したい型
 *          ※ onProgressUpdate()の引数の型
 *
 *   型3 … バックグラウンド処理完了時に受け取る型
 *          ※ doInBackground()の戻り値の型
 *          ※ onPostExecute()の引数の型
 *
 *   ※ それぞれ不要な場合は、Voidを設定すれば良い
 */
public class ProblemUpdater extends AsyncTask<String, Void, Void> {

	private final String url = "http://141517d86da34611.lolipop.jp/wgp/update.php";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... strings) {// 例外処理は省いています
		// パラメータを生成
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment", strings[0]));
		HttpPost httpPost = new HttpPost(url);
		// パラメータを設定
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			DefaultHttpClient client = new DefaultHttpClient();
			client.execute(httpPost);
			client.getConnectionManager().shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 後はステータスコードやレスポンスを煮るなり焼くなり
		return null;
	}
}