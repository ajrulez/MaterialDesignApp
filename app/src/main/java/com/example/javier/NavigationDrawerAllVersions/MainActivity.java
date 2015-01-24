package com.example.javier.NavigationDrawerAllVersions;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.javier.NavigationDrawerAllVersions.Utilitis.CircleTransform;
import com.example.javier.NavigationDrawerAllVersions.Utilitis.ColorChooserDialog;
import com.example.javier.NavigationDrawerAllVersions.Utilitis.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    ScrollView scrollView;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    Intent intent;
    FrameLayout statusBar;
    SharedPreferences.Editor editor;
    ActivityOptions options;
    final Context context = this;
    TextView textViewDisplayName, textViewTagline;
    ImageView imageViewCoverPhoto, imageViewImage;
    String urlName = "";
    String url = "https://www.googleapis.com/plus/v1/people/%2B" + urlName + "?key=AIzaSyANa7wXqlgXNYHydN7AXdxQkEpbC3QejEw";
    String displayName, tagline, givenName, familyName, coverPhoto, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Select theme saved by user
        sharedPreferences = getSharedPreferences("THEMES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);

        // Set content to the view
        setContentView(R.layout.activity_main);

        //Setup Status Bar and Toolbar
        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Activity");

        //Setup Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        new AsyncTaskParseJson().execute();

        // Fix issues for each version and modes (check method at end of this file)
        setNavigationStatusBar();

        // Setup Navigation Drawer icon
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        // Advanced Settings
        RelativeLayout chooseTheme = (RelativeLayout) findViewById(R.id.chooseTheme);
        chooseTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ColorChooserDialog dialog = new ColorChooserDialog();
                dialog.show(fm, "fragment_color_chooser");
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
            /*final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.theme_dialog);
            dialog.show();*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            urlName = "javiersegoviacordoba";
            url = "https://www.googleapis.com/plus/v1/people/%2B" + urlName + "?key=AIzaSyANa7wXqlgXNYHydN7AXdxQkEpbC3QejEw";
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json;
            try {
                json = JsonParser.readJsonFromUrl(url);

                // Storing each json item in variable
                givenName = json.getJSONObject("name").getString("givenName");
                familyName = json.getJSONObject("name").getString("familyName");
                image = json.getJSONObject("image").getString("url");
                image = image.replace("?sz=50", "");
                coverPhoto = json.getJSONObject("cover").getJSONObject("coverPhoto").getString("url");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            textViewDisplayName = (TextView) findViewById(R.id.textViewDisplayName);
            textViewDisplayName.setText(givenName);
            textViewTagline = (TextView) findViewById(R.id.textViewTagline);
            textViewTagline.setText(familyName);
            imageViewImage = (ImageView) findViewById(R.id.imageViewImage);
            Picasso.with(context).load(image).transform(new CircleTransform()).into(imageViewImage);
            imageViewCoverPhoto = (ImageView) findViewById(R.id.imageViewCoverPhoto);
            Picasso.with(context).load(coverPhoto).into(imageViewCoverPhoto);
        }
    }

    public void setThemeFragment(int theme) {
        switch (theme) {
            case 1:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 1).apply();
                break;
            case 2:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 2).apply();
                break;
            case 3:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 3).apply();
                break;
            case 4:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 4).apply();
                break;
            case 5:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 5).apply();
                break;
            case 6:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 6).apply();
                break;
        }
    }

    public void setNavigationStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                mDrawerLayout.setStatusBarBackgroundColor(color);
            }
        }
    }

    public void settingTheme(int theme) {
        switch (theme) {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }

    /*public void settingTransition() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 21) {
                    LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
                    contentLayout.setTransitionName("LAYOUT");
                    options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                            Pair.create(findViewById(R.id.contentLayout), "LAYOUT"));
                    startActivity(intent, options.toBundle());

                } else {
                    startActivity(intent);
                }
            }
        }, 300);
    }/*



    /*@Override protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        editor.putInt("POSITION", 0).apply();

    }*/
}
