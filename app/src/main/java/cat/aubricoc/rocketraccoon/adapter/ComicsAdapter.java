package cat.aubricoc.rocketraccoon.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.model.Comic;

public class ComicsAdapter extends ArrayAdapter<Comic> {

	public ComicsAdapter(Context context, List<Comic> comics) {
		super(context, R.layout.comic_list_content, comics);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View view = convertView;
		ComicViewHolder holder;
		Comic comic = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.comic_list_content, parent, false);
			holder = new ComicViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ComicViewHolder) view.getTag();
		}

		if (comic != null) {
			holder.title.setText(comic.getTitle());
			Glide.with(getContext()).load(comic.getThumbnail().getUrl()).centerCrop().crossFade().into(holder.thumbnail);
		}
		return view;
	}

	private class ComicViewHolder {

		final ImageView thumbnail;

		final TextView title;

		ComicViewHolder(View view) {
			this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			this.title = (TextView) view.findViewById(R.id.title);
		}
	}
}
