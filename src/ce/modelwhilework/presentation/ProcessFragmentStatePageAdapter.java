package ce.modelwhilework.presentation;

import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Process;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class ProcessFragmentStatePageAdapter extends FragmentStatePagerAdapter {
	
	private ProcessManager processManager;
	
	public ProcessFragmentStatePageAdapter(FragmentManager fm) {
		super(fm);
		processManager = ProcessManager.getInstance();
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new ProcessFragment();
		
		Bundle processData = new Bundle();
		processData.putInt("page_postion", position);
		processData.putString("ProcessName", this.processManager.getProcess(position).getTitle());
		fragment.setArguments(processData);
		
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Process process = this.processManager.getProcess(position);
		if (process != null){
			return process.getTitle().toString();
		}
		else{
			return "";
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		return this.processManager.getProcesses().size();
	}
}
