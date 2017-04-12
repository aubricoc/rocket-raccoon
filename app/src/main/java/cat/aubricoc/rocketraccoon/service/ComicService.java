package cat.aubricoc.rocketraccoon.service;

import android.content.Context;

import com.canteratech.restclient.exception.NoConnectionException;

import java.util.ArrayList;
import java.util.List;

import cat.aubricoc.rocketraccoon.R;
import cat.aubricoc.rocketraccoon.dao.ComicDao;
import cat.aubricoc.rocketraccoon.dao.ImageDao;
import cat.aubricoc.rocketraccoon.locator.MarvelLocator;
import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.model.Image;

public class ComicService {

	private Context context;

	private ComicService(Context context) {
		this.context = context;
	}

	public static ComicService newInstance(Context context) {
		return new ComicService(context);
	}

	public List<Comic> getComics(int index) {
		int characterId = context.getResources().getInteger(R.integer.character_id);
		try {
			List<Comic> comics = MarvelLocator.newInstance(context).getComics(characterId, index);
			for (Comic comic : comics) {
				save(comic);
			}
			return comics;
		} catch (NoConnectionException e) {
			List<Comic> comics = ComicDao.newInstance(context).getAllPaginated(index);
			for (Comic comic : comics) {
				load(comic);
			}
			return comics;
		}
	}

	public Comic getComic(Integer comicId) {
		Comic comic = ComicDao.newInstance(context).getById(comicId);
		if (comic == null) {
			try {
				comic = MarvelLocator.newInstance(context).getComic(comicId);
				save(comic);
			} catch (NoConnectionException e) {
				return null;
			}
		} else {
			load(comic);
		}
		return comic;
	}

	private void save(Comic comic) {
		ComicDao comicDao = ComicDao.newInstance(context);
		if (comicDao.exists(comic.getId())) {
			return;
		}
		comicDao.create(comic);
		ImageDao imageDao = ImageDao.newInstance(context);
		if (comic.getThumbnail() != null) {
			comic.getThumbnail().setComic(comic);
			comic.getThumbnail().setThumbnail(true);
			imageDao.create(comic.getThumbnail());
		}
		if (comic.getImages() != null) {
			for (Image image : comic.getImages()) {
				image.setComic(comic);
				image.setThumbnail(false);
				imageDao.create(image);
			}
		}
	}

	private void load(Comic comic) {
		List<Image> images = ImageDao.newInstance(context).getByComic(comic.getId());
		comic.setImages(new ArrayList<Image>());
		for (Image image : images) {
			if (image.getThumbnail()) {
				comic.setThumbnail(image);
			} else {
				comic.getImages().add(image);
			}
		}
	}
}
