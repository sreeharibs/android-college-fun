package com.vkcet.vkcet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by SREE on 23-Feb-16.
 */
public class Profile_Fragment extends Fragment implements View.OnClickListener {
    View myView;

    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;


    public TextView tvPname;
    public TextView tvPtitle;
    public TextView tvEmail;
    public TextView tvPhone;
    public TextView tvDep,tvBatch;
    public CircleImageView proPic;
    private final String imageURL = "http://codesquad.in/include/getImage.php?admNo=";
    public String adm;

    public Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater  inflater, ViewGroup container, Bundle savedInstanceState){
        myView=inflater.inflate(R.layout.profile_layout,container,false);
        tvPname=(TextView)myView.findViewById(R.id.tvPname);
        tvPtitle=(TextView)myView.findViewById(R.id.tvPtitle);
        tvEmail=(TextView)myView.findViewById(R.id.tvAdmNo);
        tvPhone=(TextView)myView.findViewById(R.id.tvPhone);
        tvDep=(TextView)myView.findViewById(R.id.tvDep);
        tvBatch=(TextView)myView.findViewById(R.id.tvBatch);
        button=(Button)myView.findViewById(R.id.btnEdit);
        proPic=(CircleImageView)myView.findViewById(R.id.proPic);

        button.setOnClickListener(this);

        SharedPreferences Spreferences =this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tvPname.setText(Spreferences.getString(Config.NAME_SHARED_PREF, "Name"));
        tvPtitle.setText("( ".concat(Spreferences.getString(Config.WORK_SHARED_PREF,"(position at Company")).concat(" )"));
        tvEmail.setText(Spreferences.getString(Config.EMAIL_SHARED_PREF, "email@provider.tld"));
        tvPhone.setText(Spreferences.getString(Config.PHONE_SHARED_PREF, "9000000000"));
        tvDep.setText(Spreferences.getString(Config.DEP_SHARED_PREF,"DEP").concat(" Dep"));
        tvBatch.setText(Spreferences.getString(Config.BATCH_SHARED_PREF,"20XX").concat(" Batch"));
        adm=Spreferences.getString(Config.ADM_SHARED_PREF,"ADM");
        getImage();
        return myView;
    }

    public void onClick(View v) {

        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()

                .replace(R.id.contentNavBar,new ProfileEdit_Fragment())
                .commit();

    }

    public void getImage() {

        class GetImage extends AsyncTask<String,Void,Bitmap> {


            public ImageView bmImage;
            ProgressDialog loading;

            public GetImage(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                bmImage.setImageBitmap(bitmap);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading your profile","Please wait...",true,true);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String url = imageURL+ strings[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }
        }

        GetImage gi = new GetImage(proPic);
        gi.execute(adm);
    }
}
