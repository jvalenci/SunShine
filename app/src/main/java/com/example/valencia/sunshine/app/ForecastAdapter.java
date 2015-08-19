package com.example.valencia.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Valencia on 8/14/2015.
 */
public class ForecastAdapter extends CursorAdapter{
    public ForecastAdapter(Context context, Cursor c, int flags) {
                super(context, c, flags);
            }

                /**
          * Prepare the weather high/lows for presentation.
          */private String formatHighLows(double high, double low,Context mContext) {
                boolean isMetric = Utility.isMetric(mContext);
                String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
                return highLowStr;
            }

                /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
                private String convertCursorRowToUXFormat(Cursor cursor, Context context) {

                       String highAndLow = formatHighLows(
                               cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                               cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP),
                               context
                               );

                        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                                " - " + highAndLow;
            }

                /*
+        Remember that these views are reused as needed.
+     */
                @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

                        return view;
            }

                /*
        This is where we fill-in the views with the contents of the cursor.
     */
                @Override
        public void bindView(View view, Context context, Cursor cursor) {
                // our view is pretty simple here --- just a text view
                        // we'll keep the UI functional with a simple (and slow!) binding.

                                        TextView tv = (TextView)view;
                tv.setText(convertCursorRowToUXFormat(cursor, context));
            }
}