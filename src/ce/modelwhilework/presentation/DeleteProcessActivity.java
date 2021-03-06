package ce.modelwhilework.presentation;

import java.io.File;
import java.util.ArrayList;

import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Process;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class DeleteProcessActivity extends Activity implements DialogInterface.OnClickListener  {

	private ListAdapterCheckBox listAdapter;
	private ArrayList<String> processes;
	private boolean closeActivity = false;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_process);
		
		activity = this;
		
		ListView lv_processes = (ListView) this.findViewById(R.id.activity_delete_process_listView);
		
		processes = new ArrayList<String>();
		loadProcesses();
		
		if(processes.size() > 0) {
			
			listAdapter = new ListAdapterCheckBox(
					this.getBaseContext(), R.layout.list_checkbox, processes);

				lv_processes.setAdapter(listAdapter);
				
				Button button_delete = (Button) this.findViewById(R.id.activity_delete_process_delete);
				button_delete.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(listAdapter.getSelectedItems().size() > 0)
							showDeleteQuestion("Do you really want to delete the selected processes from the device?");
						else
							showAlert("Please select a file!", false);
					}
				});			
		}
		else {
			showAlert("No processes to delete available!", true);
		}	
		
	}

	private void loadProcesses() {
		processes.clear();
		processes.addAll(new ArrayList<String>(ProcessManager.getInstance().getProcessesFromInternalStoreage()));
	}
	
	private void showAlert(String msg, boolean closeActivity) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
		
		this.closeActivity = closeActivity;
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
			                                		 
			                                		 boolean del = true;
			                                		 for(Process p : ProcessManager.getInstance().getProcesses()) {
			                                			 if(p.getFileTitle().equals(f)) {
			                                				 showAlert("Can't delete file: " + f + "!\nPlease close process first!", false);
			                                				 del = false;
			                                				 break;
			                                			 }
			                                		 }
			                                		 
			                                		 if(del) {
			                                			 if(!Process.deleteProcess(f.substring(0, f.indexOf("."))))
			                                				 showAlert("Error occured by process: " + f, false);
			                                		 }
			                                	 }				                                	 
			                                	 listAdapter.resetSelectedItems();
			                                	 loadProcesses();
			                                	 listAdapter.notifyDataSetChanged(); 
			                                 }
			                           }
	    );	    
	    alertDialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if(this.closeActivity)
			activity.finish();
		
	}
}
