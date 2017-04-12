package cat.aubricoc.rocketraccoon.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.model.Image;
import cat.aubricoc.rocketraccoon.service.ComicService;
import cat.aubricoc.rocketraccoon.utils.Constants;

public class ComicDetailFragment extends Fragment {

	private Integer comicId;

	private OnComicDetailLoadedListener onComicDetailLoadedListener;

	public void setOnComicDetailLoadedListener(OnComicDetailLoadedListener onComicDetailLoadedListener) {
		this.onComicDetailLoadedListener = onComicDetailLoadedListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		comicId = getArguments().getInt(Constants.ARG_COMIC_ID);

		String title = getArguments().getString(Constants.ARG_COMIC_TITLE);
		getActivity().setTitle(title);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.comic_detail, container, false);

		new AsyncTask<Integer, Void, Comic>() {

			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(getContext(), getString(R.string.loading), getString(R.string.loading_comic));
			}

			@Override
			protected Comic doInBackground(Integer... comicIds) {
				return ComicService.newInstance(getContext()).getComic(comicIds[0]);
			}

			@Override
			protected void onPostExecute(Comic comic) {
				fillData(comic);
				progressDialog.dismiss();
			}
		}.execute(comicId);

		return rootView;
	}

	private void fillData(Comic comic) {
		View rootView = getView();
		if (rootView == null) {
			return;
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

		if (onComicDetailLoadedListener != null) {
			onComicDetailLoadedListener.onComicDetailLoaded(this, comic);
		}
	}

	public interface OnComicDetailLoadedListener {
		void onComicDetailLoaded(Fragment fragment, Comic comic);
	}
}
