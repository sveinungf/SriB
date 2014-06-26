package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.EmbeddedWebViewClient;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ArticleFragment extends BaseFragment {

	private String url;

	public static ArticleFragment newInstance(String url) {
		ArticleFragment fragment = new ArticleFragment();
		fragment.setUrl(url);

		return fragment;
	}

	public ArticleFragment() {
		url = null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_article, container,
				false);

		if (url != null) {
			WebView webView = (WebView) rootView
					.findViewById(R.id.webview_article);
			webView.setWebViewClient(new EmbeddedWebViewClient());
			webView.loadUrl(url);
		}

		return rootView;
	}
}
