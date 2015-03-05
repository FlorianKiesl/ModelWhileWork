package ce.modelwhilework.presentation.contextinfo;

import java.io.IOException;

import ce.modelwhilework.data.Modus;
import ce.modelwhilework.data.ProcessManager;
import ce.modelwhilework.presentation.R;
import ce.modelwhilework.presentation.R.id;
import ce.modelwhilework.presentation.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, DialogInterface.OnClickListener, Camera.PictureCallback {
	
	private Camera camera = null;
	private Camera.PictureCallback cameraCallbackPreView;
	private Camera.ShutterCallback cameraCallbackShutter;
	private SurfaceHolder cameraViewHolder;
	private boolean bFrontCamera = false;
	private Modus modus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		modus = ProcessManager.getInstance().getCurrentProcess();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String title = bundle.getString("CARD_ID");
			if (title!= null) {
				modus = ProcessManager.getInstance().getCurrentProcess().getTopCard(title);
			}
		}
		
		if(modus == null)
			this.finish();
		   
		ImageButton imgButtonMakePic = (ImageButton) this.findViewById(R.id.activity_camera_imageButton_makePricture);
		imgButtonMakePic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				make_picture();
			}
		});		
	}

	protected void onPause() {
		super.onPause();
		if (camera != null) {
			camera.stopPreview();
			camera.release();
		}
	}

	protected void onResume() {
		super.onResume();
		SurfaceView cameraView = (SurfaceView) findViewById(R.id.surfaceviewCamera);
		cameraViewHolder = cameraView.getHolder();
		cameraViewHolder.addCallback(this);
		cameraViewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraCallbackPreView = new Camera.PictureCallback() {
			public void onPictureTaken(byte[] data, Camera c) {
			}
		};
		cameraCallbackShutter = new Camera.ShutterCallback() {
			public void onShutter() {
			}
		};
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		if(!this.modus.addContextInformationPicture(data)) {
			showAlert("Save picture fail!");
			camera.startPreview();
		}
		else
			this.finish();	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			camera = Camera.open();
        } catch (RuntimeException e) {
        }		
		
		if(camera == null) {
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		    int cameraCount = Camera.getNumberOfCameras();
			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                	bFrontCamera = true;
		        try {
		            	camera = Camera.open(camIdx);
		            	if(camera != null) { 		            		
		            		break;
		            	}		            	
		            	
		            } catch (RuntimeException e) {
		            }
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if (camera != null) {
			camera.stopPreview();
			if(bFrontCamera)
				camera.setDisplayOrientation(180);
			Camera.Parameters params = camera.getParameters();
			Camera.Size vorschauGroesse = params.getPreviewSize();
			params.setPreviewSize(vorschauGroesse.width, vorschauGroesse.height);
			camera.setParameters(params);
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
			}
			camera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
	
	private void make_picture() {
		if (camera != null) {
			camera.takePicture(this.cameraCallbackShutter,
					           this.cameraCallbackPreView, this);
		} else
			showAlert("no camera available!");			
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("error");
		alertDialog.setNegativeButton("OK", this);
		alertDialog.setMessage(msg);
		alertDialog.show();	
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
