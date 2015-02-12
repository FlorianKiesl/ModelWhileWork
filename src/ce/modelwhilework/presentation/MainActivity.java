package ce.modelwhilework.presentation;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.xmlpull.v1.XmlSerializer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Task;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.Process;

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
	}
	
	protected void onResume() {
		super.onResume();
		viewPager.setAdapter(adapter);
		
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
			adapter.addProcess("Process " + (adapter.getCount() + 1));
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(adapter.getCount());
		}
		else if (id == R.id.action_close){
			int nitem = viewPager.getCurrentItem();
			adapter.closeProcess(nitem);
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(adapter.getCount());
		}
		else if (id == R.id.action_closeall){
			adapter.clossAllProcesses();
			viewPager.setAdapter(adapter);
		}
		else if (id == R.id.action_export){
			if(!adapter.exportProcess(ProcessManager.getInstance().getCurrentProcess().getTitle()))
				showAlert("export file failed!");
		}
		else if (id == R.id.action_open){
			
			Intent intent = new Intent(getBaseContext(), OpenProcessActivity.class);
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