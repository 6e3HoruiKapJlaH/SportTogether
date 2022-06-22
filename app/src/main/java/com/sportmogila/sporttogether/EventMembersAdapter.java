package com.sportmogila.sporttogether;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EventMembersAdapter extends ArrayAdapter<User>{

    private ArrayList<User> members;
    Context context;
    Bitmap bmp;


    public EventMembersAdapter(ArrayList<User> members, Context context) {
        super(context, R.layout.members_list_item, members);
        this.members = members;
        this.context = context;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.members_list_item, null);

            ImageView avatar = convertView.findViewById(R.id.members_list_avatar);
            TextView name = convertView.findViewById(R.id.members_list_name);

            User member = members.get(position);
            name.setText(member.getName());
            if(!member.getPhotoUrl().equals(""))
                bmp = getUserAvatar(member.getPhotoUrl());
                if(bmp!=null){
                    avatar.setImageBitmap(bmp);
                }
        }

        return convertView;
    }


    public Bitmap getUserAvatar(String path){
        int responseCode = -1;
        try{
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream in = con.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(in);
                in.close();
                return bmp;
            }

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
        return null;
    }
}
