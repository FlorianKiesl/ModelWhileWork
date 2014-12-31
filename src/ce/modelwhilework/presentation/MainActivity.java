package ce.modelwhilework.presentation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	CustomViewPager viewPager;
	ProcessFragmentStatePageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (CustomViewPager) this.findViewById(R.id.pager_process);
		FragmentManager fm = this.getSupportFragmentManager();
		adapter = new ProcessFragmentStatePageAdapter(fm);
		adapter.addProcess("Process 1");
		adapter.addProcess("Process 2");
		adapter.addProcess("Process 3");
		viewPager.setAdapter(adapter);
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
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}
}