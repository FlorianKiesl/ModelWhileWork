package ce.modelwhilework.presentation;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProcessFragmentStatePageAdapter extends FragmentStatePagerAdapter {
	
	private List<String> processList;
	
	public ProcessFragmentStatePageAdapter(FragmentManager fm) {
		super(fm);
		processList = new ArrayList<String>();
	}

	@Override
	public Fragment getItem(int arg0) {
		return new ProcessFragment();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		
		return this.processList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.processList.size();
	}
	
	public void addProcess(String name){
		this.processList.add(name);
	}
	
	public void openProcess(){
		return;
	}
	
	public void exportProcess(String name){
		return;
	}
	
	public void closeProcess(String name){
		return;
	}
	
	public void clossAllProcesses(){
		return;
	}
}
