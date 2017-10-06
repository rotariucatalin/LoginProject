package com.example.user.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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
import java.util.Iterator;

public class FriendActivity extends AppCompatActivity {

    private Intent intent;
    private String  friendUserID,
                    currentUserId,
                    passingValues,
                    currentUser,
                    currentFriendUser,
                    friendProfileName,
                    friendUsername,
                    friendStatus,
                    friendDOB,
                    friendGender,
                    friendOccupation,
                    friendCompany,
                    friendNationality,
                    friendPhone,
                    friendEmail,
                    friendWebsite,
                    friendInterests,
                    count,
                    message,
                    selectedJsonToString;

    private EditText    profile_name,
                        username,
                        status,
                        dob,
                        gender,
                        occupation,
                        company,
                        nationality,
                        phone,
                        email,
                        website,
                        interests;

    private SpannableString
                        profileNameFirstString,
                        profileNameSecondString,
                        usernameFirstString,
                        usernameSecondString,
                        statusFirstString,
                        statusSecondString,
                        dobFirstString,
                        dobSecondString,
                        genderFirstString,
                        genderSecondString,
                        occupationFirstString,
                        occupationSecondString,
                        companyFirstString,
                        companySecondString,
                        nationalityFirstString,
                        nationalitySecondString,
                        phoneFirstString,
                        phoneSecondString,
                        emailFirstString,
                        emailSecondString,
                        websiteFirstString,
                        websiteSecondString,
                        interestsFirstString,
                        interestsSecondString;

    private CheckBox    profile_name_checkbox,
                        username_checkbox,
                        status_checkbox,
                        dob_checkbox,
                        gender_checkbox,
                        occupation_checkbox,
                        company_checkbox,
                        nationality_checkbox,
                        phone_checkbox,
                        email_checkbox,
                        website_checkbox,
                        interests_checkbox;

    ProgressDialog StatusProcessDialog;
    StringBuilder response;
    BufferedReader input;
    BufferedWriter writer;
    URL url;
    Menu menu;
    android.app.AlertDialog dialog = null;
    JSONObject passingValuesJSon, reader, passedJSON, requestProfileJSON, selectedProfileJSON;

    HttpURLConnection httpURLConnection     = null;

    public FriendActivity() {  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);


        intent          = getIntent();
        if (intent.hasExtra("friend_id"))       friendUserID     = intent.getStringExtra("friend_id");
        if (intent.hasExtra("current_user_id")) currentUserId    = intent.getStringExtra("current_user_id");

        profile_name                = (EditText) findViewById(R.id.profile_name);
        username                    = (EditText) findViewById(R.id.username);
        status                      = (EditText) findViewById(R.id.status);
        dob                         = (EditText) findViewById(R.id.dob);
        gender                      = (EditText) findViewById(R.id.gender);
        occupation                  = (EditText) findViewById(R.id.occupation);
        company                     = (EditText) findViewById(R.id.company);
        nationality                 = (EditText) findViewById(R.id.nationality);
        phone                       = (EditText) findViewById(R.id.phone);
        email                       = (EditText) findViewById(R.id.email);
        website                     = (EditText) findViewById(R.id.website);
        interests                   = (EditText) findViewById(R.id.interests);

        new friendProfileAsyns().execute( new String[]{ friendUserID,currentUserId } );

        profileNameSecondString = new SpannableString("Name");
        profileNameSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Name".length(), 0); // set size
        profileNameSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Name".length(), 0);// set color

        usernameSecondString = new SpannableString("Username");
        usernameSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Username".length(), 0); // set size
        usernameSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Username".length(), 0);// set color

        statusSecondString = new SpannableString("Status");
        statusSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Status".length(), 0); // set size
        statusSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Status".length(), 0);// set color

        dobSecondString = new SpannableString("Date of birth");
        dobSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Date of birth".length(), 0); // set size
        dobSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Date of birth".length(), 0);// set color

        genderSecondString = new SpannableString("Gender");
        genderSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Gender".length(), 0); // set size
        genderSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Gender".length(), 0);// set color

        occupationSecondString = new SpannableString("Occupation");
        occupationSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Occupation".length(), 0); // set size
        occupationSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Occupation".length(), 0);// set color

        companySecondString = new SpannableString("Company");
        companySecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Company".length(), 0); // set size
        companySecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Company".length(), 0);// set color

        nationalitySecondString = new SpannableString("Nationality");
        nationalitySecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Nationality".length(), 0); // set size
        nationalitySecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Nationality".length(), 0);// set color

        phoneSecondString = new SpannableString("Phone");
        phoneSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Phone".length(), 0); // set size
        phoneSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Phone".length(), 0);// set color

        emailSecondString = new SpannableString("Email");
        emailSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Email".length(), 0); // set size
        emailSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Email".length(), 0);// set color

        websiteSecondString = new SpannableString("Website");
        websiteSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Website".length(), 0); // set size
        websiteSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Website".length(), 0);// set color

        interestsSecondString = new SpannableString("Interests");
        interestsSecondString.setSpan(new RelativeSizeSpan(0.6f), 0,"Interests".length(), 0); // set size
        interestsSecondString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Interests".length(), 0);// set color

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        getMenuInflater().inflate(R.menu.friend_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(hasWindowFocus())    {
            new friendOrNoFriendRequestAsync().execute( new String[]{ friendUserID,currentUserId } );
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())    {
            case R.id.add_friend_sec_menu:          new removeOrAddFriendAsync().execute( new String[]{ friendUserID,currentUserId } ); break;
            case R.id.remove_friend_sec_menu:       new removeOrAddFriendAsync().execute( new String[]{ friendUserID,currentUserId } ); break;
            case R.id.revoke_data_sec_menu:         new removeDataVisibilityProfile().execute( new String[]{ friendUserID,currentUserId } ); break;
            case R.id.request_profile_sec_menu:     new requestDataVisibility().execute( new String[]{ friendUserID,currentUserId } ); break;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
    *   Asynctask to see if login user is friend with the select user from list
    * */
    private class friendOrNoFriendRequestAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {

            currentFriendUser   = params[0];
            currentUser         = params[1];

            response            = new StringBuilder();

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                url = new URL("http://10.0.2.2/login_project/index.php");

                passingValuesJSon   = new JSONObject();
                passingValuesJSon.put("type", "get_friend_async");
                passingValuesJSon.put("id_user", currentUser);
                passingValuesJSon.put("currend_friend", currentFriendUser);
                passingValues = passingValuesJSon.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
                writer.write(passingValues);

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
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader      = new JSONObject(response.toString());
                count       = reader.getString("count");
            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return count;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            StatusProcessDialog=new ProgressDialog(FriendActivity.this, R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            MenuItem add_friend     = menu.findItem(R.id.add_friend_sec_menu); //menu.add(0,0,3,"Add friend"); else menu.add(0,0,3,"Remove friend");
            MenuItem remove_friend  = menu.findItem(R.id.remove_friend_sec_menu); //menu.add(0,0,3,"Add friend"); else menu.add(0,0,3,"Remove friend");

            if(s.equals("0")) {
                add_friend.setVisible(true);
                remove_friend.setVisible(false);
            } else {
                remove_friend.setVisible(true);
                add_friend.setVisible(false);
            }

            StatusProcessDialog.dismiss();
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

    /*
    *   Asynctask to get all the information (private/public/requested) for specific select user
    * */
    private class friendProfileAsyns extends AsyncTask<String, Integer, String>   {

        @Override
        protected String doInBackground(String... params) {

            currentFriendUser   = params[0];
            currentUser         = params[1];

            response            = new StringBuilder();

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                url = new URL("http://10.0.2.2/login_project/index.php");

                passingValuesJSon   = new JSONObject();
                passingValuesJSon.put("type", "get_profile_allowed");
                passingValuesJSon.put("id_user", currentUser);
                passingValuesJSon.put("currend_friend", currentFriendUser);
                passingValues = passingValuesJSon.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
                writer.write(passingValues);

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
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader  = new JSONObject(response.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return String.valueOf(reader);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            StatusProcessDialog=new ProgressDialog(FriendActivity.this, R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            StatusProcessDialog.dismiss();

            try {

                passedJSON  = new JSONObject(s);

                if(passedJSON.has("user_profile_name"))         friendProfileName   = passedJSON.getString("user_profile_name");     else friendProfileName     = "Private";
                if(passedJSON.has("username"))                  friendUsername      = passedJSON.getString("username");              else friendUsername        = "Private";
                if(passedJSON.has("user_dob"))                  friendStatus        = passedJSON.getString("user_dob");              else friendStatus          = "Private";
                if(passedJSON.has("user_status_message"))       friendDOB           = passedJSON.getString("user_status_message");   else friendDOB             = "Private";
                if(passedJSON.has("user_gender"))               friendGender        = passedJSON.getString("user_gender");           else friendGender          = "Private";
                if(passedJSON.has("user_occupation"))           friendOccupation    = passedJSON.getString("user_occupation");       else friendOccupation      = "Private";
                if(passedJSON.has("user_company"))              friendCompany       = passedJSON.getString("user_company");          else friendCompany         = "Private";
                if(passedJSON.has("user_nationality"))          friendNationality   = passedJSON.getString("user_nationality");      else friendNationality     = "Private";
                if(passedJSON.has("user_phone"))                friendPhone         = passedJSON.getString("user_phone");            else friendPhone           = "Private";
                if(passedJSON.has("user_email"))                friendEmail         = passedJSON.getString("user_email");            else friendEmail           = "Private";
                if(passedJSON.has("user_website"))              friendWebsite       = passedJSON.getString("user_website");          else friendWebsite         = "Private";
                if(passedJSON.has("user_interests"))            friendInterests     = passedJSON.getString("user_interests");        else friendInterests       = "Private";

                profileNameFirstString = new SpannableString(friendProfileName);
                profileNameFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendProfileName.length(), 0); // set size
                profileNameFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendProfileName.length(), 0);// set color

                usernameFirstString = new SpannableString(friendUsername);
                usernameFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendUsername.length(), 0); // set size
                usernameFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendUsername.length(), 0);// set color

                statusFirstString = new SpannableString(friendStatus);
                statusFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendStatus.length(), 0); // set size
                statusFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendStatus.length(), 0);// set color

                dobFirstString = new SpannableString(friendDOB);
                dobFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendDOB.length(), 0); // set size
                dobFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendDOB.length(), 0);// set color

                genderFirstString = new SpannableString(friendGender);
                genderFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendGender.length(), 0); // set size
                genderFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendGender.length(), 0);// set color

                occupationFirstString = new SpannableString(friendOccupation);
                occupationFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendOccupation.length(), 0); // set size
                occupationFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendOccupation.length(), 0);// set color

                companyFirstString = new SpannableString(friendCompany);
                companyFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendCompany.length(), 0); // set size
                companyFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendCompany.length(), 0);// set color

                nationalityFirstString = new SpannableString(friendNationality);
                nationalityFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendNationality.length(), 0); // set size
                nationalityFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendNationality.length(), 0);// set color

                phoneFirstString = new SpannableString(friendPhone);
                phoneFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendPhone.length(), 0); // set size
                phoneFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendPhone.length(), 0);// set color

                emailFirstString = new SpannableString(friendEmail);
                emailFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendEmail.length(), 0); // set size
                emailFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendEmail.length(), 0);// set color

                websiteFirstString = new SpannableString(friendWebsite);
                websiteFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendWebsite.length(), 0); // set size
                websiteFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendWebsite.length(), 0);// set color

                interestsFirstString = new SpannableString(friendInterests);
                interestsFirstString.setSpan(new RelativeSizeSpan(1f), 0,friendInterests.length(), 0); // set size
                interestsFirstString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, friendInterests.length(), 0);// set color

                profile_name.setText(TextUtils.concat(profileNameFirstString,   " ", "\n", profileNameSecondString));
                username.setText(TextUtils.concat(usernameFirstString,          " ", "\n", usernameSecondString));
                status.setText(TextUtils.concat(statusFirstString,              " ", "\n", statusSecondString));
                dob.setText(TextUtils.concat(dobFirstString,                    " ", "\n", dobSecondString));
                gender.setText(TextUtils.concat(genderFirstString,              " ", "\n", genderSecondString));
                occupation.setText(TextUtils.concat(occupationFirstString,      " ", "\n", occupationSecondString));
                company.setText(TextUtils.concat(companyFirstString,            " ", "\n", companySecondString));
                nationality.setText(TextUtils.concat(nationalityFirstString,    " ", "\n", nationalitySecondString));
                phone.setText(TextUtils.concat(phoneFirstString,                " ", "\n", phoneSecondString));
                email.setText(TextUtils.concat(emailFirstString,                " ", "\n", emailSecondString));
                website.setText(TextUtils.concat(websiteFirstString,            " ", "\n", websiteSecondString));
                interests.setText(TextUtils.concat(interestsFirstString,        " ", "\n", interestsSecondString));

            } catch (JSONException e) {
                e.printStackTrace();
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

    /*
    *   Asynctask to remve/add specific selected user from list
    * */
    private class removeOrAddFriendAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            currentFriendUser   = params[0];
            currentUser         = params[1];

            response            = new StringBuilder();

                        /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                url = new URL("http://10.0.2.2/login_project/index.php");

                passingValuesJSon   = new JSONObject();
                passingValuesJSon.put("type", "add_or_remove_friend");
                passingValuesJSon.put("id_user", currentUser);
                passingValuesJSon.put("currend_friend", currentFriendUser);
                passingValues = passingValuesJSon.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
                writer.write(passingValues);

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
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader      = new JSONObject(response.toString());
                message     = reader.getString("message");
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
        protected void onPreExecute() {
            super.onPreExecute();
            StatusProcessDialog=new ProgressDialog(FriendActivity.this, R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            StatusProcessDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show();
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

    /*
    *   Asynctask to remove profile visibility approved
    * */
    private class removeDataVisibilityProfile extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            currentFriendUser   = params[0];
            currentUser         = params[1];

            response            = new StringBuilder();

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                url = new URL("http://10.0.2.2/login_project/index.php");

                passingValuesJSon   = new JSONObject();
                passingValuesJSon.put("type", "remove_data_visibility");
                passingValuesJSon.put("id_user", currentUser);
                passingValuesJSon.put("currend_friend", currentFriendUser);
                passingValues = passingValuesJSon.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
                writer.write(passingValues);

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
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader      = new JSONObject(response.toString());
                message     = reader.getString("message");
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
        protected void onPreExecute() {
            super.onPreExecute();
            StatusProcessDialog=new ProgressDialog(FriendActivity.this, R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            StatusProcessDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show();
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

    /*
    *   Asynctask to request data visibility
    * */
    private class requestDataVisibility extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            currentFriendUser   = params[0];
            currentUser         = params[1];

            response            = new StringBuilder();

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {

                url = new URL("http://10.0.2.2/login_project/index.php");

                passingValuesJSon   = new JSONObject();
                passingValuesJSon.put("type", "request_data_visibility");
                passingValuesJSon.put("id_user", currentUser);
                passingValuesJSon.put("currend_friend", currentFriendUser);
                passingValues = passingValuesJSon.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
                writer.write(passingValues);

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
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                /*
                *   Creating the JSON Object and Parse it
                * */
                reader      = new JSONObject(response.toString());

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return reader.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            StatusProcessDialog=new ProgressDialog(FriendActivity.this, R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            StatusProcessDialog.dismiss();
            try {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FriendActivity.this);
                LayoutInflater inflater = FriendActivity.this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_request_profile, null);
                builder.setView(view);
                builder.setTitle("Select profile visibility!");

                profile_name_checkbox   = (CheckBox) view.findViewById(R.id.profile_name_checkbox);
                username_checkbox       = (CheckBox) view.findViewById(R.id.username_checkbox);
                status_checkbox         = (CheckBox) view.findViewById(R.id.status_checkbox);
                dob_checkbox            = (CheckBox) view.findViewById(R.id.dob_checkbox);
                gender_checkbox         = (CheckBox) view.findViewById(R.id.gender_checkbox);
                occupation_checkbox     = (CheckBox) view.findViewById(R.id.occupation_checkbox);
                company_checkbox        = (CheckBox) view.findViewById(R.id.company_checkbox);
                nationality_checkbox    = (CheckBox) view.findViewById(R.id.nationality_checkbox);
                phone_checkbox          = (CheckBox) view.findViewById(R.id.phone_checkbox);
                email_checkbox          = (CheckBox) view.findViewById(R.id.email_checkbox);
                website_checkbox        = (CheckBox) view.findViewById(R.id.website_checkbox);
                interests_checkbox      = (CheckBox) view.findViewById(R.id.interests_checkbox);

                builder.setPositiveButton("Request", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if(profile_name_checkbox.isChecked()) try {
                            selectedProfileJSON.put("profileNameCheck", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(username_checkbox.isChecked()) try {
                            selectedProfileJSON.put("userNameCheck",    "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status_checkbox.isChecked()) try {
                            selectedProfileJSON.put("statusCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(dob_checkbox.isChecked()) try {
                            selectedProfileJSON.put("dobCheck",         "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(gender_checkbox.isChecked()) try {
                            selectedProfileJSON.put("genderCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(occupation_checkbox.isChecked()) try {
                            selectedProfileJSON.put("occupationCheck",  "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(company_checkbox.isChecked()) try {
                            selectedProfileJSON.put("companyCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(nationality_checkbox.isChecked()) try {
                            selectedProfileJSON.put("nationalityCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(phone_checkbox.isChecked()) try {
                            selectedProfileJSON.put("phoneCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(email_checkbox.isChecked()) try {
                            selectedProfileJSON.put("emailCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(website_checkbox.isChecked()) try {
                            selectedProfileJSON.put("websiteCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(interests_checkbox.isChecked()) try {
                            selectedProfileJSON.put("interestCheck",      "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new sendProfileRequestAsync().execute(new String[]{ selectedJsonToString.toString() });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();

                requestProfileJSON  = new JSONObject(s);
                if(requestProfileJSON.has("user_profile_name"))     profile_name_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("username"))              username_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_status_message"))   status_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_dob"))              dob_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_gender"))           gender_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_occupation"))       occupation_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_company"))          company_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_nationality"))      nationality_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_phone"))            phone_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_email"))            email_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_website"))          website_checkbox.setVisibility(View.VISIBLE);
                if(requestProfileJSON.has("user_interests"))        interests_checkbox.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
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

    /*
    *   Asynctask to send selected profile visibility
    * */
    private class sendProfileRequestAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
