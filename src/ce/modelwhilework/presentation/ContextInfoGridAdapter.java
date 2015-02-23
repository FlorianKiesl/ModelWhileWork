package ce.modelwhilework.presentation;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Text;
import ce.modelwhilework.data.contextinfo.Video;
import android.content.Context;
import android.support.v4.util.Pools.Pool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContextInfoGridAdapter extends BaseAdapter {

	private Context context;
	private TreeSet<ContextInformation> listContextInfo;
	
	public ContextInfoGridAdapter(Context context, TreeSet<ContextInformation> listContextInformation) {
		this.context = context;
		this.listContextInfo = listContextInformation;
	}
	
	@Override
	public int getCount() {
		return this.listContextInfo.size();
	}

	@Override
	public Object getItem(int position) {
		Iterator<ContextInformation> iterator = this.listContextInfo.iterator();
		int idx = 0;
		ContextInformation erg = null;
		while (iterator.hasNext() && idx <= position){
			erg = iterator.next();
		}
		return erg;
	}

	@Override
	public long getItemId(int position) {
		return ((ContextInformation) this.getItem(position)).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View grid;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null){
			grid = new View(context);
			grid = inflater.inflate(R.layout.list_contextinfo, null);
			TextView text = (TextView) grid.findViewById(R.id.list_contextinfo_grid_text);
			ImageView imgButton = (ImageView) grid.findViewById(R.id.list_contextinfo_grid_image);
			ContextInformation curContextInfo = (ContextInformation) this.getItem(position);
			imgButton.setTag(position);
			if (curContextInfo instanceof Picture){
				text.setText("Picture");
				imgButton.setImageResource(R.drawable.contextinfo_picture48);
			}
			else if (curContextInfo instanceof Video){
				text.setText("Video");
				imgButton.setImageResource(R.drawable.contextfile_video48);
			}
			else if (curContextInfo instanceof Audio){
				
			}
			else if (curContextInfo instanceof Text){
				
			}
			else{
				text.setText("Title");
				imgButton.setImageResource(R.drawable.contextfile_48);
			}
			
//			
//			imgButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Toast msg = Toast.makeText(v.getContext().getApplicationContext(), "Test " + v.getTag().toString(), Toast.LENGTH_LONG);
////					msg.setView(v);
//					msg.show();
//				}
//			});
		}
		else{
			grid = (View) convertView;
		}
		return grid;
	}

}
