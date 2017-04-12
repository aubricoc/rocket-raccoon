package cat.aubricoc.rocketraccoon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.service.ComicService;

public class ComicListActivity extends AppCompatActivity {

	private boolean openDetailActivity = true;

	private List<Comic> comics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comic_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.comic_list);
		assert recyclerView != null;
		setupRecyclerView(recyclerView);

		if (findViewById(R.id.comic_detail_container) != null) {
			openDetailActivity = false;
		}
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
		comics = new ArrayList<>();
		final RecyclerView.Adapter adapter = new ComicsAdapter(comics);
		recyclerView.setAdapter(adapter);

		new AsyncTask<Void, Void, List<Comic>>() {

			@Override
			protected List<Comic> doInBackground(Void... voids) {
				return ComicService.newInstance(ComicListActivity.this).getComics();
			}

			@Override
			protected void onPostExecute(List<Comic> results) {
				comics.addAll(results);
				adapter.notifyDataSetChanged();
			}
		}.execute();

	}

	class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder> {

		private final List<Comic> mValues;

		ComicsAdapter(List<Comic> items) {
			mValues = items;
		}

		@Override
		public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.comic_list_content, parent, false);
			return new ComicViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ComicViewHolder holder, int position) {
			holder.comic = mValues.get(position);
			holder.title.setText(holder.comic.getTitle());

			Glide.with(ComicListActivity.this).load(holder.comic.getThumbnail().getUrl()).into(holder.thumbnail);

			holder.view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (openDetailActivity) {
						Intent intent = new Intent(ComicListActivity.this, ComicDetailActivity.class);
						intent.putExtra(ComicDetailFragment.ARG_ITEM_ID, holder.comic.getId());
						startActivity(intent);
					} else {
						Bundle arguments = new Bundle();
						arguments.putInt(ComicDetailFragment.ARG_ITEM_ID, holder.comic.getId());
						ComicDetailFragment fragment = new ComicDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction().replace(R.id.comic_detail_container, fragment).commit();
					}
				}
			});
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		class ComicViewHolder extends RecyclerView.ViewHolder {

			final View view;

			final ImageView thumbnail;

			final TextView title;

			Comic comic;

			ComicViewHolder(View view) {
				super(view);
				this.view = view;
				this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
				this.title = (TextView) view.findViewById(R.id.title);
			}
		}
	}
}
