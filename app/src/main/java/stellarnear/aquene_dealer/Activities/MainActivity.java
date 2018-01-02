package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.io.Serializable;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class MainActivity extends AppCompatActivity {
    Perso Aquene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        if(getIntent().getExtras()!=null) {
            if (getIntent().getExtras().containsKey("perso")) {
                Aquene = (Perso) i.getSerializableExtra("perso");
            } else {
                Aquene = new Perso(getApplicationContext());
            }
        } else {
            Aquene = new Perso(getApplicationContext());
        }


        Fragment fragment = new MainActivityFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main, fragment);
        fragmentTransaction.commit();

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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                intent_stance.putExtra("perso", (Serializable) Aquene);
                startActivity(intent_stance);
                break;

            case Surface.ROTATION_270:
                Intent intent_help = new Intent(MainActivity.this, HelpActivity.class);
                intent_help.putExtra("perso", (Serializable) Aquene);
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

}
