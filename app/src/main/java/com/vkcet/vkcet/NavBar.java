package com.vkcet.vkcet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static  int backButtonCount=0;
    public String adm,batch,dep,myJSON;
    private final String imageURL = "http://codesquad.in/include/getImage.php?admNo=";
    public CircleImageView proPic;

    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL="email";
    private static final String TAG_ADMNO="admNo";
    JSONArray data = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nav_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        getData();

        SharedPreferences Spreferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        adm=Spreferences.getString(Config.ADM_SHARED_PREF, "123");
        batch=Spreferences.getString(Config.BATCH_SHARED_PREF, "2000");
        dep=Spreferences.getString(Config.DEP_SHARED_PREF, "XXX");


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact cod2mad@gmail.com for support", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hview=navigationView.inflateHeaderView(R.layout.nav_header_nav_bar);
        TextView tvName=(TextView)hview.findViewById(R.id.tvName);
        TextView tvEmail=(TextView)hview.findViewById(R.id.tvAdmNo);
        proPic =(CircleImageView)hview.findViewById(R.id.proPic);
        //SharedPreferences Spreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        tvName.setText(Spreferences.getString(Config.NAME_SHARED_PREF, "Loading..."));
        tvEmail.setText(Spreferences.getString(Config.ADM_SHARED_PREF, "XXX/DEP/YYYY"));

        //we will set the image using another method - "lazyload" (as done in new profile segment)
        /*Drawable d = new BitmapDrawable(getResources(),(decodeBase64(Spreferences.getString(Config.IMG_SHARED_PREF,"null"))) );
        proPic.setImageDrawable(d);
        getImage();*/

        Context c=getApplication().getApplicationContext();
        ImageLoader imageLoader=new ImageLoader(c);
        String adm=Spreferences.getString(Config.ADM_SHARED_PREF,"XXX/DEP/YYYY");
        imageLoader.clearCache();
        imageLoader.fileCache.clear();
        imageLoader.DisplayImage("http://codesquad.in/include/getImage.php?admNo="+adm, proPic);
        imageLoader.clearCache();

        SharedPreferences sharedPreferences = NavBar.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Bitmap mybitmap = ((BitmapDrawable)proPic.getDrawable()).getBitmap();
        editor.putString(Config.IMG_SHARED_PREF, encodeTobase64(mybitmap));
        editor.apply();
        Toast.makeText(NavBar.this, encodeTobase64(mybitmap), Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if(backButtonCount >= 1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                super.onBackPressed();
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    public void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.ADM_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.apply();

                        //Starting login activity
                        Intent intent = new Intent(NavBar.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuLogout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment myFragment=null;
        int id = item.getItemId();

        if (id == R.id.my_profile) { myFragment=new Profile_fragment_new();
            // Handle the camera action
        } else if (id == R.id.notifications) {  myFragment=new News_Fragment();

        } else if (id == R.id.my_colleagues) {  myFragment=new My_Colleagues_Fragment();

        } else if (id == R.id.tools) {  myFragment=new My_Colleagues_Fragment();

        } else if (id == R.id.nav_share) {  myFragment=new Profile_Fragment();

        } else if (id == R.id.nav_send) {  myFragment=new News_Fragment();

        }

        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentNavBar,myFragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------//

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void createDb() {

        SQLiteDatabase sampleDB = this.openOrCreateDatabase("userList", MODE_PRIVATE, null);
        String qryDrop="DROP TABLE IF EXISTS userList";
        String qryCreate="CREATE TABLE userList(name VARCHAR(50), email varchar(50), url varchar(200))";
        sampleDB.execSQL(qryDrop);
        sampleDB.execSQL(qryCreate);

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            data = jsonObj.getJSONArray(TAG_RESULTS);

            String nl,el,dpl;

            for(int i=0;i<=data.length();i++){
                JSONObject c = data.getJSONObject(i);
                String n = c.getString(TAG_NAME).trim();
                String e = c.getString(TAG_EMAIL).trim();
                String a = c.getString(TAG_ADMNO).trim();

                Toast.makeText(getApplicationContext(), n+e+a, Toast.LENGTH_SHORT).show();
                String url="http://codesquad.in/include/getImage.php?admNo="+a;
                /*String sql="INSERT INTO userList values("+"n"+","+"e"+","+"url"+")";
                sampleDB.execSQL(sql);*/

                ContentValues cv=new ContentValues();
                cv.put("name",n);
                cv.put("email",e);
                cv.put("url",url);

                sampleDB.insert("userList",null,cv);



                // nameList.add("hai");emailList.add("hello");dpList.add("http://codesquad.in/include/getImage.php?admNo=064/CSE/2013");
                Toast.makeText(getApplicationContext(),"after", Toast.LENGTH_SHORT).show();

            }
            sampleDB.close();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
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
                createDb();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}

