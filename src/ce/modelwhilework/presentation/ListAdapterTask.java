package ce.modelwhilework.presentation;

import java.util.ArrayList;

import ce.modelwhilework.data.Task;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListAdapterTask extends ArrayAdapter<Task> {
	
	LayoutInflater inflater;
	int resourceId;
	Context ctx;
	String selectedItem;
	
	public ListAdapterTask(Context context, int resourceId,
			ArrayList<Task> task) {
		super(context, resourceId, task);
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;	
		this.ctx = context;
		selectedItem = "";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewGroup viewGroup = parent;
		
		convertView = ( LinearLayout ) inflater.inflate( resourceId, null );
        
        Task task = getItem( position );
        
        EditText et = (EditText) convertView.findViewById(R.id.activity_favorite_task_editTextWorkCardTitle);
        et.setText(task.getTitle());
        
        RadioButton rb = (RadioButton) convertView.findViewById(R.id.activity_favorite_task_RadioButtont);
        rb.setOnClickListener(new View.OnClickListener() {
			
        	@Override
			public void onClick(View v) {
				
				boolean checked;
				for(int i = 0; i < viewGroup.getChildCount(); i++) {
					checked = false;
					for(int x = 0; x < ((ViewGroup) viewGroup.getChildAt(i)).getChildCount(); x++) {
						View v1 = ((ViewGroup) viewGroup.getChildAt(i)).getChildAt(x);						
						if(v1 instanceof RadioButton) {
							if(v1 != v) {
								RadioButton rb = (RadioButton) v1;
								rb.setChecked(false);
							}
							else
								checked = true;
						}
						else if(v1 instanceof LinearLayout && checked) {
							
							for(int y = 0; y < ((ViewGroup)v1).getChildCount(); y++) {
								View v2 = ((ViewGroup)v1).getChildAt(y);
								if(v2 instanceof TextView) {
									TextView tv = (TextView)v2;
									selectedItem = tv.getText().toString();
								}
							}
						}
					}
		        }
			}
		});
        
        return convertView;
	}
	
	public String getSelectedItem() { return selectedItem; }
	
}