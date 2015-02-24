package ce.modelwhilework.presentation;

import java.util.ArrayList;

import ce.modelwhilework.data.Message;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListAdapterMsg extends ArrayAdapter<Message> {
	
	LayoutInflater inflater;
	int resourceId;
	Context ctx;
	String selectedItem;
	
	public ListAdapterMsg(Context context, int resourceId,
			ArrayList<Message> msg) {
		super(context, resourceId, msg);
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;	
		this.ctx = context;
		selectedItem = "";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewGroup viewGroup = parent;
		
		convertView = ( LinearLayout ) inflater.inflate( resourceId, null );
        
		Message msg = getItem( position );
        
		EditText et = (EditText) convertView.findViewById(R.id.activity_favorite_msg_editTextCardTitle);
        et.setText(msg.getTitle());
        
        et = (EditText) convertView.findViewById(R.id.activity_favorite_msg_editTextCardReciverSender);
        et.setText(msg.getSenderReceiver());
        
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.activity_favorite_msg_checkBoxCardSend);
        cb.setChecked(msg.isSender());
        
        cb = (CheckBox) convertView.findViewById(R.id.activity_favorite_msg_checkBoxCardReceive);
        cb.setChecked(!msg.isSender());
        
        RadioButton rb = (RadioButton) convertView.findViewById(R.id.activity_favorite_msg_RadioButtont);
        rb.setOnClickListener(new View.OnClickListener() {
			
        	@Override
			public void onClick(View v) {
				
        		for(int i = 0; i < viewGroup.getChildCount(); i++) {
        			
        			RadioButton rb1 = (RadioButton) viewGroup.getChildAt(i).findViewById(R.id.activity_favorite_msg_RadioButtont);
        			EditText et = (EditText) viewGroup.getChildAt(i).findViewById(R.id.activity_favorite_msg_editTextCardTitle);
        			
        			if(rb1 == v) {
        				rb1.setChecked(true);
        				selectedItem = et.getText().toString();
        			}
        			else
        				rb1.setChecked(false);
        		}
        	}
		});

        return convertView;
	}
	
	public String getSelectedItem() { return selectedItem; }
}