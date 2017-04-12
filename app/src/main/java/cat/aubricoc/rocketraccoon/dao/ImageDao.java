package cat.aubricoc.rocketraccoon.dao;

import android.content.Context;

import com.canteratech.apa.Dao;

import java.util.List;

import cat.aubricoc.rocketraccoon.db.DatabaseHelper;
import cat.aubricoc.rocketraccoon.model.Image;

public class ImageDao extends Dao<Image, Long> {

	private ImageDao(Context context) {
		super(new DatabaseHelper(context), Image.class);
	}

	public static ImageDao newInstance(Context context) {
		return new ImageDao(context);
	}

	public List<Image> getByComic(Integer comicId) {
		return getBy("comic=?", new String[]{fromIntegerToString(comicId)});
	}
}
