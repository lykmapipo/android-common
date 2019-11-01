package com.github.lykmapipo.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(RobolectricTestRunner.class)
public class CommonTest {
    private Context context;
    private Common.Provider appProvider;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        Common.of(new Common.Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return context;
            }
        });
    }

    @Test
    public void shouldBeAbleToProvideContext() {
        Context context = Common.getApplicationContext();
        assertThat(context, is(not(equalTo(null))));
    }

    @After
    public void clean() {
        context = null;
        Common.dispose();
    }
}
