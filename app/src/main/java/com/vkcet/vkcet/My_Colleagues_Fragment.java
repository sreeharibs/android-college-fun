package com.vkcet.vkcet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SREE on 23-Feb-16.
 */
public class My_Colleagues_Fragment extends Fragment {
    View myView;
    String myJSON;
    String batch,dep;
    ProgressDialog loading;
    Thread thread1;


    ListView list;
    LazyAdapter adapter;;

    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL="email";
    private static final String TAG_ADMNO="admNo";
    JSONArray data = null;


    @Nullable
    public View onCreateView(LayoutInflater  inflater, ViewGroup container, Bundle savedInstanceState) {

       /* LayoutInflater inflater;
        ViewGroup container;*/
        super.onCreate(savedInstanceState);
        myView=inflater.inflate(R.layout.my_colleagues_layout, container, false) ;
        list = (ListView) myView.findViewById(R.id.listcolg);

        SharedPreferences sp=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        batch=sp.getString(Config.BATCH_SHARED_PREF, "2000");
        dep=sp.getString(Config.DEP_SHARED_PREF, "XXX");

        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching" + "...", false, false);

        thread1 = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request
                setList();

            }
        });

        // getData();
       // loading = ProgressDialog.show(this.getActivity(), "Please wait...", "Fetching" + "...", false, false);
        /*thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                adapter = new LazyAdapter(list.getContext(), nameList, emailList, dpList);
                adapter.imageLoader.clearCache();
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            }
        });
*/
        thread1.start();
        try {
            thread1.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runThread();

        // Button b=(Button)myView.findViewById(R.id.button1);
        //b.setOnClickListener();

        return myView;
    }

    private void runThread() {

        new Thread() {
            public void run() {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            thread1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String[] nl=nameList.toArray(new String[nameList.size()]);
                        String[] el=emailList.toArray(new String[emailList.size()]);
                        String[] dl=dpList.toArray(new String[dpList.size()]);


                        adapter = new LazyAdapter(list.getContext(), nl, el, dl);
                        adapter.imageLoader.clearCache();
                        adapter.notifyDataSetChanged();
                        list.setAdapter(adapter);
                    }
                });

            }
        }.start();
    }

    @Override
    public void onDestroy()
    {

        list.setAdapter(null);
        super.onDestroy();

    }



    //@TargetApi(Build.VERSION_CODES.M)
    /*public void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            data = jsonObj.getJSONArray(TAG_RESULTS);
            loading.dismiss();

            String nl,el,dpl;

            for(int i=0;i<=data.length();i++){
                JSONObject c = data.getJSONObject(i);
                String n = c.getString(TAG_NAME);
                String e = c.getString(TAG_EMAIL);
                String a = c.getString(TAG_ADMNO);

                Toast.makeText(getActivity(), n+e+a, Toast.LENGTH_SHORT).show();

                //nameList[i]=n;
                emailList[i]=e;
                dpList[i]="http://codesquad.in/include/getImage.php?admNo="+a;

               // nameList.add("hai");emailList.add("hello");dpList.add("http://codesquad.in/include/getImage.php?admNo=064/CSE/2013");
                Toast.makeText(getActivity(),"after", Toast.LENGTH_SHORT).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

    /*public void getData(*//*ArrayList<String> a,ArrayList<String> b,ArrayList<String> c*//*){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpGet httpget = new HttpGet("http://codesquad.in/include/check.php?batch="+batch+"&dep="+dep);
               // Toast.makeText(getActivity(), "http://codesquad.in/include/check.php?batch="+batch+"&dep="+dep, Toast.LENGTH_SHORT).show();


                // Depends on your web service
                httpget.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httpget);
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
            public void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }*/

    public void setList(){
        String dbpath="/data/data/com.vkcet.vkcet/databases/";
        String dbname="userList";
        SQLiteDatabase db=SQLiteDatabase.openDatabase(dbpath+dbname,null,SQLiteDatabase.CREATE_IF_NECESSARY);

        Cursor c=db.rawQuery("SELECT * FROM userList",null);

        if(c!=null){
            if(c.moveToFirst()){
                int i=0;
                do{
                    if(i==10) break;
                    nameList.add(i,c.getString(0));
                    emailList.add(i,c.getString(1));
                    dpList.add(i,c.getString(2));
                    i++;

                }while (c.moveToNext());
            }
        }
        loading.dismiss();
        db.close();

    }

    public ArrayList<String> nameList= new ArrayList<String>();
    public ArrayList<String> emailList= new ArrayList<String>();
    public ArrayList<String> dpList= new ArrayList<String>();
}

