package youtubeapidemo.examples.com.bakingapp.sync;

import android.content.ContentValues;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import youtubeapidemo.examples.com.bakingapp.MySingleton;
import youtubeapidemo.examples.com.bakingapp.provider.BakeContact.BakeEntry;

/**
 *  MyJobService onStartJob() is SettingUp the DataBase Table in background thread to be used in creating App widget.
 *
 */

public class MyJobService extends JobService {
    private static final String TAG = MyJobService.class.getSimpleName();
    JsonArrayRequest req;

    @Override
    public boolean onStartJob(final JobParameters job) {
        String mUrlBaking = "https://go.udacity.com/android-baking-app-json";
        req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                                getContentResolver().delete(BakeEntry.CONTENT_URI,null,null);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                String dish_Name = person.getString("name");
                                ContentValues values = new ContentValues();
                                values.put(BakeEntry.COLUMN_NAME, dish_Name);
                                getContentResolver().insert(BakeEntry.CONTENT_URI, values);
                            }
                            jobFinished(job, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(req);
        return true;
    }

/*onStopJob returns true to indicate can start JobService when network (internet is available*/

    @Override
    public boolean onStopJob(JobParameters job) {
        if (req != null)
            req.cancel();
        return true;
    }
}
