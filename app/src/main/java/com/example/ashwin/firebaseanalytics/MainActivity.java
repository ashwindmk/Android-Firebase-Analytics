package com.example.ashwin.firebaseanalytics;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://android-firebase-test-a2516.firebaseio.com/makeup-app-list/apps/");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FirebaseRecyclerAdapter<App, AppViewHolder>(App.class, R.layout.app_list_row, AppViewHolder.class, ref) {
            @Override
            public void populateViewHolder(AppViewHolder avh, App app, final int position) {
                avh.setTitle(app.getTitle());
                avh.setSubTitle(app.getSubtitle());
                avh.setAppLogo(getApplicationContext(), app.getApplogo());
                avh.setAppImage(getApplicationContext(), app.getAppimage());
                avh.setCallToActionText(app.getCalltoactiontext());

                avh.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App app = getItem(position);
                        Toast.makeText(getApplicationContext(), app.getTitle() + " ( " + app.getPackagename() +" ) is selected!", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, app.getPackagename());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, app.getTitle());

                        //Logs an app event.
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        //Sets whether analytics collection is enabled for this app on this device.
                        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

                        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
                        mFirebaseAnalytics.setMinimumSessionDuration(20000);

                        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
                        mFirebaseAnalytics.setSessionTimeoutDuration(500);

                        //Sets the user ID property.
                        mFirebaseAnalytics.setUserId(app.getPackagename());

                        //Sets a user property to a given value.
                        mFirebaseAnalytics.setUserProperty("Name", app.getTitle());
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
