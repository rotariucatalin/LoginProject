package com.example.user.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    String password, username;
    EditText usernameText, passwordText;
    Button loginButton, registerButton;
    String[] parameters = new String[2];

    OutputStream outputStream               = null;
    InputStream inputStream                 = null;
    HttpURLConnection httpURLConnection     = null;

    MCrypt mCrypt = new MCrypt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //Hide the status bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        usernameText    = (EditText)findViewById(R.id.username);
        passwordText    = (EditText)findViewById(R.id.password);
        loginButton     = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    username   = String.valueOf(mCrypt.encrypt(usernameText.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    password   = String.valueOf(mCrypt.encrypt(passwordText.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                parameters[0]   = username;
                parameters[1]   = password;

                if(checkCredetntials(parameters))    {

                    AsyncTask loginAsyncTask = new AsyntTaskLogin();
                    loginAsyncTask.execute(new String[]{username,password});

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Please complete all fields", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        });
    }

    private boolean checkCredetntials(String[] parameters) {
        int max_lenght = parameters.length - 1;
        for(int i = 0; i <= max_lenght; i++)
            if(parameters[i].length() == 0)
                return false;

        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private class AsyntTaskLogin extends AsyncTask<String, Integer, String>  {

        ProgressDialog loginProcessDialog;
        StringBuilder response  = new StringBuilder();
        String result;

        @Override
        protected String doInBackground(String... params) {
            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {
                URL url = new URL("http://10.0.2.2/test/index.php");
                JSONObject loginCredentials = new JSONObject();
                loginCredentials.put("username", params[0]);
                loginCredentials.put("password", params[1]);
                String loginString = loginCredentials.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(loginString);

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
                JSONObject reader = new JSONObject(response.toString());
                result  = reader.getString("username");

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginProcessDialog=new ProgressDialog(LoginActivity.this);
            loginProcessDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loginProcessDialog.setIndeterminate(false);
            loginProcessDialog.setMax(10000);
            loginProcessDialog.setMessage("Downloading file...");
            loginProcessDialog.show();
            loginProcessDialog.setCancelable(false);
            loginProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loginProcessDialog.dismiss();
            loginProcessDialog = ProgressDialog.show(LoginActivity.this, "Loading...", result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            loginProcessDialog.setProgress(values[0]);
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
}
