package com.kenkoooo.twitter;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
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
public class AsyncTwitterTask extends
		AsyncTask<Void, Void, ResponseList<twitter4j.Status>> {

	private final String ckey = "3nVuSoBZnx6U4vzUxf5w";
	private final String ckey_secret = "Bcs59EFbbsdF6Sl9Ng71smgStWEGwXXKSjYvPVt7qys";
	private final String atoken = "2861904956-qDXGAkruu0OjVTNO2L4fIi5RWW4E1K0wFIXve48";
	private final String atoken_secret = "JWpHZLir9aU9AHqUt8Oi2D67ptfrf9jm3j7UMdgcNcDzT";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected ResponseList<twitter4j.Status> doInBackground(Void... voids) {
		// 下準備
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(ckey)
				.setOAuthConsumerSecret(ckey_secret)
				.setOAuthAccessToken(atoken)
				.setOAuthAccessTokenSecret(atoken_secret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		ResponseList<twitter4j.Status> result = null;
		try {
			result = twitter.getMentionsTimeline();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(ResponseList<twitter4j.Status> result) {
		super.onPostExecute(result);
	}

}