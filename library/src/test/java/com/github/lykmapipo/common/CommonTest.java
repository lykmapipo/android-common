package com.github.lykmapipo.common;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.github.lykmapipo.common.provider.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.SocketException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(RobolectricTestRunner.class)
public class CommonTest {
    private Context context;
    private Provider appProvider;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        Common.of(new Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return context;
            }

            @NonNull
            @Override
            public Boolean isDebug() {
                return BuildConfig.DEBUG;
            }
        });
    }

    @Test
    public void shouldBeAbleToProvideContext() {
        Context context = Common.getApplicationContext();
        assertThat(context, is(not(equalTo(null))));
    }

    @Test
    public void shouldCheckAppDebugState() {
        Boolean isDebug = Common.isDebug();
        assertThat(isDebug, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideConnectivityManager() {
        ConnectivityManager manager = Common.Network.getConnectivityManager();
        assertThat(manager, is(not(equalTo(null))));
    }

    @Test
    public void shouldCheckNetworkConnection() {
        Boolean isConnected = Common.Network.isConnected();
        assertThat(isConnected, is(not(equalTo(null))));
    }

    @Test
    public void shouldCheckForNetworkException() {
        Boolean isNetworkException = Common.Network.isNetworkException(new SocketException());
        assertThat(isNetworkException, is(not(equalTo(null))));
    }

    @Test
    public void shouldCheckForOffline() {
        Boolean isOffline = Common.Network.isOffline(new SocketException());
        assertThat(isOffline, is(not(equalTo(null))));
    }

    @After
    public void clean() {
        context = null;
        Common.dispose();
    }
}
