package com.example.mixalis.psagmenos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.pollfish.constants.Position;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishSurveyCompletedListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.main.PollFish;

import java.sql.SQLException;
import java.util.Locale;

import Database.ExternalDbOpenHelper;
import Misc.Preferences;

import static android.media.MediaPlayer.*;
import static com.example.mixalis.psagmenos.R.*;

public class MainActivity extends Activity implements PollfishSurveyCompletedListener, PollfishClosedListener, PollfishOpenedListener, PollfishSurveyNotAvailableListener {
    private TextView enarxi;
    private TextView settings;
    private TextView playAndLearn;
    public static MediaPlayer mediaPlayer;
    ProgressBar progressBarq;
    public static final String LANGUAGE_KEY = "lang";
    boolean isSoundEnabled;
int mousiki=0;


    @Override
    public void onBackPressed()
    {
        // super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String currentLanguage = preferences.getString(LANGUAGE_KEY,Locale.getDefault().getLanguage().startsWith("el")?"el": "en");

        Resources res = MainActivity.this.getResources();
        // Change locale settings in the app.
        Locale locale = new Locale(currentLanguage);
        Locale.setDefault(locale);
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        res.getDisplayMetrics();
        setContentView(layout.activity_main);



        mediaPlayer = create(this, raw.back1);
         isSoundEnabled = (boolean) Preferences.get(this, RythmiseisActivity.SOUNDSETTINGS, RythmiseisActivity.ISSOUNDENABLED, true);
        if(isSoundEnabled)
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });


       /* try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        enarxi = (TextView) findViewById(id.enarxi);
        settings = (TextView) findViewById(R.id.title2);
        playAndLearn = (TextView) findViewById(R.id.title);
        enarxi.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                    mousiki=1;

                Intent myIntent = new Intent(MainActivity.this, EnarxiActivity.class);
                MainActivity.this.startActivity(myIntent);





            }

        });

        findViewById(R.id.title2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mousiki=1;
                Intent myIntent = new Intent(MainActivity.this, RythmiseisActivity.class);
                MainActivity.this.startActivity(myIntent);
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

    @Override
    protected void onResume() {
        enarxi.setText(R.string.start);
        settings.setText(R.string.settings);
        playAndLearn.setText(R.string.playlearn);
        new ExternalDbOpenHelper(this);

        isSoundEnabled = (boolean) Preferences.get(this, RythmiseisActivity.SOUNDSETTINGS, RythmiseisActivity.ISSOUNDENABLED, true);
        if(isSoundEnabled && !mediaPlayer.isPlaying())
            mediaPlayer.start();

        super.onResume();
        PollFish.init(this, this.getResources().getString(string.pollfish_key), Position.TOP_LEFT, 0);

    }

@Override
    protected void onPause(){
        super.onPause();
if (mousiki!=1) {
    mediaPlayer.pause();
   // mediaPlayer.release();
}
        mousiki=0;
    }


    @Override
    public void onPollfishClosed() {
        Log.d("Pollfish","Poll closed");
    }

    @Override
    public void onPollfishOpened() {
        Log.d("Pollfish","Poll opened");

    }

    @Override
    public void onPollfishSurveyCompleted(boolean b, int i) {
        Log.d("Pollfish","Poll completed");

    }

    @Override
    public void onPollfishSurveyNotAvailable() {
        Log.d("Pollfish","Poll not available");

    }
}
