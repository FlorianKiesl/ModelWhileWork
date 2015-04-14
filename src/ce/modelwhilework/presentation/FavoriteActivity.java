package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Task;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class FavoriteActivity extends Activity implements DialogInterface.OnClickListener {

	private ListAdapterTask listAdapterTask;
	private ListAdapterMsg listAdapterMsg;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		activity = this;
		
		ListView lv_tasks = (ListView) this.findViewById(R.id.activity_favorite_listView_task);
		ListView lv_msg = (ListView) this.findViewById(R.id.activity_favorite_listView_msg);

		listAdapterTask = new ListAdapterTask(this.getBaseContext(), R.layout.list_task, ProcessManager.getInstance().getFavoriteTasks());
		listAdapterMsg = new ListAdapterMsg(this.getBaseContext(), R.layout.list_msg, ProcessManager.getInstance().getFavoriteMessages());

		lv_tasks.setAdapter(listAdapterTask);
		lv_msg.setAdapter(listAdapterMsg);
		
		Button b = (Button) this.findViewById(R.id.activity_favorite_listView_button_load_task);
        b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Task t = ProcessManager.getInstance().getFavoriteTask(listAdapterTask.getSelectedItem());
				if(t != null) {
					ProcessManager.getInstance().getCurrentProcess().setTaskCard(t);
					activity.finish();
				}
				else
					showAlert("Load task fail!");					
			}
        	
        });
        
        b = (Button) this.findViewById(R.id.activity_favorite_listView_button_load_msg);
        b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Message m = ProcessManager.getInstance().getFavoriteMessage(listAdapterMsg.getSelectedItem());
				if(m != null) {
					ProcessManager.getInstance().getCurrentProcess().setMessageCard(m);
					activity.finish();
				}
				else
					showAlert("Load message fail!");						
			}
        });
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
