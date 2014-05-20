package com.xpto.manyfest.data;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
	private static DB db;

	public static void startDB(Context context) {
		if (db == null)
			db = new DB(context);
	}

	private static final String DATABASE_NAME = "cache.db";
	private static final int DATABASE_VERSION = 1;
	private static final String SQL_TABLE_CACHES = "caches";
	private static final String SQL_CREATE_CACHES = "CREATE TABLE [" + SQL_TABLE_CACHES + "] ( [name] TEXT PRIMARY KEY, [description] TEXT, [update] BIGINT );";

	private DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_CACHES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing

	}

	public static Cache get(String name) {
		if (db == null || name == null || name.length() == 0)
			return null;

		Cursor c;
		// Get cache from DB
		c = db.getReadableDatabase().query(SQL_TABLE_CACHES, new String[] { "[description]", "[update]" }, "[name] = ?", new String[] { name }, null, null,
				null);

		if (c.moveToFirst())
			return new Cache(name, c.getString(0), c.getLong(1));
		else
			return null;
	}

	public static void set(String name, String value) {
		if (db == null || name == null || name.length() == 0)
			return;

		ContentValues cValues = new ContentValues();

		cValues.put("[description]", value);
		cValues.put("[update]", new Date().getTime());

		if (get(name) != null) {
			// Update value and when
			db.getWritableDatabase().update(SQL_TABLE_CACHES, cValues, "[name] = ?", new String[] { name });
		} else {
			// Insert new cache
			cValues.put("[name]", name);

			db.getWritableDatabase().insert(SQL_TABLE_CACHES, null, cValues);
		}
	}

	public static void del(String name) {
		if (db == null || name == null || name.length() == 0)
			return;

		db.getWritableDatabase().delete(SQL_TABLE_CACHES, "[name] = ?", new String[] { name });
	}

	public static void del(String name, long before) {
		if (db == null || name == null || name.length() == 0)
			return;

		db.getWritableDatabase().delete(SQL_TABLE_CACHES, "[name] = ? AND [update] <= ?", new String[] { name, before + "" });
	}

	public static void delContaining(String name) {
		if (db == null || name == null || name.length() == 0)
			return;

		db.getWritableDatabase().delete(SQL_TABLE_CACHES, "[name] LIKE ?", new String[] { "%" + name + "%" });
	}

	public static void delAll() {
		if (db == null)
			return;

		db.getWritableDatabase().delete(SQL_TABLE_CACHES, null, null);
	}
}
