package ce.modelwhilework.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContextInfoGridAdapter extends BaseAdapter {

	private Context context;
	
	public ContextInfoGridAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null){
			grid = new View(context);
			grid = inflater.inflate(R.layout.list_contextinfo, null);
			TextView text = (TextView) grid.findViewById(R.id.list_contextinfo_grid_text);
			text.setText("Title");
			ImageButton imgButton = (ImageButton) grid.findViewById(R.id.list_contextinfo_grid_image);
			imgButton.setImageResource(R.drawable.contextfile_48);
		}
		else{
			grid = (View) convertView;
		}
		return grid;
	}

}
