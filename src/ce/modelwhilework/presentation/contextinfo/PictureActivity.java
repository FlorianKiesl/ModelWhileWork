package ce.modelwhilework.presentation.contextinfo;

import java.io.File;

import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
//			TODO: Löschen?
//			Toast msg = Toast.makeText(getBaseContext(), bundle.getString("Path"), Toast.LENGTH_LONG);
//			msg.show();
//			
			File file = new File(bundle.getString("Path"));
			if (file.exists()){
				Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
				ImageView img = (ImageView) this.findViewById(R.id.activity_picture_ivPicture);
				img.setImageBitmap(bmp);
			}
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean erg = super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case android.R.id.home:
			this.finish();
		}
		return erg;
	}
}
