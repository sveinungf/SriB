package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.listener.OnFragmentReadyListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArticleListFragment extends Fragment {

	private SearchView searchView;
	private ListView listView;
	private OnFragmentReadyListener readyListener;
	private OnItemClickListener articleClickedListener;
	private OnSearchListener searchListener;

	public static ArticleListFragment newInstance(
			OnItemClickListener articleClickedListener) {

		ArticleListFragment fragment = new ArticleListFragment();
		fragment.setArticleClickedListener(articleClickedListener);

		return fragment;
	}

	public ArticleListFragment() {
		searchView = null;
		listView = null;
		readyListener = null;
		articleClickedListener = null;
	}

	private void setArticleClickedListener(
			OnItemClickListener articleClickedListener) {
		this.articleClickedListener = articleClickedListener;
	}

	public void setSearchListener(OnSearchListener searchListener) {
		this.searchListener = searchListener;
	}

	public void setArticleListAdapter(ArticleListAdapter adapter) {
		if (listView != null) {
			listView.setAdapter(adapter);
		}
	}

	public void resetSearchView() {
		if (searchView != null) {
			searchView.setQuery("", false);
			searchView.clearFocus();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_articlelist,
				container, false);

		searchView = (SearchView) rootView
				.findViewById(R.id.searchView_articleList);
		searchView.setOnQueryTextListener(new SearchTextListener());

		listView = (ListView) rootView.findViewById(R.id.listView_articleList);
		listView.setOnItemClickListener(articleClickedListener);

		if (readyListener != null) {
			readyListener.onFragmentReady(this);
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			readyListener = (OnFragmentReadyListener) getActivity();
		} catch (ClassCastException e) {
			readyListener = null;
		}
	}

	public interface OnSearchListener {
		void onSearch(String query);
	}

	private class SearchTextListener implements OnQueryTextListener {

		@Override
		public boolean onQueryTextSubmit(String query) {
			searchListener.onSearch(query);
			searchView.clearFocus();
			return true;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			return false;
		}
	}
}
