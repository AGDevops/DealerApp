package com.example.dealerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import id.zelory.compressor.Compressor;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int SELECT_PHOTO = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference itemRef;
    String currentUserId;
    private ProgressDialog dialog;

    private ImageView itemImage;
    private TextInputEditText itemName, itemBrand, itemDescription, itemPrice, itemMRP, itemQuantity;
    private Button selectImage, publishBtn;
    String pushId;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        pushId = getIntent().getStringExtra("push_id");
        dialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.add_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                db.collection("AllItems").document(pushId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        itemRef = FirebaseStorage.getInstance().getReference();
        currentUserId = mAuth.getCurrentUser().getUid();
        dialog = new ProgressDialog(this);

        selectImage = findViewById(R.id.select_item_image);
        itemName = findViewById(R.id.item_name_input);
        itemBrand = findViewById(R.id.item_brand_input);
        itemDescription = findViewById(R.id.item_description_input);
        itemPrice = findViewById(R.id.item_price_input);
        itemMRP = findViewById(R.id.item_market_price_input);
        itemImage = findViewById(R.id.item_image_view);
        publishBtn = findViewById(R.id.item_publish_btn);
        itemQuantity = findViewById(R.id.item_quantity_input);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("publishing product");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                String brand = itemBrand.getText().toString();
                String price = itemPrice.getText().toString();
                String mrp = itemMRP.getText().toString();
                String quant = itemQuantity.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) &&
                        !TextUtils.isEmpty(brand) && !TextUtils.isEmpty(price) &&
                        !TextUtils.isEmpty(mrp) && !TextUtils.isEmpty(quant)) {

                    int price_int = Integer.parseInt(price);
                    int mrp_int = Integer.parseInt(mrp);
                    int value = mrp_int - price_int;
                    int discount_per = value*100/mrp_int;

                    String discount = String.valueOf(discount_per);

                    updateItem(name, description, brand, price, mrp, pushId, discount, quant);

                }else {
                    Toast.makeText(AddItemActivity.this, "please fill all the details", Toast.LENGTH_SHORT).show();
                }
            }

        });

        spinner = findViewById(R.id.add_item_category_spinner);

        db.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<String> cate = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    String name = doc.getData().get("item_category").toString();
                    cate.add(name);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, cate);

//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                        R.array.category_array, android.R.layout.simple_spinner_dropdown_item);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

            }
        });
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(false);
                picasso.load(resultUri).placeholder(R.drawable.avatar).into(itemImage);

                dialog.setMessage("updating product image");
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

                final StorageReference thumb_filepath = itemRef.child("ItemImages").child(pushId)
                        .child(random() + ".jpg");

                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String thumb_downloadUrl = uri.toString();

                                    Map update_hashMap = new HashMap();
                                    update_hashMap.put("item_image", thumb_downloadUrl);

                                    db.collection("AllItems").document(pushId)
                                            .update(update_hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            dialog.dismiss();
                                            Toast.makeText(AddItemActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
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

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(15);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int pos = parent.getSelectedItemPosition();
        HashMap<String, Object> addMap = new HashMap();
        if (pos == 0) {
            addMap.put("item_cat", "Electronics");
        } else if (pos == 1) {
            addMap.put("item_cat", "");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateItem(String name, String description, String brand,
                            String price, String mrp, String pushId, String discount,
                            String quantity) {
        HashMap<String, Object> addMap = new HashMap<>();
        addMap.put("item_brand", brand);
        addMap.put("item_desc", description);
        addMap.put("item_mrp", mrp);
        addMap.put("item_name", name);
        addMap.put("item_price", price);
        addMap.put("item_discount", discount);
        addMap.put("item_quantity", quantity);

        db.collection("AllItems").document(pushId).update(addMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(AddItemActivity.this, "item added successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    dialog.hide();
                    Toast.makeText(AddItemActivity.this, "something is wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
