package ce.modelwhilework.presentation;

import java.util.ArrayList;

import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class FavoriteActivity extends Activity {

	private ListAdapterTask listAdapterTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		ListView lv_tasks = (ListView) this	.findViewById(R.id.activity_favorite_listView_task);

		listAdapterTask = new ListAdapterTask(this.getBaseContext(), R.layout.list_task, ProcessManager.getInstance().getFavoriteTasks());

		lv_tasks.setAdapter(listAdapterTask);
	}
}
