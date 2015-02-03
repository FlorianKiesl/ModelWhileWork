package ce.modelwhilework.presentation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.GridView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback,
		Camera.PictureCallback {
	
	private Camera camera = null;
	private Camera.PictureCallback cameraCallbackPreView;
	private Camera.ShutterCallback cameraCallbackShutter;
	private SurfaceHolder cameraViewHolder;
	private boolean bFrontCamera = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
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
		// setType() ist ab Android 4.x überflüssig und wird ignoriert
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
		try {
			
			//todo: die daten in die kontextinformation aufnehmen und visuaöisieren!!!
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
			String name = "foto_" + df.format(new Date());
			ContentValues werte = new ContentValues();
			werte.put(MediaColumns.TITLE, name);
			werte.put(ImageColumns.DESCRIPTION, "Aufgenommen mit CameraDemo");
			Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
					werte);
			OutputStream ausgabe = getContentResolver().openOutputStream(uri);
			ausgabe.write(data);
			ausgabe.close();
			camera.startPreview();
		} catch (Exception ex) {
		}
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
	
	public void make_picture(View v) {
		if (camera != null) {
			camera.takePicture(this.cameraCallbackShutter,
					           this.cameraCallbackPreView, this);
		}
	}
}
