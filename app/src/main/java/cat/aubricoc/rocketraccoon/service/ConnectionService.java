package cat.aubricoc.rocketraccoon.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.canteratech.restclient.RestClient;

public class ConnectionService implements RestClient.ConnectionService {

	private Context context;

	private ConnectionService(Context context) {
		this.context = context;
	}

	public static ConnectionService newInstance(Context context) {
		return new ConnectionService(context);
	}

	@Override
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
