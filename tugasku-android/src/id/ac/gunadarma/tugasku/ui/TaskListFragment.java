package id.ac.gunadarma.tugasku.ui;

import java.text.SimpleDateFormat;

import id.ac.gunadarma.tugasku.R;
import id.ac.gunadarma.tugasku.db.Task;
import id.ac.gunadarma.tugasku.db.TaskSQLiteHelper;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockListFragment;

public class TaskListFragment extends SherlockListFragment {
	TaskAdapter adapter;
	TaskSQLiteHelper taskSQLiteHelper;
	int id = -1;
	boolean task = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_list, null);
	}

	public void reload(boolean task) {
		TaskAdapter adapter = new TaskAdapter(getActivity());
		adapter.addAll(taskSQLiteHelper.getTasks(task));
		setListAdapter(adapter);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				taskSQLiteHelper.markTaskDelete(id);
				reload(!task);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				id = -1;
				break;
			}
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new TaskAdapter(getActivity());
		taskSQLiteHelper = new TaskSQLiteHelper(getActivity());
		task = getArguments().getBoolean("task");
		if (task) {
			adapter.addAll(taskSQLiteHelper.getTasks(false));
		} else {
			adapter.addAll(taskSQLiteHelper.getTasks(true));
		}
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				Task task = ((Task) getListAdapter().getItem(arg2));
				builder.setMessage(task.getTitle()).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
				builder.setTitle("Deadline: "+new SimpleDateFormat("dd-MM-yyyy").format(task.getDeadline()));
				builder.create().show();

			}
		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Task task = ((Task) getListAdapter().getItem(position));
				id = task.getId();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Hapus Data " + task.getTitle() + " ?")
						.setPositiveButton("Ya", dialogClickListener)
						.setNegativeButton("Tidak", dialogClickListener).show();
				return false;
			}
		});
		setListAdapter(adapter);
	}
}
