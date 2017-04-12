package cat.aubricoc.rocketraccoon.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.fragment.ComicDetailFragment;
import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.service.ComicService;
import cat.aubricoc.rocketraccoon.utils.Constants;

public class ComicListActivity extends AppCompatActivity {

	private List<Comic> comics;

	private Fragment detailFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comic_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		ListView listView = (ListView) findViewById(R.id.comic_list);
		prepareList(listView);
	}

	private void prepareList(@NonNull ListView listView) {
		comics = new ArrayList<>();
		final ArrayAdapter adapter = new ComicsAdapter(comics);
		listView.setAdapter(adapter);

		new AsyncTask<Void, Void, List<Comic>>() {

			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(ComicListActivity.this, getString(R.string.loading), getString(R.string.loading_comics));
			}

			@Override
			protected List<Comic> doInBackground(Void... voids) {
				return ComicService.newInstance(ComicListActivity.this).getComics();
			}

			@Override
			protected void onPostExecute(List<Comic> results) {
				comics.addAll(results);
				adapter.notifyDataSetChanged();
				progressDialog.dismiss();
			}
		}.execute();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				Comic comic = (Comic) adapterView.getItemAtPosition(position);
				view.setSelected(true);

				Bundle arguments = new Bundle();
				arguments.putInt(Constants.ARG_COMIC_ID, comic.getId());
				arguments.putString(Constants.ARG_COMIC_TITLE, comic.getTitle());
				ComicDetailFragment fragment = new ComicDetailFragment();
				fragment.setArguments(arguments);
				fragment.setOnComicDetailLoadedListener(new ComicDetailFragment.OnComicDetailLoadedListener() {
					@Override
					public void onComicDetailLoaded(Fragment fragment, Comic comic) {
						detailFragment = fragment;
					}
				});
				getSupportFragmentManager().beginTransaction().replace(R.id.comic_detail_container, fragment).commit();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (detailFragment == null) {
			super.onBackPressed();
		} else {
			getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
			detailFragment = null;
			setTitle(R.string.app_name);
		}
	}

	private class ComicsAdapter extends ArrayAdapter<Comic> {

		ComicsAdapter(List<Comic> comics) {
			super(ComicListActivity.this, R.layout.comic_list_content, comics);
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			View view = convertView;
			ComicViewHolder holder;
			Comic comic = getItem(position);
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.comic_list_content, parent, false);
				holder = new ComicViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ComicViewHolder) view.getTag();
			}

			if (comic != null) {
				holder.title.setText(comic.getTitle());
				Glide.with(ComicListActivity.this).load(comic.getThumbnail().getUrl()).centerCrop().crossFade().into(holder.thumbnail);
			}
			return view;
		}

		class ComicViewHolder {

			final ImageView thumbnail;

			final TextView title;

			ComicViewHolder(View view) {
				this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
				this.title = (TextView) view.findViewById(R.id.title);
			}
		}
	}
}
