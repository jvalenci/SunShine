package com.example.valencia.sunshine.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.valencia.sunshine.app.data.WeatherContract;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

        private ShareActionProvider mShareActionProvider;
        private String mForecast;

        private static final int DETAIL_LOADER = 0;

        private static final String[] FORECAST_COLUMNS = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };

        //these constants correspond to the projection defined above, and must change if the projection changes

        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;



        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_detail,container,false);
        }


        private  Intent createShareForecastIntent(){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            //inflate menu
            inflater.inflate(R.menu.detailfragment, menu);

            //retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            //get the provider
             mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            if (mForecast != null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            getLoaderManager().initLoader(DETAIL_LOADER,null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args){
            Log.v(LOG_TAG, "Int onCreateLoader");
            Intent intent = getActivity().getIntent();
            if (intent  == null){
                return null;
            }

            //now create and return a cursorloader that will take care of creating a cursor for the data being displayed
            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data){
            Log.v(LOG_TAG, "in onLoaderFinished");
            if(!data.moveToFirst()){return;}

            String dataString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));

            String weatherDescription = data.getString(COL_WEATHER_DESC);

            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(this.getActivity(),data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
            String low = Utility.formatTemperature(this.getActivity(),data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            mForecast = String.format("%s - %s - %s/%s", dataString, weatherDescription,high,low);

            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
            detailTextView.setText(mForecast);

            //If onCreateOptionsMenu has already happened, we need to update the share intent now

            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader){

        }
    }
}
