package ce.modelwhilework.presentation;

import java.io.File;
import java.util.ArrayList;

import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ExportProcessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_export_process);
		
		
		
		Spinner spinnerExportType = (Spinner) this.findViewById(R.id.activity_export_process_exportType);
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.add("Metasonic");
		adapter.add("Model While Work");
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerExportType.setAdapter(adapter);
		spinnerExportType.setSelection(0);
		
		ListView lvExportPath = (ListView) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportPath);
		ArrayList<String> alExportPath = new ArrayList<String>();
		File sdCard = Environment.getExternalStorageDirectory();
		
		if (sdCard.exists() && sdCard.canWrite()){
			
		}
		alExportPath.add("External SD Card");
		alExportPath.add("PHP-Server (http://stefanoppl.net)");

		final ListAdapterRadioButton adapterLv = new ListAdapterRadioButton(getBaseContext(),
				R.layout.list_radiobutton, alExportPath);
		lvExportPath.setAdapter(adapterLv);
		
		Button btnSenden = (Button) this.findViewById(R.id.activity_export_process_btnSenden);
		btnSenden.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Spinner spinner = (Spinner) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportType);
				spinner.getSelectedItemId();
				
				ListView lvExportPath = (ListView) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportPath);
				
				if (spinner.getSelectedItemId() == 0){
					if(lvExportPath.getSelectedItemId() == 0){
						ProcessManager.getInstance().exportProcessMetasonicExternal(ProcessManager.getInstance().getCurrentProcess().getTitle());
					}
					else{
						UploadMetasonicProcess uploadProcess = new UploadMetasonicProcess();
						uploadProcess.execute(ProcessManager.getInstance().getCurrentProcess().getTitle());
					}
				} else{
					if(lvExportPath.getSelectedItemId() == 0){
						ProcessManager.getInstance().exportProcessExternal(ProcessManager.getInstance().getCurrentProcess().getTitle());
					}
					else{
						UploadProcess uploadProcess = new UploadProcess();
						uploadProcess.execute(ProcessManager.getInstance().getCurrentProcess().getTitle());
					}					
				}
				
				ExportProcessActivity.this.finish();
			}
		});
	}
	
	public boolean exportMetasonic(String name){
		new UploadMetasonicProcess().execute(name);
		return true;
	}
	
	private class UploadProcess extends AsyncTask<String, Boolean, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String name = ProcessManager.getInstance().getCurrentProcess().getTitle();
			if (params[0] != null) {
				name = params[0];
			}
			boolean erg = ProcessManager.getInstance().uploadProcess(name);
			return erg;
		}
		
	}
	
	private class UploadMetasonicProcess extends AsyncTask<String, Boolean, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String name = ProcessManager.getInstance().getCurrentProcess().getTitle();
			if (params[0] != null) {
				name = params[0];
			}
			boolean erg = ProcessManager.getInstance().uploadProcessMetasonic(name);
			return erg;
		}
		
	}
}
