package com.example.myapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class show extends AppCompatActivity {

    public static ImageView imageView;
    View v;
    private StorageReference mStorageRef;
    Button b1;
    Uri myuri;
    Byte byteArray;
    TextView t3;
    private RecyclerView rv;
    File gallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);
        // ArrayList<CreateList> createLists = prepareData();
        RecyclerView.Adapter adapter = new adapter(gallery);
        rv.setAdapter(adapter);


        // imageView = (ImageView) findViewById(R.id.imageView);
        v = this.getWindow().getDecorView();
        v.setBackgroundResource(R.color.black);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        //t3 = (TextView)findViewById(R.id.t3);


        Intent intent = getIntent();
        Toast.makeText(show.this, "caption : " + intent.getStringExtra("caption"), Toast.LENGTH_SHORT).show();
        //gallery = (File)intent.getExtras().get("galleryfolder");
        //String msg = intent.getStringExtra("caption");

        //t3.setText(msg);
       /* byte[] byteArray = getIntent().getByteArrayExtra("image");
         Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Drawable drawable1 = new BitmapDrawable(bmp);
        //imageView.setBackgroundDrawable(drawable1);
        UploadTask uploadTask = mStorageRef.putBytes(byteArray);
       uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(show.this , "Failed",Toast.LENGTH_LONG);

            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(show.this , " image is uploaded",Toast.LENGTH_LONG);

            }
        });



    }*/
     /*ArrayList <CreateList>  image_titles  = new ArrayList<>();
     Integer image_ids;

    private ArrayList<CreateList> prepareData() {
        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles; i++){
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles[i]);
            createList.setImage_ID(image_ids[i]);
            theimage.add(createList);
        }
        return theimage;
    }*/
    }
}



