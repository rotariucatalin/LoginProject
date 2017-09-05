package com.example.user.test;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public ArrayList<Contact> originalContactsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

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

            for(int i = 1; i <= 10; i++)   {

                contact = new Contact();
                contact.setContactStatus(0);
                contact.setContactName("Nickname");
                contact.setContactStatusMsg("StatusMSG");
                contact.setContactEmail("Email");
                contact.setContactimageString("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");
                contactArrayList.add(contact);
                originalContactsList.add(contact);
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

            Handler handler =  new Handler(context.getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                    Toast toast = Toast.makeText(context,"bbb", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
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
