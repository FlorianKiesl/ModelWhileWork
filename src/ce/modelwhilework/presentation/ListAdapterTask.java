package ce.modelwhilework.presentation;

import java.util.ArrayList;
import java.util.TreeSet;

import ce.modelwhilework.data.Task;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListAdapterTask extends ArrayAdapter<Task> {
	
	LayoutInflater inflater;
	int resourceId;
	Context ctx;
	TreeSet<String> selectedItems;
	
	public ListAdapterTask(Context context, int resourceId,
			ArrayList<Task> task) {
		super(context, resourceId, task);
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
        
        Task task = getItem( position );
        
        EditText et = (EditText) convertView.findViewById(R.id.FavorteActivity_editTextWorkCardTitle);
        et.setText(task.getTitle());
        
        RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.FavorteActivity_LayoutTaskCard);
  
        rl.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
			    
				return false;
			}
		});

        return convertView;
	}
	
	public TreeSet<String> getSelectedItems() { return selectedItems; }
	
}