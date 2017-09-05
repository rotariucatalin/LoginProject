package com.example.user.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 9/5/2017.
 */

public class ContacsAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contactList = new ArrayList<>();
    private Context context;
    private Contact contact;

    OverallMethods overallMethods           = new OverallMethods();

    public ContacsAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        contactList = objects;
    }

    private class ViewHolder {

        ImageView contactAvatar, contactProfileLink;
        TextView contactName, contactStatusMessage;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        try {
            if(position < contactList.size())   {
                contact = contactList.get(position);
            }
        } catch (Exception e) {
            Log.d(this.getClass().toString(), e.toString());
        }

        if(convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_view, parent, false);

            viewHolder                              = new ViewHolder();
            viewHolder.contactName                  = (TextView) convertView.findViewById(R.id.edit_name);
            viewHolder.contactStatusMessage         = (TextView) convertView.findViewById(R.id.text_status_message);
            viewHolder.contactAvatar                = (ImageView) convertView.findViewById(R.id.contact_image);
            viewHolder.contactProfileLink           = (ImageView) convertView.findViewById(R.id.imgbtn_offline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ( ViewHolder ) convertView.getTag();
        }

        viewHolder.contactName.setText(contact.getContactName());
        viewHolder.contactStatusMessage.setText(contact.getContactStatusMsg());
        Bitmap bitmap = overallMethods.getBitmapFromStringBase64(contact.getContactimageString());
        viewHolder.contactAvatar.setImageBitmap(overallMethods.getRoundedBitmap(bitmap));
        return convertView;
    }

    @Override
    public int getPosition(Contact item) {
        return super.getPosition(item);
    }

    @Override
    public Contact getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

}
