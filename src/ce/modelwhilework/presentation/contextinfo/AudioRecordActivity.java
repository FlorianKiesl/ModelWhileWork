package ce.modelwhilework.presentation.contextinfo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AudioRecordActivity extends Activity {
	
	private MediaRecorder myRecorder;
	private String outputFile = null;
	private Button startBtn;
	private Button stopBtn;
	private TextView text;
	private Modus modus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_audiorecord);
		
		this.text = (TextView) findViewById(R.id.text1);
		
		outputFile = ProcessManager.getInstance().getExternalCacheStorage() + "/ModelWhileYouWorkAudioRec.3gpp";
//		outputFile = Environment.getExternalStorageDirectory().
//	    		  getAbsolutePath() + "/javacodegeeksRecording.3gpp";
		
		modus = ProcessManager.getInstance().getCurrentProcess();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String title = bundle.getString("CARD_ID");
			if (title!= null) {
				modus = ProcessManager.getInstance().getCurrentProcess().getTopCard(title);
			}
		}
		
		myRecorder = new MediaRecorder();
	    myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	    myRecorder.setOutputFile(outputFile);
	    startBtn = (Button)findViewById(R.id.start);
	    startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AudioRecordActivity.this.startRecord(v);
			}
		});
	      
	    stopBtn = (Button)findViewById(R.id.stop);
	    stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AudioRecordActivity.this.stopRecord(v);
			}
		});
	      	    
	    
	}
	
	private void startRecord(View view){
		try{
			this.myRecorder.prepare();
			this.myRecorder.start();
		} catch (IllegalStateException exc){
			exc.printStackTrace();
		} catch (IOException exc){
			exc.printStackTrace();
		}
		
		text.setText("Recording Point: Recording");
		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);
	}
	
	private void stopRecord(View view){
		try{
			this.myRecorder.stop();
			this.myRecorder.release();
			this.myRecorder = null;
			this.saveAudio();
			this.finish();
			
		} catch (IllegalStateException exc){
			exc.printStackTrace();
		} catch (RuntimeException exc){
			exc.printStackTrace();
		}		
	}
	
	private boolean saveAudio(){
		File fileCached = new File(this.outputFile);
		if (fileCached.exists()){
			try {
				byte[] audioFile = FileUtils.readFileToByteArray(fileCached);
				if(this.modus != null && audioFile != null){
					this.modus.addContextInformationAudio(audioFile);
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally{
				if (fileCached.exists()){
					fileCached.delete();
				}
			}
			return false;
		}
		return false;
	}
	

}
