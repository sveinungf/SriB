package no.srib.app.client.asynctask;

import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ListBasedAdapter;
import no.srib.app.client.dao.ArticleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.memory.ArticleCacheDAOImpl;
import no.srib.app.client.dao.retrofit.ArticleDAOImpl;
import no.srib.app.client.model.Article;
import no.srib.app.client.util.NetworkUtil;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ArticleAsyncTask extends
		CacheAwareAsyncTask<String, Void, List<Article>> {

	private static final int CACHE_VALIDITY_SECONDS = 3600;

	private final Context context;
	private final ListBasedAdapter<Article> adapter;

	private boolean searching;
	private Exception exception;
	private List<Article> cacheResult;

	public ArticleAsyncTask(final Context context,
			final ListBasedAdapter<Article> adapter) {

		super(ArticleCacheDAOImpl.INSTANCE);

		this.context = context;
		this.adapter = adapter;
		searching = false;
		exception = null;
		cacheResult = null;
	}

	@Override
	protected List<Article> doInBackground(final String... queries) {
		List<Article> result = null;
		String query = null;

		if (queries != null && queries.length > 0) {
			// Searching
			query = queries[0];
			searching = true;
		} else {
			// Not searching, we can check the cache
			result = checkCache();
		}

		if (result == null && NetworkUtil.networkAvailable(context)) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			ArticleDAO dao = new ArticleDAOImpl(restApiUrl);

			try {
				result = dao.getList(query);
			} catch (DAOException e) {
				this.exception = e;
			}

			// If query == null then the result is not a search result
			if (query == null && result != null) {
				long expiration = System.currentTimeMillis()
						+ CACHE_VALIDITY_SECONDS * 1000;
				cache(result, expiration);
			}
		}

		return result;
	}

	@Override
	protected void onExpiredCache(final List<Article> cacheResult) {
		this.cacheResult = cacheResult;
		publishProgress();
	}

	@Override
	protected void onProgressUpdate(final Void... values) {
		adapter.setList(cacheResult);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecute(final List<Article> result) {
		if (exception != null) {
			Log.d("SriB", "ArticleAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
		} else if (result == null) {
			if (searching) {
				Toast.makeText(context, R.string.toast_articles_nocontent,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			adapter.setList(result);
			adapter.notifyDataSetChanged();
		}
	}
}
