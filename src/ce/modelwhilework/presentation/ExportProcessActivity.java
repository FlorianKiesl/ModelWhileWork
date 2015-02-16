package ce.modelwhilework.presentation;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
		
		
		spinnerExportType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerExportType.setSelection(0);
		
		ListView lvExportPath = (ListView) ExportProcessActivity.this.findViewById(R.id.activity_export_process_exportPath);
		ArrayList<String> alExportPath = new ArrayList<String>();
		alExportPath.add("External USB Device");
		alExportPath.add("PHP-Server (http://stefanoppl.net)");

		final ListAdapterRadioButton adapterLv = new ListAdapterRadioButton(getBaseContext(),
				R.layout.list_radiobutton, alExportPath);
		lvExportPath.setAdapter(adapterLv);
	}


}
