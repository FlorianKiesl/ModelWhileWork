package ce.modelwhilework.presentation;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class DeleteProcessActivity extends Activity implements DialogInterface.OnClickListener  {

	private Activity activity;
	private ListAdapterCheckBox listAdapter;
	ArrayList<String> processes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_process);
		
		activity = this;
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_delete_process_listView);
		
		processes = new ArrayList<String>();
		
		loadProcesses();
		
		listAdapter = new ListAdapterCheckBox(
			this.getBaseContext(), R.layout.list_checkbox, processes);

		lv_processes.setAdapter(listAdapter);
		
		Button button_delete = (Button) this.findViewById(R.id.activity_delete_process_delete);
		button_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDeleteQuestion("Do you really want to delete the selected processes from the device?");
			}
		});
	}

	private void loadProcesses() {
		
		processes.clear();
		String[] files = fileList();
		for(int i = 0; i < files.length; i++) {
			if(files[i].endsWith(".mwyw"))
				processes.add(files[i].toString());
		}
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
	}
	
	private void showDeleteQuestion(String msg) {
		
		AlertDialog.Builder alertDialog =	new AlertDialog.Builder(this);
		alertDialog.setTitle("question");
		alertDialog.setMessage(msg);
		alertDialog.setNegativeButton("cancel",
				   new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                      }
                }
		);
		alertDialog.setPositiveButton("OK",
									   new DialogInterface.OnClickListener() {
			                                 public void onClick(DialogInterface dialog, int whichButton) {			                                	 

			                                	 for(String f : listAdapter.getSelectedItems()) {
			                                		 File file = new File(getFilesDir(), f);
			                                		 if(!file.delete())
			                                			 showAlert("Can't delete file: " + f);
			                                	 }	
			                                	 loadProcesses();
			                                	 listAdapter.notifyDataSetChanged(); 
			                                 }
			                           }
	    );	    
	    alertDialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
