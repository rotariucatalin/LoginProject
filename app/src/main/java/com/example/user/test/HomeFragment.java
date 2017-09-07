package com.example.user.test;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;
    public ListView listView;
    public ContacsAdapter contacsAdapter = null;
    private Activity activity;
    public ArrayList<Contact> contactArrayList = new ArrayList<>();
    public TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int totalUser;

    OutputStream outputStream               = null;
    InputStream inputStream                 = null;
    HttpURLConnection httpURLConnection     = null;

    JSONObject reader                       = new JSONObject();
    JSONObject reader_aux                   = new JSONObject();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        context             = getContext();
        listView            = (ListView) rootView.findViewById(R.id.contacts_list_fragment);
        textView            = (TextView) rootView.findViewById(R.id.no_users);
        swipeRefreshLayout  = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        new HomeAsyncTask().execute("","","");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                contacsAdapter  = null;
                listView.setAdapter(contacsAdapter);

                new HomeAsyncTask().execute("","","");
            }
        });

        return rootView;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private class HomeAsyncTask extends AsyncTask<String, Integer, String>{

        private Contact contact;
        private Activity activity;

        @Override
        protected String doInBackground(String... params) {

            ProgressDialog loginProcessDialog;
            StringBuilder response  = new StringBuilder();

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {
                URL url = new URL("http://10.0.2.2/login_project/index.php");
                JSONObject sendingInformation = new JSONObject();
                sendingInformation.put("type", "get_all_users");
                String informationString = sendingInformation.toString();

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(informationString);

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

                reader      = new JSONObject(response.toString());
                totalUser   = reader.getInt("total");

                for(int i = 1; i <= totalUser; i++)   {

                    reader_aux  = new JSONObject(reader.getString(String.valueOf(i)));
                    contact     = new Contact();
                    contact.setContactName(reader_aux.getString("username"));
                    contact.setContactStatusMsg(reader_aux.getString("status_message"));
                    contact.setContactimageString(reader_aux.getString("avatar"));

                    contactArrayList.add(contact);
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            return "ok";
        }

        @Override
        protected void onPreExecute() {

            contactArrayList.clear();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (listView != null) {
                listView.setEnabled(true);
            }

            contacsAdapter = new ContacsAdapter(context, R.layout.list_view, contactArrayList);
            listView.setAdapter(contacsAdapter);
            swipeRefreshLayout.setRefreshing(false);

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

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}
