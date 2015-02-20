package ce.modelwhilework.presentation;

import java.io.File;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			Toast msg = Toast.makeText(getBaseContext(), bundle.getString("Path"), Toast.LENGTH_LONG);
			msg.show();
			
			File file = new File(bundle.getString("Path"));
			if (file.exists()){
				Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
				ImageView img = (ImageView) this.findViewById(R.id.activity_picture_ivPicture);
				img.setImageBitmap(bmp);
			}
		}
		
	}

}
