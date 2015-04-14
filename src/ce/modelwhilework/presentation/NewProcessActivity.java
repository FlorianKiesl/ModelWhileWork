package ce.modelwhilework.presentation;

import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ce.modelwhilework.data.Process;

public class NewProcessActivity extends Activity implements DialogInterface.OnClickListener {

	String title;
	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_process);
		activity = this;
		title = "";
		
		Button b = (Button) this.findViewById(R.id.activity_newprocess_buttonOk);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(title.length() == 0)
					showAlert("Please enter a process title!!!");
				else {
					if(!ProcessManager.getInstance().addProcess(new Process(title, Settings.getInstance().getUser(), ProcessManager.getInstance().getInternalStorage()))) {
						if(ProcessManager.getInstance().openProcess(title))
							activity.finish();
						else
							showAlert("Can't open process!!!");
					}
					else
						showAlert("Can't create process!!!\nUse a different name or load/delete the existing process.");
				}					
			}
		});
		
		final EditText et = (EditText) this.findViewById(R.id.activity_newprocess_editTextName);
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {

				title = et.getText().toString();
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
