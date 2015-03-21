package ce.modelwhilework.presentation.contextinfo;

import java.io.File;
import java.io.IOException;

import ce.modelwhilework.presentation.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioActivity extends Activity {

	MediaPlayer myPlayer;
	Button m_playPauseBtn, m_stopBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_audio);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		this.myPlayer = new MediaPlayer();
		
		m_playPauseBtn = (Button) this.findViewById(R.id.activity_audio_btn_playpause);
		m_playPauseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(myPlayer.isPlaying()){
						myPlayer.pause();
						((Button) v).setText("Play");
					}
					else {
						myPlayer.start();
						((Button) v).setText("Pause");
					}	
				} catch (Exception exc){
					exc.printStackTrace();
				}
			}
		});
		
		m_stopBtn = (Button) this.findViewById(R.id.activity_audio_btn_stop);
		m_stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AudioActivity.this.stopAudio();
			}
		});
		
		myPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				AudioActivity.this.m_playPauseBtn.setText("Play");
			}
		});
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){		
			File file = new File(bundle.getString("Path"));
			if (file.exists()){
				m_playPauseBtn.setText("Pause");
				this.playAudio(file.getAbsolutePath());
			}
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
        if (myPlayer != null) {
        	try{
        		myPlayer.stop();
        	} catch (IllegalStateException exc){
        		exc.printStackTrace();
        	}
            myPlayer.release();
            myPlayer = null;
        }
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


	private void stopAudio(){
        if (myPlayer != null) {
        	try{
        		myPlayer.stop();
        	} catch (IllegalStateException exc){
        		exc.printStackTrace();
        	}
            myPlayer.release();
            myPlayer = null;
        }
        this.finish();
	}

	private void playAudio(String path){
		
		try {
			myPlayer.setDataSource(path);
			myPlayer.prepare();
			myPlayer.start();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
