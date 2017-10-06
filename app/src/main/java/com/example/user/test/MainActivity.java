package com.example.user.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int currentApiVersion;
    EditText userStatusMessage;
    NavigationView navigationView;
    HttpURLConnection httpURLConnection;
    JSONObject reader = new JSONObject();
    String userStatusStringFromServer, userID, editTextUserMessage;
    Intent intent;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        currentApiVersion   = android.os.Build.VERSION.SDK_INT;
        
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        intent  = getIntent();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userID              = intent.getStringExtra("userID");

        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, homeFragment);
        fragmentTransaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*
        *   CONTACT LIST
        * */

        if (id == R.id.nav_home) {

            Bundle bundle = new Bundle();
            bundle.putString("userID", userID);

            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, homeFragment);
            homeFragment.setArguments(bundle);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_friends){

            bundle = new Bundle();
            bundle.putString("userID", userID);
            FriendsFragment friendsFragment = new FriendsFragment();
            friendsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, friendsFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_send){

            /*
            *   LOGOUT
            * */
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if( id == R.id.nav_status_message) {

            AsyncTask statusMessageAsync = new statusMessageAsync();
            statusMessageAsync.execute(new String[]{userID});

        } else if (id == R.id.nav_share)   {

            bundle = new Bundle();
            bundle.putString("userID", userID);
            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, settingsFragment);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setStatusMessage(EditText userStatusMessage, final Intent intent, String stringFromServer) {

        android.app.AlertDialog dialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_status_msg, null);
        builder.setView(view);
        builder.setTitle("Quick Edit Status Message");
        userStatusMessage       = (EditText) view.findViewById(R.id.et_new_status_msg);

        final EditText editTextUserStatus = (EditText) view.findViewById(R.id.et_new_status_msg);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        setFragmentActivated(currentFragment);

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                editTextUserMessage = editTextUserStatus.getText().toString();
                AsyncTask setStatusMessageAsync = new setStatusMessageAsync();
                setStatusMessageAsync.execute(new String[]{userID, editTextUserMessage});
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                setFragmentActivated(currentFragment);
            }
        });

        dialog = builder.create();
        userStatusMessage.setText(stringFromServer);
        userStatusMessage.setSelection(userStatusMessage.getText().length());
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                setFragmentActivated(currentFragment);
            }
        });
    }

    /*
    *   Method to activated the last fragment
    * */
    private void setFragmentActivated(Fragment currentFragment) {

        if (currentFragment instanceof HomeFragment)    navigationView.getMenu().getItem(0).setChecked(true);
        if (currentFragment instanceof FriendsFragment) navigationView.getMenu().getItem(1).setChecked(true);

    }

    private class statusMessageAsync extends AsyncTask<String, Integer, String>  {

        StringBuilder response  = new StringBuilder();
        String success;

        @Override
        protected String doInBackground(String... params) {

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                URL url = new URL("http://10.0.2.2/login_project/index.php");
                JSONObject userStatusJson = new JSONObject();
                userStatusJson.put("type", "get_status_message");
                userStatusJson.put("id_user", params[0]);
                String userStatusString = userStatusJson.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(userStatusString);

                writer.flush();
                writer.close();
                os.close();

                /*
                *   Getting the response from SERVER
                * */
                int responseFromServer = httpURLConnection.getResponseCode();

                if (responseFromServer == HttpURLConnection.HTTP_OK)    {

                    /*
                    *   Reading the output/response from SERVER
                    * */
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader                          = new JSONObject(response.toString());
                success                         = reader.getString("success");
                userStatusStringFromServer      = reader.getString("user_status");

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return userStatusStringFromServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setStatusMessage(userStatusMessage, intent, s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class setStatusMessageAsync extends AsyncTask<String, Integer, String>{

        StringBuilder response  = new StringBuilder();
        String success, message;

        @Override
        protected String doInBackground(String... params) {

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                URL url = new URL("http://10.0.2.2/login_project/index.php");
                JSONObject setUserStatusJson = new JSONObject();
                setUserStatusJson.put("type", "set_status_message");
                setUserStatusJson.put("id_user", params[0]);
                setUserStatusJson.put("status_message", params[1]);
                String setUserStatusString = setUserStatusJson.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(setUserStatusString);

                writer.flush();
                writer.close();
                os.close();

                /*
                *   Getting the response from SERVER
                * */
                int responseFromServer = httpURLConnection.getResponseCode();

                if (responseFromServer == HttpURLConnection.HTTP_OK)    {

                    /*
                    *   Reading the output/response from SERVER
                    * */
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader                          = new JSONObject(response.toString());
                success                         = reader.getString("success");
                message                         = reader.getString("message");

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        final View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /*
    *   Method to hide the navigation bar
    * */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
