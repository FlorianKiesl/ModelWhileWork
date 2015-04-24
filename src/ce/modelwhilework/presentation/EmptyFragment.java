package ce.modelwhilework.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyFragment extends Fragment {
	
	private View fragment;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        
		fragment = inflater.inflate(R.layout.fragment_empty, container, false);
		
		return fragment;
	}
	
	public void onPause() {
		   super.onPause();
	};  

	public void onResume() {
		   super.onResume();	   
	}
}
	
	