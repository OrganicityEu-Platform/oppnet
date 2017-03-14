package info.fshi.oppnetdemo1.fragment;

import info.fshi.oppnetdemo1.R;
import info.fshi.oppnetdemo1.data.QueueManager;
import info.fshi.oppnetdemo1.http.WebServerConnector;
import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NetworkFragment extends Fragment {

	Context mContext;

	private final String TAG = "NetworkFragment";

	public NetworkFragment(Context context){
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.network_fragment_layout, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Log.d(TAG, "create views");

		TextView queueLenTv = (TextView) getView().findViewById(R.id.network_queue_len);
		queueLenTv.setText(String.valueOf(QueueManager.getInstance(mContext).getQueueLength()));

		TextView receivedTv = (TextView) getView().findViewById(R.id.network_received);
		receivedTv.setText(String.valueOf(QueueManager.getInstance(mContext).packetsReceived));

		TextView sentTv = (TextView) getView().findViewById(R.id.network_sent);
		sentTv.setText(String.valueOf(QueueManager.getInstance(mContext).packetsSent));

		TextView creditTv = (TextView) getView().findViewById(R.id.network_credit);
		creditTv.setText(String.valueOf(QueueManager.getInstance(mContext).packetsSent + QueueManager.getInstance(mContext).packetsReceived));	

		TextView peerTv = (TextView) getView().findViewById(R.id.network_peer);
		peerTv.setText(String.valueOf(QueueManager.getInstance(mContext).peers));	

		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null) {
			WifiInfo info = wifiManager.getConnectionInfo();
			if (info != null) {
				String ssid = info.getSSID();
				TextView wifiTv = (TextView) getView().findViewById(R.id.network_wifi);
				wifiTv.setText(ssid); 	
			}
		}
		
		Button uploadDataBtn = (Button) getView().findViewById(R.id.btn_send_data);
		uploadDataBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebServerConnector.getInstance(mContext).uploadSensorData(System.currentTimeMillis());
			}
			
		});
	}
}
