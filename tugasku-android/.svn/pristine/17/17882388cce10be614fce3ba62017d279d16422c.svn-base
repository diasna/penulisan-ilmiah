package id.ac.gunadarma.tugasku.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TaskContentProvider extends ContentProvider {
	
	public static final UriMatcher URI_MATCHER = buildUriMatcher();
    public static final String PATH = "task";
    public static final int PATH_TOKEN = 100;
    public static final String PATH_FOR_ID = "task/*";
    public static final int PATH_FOR_ID_TOKEN = 200;
    
    public static final String AUTHORITY = "id.ac.gunadarma.tugasku.provider";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.tugasku.task";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.tugasku.task";

    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/task");
    
    private TaskSQLiteHelper dbHelper;
    
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;
        matcher.addURI(authority, PATH, PATH_TOKEN);
        matcher.addURI(authority, PATH_FOR_ID, PATH_FOR_ID_TOKEN);
        return matcher;
    }
    
    @Override
    public boolean onCreate() {
		Context ctx = getContext();
		dbHelper = new TaskSQLiteHelper(ctx);
		return true;
    }

    @Override
    public String getType(Uri uri) {
    	final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN:
                return CONTENT_TYPE_DIR;
            case PATH_FOR_ID_TOKEN:
                return CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(TaskSQLiteHelper.TABLE_TASK);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN: {
                int tvShowId = (int)ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(TaskSQLiteHelper.TABLE_TASK);
                builder.appendWhere(TaskSQLiteHelper.KEY_ID + "=" + tvShowId);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case PATH_TOKEN: {
                long id = db.insert(TaskSQLiteHelper.TABLE_TASK, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        int rowsDeleted = -1;
        switch (token) {
            case (PATH_TOKEN):
                rowsDeleted = db.delete(TaskSQLiteHelper.TABLE_TASK, selection, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN):
                String tvShowIdWhereClause = TaskSQLiteHelper.KEY_ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    tvShowIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(TaskSQLiteHelper.TABLE_TASK, tvShowIdWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (URI_MATCHER.match(uri)) {
		case PATH_TOKEN:
			break;
		case PATH_FOR_ID_TOKEN:
			String id = uri.getPathSegments().get(1);
			selection = TaskSQLiteHelper.KEY_ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int updateCount = db.update(TaskSQLiteHelper.TABLE_TASK, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
    }
}
