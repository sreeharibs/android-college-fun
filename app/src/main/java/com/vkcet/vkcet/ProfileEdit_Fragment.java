package com.vkcet.vkcet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SREE on 23-Feb-16.
 */
public class ProfileEdit_Fragment extends Fragment implements View.OnClickListener {
    View myView;
    de.hdodenhof.circleimageview.CircleImageView circleView ;
    final int SELECT_PHOTO = 1;
    public static final String UPLOAD_URL = "http://codesquad.in/include/image.php";
    public static final String UPDATE_URL = "http://codesquad.in/include/update.php";
    public static final String UPLOAD_KEY = "image";

    public Bitmap selectedImage;
    public int imgselected=0;
    private final String imageURL = "http://codesquad.in/include/getImage.php?admNo=";

    public ImageButton btnUpdate;
    public String adm;

    public EditText etEmail;
    public EditText etPhone;
    public EditText etPassword;

    public Boolean updatePersonal=false;
    public Boolean updateProfession=false;
    public Boolean updatePrivacy=false;
    @Nullable
    //@Override
    public View onCreateView(LayoutInflater  inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.profle_edit_layout,container,false);

        CoordinatorLayout profileEditLayout= (CoordinatorLayout) myView.findViewById(R.id.ProfileEdit);
        EditText etName=(EditText)profileEditLayout.findViewById(R.id.etName);
        EditText etEmail=(EditText)profileEditLayout.findViewById(R.id.etEmail);
        EditText etPhone=(EditText)profileEditLayout.findViewById(R.id.etPhone);
        EditText etJobTitle=(EditText)profileEditLayout.findViewById(R.id.etJobTitle);
        EditText etCompany=(EditText)profileEditLayout.findViewById(R.id.etCompany);
        EditText etIndustry=(EditText)profileEditLayout.findViewById(R.id.etIndustry);
        Spinner spPrivacy=(Spinner) profileEditLayout.findViewById(R.id.spPrivacy);
        TextView tvDep=(TextView) profileEditLayout.findViewById(R.id.tvDep);
        TextView tvBatch=(TextView )profileEditLayout.findViewById(R.id.tvEducation);
        EditText etPassword = (EditText) profileEditLayout.findViewById(R.id.etPassword);
        Button btnUpdatePersonal = (Button) profileEditLayout.findViewById(R.id.btnUpdatePersonal);
        Button btnUpdateProfession = (Button) profileEditLayout.findViewById(R.id.btnUpdateProfession);
        Button btnUpdatePrivacy = (Button) profileEditLayout.findViewById(R.id.btnUpdatePrivacy);

        btnUpdatePersonal.setOnClickListener(this);
        btnUpdatePrivacy.setOnClickListener(this);
        btnUpdateProfession.setOnClickListener(this);


        FrameLayout propicLayout= (FrameLayout) profileEditLayout.findViewById(R.id.photoHeader);
        TextView level=(TextView) propicLayout.findViewById(R.id.tvLevel);
        CircleImageView cv= (CircleImageView) propicLayout.findViewById(R.id.civProfilePic);
        cv.setImageDrawable(Drawable.createFromPath("drawable/profile.jpg"));

        SharedPreferences Spreferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        adm=Spreferences.getString(Config.ADM_SHARED_PREF, "123");

        Context c=propicLayout.getContext();
        ImageLoader imageLoader=new ImageLoader(c);
        String adm=Spreferences.getString(Config.ADM_SHARED_PREF,"XXX/DEP/YYYY");
        imageLoader.clearCache();
        imageLoader.fileCache.clear();
        imageLoader.DisplayImage("http://codesquad.in/include/getImage.php?admNo=" + adm, cv);
        imageLoader.clearCache();

        etName.setText(Spreferences.getString(Config.NAME_SHARED_PREF, "Name"));
//        tvTitle.setText(Spreferences.getString(Config.WORK_SHARED_PREF,"(position at Company"));
        etEmail.setText(Spreferences.getString(Config.EMAIL_SHARED_PREF, "email@provider.tld"));
        etPhone.setText(Spreferences.getString(Config.PHONE_SHARED_PREF, "9000000000"));
        level.setText("");
        etPassword.setText(Spreferences.getString(Config.PWD_SHARED_PREF,"password"));

        tvDep.setText("Department of ".concat(Spreferences.getString(Config.DEP_SHARED_PREF, "DEP")));
        tvBatch.setText(Spreferences.getString(Config.BATCH_SHARED_PREF, "20XX").concat(" Batch"));
        etJobTitle.setText(Spreferences.getString(Config.WORK_SHARED_PREF, "Work"));
        etCompany.setText(Spreferences.getString(Config.CMPNY_SHARED_PREF, "Company"));
        etIndustry.setText(Spreferences.getString(Config.INDUS_SHARED_PREF, "Industry"));


        return myView;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        circleView.setImageBitmap(selectedImage);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    //Upload Image - Help

    public String getStringImage(Bitmap bmp) {
        if(imgselected==1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (imgselected == 1) bmp.compress(Bitmap.CompressFormat.WEBP, 1, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
           return String.valueOf(0);
    }

    //Upload Image - Original
    private void update(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            public String email = String.valueOf(etEmail.getText());
            public String phone = String.valueOf(etPhone.getText());
            public String password = String.valueOf(etPassword.getText());



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(s.equalsIgnoreCase("Success")) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.EMAIL_SHARED_PREF, email);
                    editor.putString(Config.PHONE_SHARED_PREF, phone);
                    editor.putString(Config.PWD_SHARED_PREF, password);

                    editor.apply();
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.contentNavBar,new Profile_Fragment())
                            .commit();

                }
            }



            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                if(updatePersonal){

                if (imgselected==1) {

                    HashMap<String, String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, uploadImage);
                    data.put(Config.KEY_ADM, adm);
                    data.put("email", email);
                    data.put("phone", phone);
                    data.put("password", password);
                    imgselected = 0;


                    String result = rh.sendPostRequest(UPLOAD_URL, data);
                    return result;
                }

                else
                {
                    HashMap<String, String> data = new HashMap<>();
                    data.put(Config.KEY_ADM, adm);
                    data.put("email", email);
                    data.put("phone", phone);
                    data.put("password", password);
                    imgselected = 0;


                    String result = rh.sendPostRequest(UPDATE_URL, data);
                    return result;
                }
            }
            return "";}
        }

        UploadImage ui = new UploadImage();
        ui.execute(selectedImage);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.circleView)
            {
                Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                imgselected=1;
            }
        else if (v.getId()==R.id.circleView/*R.id.btnUpdate*/)
        {
            Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT).show();

            //uploadImage();

        }
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

        GetImage gi = new GetImage(circleView);
        gi.execute(adm);
    }

}
