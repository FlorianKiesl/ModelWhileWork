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
				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_xoffset);	
				if(!Settings.getInstance().setOffsetX(tv.getText().toString()))
					showAlert("Error by save offset x!!!");
				
				tv = (TextView) activity.findViewById(R.id.activity_settings_editText_yoffset);	
				if(!Settings.getInstance().setOffsetY(tv.getText().toString()))
					showAlert("Error by save offset y!!!");
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
