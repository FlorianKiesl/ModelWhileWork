package ce.modelwhilework.presentation;

import java.util.ArrayList;

import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class LoadProcessActivity extends Activity implements DialogInterface.OnClickListener  {

	private ListAdapterRadioButton listAdapter;
	private ArrayList<String> processes;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_process);
		activity = this;
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_load_process_listView);
		
		processes = new ArrayList<String>();
		loadProcesses();
		
		 listAdapter = new ListAdapterRadioButton(this.getBaseContext(), R.layout.list_radiobutton, processes);

		lv_processes.setAdapter(listAdapter);
		
		Button button_load = (Button) this.findViewById(R.id.activity_load_process_load);
		button_load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String processName = listAdapter.getSelectedItem();
				int pos;
				if((pos = processName.indexOf(".")) > 0)
					processName = processName.substring(0, pos);
				
				if(processName.length() == 0)
					showAlert("Please select a file!");
				else if(ProcessManager.getInstance().getProcess(processName) != null)
					showAlert("Process is already open!!!");				
				else if(ProcessManager.getInstance().openProcess(processName))
					activity.finish();
				else
					showAlert("Open process fail!!!");
			}
		});
	}
	
	private void loadProcesses() {
		
		processes.clear();
		processes = new ArrayList<String>(ProcessManager.getInstance().getProcessesFromInternalStoreage());
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
