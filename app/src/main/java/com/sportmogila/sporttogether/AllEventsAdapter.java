package com.sportmogila.sporttogether;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportmogila.sporttogether.models.Event;

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

            TextView name = (TextView) convertView.findViewById(R.id.events_list_name);
            TextView sport = (TextView) convertView.findViewById(R.id.events_list_sport);
            TextView eventAt = (TextView) convertView.findViewById(R.id.events_list_event_at);

            Event event = events.get(position);
            name.setText(event.getName());
            sport.setText(event.getSport());
            eventAt.setText(event.getEventAt());

        }

        return convertView;
    }
}
