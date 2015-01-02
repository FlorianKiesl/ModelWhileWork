package ce.modelwhilework.presentation;

import java.io.File;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import ce.modelwhilework.data.Card;
import ce.modelwhilework.data.Task;
import ce.modelwhilework.data.Message;
import ce.modelwhilework.data.Process;

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
		
		//ToDo:Test XML
		Card card;
		Process test = new  Process("Test");
		card = new Message("message");
		test.addCard(card);
		card = new Task("task");
		StringWriter writer = new StringWriter();
		XmlSerializer xmlSerializer = Xml.newSerializer();
			
		try {
			xmlSerializer.setOutput(writer);
			//test.writeXML(xmlSerializer);
			String s = writer.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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