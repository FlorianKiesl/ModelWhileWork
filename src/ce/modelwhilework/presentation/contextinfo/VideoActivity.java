package ce.modelwhilework.presentation.contextinfo;

import java.io.File;

import ce.modelwhilework.data.Modus;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	private VideoView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_video);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			
			this.videoView = (VideoView) this.findViewById(R.id.activity_video_videoView);
			this.videoView.setVideoPath(bundle.getString("Path"));
			this.videoView.setKeepScreenOn(true);
			
			MediaController mc = new MediaController(this);
			videoView.setMediaController(mc);
			mc.setMediaPlayer(videoView);
			
			this.videoView.start();
			this.videoView.requestFocus();
			
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(this.videoView != null){
			if(videoView.isPlaying()) {
			    videoView. stopPlayback ();
			}			
		}
	}

}
