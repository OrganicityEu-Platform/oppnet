package info.fshi.oppnetdemo1.http;

import info.fshi.oppnetdemo1.data.QueueManager;
import info.fshi.oppnetdemo1.experiment.Experiment;
import info.fshi.oppnetdemo1.experiment.ExperimentList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class WebServerConnector extends BroadcastReceiver{

	private final String ENTITY_URI = "urn:oc:entity:london:oppnet:";

	private final static String TAG = "web server connector";
	private static String serverUrl = "http://129.31.190.98:10226/v1/contextEntities/";

	private final static String EXPERIMENT_URL = "http://experimenters.organicity.eu:8081/";
	
	private static Context mContext = null;

	private static long updatedTime;

	private static WebServerConnector _obj = null;
	
	private WebServerConnector(Context context){
		mContext = context;
		updatedTime = System.currentTimeMillis();
		//new RegisterDeviceTask().execute();
	}
	
	public static WebServerConnector getInstance(Context context){
		if(_obj == null){
			_obj = new WebServerConnector(context);
		}
		return _obj;
	}
	
	public WebServerConnector(){}

	public void registerDevice(){

	}
	
	public void getAllExperiments(){
		new GetAllExperimentsTask().execute();
	}
	
	private class GetAllExperimentsTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... data) {
			Log.d(TAG, "Get experiment list from the server");
			
			URL url;
			HttpURLConnection connection = null;
			try{
				url = new URL(EXPERIMENT_URL + "allexperiments");
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("GET");
				connection.connect();

				InputStream in = new BufferedInputStream(connection.getInputStream());
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
				StringBuilder responseStrBuilder = new StringBuilder();

				String inputStr;
				while ((inputStr = streamReader.readLine()) != null)
					responseStrBuilder.append(inputStr);
				JSONObject json = new JSONObject(responseStrBuilder.toString());
				
				JSONArray expArray = json.getJSONArray("experiments");
				
				ArrayList<Experiment> updatedExp = new ArrayList<Experiment>();
				
				for(int i=0; i < expArray.length(); i++){
					Experiment exp = new Experiment();
					JSONObject expJSON = (JSONObject) expArray.get(i);
					exp.id = expJSON.getString("experimentId");
					exp.name = expJSON.getString("name");
					exp.description = expJSON.getString("description");
					exp.startTime = expJSON.getString("startDate");
					exp.endTime = expJSON.getString("endDate");
					JSONArray areaArray = expJSON.getJSONArray("area");
					if(areaArray.length() > 0){
						JSONObject area = (JSONObject) areaArray.get(0);
						JSONArray areaCoord = area.getJSONArray("coordinates");
						for(int j=0; j<areaCoord.length(); j++){
							exp.area.add(new LatLng(areaCoord.getJSONArray(j).getDouble(1), areaCoord.getJSONArray(j).getDouble(0)));						
						}
					}
					if(ExperimentList.joinedExp != null){
						if(exp.id.compareToIgnoreCase(ExperimentList.joinedExp.id) == 0){
							exp.joined = 1;
							ExperimentList.joinedExp = new Experiment(exp);
						}
					}
					updatedExp.add(exp);
				}
				ExperimentList.update(updatedExp);
			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
				serverAvailable = false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(connection != null){
					connection.disconnect();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ExperimentList.experimentListAdapter.notifyDataSetChanged();
			ExperimentList.experimentListAdapter.sortList();
			super.onPostExecute(result);
		}
	}

	public void sendSensorData(String data){
		new SendSensorDataTask().execute(data);
	}

	private static boolean serverAvailable = true;
	
	private class SendSensorDataTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... data) {

			String entityId = ENTITY_URI + "data:" + data[2];

			Log.d(TAG, "send data to server");
			
			JSONObject content = new JSONObject();
			try {
				// experiment id
				// application id
				// user id
				content.put("type", "data");
				JSONArray attrs = new JSONArray();
				JSONObject attr = new JSONObject();
				attr.put("name", "light");
				attr.put("type", "string");
				attr.put("value", data[1]);
				attrs.put(attr);
				attr = new JSONObject();
				attr.put("name", "path");
				attr.put("type", "string");
				attr.put("value", data[0]);
				attrs.put(attr);
				attr = new JSONObject();
				attr.put("name", "delay");
				attr.put("type", "string");
				attr.put("value", data[3]);
				attrs.put(attr);
				content.put("attributes", attrs);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			URL url;
			HttpURLConnection connection = null;
			try{
				Log.d(TAG, serverUrl + entityId);
				
				url = new URL(serverUrl + entityId);
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.connect();

				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				out.write(content.toString().getBytes());
				out.close();

				InputStream in = new BufferedInputStream(connection.getInputStream());
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
				StringBuilder responseStrBuilder = new StringBuilder();

				String inputStr;
				while ((inputStr = streamReader.readLine()) != null)
					responseStrBuilder.append(inputStr);
				Log.d(TAG, responseStrBuilder.toString());

			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
				serverAvailable = false;
			} finally {
				if(connection != null){
					connection.disconnect();
				}
			}
			return null;
		}
	}

	private class RegisterDeviceTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			// check if exists

			String entityId = ENTITY_URI + "resource:" + Secure.getString(mContext.getContentResolver(),
					Secure.ANDROID_ID); // unique identifier of the device

			URL url;
			HttpURLConnection connection = null;
			// register to orion

			JSONObject content = new JSONObject();
			try {
				content.put("type", "phone");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try{
				Log.d(TAG, serverUrl + entityId);
//				DataManager.getInstance(mContext).saveLog(serverUrl + entityId);
				
				url = new URL(serverUrl + entityId);
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.connect();

				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				out.write(content.toString().getBytes());
				out.close();

				InputStream in = new BufferedInputStream(connection.getInputStream());
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
				StringBuilder responseStrBuilder = new StringBuilder();

				String inputStr;
				while ((inputStr = streamReader.readLine()) != null)
					responseStrBuilder.append(inputStr);
//				Log.d(TAG, responseStrBuilder.toString());

			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
			} finally {
				if(connection != null){
					connection.disconnect();
				}
			}
			return null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			// Do whatever
			Log.d(TAG, "wifi connected");
//			DataManager.getInstance(mContext).saveLog("wifi connected");
			
			serverAvailable = true;
			
			// send data to sink
			long currentTime = System.currentTimeMillis();

			if(mContext != null){
				if(currentTime - updatedTime > 1000*60*1){
					while(QueueManager.getInstance(mContext).getQueueLength() > 0 & serverAvailable){
						String[] data = QueueManager.getInstance(mContext).getFromQueue();
						new SendSensorDataTask().execute(data);
					}
					QueueManager.getInstance(mContext).updateName();
					Log.d(TAG, "update name to " + String.valueOf(QueueManager.getInstance(mContext).getQueueLength()));
					new GetAllExperimentsTask().execute();
//					MainActivity.txMyQueueLen.setText(String.valueOf(QueueManager.getInstance(mContext).getQueueLength()));
					updatedTime = currentTime;
				}
			}
		}
	}
}