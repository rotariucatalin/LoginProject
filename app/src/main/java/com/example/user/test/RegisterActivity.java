package com.example.user.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity{

    Button registerButton;
    EditText usernameEditText,passwordEditText;
    String username,password, credentialsFromServerString;
    String[] parameters = new String[2];

    OutputStream outputStream               = null;
    InputStream inputStream                 = null;
    HttpURLConnection httpURLConnection     = null;

    JSONObject reader                       = new JSONObject();
    JSONObject responseFromserver           = new JSONObject();

    OverallMethods overallMethods           = new OverallMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //Hide the status bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        registerButton      = (Button)findViewById(R.id.registerButton);
        usernameEditText    = (EditText)findViewById(R.id.username);
        passwordEditText    = (EditText)findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username    = usernameEditText.getText().toString();
                password    = passwordEditText.getText().toString();

                parameters[0]   = username;
                parameters[1]   = password;

                if(overallMethods.checkCredetntials(parameters))   {

                    AsyncTask registerAsyncTask = new RegisterActivity.AsyncTaskRegister();
                    registerAsyncTask.execute(new String[]{username,password});

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Please complete all fields", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        });
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
                overallMethods.hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    private class AsyncTaskRegister extends AsyncTask<String, Integer, String>  {

        ProgressDialog loginProcessDialog;
        StringBuilder response  = new StringBuilder();
        String success, message;

        @Override
        protected String doInBackground(String... params) {

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {
                URL url = new URL("http://10.0.2.2/login_project/index.php");
                JSONObject loginCredentials = new JSONObject();
                loginCredentials.put("type", "register");
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
                reader              = new JSONObject(response.toString());
                success             = reader.getString("success");
                message             = reader.getString("message");

                JSONObject credentialsFromServer = new JSONObject();
                credentialsFromServer.put("success", success);
                credentialsFromServerString = credentialsFromServer.toString();

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return credentialsFromServerString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginProcessDialog=new ProgressDialog(RegisterActivity.this);
            loginProcessDialog.setMessage("Seding credentials");
            loginProcessDialog.show();
            loginProcessDialog.setCancelable(false);
            loginProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loginProcessDialog.dismiss();

            try {
                responseFromserver      = new JSONObject(result);
                success                 = responseFromserver.getString("success");
                message                 = responseFromserver.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(success.equals("1")) {

                Snackbar.make(findViewById(android.R.id.content), message.toString(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent goToMainAtivity = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(goToMainAtivity);
                    }
                }, 2000);   //5 seconds
            } else if(success.equals("0")) {
                Snackbar.make(findViewById(android.R.id.content), message.toString(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            }
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

}
