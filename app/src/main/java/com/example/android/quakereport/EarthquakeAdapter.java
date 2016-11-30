package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import static com.example.android.quakereport.R.id.magnitude;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }

    private int getMagnitudeColor(String magnitude) {
        int magnitudeColorResourceId;

        double magnitudeValue = Double.parseDouble(magnitude);
        int magnitudeFloor = (int) Math.floor(magnitudeValue);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
        /*
        Note on color values: In Java code, you can refer to the colors that you defined in the colors.xml
        file using the color resource ID such as R.color.magnitude1, R.color.magnitude2. You still need to
        convert the color resource ID into a color integer value though.

        Example: int magnitude1Color = ContextCompat.getColor(getContext(), R.color.magnitude1);

        You can call ContextCompat getColor() to convert the color resource ID into an actual integer
        color value, and return the result as the return value of the getMagnitudeColor() helper method.
        */
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeView = (TextView)listItemView.findViewById(magnitude);
        magnitudeView.setText(currentEarthquake.getMagnitude());

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        TextView offsetView = (TextView)listItemView.findViewById(R.id.offset);
        offsetView.setText(currentEarthquake.getOffset());

        TextView priLocView = (TextView)listItemView.findViewById(R.id.primary_location);
        priLocView.setText(currentEarthquake.getPriLoc());

        TextView dateView = (TextView)listItemView.findViewById(R.id.date);
        dateView.setText(currentEarthquake.getDate());

        TextView timeView = (TextView)listItemView.findViewById(R.id.time);
        timeView.setText(currentEarthquake.getTime());

        return listItemView;
    }

}
