package id.ac.gunadarma.tugasku.db;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;


public class Task {
	private int id;
	private String title;
	private long deadline;
	private boolean done;
	private boolean deleted = false;
	private String remoteId;
	
	public Task(int id, String title, long deadline, boolean done, boolean deleted, String remoteId) {
		this.id = id;
		this.title = title;
		this.deadline = deadline;
		this.done = done;
		this.deleted = deleted;
		this.remoteId = remoteId;
	}

	public Task(String title, long deadline, boolean done, boolean deleted, String remoteId) {
		this.title = title;
		this.deadline = deadline;
		this.done = done;
		this.deleted = deleted;
		this.remoteId = remoteId;
	}

	public Task() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", deadline=" + deadline
				+ ", done=" + done + ", deleted=" + deleted + ", remoteId="
				+ remoteId + "]";
	}

	public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TaskSQLiteHelper.KEY_ID, getId());
        values.put(TaskSQLiteHelper.KEY_TITLE, getTitle());
        values.put(TaskSQLiteHelper.KEY_DEADLINE, getDeadline());
        values.put(TaskSQLiteHelper.KEY_DONE, isDone());
        values.put(TaskSQLiteHelper.KEY_DELETED, isDeleted());
        values.put(TaskSQLiteHelper.KEY_REMOTE_ID, getRemoteId());
        return values;
    }

    public static Task fromCursor(Cursor curTvShows) {
    	int id = curTvShows.getInt(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_ID));
    	String title = curTvShows.getString(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_TITLE));
    	long deadline = curTvShows.getLong(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_DEADLINE));
    	boolean done = curTvShows.getInt(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_DONE)) == 1 ? true : false;           
    	boolean deleted = curTvShows.getInt(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_DELETED)) == 1 ? true : false;
    	String remoteId = curTvShows.getString(curTvShows.getColumnIndex(TaskSQLiteHelper.KEY_REMOTE_ID));
        return new Task(id, title, deadline, done, deleted, remoteId);
    }
    
    public id.ac.gunadarma.tugasku.helper.dao.Task getTaskRemote(){
    	return new id.ac.gunadarma.tugasku.helper.dao.Task(title, new Date(deadline), done, deleted);
    }
}
