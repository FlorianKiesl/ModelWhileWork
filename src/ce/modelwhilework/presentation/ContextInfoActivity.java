package ce.modelwhilework.presentation;

import ce.modelwhilework.data.ProcessManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

public class ContextInfoActivity extends Activity {

	private String card_id = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contextinfo);
		ContextInfoGridAdapter adapter = new ContextInfoGridAdapter(this);
		GridView grid = (GridView) this.findViewById(R.id.grid);
		grid.setAdapter(adapter);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			card_id = bundle.getString("CARD_ID");
		}
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
			if(card_id.length() > 0)
				intent.putExtra("CARD_ID", card_id);
		    startActivity(intent);
		}
		else if (id == R.id.action_newAudio){			
		}
		return super.onOptionsItemSelected(item);
	}

}
