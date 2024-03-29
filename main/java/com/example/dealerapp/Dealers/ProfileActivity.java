package com.example.dealerapp.Dealers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore mUserDatabase;
    private FirebaseUser mCurrentUser;

    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName,mMobile,mCity,mState,mShopName,mShopGST,mShopAddress;

    private Button mDetailBtn;
    private Button mImageBtn;

    String download_url;
    String thumb_downloadUrl;

    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_name);
        mMobile = (TextView) findViewById(R.id.settings_phone);
        mShopName = (TextView) findViewById(R.id.shop_name);
        mCity = (TextView) findViewById(R.id.shop_city);
        mState = (TextView) findViewById(R.id.shop_state);
        mShopAddress = (TextView) findViewById(R.id.shop_address);
        mShopGST = (TextView) findViewById(R.id.shop_gst);

        mDetailBtn = (Button) findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) findViewById(R.id.settings_image_btn);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current_uid = mCurrentUser.getUid();


        pd = new ProgressDialog(ProfileActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        //mUserDatabase = FirebaseFirestore.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase = FirebaseFirestore.getInstance();
        mUserDatabase.collection("Dealers").document(current_uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    final String image = documentSnapshot.get("profile_thumbUrl").toString();
                    String first_name = documentSnapshot.get("first_name").toString();
                    String last_name = documentSnapshot.get("last_name").toString();
                    String name = first_name + " " +last_name;
                    String gst = documentSnapshot.get("gst").toString();
                    String shopname = documentSnapshot.get("company").toString();
                    String city = documentSnapshot.get("address1").toString();
                    String state = documentSnapshot.get("address2").toString();
                    String mob = documentSnapshot.get("mob").toString();
                    //String address = documentSnapshot.get("address").toString();

                    mName.setText(name);
                    mMobile.setText(mob);
                    mShopName.setText(shopname);
                    mCity.setText(city);
                    mState.setText(state);
                    //mShopAddress.setText(address);
                    mShopGST.setText(gst);

                    if (!image.equals("default")) {

                        //Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.logo).into(mDisplayImage);

                        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                .into(mDisplayImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(image).into(mDisplayImage);

                                    }
                                });

                    }

                    pd.dismiss();
                }else {
                    mUserDatabase.collection("Users").document(current_uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot.exists()){
                                final String image = documentSnapshot.get("profile_thumbUrl").toString();
                                String first_name = documentSnapshot.get("first_name").toString();
                                String last_name = documentSnapshot.get("last_name").toString();
                                String name = first_name + " " +last_name;
                                String shopname = documentSnapshot.get("company").toString();
                                String city = documentSnapshot.get("address1").toString();
                                String state = documentSnapshot.get("address2").toString();
                                String mob = documentSnapshot.get("mob").toString();
                                //String address = documentSnapshot.get("address").toString();

                                mName.setText(name);
                                mMobile.setText(mob);
                                mShopName.setText(shopname);
                                mCity.setText(city);
                                mState.setText(state);
                                //mShopAddress.setText(address);
                                mShopGST.setText("Should be verified first");

                                if (!image.equals("default")) {

                                    //Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.logo).into(mDisplayImage);

                                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                            .into(mDisplayImage, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Picasso.get().load(image).into(mDisplayImage);

                                                }
                                            });

                                }

                                pd.dismiss();
                            }
                        }
                    });
                }
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);
                        */

            }
        });

        mDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mob = mMobile.getText().toString();
                String name = mName.getText().toString();
                /*
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.putExtra("mob",mob);
                intent.putExtra("name",name);
                startActivity(intent);
                */
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(ProfileActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(ProfileActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                final String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");



                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            //final String download_url = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                            task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url = uri.toString();
                                }
                            });
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    if(thumb_task.isSuccessful()){

                                        thumb_task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                thumb_downloadUrl = uri.toString();
                                            }
                                        });

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("profile_pic", download_url);
                                        update_hashMap.put("profile_thumbUrl", thumb_downloadUrl);

                                        mUserDatabase.collection("Users").document(current_user_id).update(update_hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(ProfileActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });
                                        //Also Edit Here
                                        /*
                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(ProfileActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });

                                        */
                                    } else {

                                        Toast.makeText(ProfileActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(ProfileActivity.this, "Error in uploading.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
