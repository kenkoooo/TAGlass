package com.kenkoooo;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

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
public class AsyncJSONTask extends
		AsyncTask<Void, Void, HashMap<String, Integer>> {

	private final String url = "http://141517d86da34611.lolipop.jp/wgp/json.php";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected HashMap<String, Integer> doInBackground(Void... voids) {
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpGet.setHeader("Connection", "Keep-Alive");

		HashMap<String, Integer> hashMap = new HashMap<>();
		String receive = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("");
			} else {
				receive = EntityUtils.toString(response.getEntity(), "UTF-8");

				// JsonFactoryの生成
				JsonFactory factory = new JsonFactory();
				// JsonParserの取得
				JsonParser parser = factory.createJsonParser(receive);

				// 配列の処理
				if (parser.nextToken() == JsonToken.START_ARRAY) {
					while (parser.nextToken() != JsonToken.END_ARRAY) {
						// 各オブジェクトの処理
						if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
							String ans = "";
							int count = 0;

							while (parser.nextToken() != JsonToken.END_OBJECT) {
								String name = parser.getCurrentName();
								parser.nextToken();
								if ("ANS".equals(name)) {
									ans = parser.getText();
								} else if ("COUNT".equals(name)) {
									count = Integer.parseInt(parser.getText());
								} else {
									parser.skipChildren();
								}
							}

							hashMap.put(ans, count);
						} else {
							parser.skipChildren();
						}
					}
				} else {
					parser.skipChildren();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hashMap;
	}

	@Override
	protected void onPostExecute(HashMap<String, Integer> hashMap) {
		super.onPostExecute(hashMap);
	}

}