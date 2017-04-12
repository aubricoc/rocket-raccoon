package cat.aubricoc.rocketraccoon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.canteratech.apa.DatabaseReflection;

import java.util.List;

import cat.aubricoc.rocketraccoon.model.Comic;
import cat.aubricoc.rocketraccoon.model.Image;
import cat.aubricoc.rocketraccoon.utils.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> createTables = DatabaseReflection.getInstance().prepareCreateTables(Comic.class, Image.class);
		for (String sql : createTables) {
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sv, int oldVersion, int newVersion) {

	}
}
