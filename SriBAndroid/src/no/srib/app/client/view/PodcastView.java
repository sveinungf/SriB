package no.srib.app.client.view;

import no.srib.app.client.R;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.FontFactory;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PodcastView extends LinearLayout {

	private static final int DEFAULT_IMAGE_ID = R.drawable.podcast_default_art;
	private static final int FONT_ID = R.string.font_clairehandbold;

	@InjectView(R.id.imageView_podcastItem_art) ImageView imageView;
	@InjectView(R.id.textView_podcastItem_date) TextView dateTextView;
	@InjectView(R.id.textView_podcastItem_programname) TextView programNameTextView;

	private Drawable defaultImage;

	public PodcastView(final Context context) {
		super(context);
		init(context);
	}

	public PodcastView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(final Context context) {
		setOrientation(VERTICAL);

		LayoutInflater.from(context).inflate(R.layout.griditem_podcast, this,
				true);
		ButterKnife.inject(this);

		defaultImage = context.getResources().getDrawable(DEFAULT_IMAGE_ID);
		Typeface font = FontFactory.INSTANCE.getFont(context, FONT_ID);
		dateTextView.setTypeface(font);
		programNameTextView.setTypeface(font);
	}

	public void showPodcast(final Podcast podcast, final String formattedDate) {
		dateTextView.setText(formattedDate);
		programNameTextView.setText(podcast.getProgram());

		UrlImageViewHelper.setUrlDrawable(imageView, podcast.getImageUrl(),
				defaultImage);
	}
}