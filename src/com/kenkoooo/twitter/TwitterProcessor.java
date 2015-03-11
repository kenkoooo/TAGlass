package com.kenkoooo.twitter;

import java.util.concurrent.ExecutionException;

import twitter4j.ResponseList;
import twitter4j.Status;
import android.util.Log;

public class TwitterProcessor {

	public void getMentions() {
		AsyncTwitterTask task = new AsyncTwitterTask();
		task.execute();
		try {
			ResponseList<Status> list = task.get();
			for (Status status : list) {
				Log.i("aaaaaaaaaaaaa", status.getText());
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
