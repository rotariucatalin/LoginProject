package com.example.user.test;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
public class FriendsFragment extends Fragment {

    private Context context;
    public ListView listView;
    private Activity activity;
    public TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int totalUser;
    private Contact contact;
    ProgressDialog StatusProcessDialog;
    BufferedReader input;
    String userID;
    TextView no_friends;
    Bundle bundle;

    OutputStream outputStream                   = null;
    InputStream inputStream                     = null;
    HttpURLConnection httpURLConnection         = null;
    public ContacsAdapter contacsAdapter        = null;
    public ArrayList<Contact> contactArrayList  = new ArrayList<>();

    JSONObject reader                           = new JSONObject();
    JSONObject reader_aux                       = new JSONObject();

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        userID                  = getArguments().getString("userID");

        context                 = getContext();
        listView                = (ListView) rootView.findViewById(R.id.friend_list_fragment);
        textView                = (TextView) rootView.findViewById(R.id.no_users);
        swipeRefreshLayout      = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        no_friends              = (TextView) rootView.findViewById(R.id.no_friends);

        new FriendsAsyncTask().execute(userID,"","");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                contacsAdapter  = null;
                contactArrayList.clear();
                no_friends.setVisibility(View.GONE);
                listView.setAdapter(contacsAdapter);

                new FriendsAsyncTask().execute(userID,"","");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                *   Get the current Contact object and get the specific ID
                * */
                Contact selectedContact     = contacsAdapter.getItem(position);
                Intent goToContactProfile   = new Intent(context, FriendActivity.class);
                goToContactProfile.putExtra("friend_id", selectedContact.getContactId());
                goToContactProfile.putExtra("current_user_id", userID);
                startActivity(goToContactProfile);
            }
        });

        /*
        *   Create notification. When click on notification go to MainActivity
        * */

        Intent intent = new Intent(context, MainActivity.class);
        bundle = new Bundle();
        bundle.putString("userID", userID);
        intent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

/*        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_chat)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("IASO");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());*/

        /*
        *   End notification create
        * */
        return rootView;
    }

    private class FriendsAsyncTask extends AsyncTask<String, Integer, String> {

        StringBuilder response                      = new StringBuilder();
        JSONObject sendingInformation               = new JSONObject();

        @Override
        protected String doInBackground(String... params) {

            /*
            *   Posting the credentials to PHP SERVER
            * */
            try {
                URL url = new URL("http://10.0.2.2/login_project/index.php");
                JSONObject sendingInformation = new JSONObject();
                sendingInformation.put("type", "get_all_friends");
                sendingInformation.put("id_user", params[0]);
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
                totalUser   = reader.getInt("total");

                for(int i = 1; i <= totalUser; i++)   {

                    reader_aux  = new JSONObject(reader.getString(String.valueOf(i)));
                    contact     = new Contact();
                    contact.setContactId(reader_aux.getString("id_user"));
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

            super.onPreExecute();
            contactArrayList.clear();
            StatusProcessDialog=new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
            StatusProcessDialog.show();
            StatusProcessDialog.setCancelable(false);
            StatusProcessDialog.setCanceledOnTouchOutside(false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (listView != null) {
                listView.setEnabled(true);
            }

            if(contactArrayList.size() > 0) {

                no_friends.setVisibility(View.GONE);
                contacsAdapter = new ContacsAdapter(context, R.layout.list_view, contactArrayList);
                listView.setAdapter(contacsAdapter);
            } else {
                no_friends.setVisibility(View.VISIBLE);
            }

            swipeRefreshLayout.setRefreshing(false);
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

}
