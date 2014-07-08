package no.srib.app.client.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

	private int statusCode;
	private HttpClient httpClient;
	private HttpResponseListener responseListener;

	public HttpAsyncTask(HttpResponseListener responseListener) {
		statusCode = -1;
		httpClient = new DefaultHttpClient();
		this.responseListener = responseListener;
	}

	@Override
	protected String doInBackground(String... url) {
		String responseString = null;

		try {
			HttpGet httpGet = new HttpGet(url[0]);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				httpResponse.getEntity().writeTo(out);
				out.close();
				responseString = out.toString("UTF-8");
			} else {
				Log.d("SriB", "HttpAsyncTask: HTTP status code " + statusCode);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("SriB", "HttpAsyncTask: " + e.getMessage());
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		responseListener.onResponse(statusCode, result);
	}

	public interface HttpResponseListener {
		void onResponse(int statusCode, String response);
	}
}
