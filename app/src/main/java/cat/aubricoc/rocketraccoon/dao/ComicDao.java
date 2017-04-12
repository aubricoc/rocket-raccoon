package cat.aubricoc.rocketraccoon.dao;

import android.content.Context;

import com.canteratech.apa.Dao;

import java.util.List;

import cat.aubricoc.rocketraccoon.db.DatabaseHelper;
import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.utils.Constants;

public class ComicDao extends Dao<Comic, Integer> {

	private ComicDao(Context context) {
		super(new DatabaseHelper(context), Comic.class);
	}

	public static ComicDao newInstance(Context context) {
		return new ComicDao(context);
	}

	public List<Comic> getAllPaginated(int index) {
		return getAll(null, index + "," + Constants.NUM_PAGINATION);
	}
}
