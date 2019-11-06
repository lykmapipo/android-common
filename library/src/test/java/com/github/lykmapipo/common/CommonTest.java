package com.github.lykmapipo.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.github.lykmapipo.common.provider.Provider;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

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
    public void shouldBeAbleToProvideGson() {
        Gson gson = Common.gson();
        assertThat(gson, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeAbleToProvideContext() {
        Context context = Common.applicationContext();
        assertThat(context, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeAbleToProvidePackageManager() {
        PackageManager packageManager = Common.packageManager();
        assertThat(packageManager, is(not(equalTo(null))));
    }

    @Test
    public void shouldCheckAppDebugState() {
        Boolean isDebug = Common.isDebug();
        assertThat(isDebug, is(not(equalTo(null))));
    }

    @Test
    public void shouldConvertValueToJson() {
        User user = new User("John Doe");
        String json = "{\"name\":\"John Doe\",\"phoneNumber\":null}";
        String converted = Common.Value.toJson(user);
        assertThat(converted, is(equalTo(json)));
    }

    @Test
    public void shouldConvertJsonToValue() {
        User user = new User("John Doe");
        String json = "{\"name\":\"John Doe\"}";
        User converted = Common.Value.fromJson(json, User.class);
        assertThat(converted, is(equalTo(user)));
    }

    @Test
    public void shouldCreateSetOfValues() {
        Set<Integer> set = Common.Value.setOf(1);
        assertThat(set, is(not(equalTo(null))));
    }

    @Test
    public void shouldCreateListOfValues() {
        List<Integer> list = Common.Value.listOf(1);
        assertThat(list, is(not(equalTo(null))));
    }

    @Test
    public void shouldJoinListString() {
        assertThat(Common.Strings.join("", "1", "2"), is(equalTo("1,2")));
    }

    @Test
    public void shouldGetValueOrDefault() {
        User user = new User("John");
        assertThat(Common.Value.valueOr(null, user), is(equalTo(user)));
        assertThat(Common.Value.valueOr(null, 1), is(equalTo(1)));
        assertThat(Common.Strings.valueOr(null), is(equalTo("N/A")));
        assertThat(Common.Strings.valueOr("", "1"), is(equalTo("1")));
    }

    @Test
    public void shouldCheckForEmptyString() {
        assertThat(Common.Strings.isEmpty(""), is(equalTo(true)));
        assertThat(Common.Strings.isEmpty(null), is(equalTo(true)));
        assertThat(Common.Strings.areEmpty(null, ""), is(equalTo(true)));
        assertThat(Common.Strings.areEmpty("", "1"), is(equalTo(true)));
        assertThat(Common.Strings.areEmpty("1", "2"), is(equalTo(false)));
    }

    @Test
    public void shouldClearDateTime() {
        Date date = Common.Dates.clearTime(new Date());
        assertThat(date, is(not(equalTo(null))));
        assertThat(date.getHours(), is(equalTo(0)));
        assertThat(date.getMinutes(), is(equalTo(0)));
        assertThat(date.getSeconds(), is(equalTo(0)));
    }

    @Test
    public void shouldResetDateToMidNigth() {
        Date date = Common.Dates.midNightOf(new Date());
        assertThat(date, is(not(equalTo(null))));
        assertThat(date.getHours(), is(equalTo(23)));
        assertThat(date.getMinutes(), is(equalTo(59)));
        assertThat(date.getSeconds(), is(equalTo(59)));
    }

    @Test
    public void shouldAddDaysToDate() {
        Date date = new Date();
        Date dt = Common.Dates.addDays(date, 2);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() + 2)));
    }

    @Test
    public void shouldSubtractDaysToDate() {
        Date date = new Date();
        Date dt = Common.Dates.addDays(date, -2);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() - 2)));
    }

    @Test
    public void shouldObtainToday() {
        Date dt = Common.Dates.today();
        assertThat(dt, is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainTodayMidNight() {
        Date dt = Common.Dates.todayMidNight();
        assertThat(dt, is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateBefore() {
        Date date = new Date();
        Date dt = Common.Dates.before(date, 1);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() - 1)));
    }

    @Test
    public void shouldObtainDateAfter() {
        Date date = new Date();
        Date dt = Common.Dates.after(date, 1);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() + 1)));
    }

    @Test
    public void shouldObtainYesterdayOfGivenDate() {
        Date date = new Date();
        Date dt = Common.Dates.yesterdayOf(date);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() - 1)));
    }

    @Test
    public void shouldObtainYesterday() {
        Date date = new Date();
        Date dt = Common.Dates.yesterday();
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() - 1)));
    }

    @Test
    public void shouldObtainTomorrowOfGivenDate() {
        Date date = new Date();
        Date dt = Common.Dates.tomorrowOf(date);
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() + 1)));
    }

    @Test
    public void shouldObtainTomorrow() {
        Date date = new Date();
        Date dt = Common.Dates.tomorrow();
        assertThat(dt, is(not(equalTo(null))));
        assertThat(dt.getDate(), is(equalTo(date.getDate() + 1)));
    }

    @Test
    public void shouldCheckIfDateIsAfter() {
        Date tm = Common.Dates.tomorrow();
        Date td = Common.Dates.today();
        Boolean after = Common.Dates.isAfter(td, tm);
        assertThat(after, is(equalTo(true)));
    }

    @Test
    public void shouldCheckIfDateIsAfterToday() {
        Date tm = Common.Dates.tomorrow();
        Boolean after = Common.Dates.isAfterToday(tm);
        assertThat(after, is(equalTo(true)));
    }

    @Test
    public void shouldCheckIfDateIsBefore() {
        Date yd = Common.Dates.yesterday();
        Date td = Common.Dates.today();
        Boolean after = Common.Dates.isBefore(td, yd);
        assertThat(after, is(equalTo(true)));
    }

    @Test
    public void shouldCheckIfDateIsBeforeToday() {
        Date yd = Common.Dates.yesterday();
        Boolean after = Common.Dates.isBeforeToday(yd);
        assertThat(after, is(equalTo(true)));
    }

    @Test
    public void shouldParseDate() {
        Date dt = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        assertThat(dt, is(not(equalTo(null))));
    }

    @Test
    public void shouldFormatDate() {
        Date dp = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        String df = Common.Dates.format(dp, "dd MMM yyyy");
        assertThat(dp, is(not(equalTo(null))));
        assertThat(df, is(equalTo("31 Jan 2019")));
    }

    @Test
    public void shouldObtainTimezone() {
        String tz = Common.Dates.timezone();
        assertThat(tz, is(not(equalTo(null))));
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

    @Test
    public void shouldObtainDefaultBundle() {
        Bundle bundle = Common.Bundles.defaults();
        assertThat(bundle, is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainEmptyBundle() {
        Bundle bundle = Common.Bundles.empty();
        assertThat(bundle, is(not(equalTo(null))));
    }

    @Test
    public void shouldMergeBundles() {
        Bundle bundle = Common.Bundles.from(Common.Bundles.empty());
        assertThat(bundle, is(not(equalTo(null))));
    }

    @Test
    public void shouldMergeBundleables() {
        Bundle bundle = Common.Bundles.from(new User("John Doe"));
        assertThat(bundle, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideBackgroundExecutor() {
        Executor executor = Common.AppExecutors.background();
        assertThat(executor, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideScheduleExecutor() {
        Executor executor = Common.AppExecutors.schedule();
        assertThat(executor, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideDiskIOExecutor() {
        Executor executor = Common.AppExecutors.diskIO();
        assertThat(executor, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideNetworkIOExecutor() {
        Executor executor = Common.AppExecutors.networkIO();
        assertThat(executor, is(not(equalTo(null))));
    }

    @Test
    public void shouldProvideMainThreadExecutor() {
        Executor executor = Common.AppExecutors.mainThread();
        assertThat(executor, is(not(equalTo(null))));
    }

    @After
    public void clean() {
        context = null;
        Common.dispose();
    }
}
