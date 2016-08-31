package id.ac.gunadarma.tugasku.sync;

import id.ac.gunadarma.tugasku.account.AccountGeneral;
import id.ac.gunadarma.tugasku.db.Task;
import id.ac.gunadarma.tugasku.db.TaskContentProvider;
import id.ac.gunadarma.tugasku.db.TaskSQLiteHelper;
import id.ac.gunadarma.tugasku.helper.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class TaskSyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String TAG = "TaskSyncAdapter";
	
	private final AccountManager mAccountManager;

    public TaskSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }
    
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
            ContentProviderClient provider, SyncResult syncResult) {
    	StringBuilder sb = new StringBuilder();
        if (extras != null) {
            for (String key : extras.keySet()) {
                sb.append(key + "[" + extras.get(key) + "] ");
            }
        }
		Log.d(TAG, "onPerformSync for account[" + account.name + "], Extras: " + sb.toString());
		
		try {
			
			String authToken = mAccountManager.blockingGetAuthToken(account, AccountManager.KEY_AUTHTOKEN, true);
	        String userObjectId = mAccountManager.getUserData(account, AccountGeneral.USERDATA_USER_OBJ_ID);
	        
	        TaskSQLiteHelper sqLiteHelper = new TaskSQLiteHelper(getContext());
	        
	        Log.d(TAG, "onPerformSync [" + authToken + "]. userObjectId: " + userObjectId);
	        
	        List<id.ac.gunadarma.tugasku.helper.dao.Task> remoteTaskList = Api.getTaskList(authToken);
	        Log.d(TAG, "onPerformSync, Remote Task: " + remoteTaskList.toString());
	        
	        ArrayList<Task> localTaskList = new ArrayList<Task>();
	        Cursor curTvShows = provider.query(TaskContentProvider.CONTENT_URI, null, null, null, null);
	        if (curTvShows != null) {
	            while (curTvShows.moveToNext()) {
	            	localTaskList.add(Task.fromCursor(curTvShows));
	            }
	            curTvShows.close();
	        }
	        Log.d(TAG, "onPerformSync, Local Task: " + localTaskList.toString());
	        
	        ArrayList<Task> taskToRemote = new ArrayList<Task>();
	        for (Task localTask : localTaskList) {
	            if("".equals(localTask.getRemoteId()))
	            	taskToRemote.add(localTask);
	        }
	        Log.d(TAG, "onPerformSync, Task to Remote: " + taskToRemote.toString());
	        
	        ArrayList<Task> taskToLocal = new ArrayList<Task>();
            for (id.ac.gunadarma.tugasku.helper.dao.Task remoteTask : remoteTaskList) {
            	if(!sqLiteHelper.isSynced(remoteTask.id))
            		taskToLocal.add(remoteTask.getTaskLocal());
            }
            Log.d(TAG, "onPerformSync, Task to Local: " + taskToLocal.toString());
        
            if (taskToRemote.size() == 0) {
                Log.d("Task", TAG + "> No local changes to update server");
            } else {
                Log.d("Task", TAG + "> Updating remote server with local changes");
                for (Task remoteTask : taskToRemote) {
                    Log.d("Task", TAG + "> Local -> Remote [" + remoteTask.getId() + "]");
                    String id = Api.postTask(remoteTask.getTaskRemote(), authToken);
                    if(!"".equals(id)){
                    	sqLiteHelper.markSynced(remoteTask.getId(), id);
                    }
                }
            }
            
            if (taskToLocal.size() == 0) {
                Log.d("Task", TAG + "> No server changes to update local database");
            } else {
                Log.d("Task", TAG + "> Updating local database with remote changes");
                int i = 0;
                ContentValues showsToLocalValues[] = new ContentValues[taskToLocal.size()];
                for (Task localTask : taskToLocal) {
                    Log.d("Task", TAG + "> Remote -> Local [" + localTask.getRemoteId() + "]");
                    showsToLocalValues[i++] = localTask.getContentValues();
                }
                provider.bulkInsert(TaskContentProvider.CONTENT_URI, showsToLocalValues);
            }
            Log.d("Task", TAG + "> Finished.");
            
		} catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            syncResult.stats.numAuthExceptions++;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
