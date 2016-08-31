package id.ac.gunadarma.tugasku.db;

import id.ac.gunadarma.tugasku.helper.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskSQLiteHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TaskDB";
	
	public static final String TABLE_TASK = "task";
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DEADLINE = "deadline";
	public static final String KEY_DONE = "done";
	public static final String KEY_DELETED = "deleted";
	public static final String KEY_REMOTE_ID = "remote";
	
	public TaskSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TASK_TABLE = 
				"CREATE TABLE "+TABLE_TASK+" ( "
					+KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+KEY_TITLE+ " TEXT, "
					+KEY_DEADLINE+ " INTEGER, "
					+KEY_DONE+ " INTEGER," 
					+KEY_DELETED+ " INTEGER,"
					+KEY_REMOTE_ID+ " TEXT)";
		db.execSQL(CREATE_TASK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_TASK);
		this.onCreate(db);
	}
	
	public void addTask(Task task){
		Log.d("addTask", task.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, task.getTitle());
		Calendar to = Calendar.getInstance();
		to.setTime(new Date(task.getDeadline()));
		values.put(KEY_DEADLINE, to.getTimeInMillis());
		values.put(KEY_DONE, task.isDone() ? 1 : 0);
		values.put(KEY_DELETED, task.isDeleted() ? 1 : 0);
		values.put(KEY_REMOTE_ID, task.getRemoteId());
		db.insert(TABLE_TASK, null, values);
		db.close();
	}
	
	public void markTaskDelete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DELETED, 1);
		int i = db.update(TABLE_TASK, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		Log.d("markTaskDelete("+id+")", i+"");
		db.close();
	}
	
	public void markTaskStatus(int id, boolean done){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DONE, done ? 1 : 0);
		int i = db.update(TABLE_TASK, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		Log.d("markTaskStatus("+id+")", i+"");
		db.close();
	}
	
	public void markSynced(int id, String remoteid){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_REMOTE_ID, remoteid);
		int i = db.update(TABLE_TASK, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		Log.d("markSynced("+id+") ["+remoteid+"]", i+"");
		db.close();
	}
	
	public boolean isSynced(String remoteId){
		String query = "SELECT * FROM "+TABLE_TASK+
				" WHERE "+KEY_REMOTE_ID+" = '"+remoteId+"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Task task = null;
		if (cursor.moveToFirst()) {
			do {
				task = new Task();
				task.setId(cursor.getInt(0));
				task.setTitle(cursor.getString(1));
				task.setDeadline(cursor.getLong(2));
				task.setDone(cursor.getInt(3) == 1 ? true : false);
				task.setDeleted(cursor.getInt(4) == 1 ? true : false);
			} while (cursor.moveToNext());
		}
		return task != null;
	}
	
	public int getNearDeadline(){
		String query = "SELECT * FROM "+TABLE_TASK+
				" WHERE "+KEY_DONE+" = '0'"+
				" AND "+KEY_DELETED+" = '0' ORDER BY "+KEY_DEADLINE+" ASC LIMIT 1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		int day = 0;
		if (cursor.moveToFirst()) {
			do {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(cursor.getLong(2)));
				day = Util.countDays(cal);
			} while (cursor.moveToNext());
		}
		Log.d("getNearDeadline()", day+" days");
		db.close();
		return day;
	}
	
	public List<Task> getTasks(boolean isDone){
		List<Task> tasks = new LinkedList<Task>();
		String query = "SELECT * FROM "+TABLE_TASK+
				" WHERE "+KEY_DONE+" = '"+(isDone? 1 : 0)+"'"+
				" AND "+KEY_DELETED+" = '0' ORDER BY "+KEY_DEADLINE+" ASC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Task task = null;
		if (cursor.moveToFirst()) {
			do {
				task = new Task();
				task.setId(cursor.getInt(0));
				task.setTitle(cursor.getString(1));
				task.setDeadline(cursor.getLong(2));
				task.setDone(cursor.getInt(3) == 1 ? true : false);
				task.setDeleted(cursor.getInt(4) == 1 ? true : false);
				task.setRemoteId(cursor.getString(5));
				tasks.add(task);
			} while (cursor.moveToNext());
		}
		Log.d("getTasks()", tasks.toString());
		db.close();
		return tasks;
	}
}
