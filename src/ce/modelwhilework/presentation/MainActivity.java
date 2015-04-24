package ce.modelwhilework.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ce.modelwhilework.data.CardAttribute;
import ce.modelwhilework.data.ProcessManager;

public class MainActivity extends FragmentActivity implements DialogInterface.OnClickListener {

	CustomViewPager viewPager;
	ProcessFragmentStatePageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ProcessManager pm = ProcessManager.getInstance();
		pm.setInternalDir(getFilesDir());
		pm.setExternalDir(getExternalFilesDir(null));
		pm.setExternalCacheDir(getExternalCacheDir());
		
		PagerTitleStrip pts = (PagerTitleStrip) this.findViewById(R.id.title);
		pts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ProcessManager.getInstance().getCurrentProcess() != null) {
					Intent intent = new Intent(getBaseContext(), TextInputActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.putExtra("CardAttrib", CardAttribute.PROCESSTITLE.toString());
					intent.putExtra("ProcessName", ProcessManager.getInstance().getCurrentProcess().getTitle());
					intent.putExtra("DefaultText", ProcessManager.getInstance().getCurrentProcess().getTitle());	
				    startActivity(intent);
				}
			}
		});
		
		viewPager = (CustomViewPager) this.findViewById(R.id.pager_process);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				ProcessManager.getInstance().setCurrentProcess(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		FragmentManager fm = this.getSupportFragmentManager();
		adapter = new ProcessFragmentStatePageAdapter(fm);
		ProcessManager.getInstance().loadFavorites();
	}
	
	@Override
	protected void onStop() {
		ProcessManager.getInstance().storeFavorites();
		super.onStop();
		
	}

	protected void onResume() {
		super.onResume();
		viewPager.setAdapter(adapter);
		viewPager.getAdapter().notifyDataSetChanged();
		
		int lastItem = ProcessManager.getInstance().getCurrentProcessPos();
		if(lastItem >= 0 && lastItem < adapter.getCount())
			viewPager.setCurrentItem(lastItem);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_new){
			
			Intent intent = new Intent(getBaseContext(), NewProcessActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    startActivity(intent);
		}
		else if (id == R.id.action_close){
			
			ProcessManager.getInstance().closeProcess(viewPager.getCurrentItem());
			viewPager.setAdapter(adapter);
			int nextItem = ProcessManager.getInstance().getCurrentProcessPos();			
			if(nextItem >= 0 && nextItem < adapter.getCount())
				viewPager.setCurrentItem(nextItem);
		}
		else if (id == R.id.action_closeall){
			ProcessManager.getInstance().closeAllProcesses();
			viewPager.setAdapter(adapter);
		}
		else if (id == R.id.action_export){
			if(adapter.getCount() <= 0) {
				showAlert("Please open a process first!");
			}
			else{
				Intent intent = new Intent(getBaseContext(), ExportProcessActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);
			}
		}
		else if ( id == R.id.action_import){
			Intent intent = new Intent(getBaseContext(), LoadProcessActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.putExtra("IMPORT", "IMPORT");
		    startActivity(intent);			
		}
		else if (id == R.id.action_open){
			
			Intent intent = new Intent(getBaseContext(), LoadProcessActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    startActivity(intent);
		}
		else if (id == R.id.action_delete){
			
			Intent intent = new Intent(getBaseContext(), DeleteProcessActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    startActivity(intent);
		}
		else if (id == R.id.action_settings){
			Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
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
		// TODO Auto-generated method stub
		
	}
}