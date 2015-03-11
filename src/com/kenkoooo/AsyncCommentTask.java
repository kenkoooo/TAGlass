package com.kenkoooo;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class AsyncCommentTask extends AsyncTask<Void, Void, String[]> {

	private final String url = "http://141517d86da34611.lolipop.jp/wgp/json_comments.php";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String[] doInBackground(Void... voids) {
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpGet.setHeader("Connection", "Keep-Alive");

		ArrayList<String> commentList = new ArrayList<>();
		String json = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("");
			} else {
				json = EntityUtils.toString(response.getEntity(), "UTF-8");

				// JsonFactoryの生成
				JsonFactory factory = new JsonFactory();
				// JsonParserの取得
				JsonParser parser = factory.createJsonParser(json);

				// 配列の処理
				if (parser.nextToken() == JsonToken.START_ARRAY) {
					while (parser.nextToken() != JsonToken.END_ARRAY) {
						// 各オブジェクトの処理
						if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
							String comment = "";
							while (parser.nextToken() != JsonToken.END_OBJECT) {
								String name = parser.getCurrentName();
								parser.nextToken();
								if ("comment".equals(name)) {
									comment = parser.getText();
								} else {
									parser.skipChildren();
								}
							}
							commentList.add(comment);
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

		return commentList.toArray(new String[0]);
	}

	@Override
	protected void onPostExecute(String[] comments) {
		super.onPostExecute(comments);
	}

}
