package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.CardAttribute;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextInputActivity extends Activity implements DialogInterface.OnClickListener  {

	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_input);
		
		activity = this;
		final String strCardAttribute;		
		final ce.modelwhilework.data.Process process;
		
		final EditText te_text = (EditText) this.findViewById (R.id.activity_input_text_edittext);
		
		Bundle args = getIntent().getExtras();
		if (args != null) {
			strCardAttribute = args.getString("CardAttrib");
			
			if (strCardAttribute == null) {
				showAlert("General error: connection to card is missing!");
				this.finish();
			}
			else
			{
				process = ProcessManager.getInstance().getProcess(args.getString("ProcessName"));
				
				if (process == null) {
					showAlert("General error: can't get process!");
					this.finish();
				}
				else {
					
					if(args.getString("DefaultText") != null)
					{
						te_text.setText(args.getString("DefaultText"));
						te_text.setSelection(te_text.getText().length());
					}
					
					Button button = (Button) this.findViewById(R.id.activity_input_text_ok);
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							boolean alert = false;
							
							if(strCardAttribute.equals(CardAttribute.TITLETASK.toString()))
								process.setTaskCardTitle(te_text.getText().toString());
							else if(strCardAttribute.equals(CardAttribute.TITLEMSG.toString()))
								process.setMessageCardTitle(te_text.getText().toString());
							else if(strCardAttribute.equals(CardAttribute.PERSONMSG.toString()))
								process.setMessageCardSenderReceiver(te_text.getText().toString());
							else if(strCardAttribute.equals(CardAttribute.TITLEMAINSTACK.toString()))
							{
								if(!process.setTopCardTitleMainStack(te_text.getText().toString()))	{
									alert = true;
									showAlert("Error: You have to enter a valid and unique title!!!");		
								}
							}
							else if(strCardAttribute.equals(CardAttribute.TITLESIDESTACK.toString()))
							{
								if(!process.setTopCardTitleSideStack(te_text.getText().toString())) {
									alert = true;
									showAlert("Error: You have to enter a valid and unique title!!!");		
								}
							}
							else if(strCardAttribute.equals(CardAttribute.PERSONMAINSTACK.toString()))
							{
								if(te_text.getText().length() == 0) {
									alert = true;
									showAlert("You have to enter a sender/receiver!!!");
								}
								else
								{
									if(!process.setTopCardSenderReceiverMainStack(te_text.getText().toString()))
										showAlert("Error: can't set sender/receiver!!!!");		
								}
							}
							else if(strCardAttribute.equals(CardAttribute.PERSONSIDESTACK.toString()))
							{
								if(te_text.getText().length() == 0) {
									alert = true;
									showAlert("You have to enter a sender/receiver!!!");
								}
								else
								{
									if(!process.setTopCardSenderReceiverSideStack(te_text.getText().toString()))
										showAlert("Error: can't set sender/receiver!!!!");	
								}
							}
							else if(strCardAttribute.equals(CardAttribute.USERROLE.toString()))
								process.setUserRole(te_text.getText().toString());
							else {
								alert = true;
								showAlert("General error: invalid argument!");
							}
							
							if(!alert)
								activity.finish();
						}
					});
					
					button = (Button) this.findViewById(R.id.activity_input_text_cancel);
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							activity.finish();			
						}
					});
					
				}
			}		
		}
		else
		{
			showAlert("General error: arguments are missing!");
			this.finish();
		}
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
		activity.finish();
	}
}
