package ce.modelwhilework.presentation.contextinfo;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ce.modelwhilework.data.contextinfo.Audio;
import ce.modelwhilework.data.contextinfo.ContextInformation;
import ce.modelwhilework.data.contextinfo.Picture;
import ce.modelwhilework.data.contextinfo.Text;
import ce.modelwhilework.data.contextinfo.Video;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.drawable;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import android.content.ClipData;
import android.content.Context;
import android.support.v4.util.Pools.Pool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
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
			idx++;
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
			grid.setTag(((ContextInformation) this.getItem(position)).getID());
			TextView text = (TextView) grid.findViewById(R.id.list_contextinfo_grid_text);
			ImageView imgView = (ImageView) grid.findViewById(R.id.list_contextinfo_grid_image);
			ContextInformation curContextInfo = (ContextInformation) this.getItem(position);
//			imgView.setTag(position);
			if (curContextInfo instanceof Picture){
				text.setText("Picture");
				imgView.setImageResource(R.drawable.contextfile_picture48);
			}
			else if (curContextInfo instanceof Video){
				text.setText("Video");
				imgView.setImageResource(R.drawable.contextfile_video48);
			}
			else if (curContextInfo instanceof Audio){
				text.setText("Audio");
				imgView.setImageResource(R.drawable.contextfile_audio48);				
			}
			else if (curContextInfo instanceof Text){
				text.setText("Text");
				imgView.setImageResource(R.drawable.contextfile_text48);				
			}
			else{
				text.setText("Title");
				imgView.setImageResource(R.drawable.contextfile_48);
			}
		}
		else{
			grid = (View) convertView;
		}
		return grid;
	}

}
