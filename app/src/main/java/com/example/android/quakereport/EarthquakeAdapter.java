package com.example.android.quakereport;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jocelinthomas on 09/11/18.
 */

public class EarthquakeAdapter extends ArrayAdapter<EarthQuake>{
    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, ArrayList<EarthQuake> earthQuakes) {
        super(context,0,earthQuakes);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        EarthQuake quake = getItem(position);
        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        TextView time = (TextView) listItemView.findViewById(R.id.time);


        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(quake.getmMagnitude());
        magnitude.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(quake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Get the original location string from the Earthquake object,
        // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
        String originalLocation = quake.getmLocation();
        String primaryLocation;
        String locationOffset;

        // Check whether the originalLocation string contains the " of " text
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " of " text. We expect an array of 2 Strings, where
            // the first String will be "5km N" and the second String will be "Cairo, Egypt".
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            // Location offset should be "5km N " + " of " --> "5km N of"
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            // Primary location should be "Cairo, Egypt"
            primaryLocation = parts[1];
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            locationOffset = getContext().getString(R.string.near_the);
            // The primary location will be the full location string "Pacific-Antarctic Ridge".
            primaryLocation = originalLocation;
        }
        //location.setText(quake.getmLocation());
        primaryLocationView.setText(primaryLocation);
        locationOffsetView.setText(locationOffset);






        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(quake.getmTimeInMilliseconds());
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        date.setText(formattedDate);

        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        time.setText(formattedTime);
        
        return listItemView;


    }

    private int getMagnitudeColor(double magnitudecolor) {
        int imageID;
        int magfloor = (int) Math.floor(magnitudecolor);

        switch (magfloor)
        {
            case 0:

            case 1:
                imageID = R.color.magnitude1;
                break;

            case 2:
                imageID = R.color.magnitude2;
                break;

            case 3:
                imageID = R.color.magnitude3;
                break;

            case 4:
                imageID = R.color.magnitude4;
                break;

            case 5:
                imageID = R.color.magnitude5;
                break;

            case 6:
                imageID = R.color.magnitude6;
                break;

            case 7:
                imageID = R.color.magnitude7;
                break;

            case 8:
                imageID = R.color.magnitude8;
                break;

            case 9:
                imageID = R.color.magnitude9;
                break;

            default:
                imageID = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),imageID);


    }

    private String formatMagnitude(double v) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(v);
    }

    private String formatDate(Date dateObject) {
       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MMM dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return dateFormat.format(dateObject);
        
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
