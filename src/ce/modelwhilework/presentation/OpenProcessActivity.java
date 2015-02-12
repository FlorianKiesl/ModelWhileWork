package ce.modelwhilework.presentation;

import java.io.File;
import java.util.ArrayList;

import ce.modelwhilework.data.Process;
import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class OpenProcessActivity extends Activity implements DialogInterface.OnClickListener  {

	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_process);
		activity = this;
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_open_process_listView);
		
		ArrayList<String> processes = new ArrayList<String>();
		
		String[] files = fileList();
		for(int i = 0; i < files.length; i++)
			processes.add(files[i].toString());
		
		final RadioButtonListAdapter listAdapter = new RadioButtonListAdapter(
			this.getBaseContext(), R.layout.list_radiobutton, processes);

		lv_processes.setAdapter(listAdapter);
		
		Button button_load = (Button) this.findViewById(R.id.activity_open_process_load);
		button_load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(ProcessManager.getInstance().openProcess(listAdapter.getSelectedItem()))
					activity.finish();
				else
					showAlert("Load process fail!!!");
			}
		});
		
		Button button_cancle = (Button) this.findViewById(R.id.activity_open_process_cancel);
		button_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
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
