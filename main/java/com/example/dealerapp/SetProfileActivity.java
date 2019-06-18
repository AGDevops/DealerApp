package com.example.dealerapp;

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

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SetProfileActivity extends AppCompatActivity {


    private TextView selectText, helloText;
    private Button nextBtn;
    private CircleImageView profileImg;
    private TextView choosePhoto;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUser;
    private StorageReference mProfileStorage;
    public static final int SELECT_PHOTO = 1;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        selectText = findViewById(R.id.select_who_text);
        nextBtn = findViewById(R.id.set_up_profile_next_btn);
        profileImg = findViewById(R.id.startup_user_profile);
        choosePhoto = findViewById(R.id.choose_profile_photo_text);
        helloText = findViewById(R.id.hello_mr_username_text);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        dialog = new ProgressDialog(this);
        mProfileStorage = FirebaseStorage.getInstance().getReference();

        db.collection("Users").document(currentUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String name = documentSnapshot.get("user_name").toString();
                helloText.setText("Hello Mr. " + name);
            }
        });

        selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BottomSheetFrag fragment = new BottomSheetFrag();
//                fragment.show(getSupportFragmentManager(), "TAG");
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        selectText.setText("Dealer");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetProfileActivity.this, DealerInfoActivity.class);
                startActivity(intent);
                finish();
//                if (selectText.getText().toString().equals("Who are you?")){
//                    Toast.makeText(SetProfileActivity.this, "please select who you are", Toast.LENGTH_SHORT).show();
//                }else {
//                    db.collection("Users").document(currentUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                            String type = documentSnapshot.get("type").toString();
//                            if (type.equals("Owner")){
//                                Intent intent = new Intent(SetProfileActivity.this, OwnerInfoActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//
//                            }else {
//                                Intent intent = new Intent(SetProfileActivity.this, DealerInfoActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(false);
                picasso.load(resultUri).placeholder(R.drawable.avatar).into(profileImg);

                dialog.setMessage("updating profile");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final File thumb_file_Path = new File(resultUri.getPath());

                final Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(50)
                        .compressToBitmap(thumb_file_Path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference thumb_filepath = mProfileStorage.child("profile_images").child("thumb")
                        .child(currentUser + ".jpg");

                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String thumb_downloadUrl = uri.toString();

                                    Map update_hashMap = new HashMap();
                                    update_hashMap.put("profile_thumbUrl", thumb_downloadUrl);

                                    db.collection("Users").document(currentUser)
                                            .update(update_hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            dialog.dismiss();
                                            Toast.makeText(SetProfileActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });

                        }
                    }
                });


            }
        }
    }
}
