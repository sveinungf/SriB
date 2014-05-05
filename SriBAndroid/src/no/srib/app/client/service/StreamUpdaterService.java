package no.srib.app.client.service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener.Status;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class StreamUpdaterService extends Service {

	private static final int MAX_TIMER_FAILS = 2;
	private static final int TIMER_FAIL_TRESHOLD = 10000;

	private final IBinder BINDER;
	private final ObjectMapper MAPPER;

	private AtomicBoolean updating;
	private AtomicInteger timerFails;
	private Handler timerHandler;
	private Runnable streamScheduleUpdater;
	private OnStreamUpdateListener streamUpdateListener;

	public StreamUpdaterService() {
		BINDER = new StreamUpdaterBinder();
		MAPPER = new ObjectMapper();
	}

	public void setStreamUpdateListener(
			OnStreamUpdateListener streamUpdateListener) {
		this.streamUpdateListener = streamUpdateListener;
	}

	public void updateFrom(String updateURL) {
		if (streamScheduleUpdater == null) {
			streamScheduleUpdater = new StreamScheduleUpdater(updateURL);
			timerHandler.postDelayed(streamScheduleUpdater, 0);
			updating.set(true);
		}
	}

	public boolean isUpdating() {
		return updating.get();
	}

	public void update() {
		if (streamScheduleUpdater != null) {
			timerHandler.postDelayed(streamScheduleUpdater, 0);
			updating.set(true);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		updating = new AtomicBoolean(false);
		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();
		streamScheduleUpdater = null;
		streamUpdateListener = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		timerHandler.removeCallbacks(streamScheduleUpdater);
		updating.set(false);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return BINDER;
	}

	public class StreamUpdaterBinder extends Binder {
		public StreamUpdaterService getService() {
			return StreamUpdaterService.this;
		}
	}

	public interface OnStreamUpdateListener {
		enum Status {
			NO_INTERNET,
			SERVER_UNREACHABLE,
			INVALID_RESPONSE,
			CONNECTING
		}

		void onStatus(Status status);

		void onStreamUpdate(StreamSchedule streamSchedule);
	}

	private class StreamScheduleUpdater implements Runnable {

		private String updateURL;

		public StreamScheduleUpdater(String updateURL) {
			this.updateURL = updateURL;
		}

		@Override
		public void run() {
			HttpAsyncTask streamScheduleTask = new HttpAsyncTask(
					new StreamScheduleResponseListener());
			streamScheduleTask.execute(updateURL);

			streamUpdateListener.onStatus(Status.CONNECTING);
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(String response) {
			updating.set(false);

			if (response == null) {
				if (!isNetworkAvailable()) {
					streamUpdateListener.onStatus(Status.NO_INTERNET);
				} else {
					streamUpdateListener.onStatus(Status.SERVER_UNREACHABLE);
				}
			} else {
				Log.d("SriB", response);

				try {
					StreamSchedule streamSchedule = MAPPER.readValue(response,
							StreamSchedule.class);

					if (streamUpdateListener != null) {
						streamUpdateListener.onStreamUpdate(streamSchedule);
					}

					long delay = streamSchedule.getTime()
							- System.currentTimeMillis();

					Log.d("SriB", "delay: " + delay);

					if (delay < TIMER_FAIL_TRESHOLD) {
						timerFails.incrementAndGet();
						Log.d("SriB", "timerFails: " + timerFails.get());
					} else {
						timerFails.set(0);
					}

					if (timerFails.get() < MAX_TIMER_FAILS) {
						timerHandler.removeCallbacks(streamScheduleUpdater);
						timerHandler.postDelayed(streamScheduleUpdater, delay);
					} else {
						Log.d("SriB", "Time on client is set too far ahead");
					}
				} catch (IOException e) {
					streamUpdateListener.onStatus(Status.INVALID_RESPONSE);
				}
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}