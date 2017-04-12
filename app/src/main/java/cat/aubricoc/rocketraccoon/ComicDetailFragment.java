package cat.aubricoc.rocketraccoon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.model.Image;
import cat.aubricoc.rocketraccoon.service.ComicService;

public class ComicDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	private Integer comicId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			comicId = getArguments().getInt(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.comic_detail, container, false);

		new AsyncTask<Integer, Void, Comic>() {

			@Override
			protected Comic doInBackground(Integer... comicIds) {
				return ComicService.newInstance(getContext()).getComic(comicIds[0]);
			}

			@Override
			protected void onPostExecute(Comic comic) {
				fillData(comic);
			}
		}.execute(comicId);

		return rootView;
	}

	private void fillData(Comic comic) {
		View rootView = getView();
		if (rootView == null) {
			return;
		}

		CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
		if (appBarLayout != null) {
			appBarLayout.setTitle(comic.getTitle());
		}

		ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
		TextView titleView = (TextView) rootView.findViewById(R.id.title);
		TextView descriptionView = (TextView) rootView.findViewById(R.id.description);

		List<Image> images = comic.getImages();
		if (images != null && !images.isEmpty()) {
			Image image = images.get(new Random().nextInt(images.size()));
			Glide.with(getContext()).load(image.getUrl()).into(imageView);
		}

		titleView.setText(comic.getTitle());
		descriptionView.setText(Html.fromHtml(comic.getDescription()));
	}
}
