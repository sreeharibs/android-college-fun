package com.vkcet.vkcet;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SREE on 23-Feb-16.
 */
public class News_Fragment extends Fragment {
    View myView;
    ProgressDialog loading;

    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT="content";

    JSONArray news= null;
    ArrayList<HashMap<String, String>> newsList;

    ListView list;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater  inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.news_layout,container,false);
        list = (ListView) myView.findViewById(R.id.listView);
        newsList = new ArrayList<HashMap<String,String>>();
        loading = ProgressDialog.show(this.getActivity(), "Please wait...", "Fetching" +
                "...", false, false);
        getData();
        return myView;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            news  = jsonObj.getJSONArray(TAG_RESULTS);
            loading.dismiss();
            for(int i=0;i<news.length();i++){
                JSONObject c = news.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);

                HashMap<String,String> news = new HashMap<String,String>();

                news.put(TAG_TITLE, title);
                news.put(TAG_CONTENT, content);

                newsList.add(news);
            }
            if(News_Fragment.this.isVisible()){
            ListAdapter adapter = new SimpleAdapter(
                    this.getActivity(), newsList, R.layout.list_item,
                    new String[]{TAG_TITLE,TAG_CONTENT},
                    new int[]{R.id.tvTitle, R.id.tvDetails}
            );

            list.setAdapter(adapter);}

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://codesquad.in/include/getNews.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}
