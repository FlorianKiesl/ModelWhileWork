package ce.modelwhilework.presentation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RadioButtonListAdapter extends ArrayAdapter<String> {
	
	LayoutInflater inflater;
	int resourceId;
	Context ctx;
	String selectedItem;
	
	public RadioButtonListAdapter(Context context, int resourceId,
			ArrayList<String> title) {
		super(context, resourceId, title);
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;	
		this.ctx = context;
		selectedItem = "";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewGroup viewGroup = parent;
		
		convertView = ( RelativeLayout ) inflater.inflate( resourceId, null );
        
        String title = getItem( position );
        
        TextView tv = (TextView) convertView.findViewById(R.id.activity_list_radioButton_radioButton_TextView);
        tv.setText(title);
        
        RadioButton rb = (RadioButton) convertView.findViewById(R.id.activity_list_radioButton_radioButton);
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
						else if(v1 instanceof TextView && checked) {
							TextView tv = (TextView)v1;
							selectedItem = tv.getText().toString();
						}
					}
		        }
			}
		});

        return convertView;
	}
	
	public String getSelectedItem() { return selectedItem; }
	
}