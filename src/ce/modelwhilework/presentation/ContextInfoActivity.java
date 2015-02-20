package ce.modelwhilework.presentation;

import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		return super.onOptionsItemSelected(item);
	}

}
