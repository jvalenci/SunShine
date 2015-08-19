package com.example.valencia.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Valencia on 8/14/2015.
 */
public class ForecastAdapter extends CursorAdapter{

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
    public ForecastAdapter(Context context, Cursor c, int flags){
        super(context,c,flags);
    }

        private static final int VIEW_TYPE_TODAY = 0;
        private static final int VIEW_TYPE_FUTURE_DAY = 1;
        private static final int VIEW_TYPE_COUNT = 2;

    @Override
    public int getItemViewType(int position){
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount(){
        return VIEW_TYPE_COUNT;
    }



                /*
+        Remember that these views are reused as needed.
+     */
                @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                //choose the layout type
                    int viewType = getItemViewType(cursor.getPosition());
                    int layoutId = -1;

                    if ( viewType == VIEW_TYPE_TODAY){
                        layoutId = R.layout.list_item_forecast_today;
                    }else if (viewType == VIEW_TYPE_FUTURE_DAY){
                        layoutId = R.layout.list_item_forecast;
                    }

                    View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
                    ViewHolder viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);

                        return view;
            }

                /*
        This is where we fill-in the views with the contents of the cursor.
     */
                @Override
        public void bindView(View view, Context context, Cursor cursor) {
                // our view is pretty simple here --- just a text view
                        // we'll keep the UI functional with a simple (and slow!) binding.

                    ViewHolder viewHolder = (ViewHolder) view.getTag();
//
//
                    //use place holder image for now
                    viewHolder.iconView.setImageResource(R.drawable.ic_launcher);

                    //read date from cursor
                    long weatherDate = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);

                    viewHolder.dateView.setText(Utility.getFriendlyDayString(context,weatherDate));

                    //read weather forecast from cursor
                    String weatherForecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);

                    viewHolder.descriptionView.setText(weatherForecast);

                    //read user preference for metric or imperial temperature units
                    boolean isMetric = Utility.isMetric(context);

                    //read high temperature from cursor
                    double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);

                    viewHolder.highTempView.setText(Utility.formatTemperature(high,isMetric));

                    //read low temperature from cursor
                    double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
                    
                    viewHolder.lowTempView.setText(Utility.formatTemperature(low, isMetric));

        }
}
