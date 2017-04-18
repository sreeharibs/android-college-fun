package com.vkcet.vkcet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public EditText etAdmissionNumber, etPassword;
    public Button btnLogin;

    public boolean loggedIn;
    public ProgressDialog loading;
    public int backButtonCount = 0;
    public final String imageURL = "http://codesquad.in/include/getImage.php?admNo=";
    public String adm;
    public ImageView proPic;
    public Bitmap bitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        proPic= (ImageView) findViewById(R.id.proPic);
        setSupportActionBar(toolbar);
        init();
    }


    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if (loggedIn) {
            //We will start the Profile Activity
            Intent intent = new Intent(MainActivity.this, NavBar.class);
            startActivity(intent);
        }
    }

    public void getData() {
        String admNo = etAdmissionNumber.getText().toString().trim();


        if (admNo.equals("")) {
            Toast.makeText(this, "Please enter Admission Number", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this, "Please wait...", "Logging In" +
                "...", false, false);

        String url = Config.LOGIN_URL + etAdmissionNumber.getText().toString().trim() + "&password=" + etPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No Network Connection", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response) {
        String admNo = "";
        String password = "";
        String role = "";
        String name = "";
        String phone = "";
        String email = "";
        String batch = "";
        String dep = "";
        String work = "";
        String address="";
        String company="";
        String industry="";
        int privacy=0;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            admNo = data.getString("admNo");
            password = data.getString("password");
            role = data.getString("role");
            name = data.getString("name");
            phone = data.getString("phone");
            email = data.getString("email");
            batch = data.getString("batch");
            dep = data.getString("dep");
            work = data.getString("work");
            address=data.getString("address");
            company=data.getString("company");
            industry=data.getString("industry");
            privacy=data.getInt("privacy");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (dep != "null") {


            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.ADM_SHARED_PREF, admNo);
            editor.putString(Config.PWD_SHARED_PREF, password);
            editor.putString(Config.ROLE_SHARED_PREF, role);
            editor.putString(Config.NAME_SHARED_PREF, name);
            editor.putString(Config.PHONE_SHARED_PREF, phone);
            editor.putString(Config.EMAIL_SHARED_PREF, email);
            editor.putString(Config.BATCH_SHARED_PREF, batch);
            editor.putString(Config.DEP_SHARED_PREF, dep);
            editor.putString(Config.WORK_SHARED_PREF, work);
            editor.putString(Config.ADDR_SHARED_PREF, address);
            editor.putString(Config.CMPNY_SHARED_PREF, company);
            editor.putString(Config.INDUS_SHARED_PREF, industry);
            editor.putInt(Config.PRIVC_SHARED_PREF, privacy);
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);   //edited on 30-06-2016 by adding additional fields

            //Saving values to editor
            editor.apply();

            Intent intent = new Intent(MainActivity.this, NavBar.class);
            startActivity(intent);

            SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String myName = sp.getString(Config.NAME_SHARED_PREF.trim(), "name");
            if ((myName.equals("name") != true) && (myName.equals("null") != true))
                Toast.makeText(MainActivity.this, "Welcome " + myName, Toast.LENGTH_LONG).show();

        } else {
            //If the server response is not success
            //Displaying an error message on toast
            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
            loading.dismiss();
        }
    }


    public void init() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etAdmissionNumber = (EditText) findViewById(R.id.etAdmissionNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        etAdmissionNumber.setOnClickListener(this);
        etPassword.setOnClickListener(this);

        etAdmissionNumber.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {
            getData();

        }

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (etAdmissionNumber.isFocused()) {
            if (etAdmissionNumber.getText().toString().equals("Admission Number"))
                etAdmissionNumber.setText("");
            if (etPassword.getText().toString().equals("")) etPassword.setText(Config.SET_PASSWORD);
        }
        if (etPassword.isFocused()) {
            if (etPassword.getText().toString().equals("Password")) etPassword.setText("");
            if (etAdmissionNumber.getText().toString().equals(""))
                etAdmissionNumber.setText(Config.SET_ADMISSION_NUMBER);
        }

    }




    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
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
}