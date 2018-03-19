package com.example.oklyso.Oblig2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.example.oklyso.Oblig2.Adapter.FeedAdapter;
import com.example.oklyso.Oblig2.Common.HTTPDataHandler;
import com.example.oklyso.Oblig2.Model.RSSObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    String feedUrl;


    // Used for collecting the RSS information through JSON
    public String RSS_link;
    private final String RSS_to_Json_API ="https://api.rss2json.com/v1/api.json?api_key=hnvqmpw4agg2e7in5zrfzi7eium8zirvfol08ked&count=100&rss_url=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //Using the default link OR the preferred one in settings.
        SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
        RSS_link = settings.getString("Url", "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml");


      // Making the Feed-Toolbar.
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Feed");
        setSupportActionBar(toolbar);


        //Creating the Viewer
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        //loading rss
        Timer repeate = new Timer ();
        TimerTask Task = new TimerTask() {
            @Override
            public void run () {
                Looper.prepare();
                loadRSS();
                Looper.loop();
            }
        };

        int[] minutes = { 60,720, 1440};
        int time = minutes[settings.getInt("Timer",MODE_PRIVATE)];
        //1000*60*10 every 10min *60hour * 1440 24 hours
        repeate.schedule (Task, 0l, 1000*60*time);
    }

    private void loadRSS() {
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Loading");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(strings[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {

                SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
                int size = settings.getInt("Elements", 1);

                mDialog.dismiss();
                rssObject = new Gson().fromJson(s,RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject,getBaseContext(), size);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        Intent intent = getIntent();
        feedUrl = intent.getStringExtra(RSSFeedPref.EXTRA_MESSAGE);
        if(feedUrl != null){
            RSS_link = feedUrl;
        }



        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh)
            loadRSS();

        return true;
    }

    public void goToRSSFeedPref(View view) {
        // Button response to move to the next Activity ( Preferences)
        Intent intent = new Intent(this, RSSFeedPref.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("Change", MODE_PRIVATE).edit();
        editor.putString("Url", RSS_link);
        editor.apply();
    }
}
