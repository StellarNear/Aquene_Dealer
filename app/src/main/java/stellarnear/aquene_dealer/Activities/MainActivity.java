package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import stellarnear.aquene_dealer.R;
import stellarnear.aquene_dealer.Perso.Perso;


public class MainActivity extends AppCompatActivity {
    public static Perso aquene;
    boolean loading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_modeDEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        if (aquene==null) {
            final Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.start_back_color));
            lockOrient();
            Thread persoCreation = new Thread(new Runnable() {
                public void run() {
                    aquene = new Perso(getApplicationContext());
                    loading=true;
                }
            });
            persoCreation.start();


            final ImageView image = new ImageView(getApplicationContext());
            image.setImageDrawable(getDrawable(R.drawable.monk_female_background));
            image.setBackgroundColor(getColor(R.color.start_back_color));
            setContentView(image);

            Thread loadListner = new Thread(new Runnable() {
                public void run() {
                    setLoadCompleteListner(image,window);
                }
            });
            loadListner.start();

        } else {
            buildMainPage();
        }
    }

    private void setLoadCompleteListner(final ImageView image,final Window window) {
        Timer timerRefreshLoading = new Timer();
        timerRefreshLoading.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(loading){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageDrawable(getDrawable(R.drawable.monk_female_background_done));
                            image.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View arg0, MotionEvent arg1) {
                                    unlockOrient();
                                    window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
                                    buildMainPage();
                                    return true;//always return true to consume event
                                }
                            });
                        }
                    });
                }
            }
        },0,333);
    }

    private void buildMainPage() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ImageButton toStance=(ImageButton) findViewById(R.id.button_to_stance);
        toStance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        ImageButton toHelp=(ImageButton) findViewById(R.id.button_to_help);
        toHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        });

        startFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (aquene!=null) {
            aquene.refresh();
            startFragment();
        }
    }

    private void startFragment() {
        Fragment fragment = new MainActivityFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_frame_layout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            unlockOrient();
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                //on y est déja
                break;

            case Surface.ROTATION_90:
                Intent intent_stance = new Intent(MainActivity.this, StanceActivity.class);
                startActivity(intent_stance);
                break;

            case Surface.ROTATION_270:
                Intent intent_help = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent_help);
                break;
        }
    }

    private void checkOrientStart(int screenOrientation) {
        if (getRequestedOrientation()!=screenOrientation) {
            setRequestedOrientation(screenOrientation);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
            }, 2500);
        }
    }

    private void lockOrient() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void unlockOrient() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

}
