package ce.modelwhilework.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

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
		modus.getContextInformations();

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
				Intent intent = new Intent(ContextInfoActivity.this.getBaseContext(), PictureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				Bundle contextInfo = new Bundle();
				contextInfo.putString("Path", ((ContextInformation) adapterInfoGrid.getItem(position)).getPath());
				intent.putExtra("Path", ((ContextInformation) adapterInfoGrid.getItem(position)).getPath());
				startActivity(intent);
//				Toast msg = Toast.makeText(getApplicationContext(), "Test " + position, Toast.LENGTH_LONG);
//////			msg.setView(v);
//				msg.show();
				
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
		}
		else if (id == R.id.action_newVideo){
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(ProcessManager.getInstance().getExternalCacheStorage(), "videoCache").toURI());
			startActivityForResult(intent, 0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		File file = new File(data.getData().getPath());
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
