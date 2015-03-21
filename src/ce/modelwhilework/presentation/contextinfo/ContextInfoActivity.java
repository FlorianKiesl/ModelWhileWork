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
import ce.modelwhilework.data.contextinfo.Text;
import ce.modelwhilework.data.contextinfo.Video;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import ce.modelwhilework.presentation.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class ContextInfoActivity extends Activity {

	private Modus modus;
	
	private ContextInfoGridAdapter adapterInfoGrid;
	private GridView grid;
	
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

	@Override
	protected void onPostResume() {
		super.onPostResume();
		
		adapterInfoGrid = new ContextInfoGridAdapter(this, this.modus.getContextInformations());
		
		grid = (GridView) this.findViewById(R.id.grid);
		grid.setAdapter(adapterInfoGrid);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContextInformation contextInfo = (ContextInformation) adapterInfoGrid.getItem(position);
				if (contextInfo != null){
					Intent intent = null;
					if (contextInfo instanceof Picture){
						intent = new Intent(ContextInfoActivity.this.getBaseContext(), PictureActivity.class);
					}
					else if (contextInfo instanceof Video){
						intent = new Intent(ContextInfoActivity.this.getBaseContext(), VideoActivity.class);
					}
					else if (contextInfo instanceof Audio){
						intent = new Intent(ContextInfoActivity.this, AudioActivity.class);

					}
					else if (contextInfo instanceof Text){
						intent = new Intent(ContextInfoActivity.this, TextActivity.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("Path", contextInfo.getPath());
				    startActivity(intent);
				}
			}
		});
		
		grid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.performClick();
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

				view.startDrag(data, shadowBuilder, view, 0);
				return false;
			}
			
		});
		
		ImageView btnDel = (ImageView) this.findViewById(R.id.activity_contextinfo_iv_del);
		
		btnDel.setOnDragListener(new OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				
				switch(event.getAction()){
					case DragEvent.ACTION_DROP:
						if (event.getLocalState() instanceof LinearLayout){
							
							
							
							LinearLayout item = (LinearLayout) event.getLocalState();
							int id = Integer.parseInt(item.getTag().toString());
							
							ContextInfoActivity.this.showDeleteQuestion("Are you sure to delete this contextinformation?", id);

						}
						break;
				}
				return true;
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
			Intent intent = new Intent(this, TextActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			if(this.modus instanceof Card)
				intent.putExtra("CARD_ID", ((Card) this.modus).getTitle());
		    startActivity(intent);
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
			startActivityForResult(intent, 0);
		}
		else if (id == android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.getData() != null){
			File file = new File(this.getFilePathFromContentUri(data.getData()));
			if (file.exists()){
				byte[] byteArrVideo = null;
				try {
					byteArrVideo = FileUtils.readFileToByteArray(file);
					if (byteArrVideo != null){
						if (this.modus.addContextInformationVideo(byteArrVideo)){
							file.delete();
						}
					}
				} catch (IOException e) {
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
	
	private void showDeleteQuestion(String msg, final int dropTagId) {
		
		AlertDialog.Builder alertDialog =	new AlertDialog.Builder(this);
		alertDialog.setTitle("question");
		alertDialog.setMessage(msg);
		alertDialog.setNegativeButton("No",
				   new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                    	  dialog.dismiss();
                      }
                }
		);
		alertDialog.setPositiveButton("Yes",
									   new DialogInterface.OnClickListener() {
			                                 public void onClick(DialogInterface dialog, int whichButton) {			                                	 
			         							
			         							ContextInfoActivity.this.modus.removeContextInfo(dropTagId);
			         							ContextInfoActivity.this.adapterInfoGrid.notifyDataSetChanged();
			         							grid.setAdapter(ContextInfoActivity.this.adapterInfoGrid);
			         							
			         							dialog.dismiss();
			                                 }
			                           }
	    );
	    alertDialog.show();
	}
}
