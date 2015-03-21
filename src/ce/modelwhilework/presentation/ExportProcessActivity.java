package ce.modelwhilework.presentation;

import java.io.File;
import java.text.ChoiceFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExportProcessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_export_process);
		
		
		
		Spinner spinnerExportType = (Spinner) this.findViewById(R.id.activity_export_process_exportType);
		ArrayAdapterSpinner adapter = new ArrayAdapterSpinner(this, android.R.layout.simple_spinner_item);
		
		adapter.add(new AbstractMap.SimpleEntry<String, String>("Metasonic","Metasonic"));
		adapter.add(new AbstractMap.SimpleEntry<String, String>("MWYW","Model While You Work"));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerExportType.setAdapter(adapter);
		spinnerExportType.setSelection(0);
		
		ListView lvExportPath = (ListView) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportPath);
		final File sdCard = Environment.getExternalStorageDirectory();

		final ListAdapterRadioButton adapterLv = new ListAdapterRadioButton(getBaseContext(),
				R.layout.list_radiobutton);
		
		spinnerExportType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				adapterLv.clear();

				Entry<String,String> selectedType = (Entry<String,String>) ((Spinner) parent).getItemAtPosition(position);
				
				if (sdCard.exists() && sdCard.canWrite()){
					adapterLv.add(new AbstractMap.SimpleEntry<String,String>("SDExt", "External SD-Card"));
				}
				if (selectedType.getKey().compareToIgnoreCase("Metasonic") == 0){
					
					adapterLv.add(new AbstractMap.SimpleEntry<String,String>("Webserver", "PHP-Server (" + Settings.getInstance().getServerMetasonic() + ")"));
					adapterLv.add(new AbstractMap.SimpleEntry<String, String>("Webservice", "Webservice (" + Settings.getInstance().getWebservic() + ")"));
										
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				adapterLv.clear();
				if (sdCard.exists() && sdCard.canWrite()){
					adapterLv.add(new AbstractMap.SimpleEntry<String,String>("SDExt", "External SD-Card"));
				}
				adapterLv.add(new AbstractMap.SimpleEntry<String,String>("Webserver", "PHP-Server (" + Settings.getInstance().getServerMetasonic() + ")"));
				adapterLv.add(new AbstractMap.SimpleEntry<String, String>("Webservice", "Webservice (" + Settings.getInstance().getWebservic() + ")"));
			}
		});

		lvExportPath.setAdapter(adapterLv);
		
		Button btnExport = (Button) this.findViewById(R.id.activity_export_process_btnSenden);
		btnExport.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				try{
					Spinner spinner = (Spinner) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportType);
					@SuppressWarnings("unchecked")
					Entry<String,String> selectedType = (Entry<String,String>)spinner.getSelectedItem();
					Entry<String,String> selectedPath = adapterLv.getSelectedItem();
					
					if (selectedType.getKey() == "Metasonic"){
						if(selectedPath.getKey() == "SDExt"){
							ProcessManager.getInstance().exportProcessMetasonicExternal(ProcessManager.getInstance().getCurrentProcess().getTitle());
						}
						else if(selectedPath.getKey() == "Webservice"){
							ProcessManager.getInstance().exportProcessWebservice(ProcessManager.getInstance().getCurrentProcess().getTitle(), ExportProcessActivity.this);
						}
						else{
							UploadMetasonicProcess uploadProcess = new UploadMetasonicProcess();
							uploadProcess.execute(ProcessManager.getInstance().getCurrentProcess().getTitle());
						}
					} 
					else{
						if(selectedPath.getKey() == "SDExt"){
							ProcessManager.getInstance().exportProcessExternal(ProcessManager.getInstance().getCurrentProcess().getTitle());
						}
						else if(selectedPath.getKey() == "Webservice"){
							//ProcessManager.getInstance().exportProcessWebservice(ProcessManager.getInstance().getCurrentProcess().getTitle());
						}
						else{
							UploadProcess uploadProcess = new UploadProcess();
							uploadProcess.execute(ProcessManager.getInstance().getCurrentProcess().getTitle());
						}					
					}
					
					ExportProcessActivity.this.finish();				
				} catch (Exception exc){
					exc.printStackTrace();
				}

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
	
	private class ArrayAdapterSpinner extends ArrayAdapter<java.util.Map.Entry<String, String>>{

		private int textViewResourceId;

		public ArrayAdapterSpinner(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View dropDown = getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			((CheckedTextView) dropDown).setText(super.getItem(position).getValue());		
			return dropDown;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
			View txtView = super.getView(position, convertView, parent);
			
			((TextView) txtView).setText(super.getItem(position).getValue());
			return txtView;
		}
		
	}
	
	private class ListAdapterRadioButton extends ArrayAdapter<java.util.Map.Entry<String, String>> {
		
		LayoutInflater inflater;
		int resourceId;
		Context ctx;
		private int selectedPos = -1;
//		String selectedItem;

		public ListAdapterRadioButton(Context context, int resourceId) {
			super(context, resourceId);
			inflater = LayoutInflater.from(context);
			this.resourceId = resourceId;	
			this.ctx = context;
//			selectedItem = "";
		}
		
		public Entry<String, String> getSelectedItem(){
			return super.getItem(this.selectedPos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			super.getView(position, convertView, parent);
			
			final ViewGroup viewGroup = parent;

			if (convertView == null){
				convertView = ( RelativeLayout ) inflater.inflate( resourceId, null );
			}

	        String title = getItem( position ).getValue();
	        
	        TextView tv = (TextView) convertView.findViewById(R.id.activity_list_radioButton_radioButton_TextView);
	        tv.setText(title);
	        
	        RadioButton rb = (RadioButton) convertView.findViewById(R.id.activity_list_radioButton_radioButton);
	        if (this.selectedPos == -1 && position == 0){
		        rb.setChecked(true);
		        notifyDataSetChanged();
		        this.selectedPos = 0;
	        }
	        rb.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(int i = 0; i < viewGroup.getChildCount(); i++) {
						for(int x = 0; x < ((ViewGroup) viewGroup.getChildAt(i)).getChildCount(); x++) {
							View v1 = ((ViewGroup) viewGroup.getChildAt(i)).getChildAt(x);						
							if(v1 instanceof RadioButton) {
								if(v1 != v) {
									RadioButton rb = (RadioButton) v1;
									rb.setChecked(false);
								}
								else
									ListAdapterRadioButton.this.selectedPos = i;
							}
						}
			        }
					v.setSelected(true);
					notifyDataSetChanged();
				}
			});
	        
	        return convertView;
		}
	}
	
	
}
