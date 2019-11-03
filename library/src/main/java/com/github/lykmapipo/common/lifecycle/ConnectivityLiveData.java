package com.github.lykmapipo.common.lifecycle;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.lifecycle.LiveData;

/**
 * A {@see LiveData} class which wraps the network connection status.
 * <p>
 * {@see https://www.brightec.co.uk/ideas/connectivitylivedata}
 *
 * @since 0.1.0
 */
public class ConnectivityLiveData extends LiveData<Boolean> {
    // refs
    private ConnectivityManager connectivity;
    private NetworkCallback listener = new NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            postValue(true);
        }

        @Override
        public void onLost(Network network) {
            postValue(false);
        }
    };

    public ConnectivityLiveData(ConnectivityManager connectivity) {
        this.connectivity = connectivity;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivity.registerDefaultNetworkCallback(listener);
        } else {
            NetworkRequest request = new NetworkRequest.Builder().build();
            connectivity.registerNetworkCallback(request, listener);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        connectivity.unregisterNetworkCallback(listener);
    }
}
