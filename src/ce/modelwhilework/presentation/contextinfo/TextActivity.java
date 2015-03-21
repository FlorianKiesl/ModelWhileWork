package ce.modelwhilework.presentation.contextinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.presentation.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TextActivity extends Activity {
	
	private Modus modus;
	private boolean newText;
	private String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_text);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		modus = ProcessManager.getInstance().getCurrentProcess();
		Bundle bundle = getIntent().getExtras();
		newText = true;
		if (bundle != null) {
			if (bundle.containsKey("CARD_ID")){
				String title = bundle.getString("CARD_ID");
				if (title!= null) {
					modus = ProcessManager.getInstance().getCurrentProcess().getTopCard(title);
				}
			}
			else if (bundle.containsKey("Path")){
				newText = false;
				path = bundle.getString("Path");
			}
		}
		Button btnSave = (Button) this.findViewById(R.id.activity_text_save);
		final EditText description = (EditText) this.findViewById(R.id.activity_text_description);
		
		if (newText){
			btnSave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String text = description.getText().toString();
					TextActivity.this.modus.addContextInformationText(text.getBytes());
					TextActivity.this.finish();
				}
			});
		}
		else{
			description.setText(this.readSavedData(path));
			btnSave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String text = description.getText().toString();
					TextActivity.this.writeByteArrFile(text.getBytes(), path);
					TextActivity.this.finish();
				}
			});
		}
	}
	
	//TODO: Im Modus gibts dies Methode auch => eventuell zusammenfassen. Lösung finden
	private boolean writeByteArrFile(byte[] data, String path){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(data);			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}	
		return true;
	}
	
    public String readSavedData ( String path) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = new FileInputStream(new File(path)) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean erg = super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case android.R.id.home:
			this.finish();
		}
		return erg;
	}
	
}
