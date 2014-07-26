package no.srib.app.client.fragment;

import no.srib.app.client.util.BusProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	protected abstract View onBaseCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public final View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View view = onBaseCreateView(inflater, container, savedInstanceState);

		// view may be null if a fragment does not support the current
		// orientation
		if (view != null) {
			BusProvider.INSTANCE.get().post(this);
		}

		return view;
	}
}
