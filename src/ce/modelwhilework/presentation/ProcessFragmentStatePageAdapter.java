package ce.modelwhilework.presentation;

import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.data.Process;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class ProcessFragmentStatePageAdapter extends FragmentStatePagerAdapter {
	
	public ProcessFragmentStatePageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		Fragment fragment;
		
		if(ProcessManager.getInstance().getProcesses().size() > 0) {
			
			fragment = new ProcessFragment();
			
			Bundle processData = new Bundle();
			processData.putInt("page_postion", position);
			processData.putString("ProcessName", ProcessManager.getInstance().getProcess(position).getTitle());
			fragment.setArguments(processData);
		}
		else
			fragment = new EmptyFragment();		
		
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		
		if(ProcessManager.getInstance().getProcesses().size() > 0) {
			Process process = ProcessManager.getInstance().getProcess(position);
			if (process != null){
				return process.getTitle().toString();
			}
			else{
				return "";
			}			
		}
		else 
			return "welcome!!!";		
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
		
		if(ProcessManager.getInstance().getProcesses().size() > 0)
			return ProcessManager.getInstance().getProcesses().size();
		
		return 1;
	}
}
