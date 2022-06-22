package com.sportmogila.sporttogether;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllEventsAdapter extends ArrayAdapter<Event>{

    private ArrayList<Event> events;
    Context context;


    public AllEventsAdapter(ArrayList<Event> events, Context context) {
        super(context, R.layout.events_list_item, events);
        this.events = events;
        this.context = context;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.events_list_item, null);

            TextView name = convertView.findViewById(R.id.events_list_name);
            TextView eventAt = convertView.findViewById(R.id.events_list_event_at);
            TextView city = convertView.findViewById(R.id.events_list_city);
            TextView members = convertView.findViewById(R.id.events_list_members);
            ImageView image = convertView.findViewById(R.id.events_list_image);
            Event event = events.get(position);
            name.setText(event.getName());
            eventAt.setText(event.getEventAt());
            city.setText("м. "+event.getLocation().getCity());
            members.setText(Html.fromHtml(getMembersText(event)));

            switch (event.getSport()){
                case "⚽ Футбол":
                    image.setImageResource(R.drawable.sport_football);
                    break;
                case "\uD83C\uDFC0 Баскетбол":
                    image.setImageResource(R.drawable.sport_basketball);
                    break;
                case "\uD83C\uDFD0 Волейбол":
                    image.setImageResource(R.drawable.sport_volleyball);
                    break;
                case "\uD83C\uDFBE Теніс":
                    image.setImageResource(R.drawable.sport_tenis);
                    break;
                case "\uD83E\uDD4A Бокс":
                    image.setImageResource(R.drawable.sport_box);
                    break;
            }
        }

        return convertView;
    }

    public String getMembersText(Event event){
        String str = "Учасники: <b>"+event.getOwner().getName()+"</b>";
        int count = event.getMembers().size();
        if(count>0){
            str+=" і ще <b>"+count+"</b>";
        }
        return str;
    }
}
