package ce.modelwhilework.presentation;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class ListAdapterCheckBox extends ArrayAdapter<String> {
	
	LayoutInflater inflater;
	int resourceId;
	Context ctx;
	TreeSet<String> selectedItems;
	
	public ListAdapterCheckBox(Context context, int resourceId,
			ArrayList<String> title) {
		super(context, resourceId, title);
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;	
		this.ctx = context;
		selectedItems = new TreeSet<String>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewGroup viewGroup = parent;
		
		selectedItems.clear();
		convertView = ( RelativeLayout ) inflater.inflate( resourceId, null );
        
        String title = getItem( position );
        
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.activity_list_radioButton_checkbox);
        cb.setText(title);
        
        cb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(v instanceof CheckBox) {
					CheckBox cb = (CheckBox)v;
					if(cb.isChecked())
						selectedItems.add(cb.getText().toString());
					else
						selectedItems.remove(cb.getText().toString());
				}
			}
        });

        return convertView;
	}
	
	public TreeSet<String> getSelectedItems() { return selectedItems; }
	
}