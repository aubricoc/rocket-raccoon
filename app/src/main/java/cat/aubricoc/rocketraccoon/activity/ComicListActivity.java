package cat.aubricoc.rocketraccoon.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.adapter.ComicsAdapter;
import cat.aubricoc.rocketraccoon.fragment.ComicDetailFragment;
import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.service.ComicService;
import cat.aubricoc.rocketraccoon.utils.Constants;

public class ComicListActivity extends AppCompatActivity {

	private List<Comic> comics;

	private ArrayAdapter adapter;

	private Fragment detailFragment;

	private int index = 0;

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

	private void prepareList(ListView listView) {
		comics = new ArrayList<>();
		adapter = new ComicsAdapter(this, comics);
		listView.setAdapter(adapter);

		new GetComicsTask().execute(index);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				Comic comic = (Comic) adapterView.getItemAtPosition(position);
				view.setSelected(true);
				openFragment(comic);
			}
		});

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			private int previousLastItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				final int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (previousLastItem != lastItem) {
						previousLastItem = lastItem;
						index += Constants.NUM_PAGINATION;
						new GetComicsTask().execute(index);
					}
				}
			}

			public void clear() {
				previousLastItem = 0;
			}
		});
	}

	private void openFragment(Comic comic) {
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

	private class GetComicsTask extends AsyncTask<Integer, Void, List<Comic>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ComicListActivity.this, getString(R.string.loading), getString(R.string.loading_comics));
		}

		@Override
		protected List<Comic> doInBackground(Integer... indexes) {
			return ComicService.newInstance(ComicListActivity.this).getComics(indexes[0]);
		}

		@Override
		protected void onPostExecute(List<Comic> results) {
			comics.addAll(results);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}
	}
}
