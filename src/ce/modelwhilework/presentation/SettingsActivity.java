package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity implements DialogInterface.OnClickListener {

	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		activity = this;
		TextView tv = (TextView) activity.findViewById(R.id.activity_settings_editText_user);
		tv.setText(Settings.getInstance().getUser());
		
		tv = (TextView) activity.findViewById(R.id.activity_settings_editText_metasonic_server_path);
		tv.setText(Settings.getInstance().getServerMetasonic());
		
		tv = (TextView) activity.findViewById(R.id.activity_settings_editText_webservice_path);
		tv.setText(Settings.getInstance().getWebservic());
//		
//		tv = (TextView) activity.findViewById(R.id.activity_settings_editText_webservice_parameter);
//		tv.setText(Settings.getInstance().getParameterWebservice());
//		
		tv = (TextView) activity.findViewById(R.id.activity_settings_editText_xoffset);
		tv.setText(Settings.getInstance().getOffsetX());
		
		tv = (TextView) activity.findViewById(R.id.activity_settings_editText_yoffset);
		tv.setText(Settings.getInstance().getOffsetY());
		
		Button buttonSave = (Button) this.findViewById(R.id.activity_settings_button_save);	
		buttonSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				TextView tv = (TextView) activity.findViewById(R.id.activity_settings_editText_user);	
				if(!Settings.getInstance().setUser(tv.getText().toString()))
					showAlert("Error by save user!!!");
				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_metasonic_server_path);	
				if(!Settings.getInstance().setServerMetasonic(tv.getText().toString()))
					showAlert("Error by save Metasonic server path!!!");
				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_webservice_path);	
				if(!Settings.getInstance().setWebservice(tv.getText().toString()))
					showAlert("Error by save Webservice path!!!");
//				
//				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_webservice_parameter);	
//				if(!Settings.getInstance().setParameterWebservice(tv.getText().toString()))
//					showAlert("Error by save Webservice Parameter!!!");
//				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_xoffset);	
				if(!Settings.getInstance().setOffsetX(tv.getText().toString()))
					showAlert("Error by save offset x!!!");
				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_yoffset);	
				if(!Settings.getInstance().setOffsetY(tv.getText().toString()))
					showAlert("Error by save offset y!!!");
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		Settings.getInstance().setOffsetX("0.3");
//		Settings.getInstance().setOffsetY("0.4");
//		Settings.getInstance().setUser("Florian");
//		Settings.getInstance().setServerMetasonic("Server");
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
