package ce.modelwhilework.business;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import ce.modelwhilework.data.Settings;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class RestWebService {

	public void invokeExportXML(String xmlFileData, Context ctx){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put(Settings.getInstance().getParameterWebservice(), xmlFileData);
		params.setContentEncoding("UTF-8");
		try {
			HttpEntity entity = null;
			try {
				entity = new StringEntity(xmlFileData);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					if (arg2 != null){
						System.out.println("Sucess: " + arg2.toString());
					}
					System.out.println("Sucess: ");
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					System.out.println("Error:");
				}
			};
			String url = Settings.getInstance().getWebservic();
//			client.addHeader("Content-Type", "text/plain");
			RequestHandle request = client.post(ctx, url, entity, "text/plain", responseHandler);

//			RequestHandle request = client.post(url, params, responseHandler);
						
		} catch (Exception exc){
			exc.printStackTrace();
			Context context = ctx;
			CharSequence text = exc.getMessage();
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}
	
	public void postExportXML(String xmlFileData){
		new Task().execute(xmlFileData);
	}
	
	class Task extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			HttpPost postMethod;
			HttpClient httpClient;
			httpClient = new DefaultHttpClient();
			
			postMethod = new HttpPost(Settings.getInstance().getWebservic());
			try{
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
	            params1.add(new BasicNameValuePair(Settings.getInstance().getParameterWebservice(), "Test"));
	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,HTTP.UTF_8);
	            
				postMethod.setEntity(ent);
				HttpResponse response = httpClient.execute(postMethod);
				HttpEntity entity = response.getEntity();
				
			} catch (Exception exc){
				exc.printStackTrace();
			}
			finally{
//				postMethod.abort();
//				postMethod.
			}
			return "";
		}

		
	}
}
