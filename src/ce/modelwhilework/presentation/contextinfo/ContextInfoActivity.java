package ce.modelwhilework.presentation.contextinfo;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Video;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import ce.modelwhilework.presentation.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.VideoView;

public class ContextInfoActivity extends Activity {

	private Modus modus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contextinfo);
		
		modus = ProcessManager.getInstance().getCurrentProcess();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String card_id = bundle.getString("CARD_ID");
			if (card_id!= null) {
				modus = ProcessManager.getInstance().getCurrentProcess().getTopCard(card_id);
			}
		}
	}
	private ContextInfoGridAdapter adapterInfoGrid;
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		
		adapterInfoGrid = new ContextInfoGridAdapter(this, this.modus.getContextInformations());
		GridView grid = (GridView) this.findViewById(R.id.grid);
		grid.setAdapter(adapterInfoGrid);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle contextInfoBundle;
				ContextInformation contextInfo = (ContextInformation) adapterInfoGrid.getItem(position);
				if (contextInfo != null){
					if (contextInfo instanceof Picture){
						Intent intent = new Intent(ContextInfoActivity.this.getBaseContext(), PictureActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						intent.putExtra("Path", contextInfo.getPath());
						startActivity(intent);
					}
					else if (contextInfo instanceof Video){
						Intent intent = new Intent(ContextInfoActivity.this.getBaseContext(), VideoActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						intent.putExtra("Path", contextInfo.getPath());
						startActivity(intent);
					}	
					else if (contextInfo instanceof Audio){
						ContextInfoActivity.this.playAudio(contextInfo.getPath());
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.getMenuInflater().inflate(R.menu.contextinfo, menu);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_newDescription){
		}
		else if (id == R.id.action_newPicture){
			
			Intent intent = new Intent(this, CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			if(this.modus instanceof Card)
				intent.putExtra("CARD_ID", ((Card) this.modus).getTitle());
		    startActivity(intent);
		}
		else if (id == R.id.action_newAudio){		
			Intent intent = new Intent(this, AudioRecordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			if(this.modus instanceof Card)
				intent.putExtra("CARD_ID", ((Card) this.modus).getTitle());
		    startActivity(intent);
		}
		else if (id == R.id.action_newVideo){
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(ProcessManager.getInstance().getExternalCacheStorage(), "videoCache").toURI());
			startActivityForResult(intent, 0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data.getData() != null){
			File file = new File(this.getFilePathFromContentUri(data.getData()));
			Toast.makeText(getApplicationContext(), data.getData().getPath(), Toast.LENGTH_LONG).show();
			if (file.exists()){
				byte[] byteArrVideo = null;
				try {
					byteArrVideo = FileUtils.readFileToByteArray(file);
					if (byteArrVideo != null){
						if (this.modus.addContextInformationVideo(byteArrVideo)){
							file.delete();
							Toast.makeText(getApplicationContext(), "Video erfolgreich gespeichert", Toast.LENGTH_LONG).show();
						}
					}
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Video konnte nicht gespeichert werden!", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}				
			}			
		}


	}
	
	private String getFilePathFromContentUri(Uri selectedVideoUri) {
	    String filePath;
	    String[] filePathColumn = {MediaColumns.DATA};

	    Cursor cursor = getContentResolver().query(selectedVideoUri, filePathColumn, null, null, null);
	    cursor.moveToFirst();

	    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	    filePath = cursor.getString(columnIndex);
	    cursor.close();
	    return filePath;
	}
	
	private void playAudio(String path){
		MediaPlayer myPlayer = new MediaPlayer();
		try {
			myPlayer.setDataSource(path);
			myPlayer.prepare();
			myPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
