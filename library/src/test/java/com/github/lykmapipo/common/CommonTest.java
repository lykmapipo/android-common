package com.github.lykmapipo.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.ArraySet;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.lykmapipo.common.data.Query;
import com.github.lykmapipo.common.provider.Provider;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public void shouldGenerateRandomColor() {
        Integer color = Common.Colors.randomColor();
        assertThat(color, is(not(equalTo(null))));
    }


    @Test
    public void shouldParseColorFromHexadecimalString() {
        assertThat(Common.Colors.parseColor(null), is(not(equalTo(null))));
        assertThat(Common.Colors.parseColor(""), is(not(equalTo(null))));
        assertThat(Common.Colors.parseColor(" "), is(not(equalTo(null))));
        assertThat(Common.Colors.parseColor("#271d45"), is(not(equalTo(null))));
    }

    @Test
    public void shouldParseColorColorResource() {
        assertThat(Common.Colors.parseColor(null), is(not(equalTo(null))));
        assertThat(Common.Colors.parseColor(R.color.sample_color), is(not(equalTo(null))));
    }

    @Test
    public void shouldGenerateObjectColor() {
        Integer color = Common.Colors.colorFor(new Profile("John Doe"));
        Integer next = Common.Colors.colorFor(new Profile("John Doe"));
        assertThat(color, is(not(equalTo(null))));
        assertThat(color, is(equalTo(next)));
    }

    @Test
    public void shouldGenerateAvatarableColor() {
        Integer color = Common.Colors.colorFor(new User("John Doe"));
        Integer next = Common.Colors.colorFor(new User("John Doe"));
        assertThat(color, is(not(equalTo(null))));
        assertThat(color, is(equalTo(next)));
    }

    @Test
    public void shouldGenerateAvatarableWithNoColor() {
        Integer color = Common.Colors.colorFor(new UserNoColor("John Doe"));
        Integer next = Common.Colors.colorFor(new UserNoColor("John Doe"));

        assertThat(color, is(not(equalTo(null))));
        assertThat(color, is(equalTo(next)));
    }

    @Test
    public void shouldGenerateLetterAvatar() {
        TextDrawable drawable = Common.Drawables.letterAvatarFor("L", Common.Colors.randomColor());
        assertThat(drawable, is(not(equalTo(null))));
    }

    @Test
    public void shouldGenerateObjectLetterAvatar() {
        TextDrawable drawable = Common.Drawables.letterAvatarFor(new Profile("John Doe"));
        assertThat(drawable, is(not(equalTo(null))));
    }

    @Test
    public void shouldGenerateAvatarableLetterAvatar() {
        TextDrawable drawable = Common.Drawables.letterAvatarFor(new User("John Doe"));
        assertThat(drawable, is(not(equalTo(null))));
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
    public void shouldCheckForEmptySet() {
        assertThat(Common.Value.isEmpty(null), is(equalTo(true)));
        assertThat(Common.Value.isEmpty(new ArraySet<String>()), is(equalTo(true)));
        assertThat(Common.Value.isEmpty(Common.Value.setOf("1")), is(equalTo(false)));
    }

    @Test
    public void shouldCheckForEmptyList() {
        assertThat(Common.Value.isEmpty(null), is(equalTo(true)));
        assertThat(Common.Value.isEmpty(new ArrayList<>()), is(equalTo(true)));
        assertThat(Common.Value.isEmpty(Common.Value.listOf("1")), is(equalTo(false)));
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
    public void shouldCreateMapOfKeyValue() {
        Map<String, Integer> map = Common.Value.mapOf("1", 1);
        assertThat(map, is(not(equalTo(null))));
    }

    @Test
    public void shouldCreateMapOfValues() {
        Map<String, Integer> map = Common.Value.mapOf(
                Common.Value.mapOf("1", 1),
                null,
                Common.Value.mapOf("2", 2),
                Common.Value.mapOf("1", 1)
        );
        assertThat(map, is(not(equalTo(null))));
    }

    @Test
    public void shouldJoinListString() {
        assertThat(Common.Strings.join("", "1", "2"), is(equalTo("1,2")));
        assertThat(Common.Strings.join("", Common.Value.listOf("1", "2")), is(equalTo("1,2")));
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
    public void shouldObtainDateYear() {
        Date date = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        Integer year = Common.Dates.yearOf(date);
        assertThat(year, is(equalTo(2019)));
        assertThat(Common.Dates.yearOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateMonth() {
        Date date = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        Integer month = Common.Dates.monthOf(date);
        assertThat(month, is(equalTo(0)));
        assertThat(Common.Dates.monthOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateDayOfMonth() {
        Date date = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        Integer monthDay = Common.Dates.dayOfMonthOf(date);
        assertThat(monthDay, is(equalTo(31)));
        assertThat(Common.Dates.dayOfMonthOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateDayOfWeek() {
        Date date = Common.Dates.parse("31 Jan 2019", "dd MMM yyyy");
        Integer dayOfWeekOf = Common.Dates.dayOfWeekOf(date);
        assertThat(dayOfWeekOf, is(equalTo(5)));
        assertThat(Common.Dates.dayOfWeekOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateHourOfDay() {
        Date date = Common.Dates.parse("31 Jan 2019 23:53:21", "dd MMM yyyy HH:mm:ss");
        Integer hourOfDay = Common.Dates.hourOfDayOf(date);
        assertThat(hourOfDay, is(equalTo(23)));
        assertThat(Common.Dates.hourOfDayOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateMinute() {
        Date date = Common.Dates.parse("31 Jan 2019 23:53:21", "dd MMM yyyy HH:mm:ss");
        Integer minuteOf = Common.Dates.minuteOf(date);
        assertThat(minuteOf, is(equalTo(53)));
        assertThat(Common.Dates.minuteOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateSecond() {
        Date date = Common.Dates.parse("31 Jan 2019 23:53:21", "dd MMM yyyy HH:mm:ss");
        Integer secondOf = Common.Dates.secondOf(date);
        assertThat(secondOf, is(equalTo(21)));
        assertThat(Common.Dates.secondOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateDayOfWeekDisplayName() {
        Date date = Common.Dates.parse("31 Jan 2019 23:53:21", "dd MMM yyyy HH:mm:ss");
        String displayName = Common.Dates.dayOfWeekDisplayNameOf(date);
        assertThat(displayName, is(equalTo("Thursday")));
        assertThat(Common.Dates.dayOfWeekDisplayNameOf(), is(not(equalTo(null))));
    }

    @Test
    public void shouldObtainDateMonthDisplayName() {
        Date date = Common.Dates.parse("31 Jan 2019 23:53:21", "dd MMM yyyy HH:mm:ss");
        String displayName = Common.Dates.monthDisplayNameOf(date);
        assertThat(displayName, is(equalTo("January")));
        assertThat(Common.Dates.monthDisplayNameOf(), is(not(equalTo(null))));
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
    }

    // Resources Test

    @Test
    public void shouldGetStringResource() {
        assertThat(Common.Resources.getString(R.string.app_name), is(not(equalTo(null))));
        assertThat(Common.Resources.getString(R.string.app_name), is(equalTo("Library")));
    }

    @Test
    public void shouldGetFormattedStringResource() {
        String name = "Lally";
        Integer count = 2;
        assertThat(Common.Resources.getString(R.string.app_messages, name, count), is(not(equalTo(null))));
        assertThat(Common.Resources.getString(R.string.app_messages, name, count), is(equalTo("Hello, Lally! You have 2 new messages.")));
    }

    @Test
    public void shouldGetColorResource() {
        assertThat(Common.Resources.getColor(R.color.sample_color), is(not(equalTo(null))));
    }

    // Query Tests

    @Test
    public void shouldAllowEQFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$eq("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$eq\":1.28}}")));
    }

    @Test
    public void shouldAllowQTFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$gt("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$gt\":1.28}}")));
    }

    @Test
    public void shouldAllowQTEFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$gte("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$gte\":1.28}}")));
    }

    @Test
    public void shouldAllowINFilter() {
        Map<String, Map<String, Set<Double>>> price = Query.Filter.$in("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$in\":[1.28]}}")));
    }

    @Test
    public void shouldAllowLTFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$lt("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$lt\":1.28}}")));
    }

    @Test
    public void shouldAllowLTEFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$lte("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$lte\":1.28}}")));
    }

    @Test
    public void shouldAllowNEFilter() {
        Map<String, Map<String, Double>> price = Query.Filter.$ne("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$ne\":1.28}}")));
    }

    @Test
    public void shouldAllowNINFilter() {
        Map<String, Map<String, Set<Double>>> price = Query.Filter.$nin("price", 1.28);
        assertThat(price, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(price), is(equalTo("{\"price\":{\"$nin\":[1.28]}}")));
    }

    @Test
    public void shouldAllowANDFilter() {
        Map<String, Map<String, Double>> min = Query.Filter.$gt("price", 1.28);
        Map<String, Map<String, Double>> max = Query.Filter.$lt("price", 1.28);
        Map<String, List<Object>> and = Query.Filter.$and(min, max);
        assertThat(and, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(and), is(equalTo("{\"$and\":[{\"price\":{\"$gt\":1.28}},{\"price\":{\"$lt\":1.28}}]}")));
    }

    @Test
    public void shouldAllowORFilter() {
        Map<String, Map<String, Double>> min = Query.Filter.$gt("price", 1.28);
        Map<String, Map<String, Double>> max = Query.Filter.$lt("price", 1.28);
        Map<String, List<Object>> and = Query.Filter.$or(min, max);
        assertThat(and, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(and), is(equalTo("{\"$or\":[{\"price\":{\"$gt\":1.28}},{\"price\":{\"$lt\":1.28}}]}")));
    }

    @Test
    public void shouldProvideSearchQuery() {
        Query query = Query.create("1");
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"q\":\"1\",\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @Test
    public void shouldProvidePageQuery() {
        Query query = Query.create(1L);
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @Test
    public void shouldProvideSortingQuery() {
        Query query = Query.create(1L);
        query.descBy("price");
        query.ascBy("qty");
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"limit\":\"10\",\"page\":\"1\",\"sort\":\"{\\\"qty\\\":1,\\\"price\\\":-1}\"}")));
    }

    @Test
    public void shouldProvideSimpleFilterQuery() {
        Query query = Query.create(1L);
        query.filter(
                Query.Filter.$eq("price", 1.28)
        );
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"filter\":\"{\\\"price\\\":{\\\"$eq\\\":1.28}}\",\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @Test
    public void shouldProvideMergedFilterQuery() {
        Query query = Query.create(1L);
        query.filter(
                Query.Filter.$eq("price", 1.28),
                Query.Filter.$lt("qty", 12)
        );
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"filter\":\"{\\\"qty\\\":{\\\"$lt\\\":12},\\\"price\\\":{\\\"$eq\\\":1.28}}\",\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @Test
    public void shouldProvideLogicalFilterQuery() {
        Query query = Query.create(1L);
        query.filter(
                Query.Filter.$or(
                        Query.Filter.$eq("price", 1.28),
                        Query.Filter.$lt("qty", 12)
                )
        );
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"filter\":\"{\\\"$or\\\":[{\\\"price\\\":{\\\"$eq\\\":1.28}},{\\\"qty\\\":{\\\"$lt\\\":12}}]}\",\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @Test
    public void shouldProvideMergedLogicalFilterQuery() {
        Query query = Query.create(1L);
        query.filter(
                Query.Filter.$and(
                        Query.Filter.$or(
                                Query.Filter.$eq("price", 0.99),
                                Query.Filter.$eq("price", 1.99)
                        ),
                        Query.Filter.$or(
                                Query.Filter.$eq("qty", 1),
                                Query.Filter.$eq("qty", 2)
                        )
                )
        );
        Map<String, String> queryMap = query.toQueryMap();
        assertThat(query, is(not(equalTo(null))));
        assertThat(queryMap, is(not(equalTo(null))));
        assertThat(Common.Value.toJson(queryMap), is(equalTo("{\"filter\":\"{\\\"$and\\\":[{\\\"$or\\\":[{\\\"price\\\":{\\\"$eq\\\":0.99}},{\\\"price\\\":{\\\"$eq\\\":1.99}}]},{\\\"$or\\\":[{\\\"qty\\\":{\\\"$eq\\\":1}},{\\\"qty\\\":{\\\"$eq\\\":2}}]}]}\",\"limit\":\"10\",\"page\":\"1\"}")));
    }

    @After
    public void clean() {
        context = null;
        Common.dispose();
    }
}
