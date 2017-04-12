package cat.aubricoc.rocketraccoon.service;

import android.content.Context;

import java.util.List;

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.locator.MarvelLocator;
import cat.aubricoc.rocketraccoon.model.Comic;

public class ComicService {

	private Context context;

	private ComicService(Context context) {
		this.context = context;
	}

	public static ComicService newInstance(Context context) {
		return new ComicService(context);
	}

	public List<Comic> getComics() {
		int characterId = context.getResources().getInteger(R.integer.character_id);
		List<Comic> comics = MarvelLocator.newInstance().getComics(characterId);
		return comics;
	}

	public Comic getComic(Integer comicId) {
		return MarvelLocator.newInstance().getComic(comicId);
	}
}
