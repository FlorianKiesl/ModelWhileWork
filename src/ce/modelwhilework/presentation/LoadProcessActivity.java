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
	private boolean internal = true, closeActivity = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_process);
		activity = this;
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if(bundle.getString("IMPORT") != null) 
				internal = false;
		}
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_load_process_listView);
		
		processes = new ArrayList<String>();
		loadProcesses();
		
		if(processes.size() > 0) {
			
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
						showAlert("Please select a file!", false);
					else if(ProcessManager.getInstance().getProcess(processName) != null)
						showAlert("Process is already open!!!", false);				
					else if(internal && ProcessManager.getInstance().openProcess(processName))
						activity.finish();
					else if(!internal && ProcessManager.getInstance().importProcess(processName))
						activity.finish();
					else if(!internal)
						showAlert("Import process fail!!! Please remove process from internal store before importing from external!", false);
					else
						showAlert("Open process fail!!!", false);
					
					if(processes.size() == 0)
						activity.finish();
				}
			});
		}
		else {
			showAlert("No processes to load available!", true);
		}	
	}
	
	private void loadProcesses() {
		
		processes.clear();
		if(internal)
			processes = new ArrayList<String>(ProcessManager.getInstance().getProcessesFromInternalStoreage());
		else
			processes = new ArrayList<String>(ProcessManager.getInstance().getProcessesFromExternalStoreage());
	}

	private void showAlert(String msg, boolean closeActivity) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
		
		this.closeActivity = closeActivity;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if(this.closeActivity)
			activity.finish();
		
	}
}
