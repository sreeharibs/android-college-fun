package com.vkcet.vkcet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
public class Profile_fragment_new extends Fragment implements View.OnClickListener {
    View myView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View myLayout = inflater.inflate(R.layout.fragment_profile_fragment_new, container, false);
        CoordinatorLayout coordinatorLayout= (CoordinatorLayout) myLayout.findViewById(R.id.Profile);
        FrameLayout frameLayout= (FrameLayout) coordinatorLayout.findViewById(R.id.photoHeader);
        TextView tvName= (TextView) coordinatorLayout.findViewById(R.id.tvName);
        TextView tvTitle= (TextView) coordinatorLayout.findViewById(R.id.tvTitle);
        TextView tvEducation= (TextView) coordinatorLayout.findViewById(R.id.tvEducation);
        TextView tvAddress= (TextView) coordinatorLayout.findViewById(R.id.tvAddress);
        Button btnHome= (Button) coordinatorLayout.findViewById(R.id.btnHome);
        Button btnEdit= (Button) coordinatorLayout.findViewById(R.id.btnEdit);
        TextView tvSummary= (TextView) coordinatorLayout.findViewById(R.id.tvSummary);
        CircleImageView cv= (CircleImageView) frameLayout.findViewById(R.id.civProfilePic);
        TextView tvLevel = (TextView) frameLayout.findViewById(R.id.tvLevel);

        CoordinatorLayout academics= (CoordinatorLayout) myLayout.findViewById(R.id.Profile_academic);
        TextView tvDep=(TextView) academics.findViewById(R.id.tvDep);
        TextView tvBatch=(TextView) academics.findViewById(R.id.tvBatch);

        CoordinatorLayout professional= (CoordinatorLayout) myLayout.findViewById(R.id.Profile_professional);
        TextView tvWork=(TextView) professional.findViewById(R.id.tvWork);
        TextView tvIndustry=(TextView) professional.findViewById(R.id.tvIndustry);

        btnHome.setOnClickListener(this);
        btnEdit.setOnClickListener(this );

        tvLevel.setText("");
        Context c=frameLayout.getContext();

        ImageLoader i=new ImageLoader(c);
        i.clearCache();

        ProgressBar pgbar= (ProgressBar) myLayout.findViewById(R.id.pbHeaderProgress);
        cv.setVisibility(myLayout.GONE);
        pgbar.setVisibility(myLayout.VISIBLE);



        View photoHeader = myLayout.findViewById(R.id.photoHeader);

        SharedPreferences Spreferences =this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        tvName.setText(Spreferences.getString(Config.NAME_SHARED_PREF, "Name"));
//        tvTitle.setText(Spreferences.getString(Config.WORK_SHARED_PREF,"(position at Company"));
        tvEducation.setText(Spreferences.getString(Config.EMAIL_SHARED_PREF, "email@provider.tld"));
        tvAddress.setText(Spreferences.getString(Config.PHONE_SHARED_PREF, "9000000000"));
        tvSummary.setText("");

        tvDep.setText("Department of ".concat(Spreferences.getString(Config.DEP_SHARED_PREF,"DEP")));
        tvBatch.setText(Spreferences.getString(Config.BATCH_SHARED_PREF, "20XX").concat(" Batch"));
        tvWork.setText(Spreferences.getString(Config.WORK_SHARED_PREF,"Work").concat(" at ").concat(Spreferences.getString(Config.CMPNY_SHARED_PREF,"Company")));
        tvIndustry.setText(Spreferences.getString(Config.INDUS_SHARED_PREF, "Industry"));

        ImageLoader imageLoader=new ImageLoader(c);
        String adm=Spreferences.getString(Config.ADM_SHARED_PREF,"XXX/DEP/YYYY");
        imageLoader.clearCache();
        imageLoader.fileCache.clear();
        imageLoader.DisplayImage("http://codesquad.in/include/getImage.php?admNo=" + adm, cv);
        imageLoader.clearCache();


            cv.setVisibility(myLayout.VISIBLE);
            pgbar.setVisibility(myLayout.GONE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        /* For devices equal or higher than lollipop set the translation above everything else */
            photoHeader.setTranslationZ(6);
        /* Redraw the view to show the translation */
            photoHeader.invalidate();
        }

        return myLayout;
    }

    public void onClick(View v) {

        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()

                .replace(R.id.contentNavBar,new ProfileEdit_Fragment())
                .commit();

    }

}
