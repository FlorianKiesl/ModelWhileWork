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

	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_process);
		activity = this;
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_load_process_listView);
		
		ArrayList<String> processes = new ArrayList<String>();
		
		String[] files = fileList();
		for(int i = 0; i < files.length; i++) {
			if(files[i].endsWith(".mwyw"))
				processes.add(files[i].toString());
		}
		
		final ListAdapterRadioButton listAdapter = new ListAdapterRadioButton(
			this.getBaseContext(), R.layout.list_radiobutton, processes);

		lv_processes.setAdapter(listAdapter);
		
		Button button_load = (Button) this.findViewById(R.id.activity_load_process_load);
		button_load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String processName = listAdapter.getSelectedItem();
				int pos;
				if((pos = processName.indexOf(".")) > 0)
					processName = processName.substring(0, pos);
				
				if(ProcessManager.getInstance().openProcess(processName))
					activity.finish();
				else
					showAlert("Load process fail!!!");
			}
		});
		
		Button button_cancle = (Button) this.findViewById(R.id.activity_load_process_cancel);
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
