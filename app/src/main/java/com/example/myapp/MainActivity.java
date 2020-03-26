package com.example.myapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {


    private ImageView captureImage;
    ImageButton click;
    TextView textView;
    EditText editText;
    View v;
    Bitmap bitmap;
    String pathToFile;
    Intent cameraIntent;
    Bitmap photoreducedSizeBitmap;
    File  galleryFolder;
    byte[] byteArray;

    FloatingActionButton send;
    String IMAGE_GALLERY = "images";

    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    private StorageReference mStorageRef;
    public LruCache<String, Bitmap> mMemoryCache;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createImageGallery();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

// Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;

            }

        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = this.getWindow().getDecorView();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("folderName/file.jpg ");



        captureImage = (ImageView) findViewById(R.id.captureImage);
        click = (ImageButton) findViewById(R.id.clickButton);
        //textView = (TextView)findViewById(R.id.textView1);
        editText = (EditText) findViewById(R.id.caption);
        send = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        editText.setVisibility(View.INVISIBLE);
        send.setVisibility(View.INVISIBLE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = editText.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoreducedSizeBitmap .compress(Bitmap.CompressFormat.PNG, 1, stream);
                byteArray = stream.toByteArray();

                Intent intent = new Intent(getApplicationContext(), upload.class);
                intent.putExtra("caption", caption);
                //intent.putExtra("image", byteArray);

                mStorageRef = FirebaseStorage.getInstance().getReference("images");
                UploadTask uploadTask = mStorageRef.putBytes(byteArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                 public void onFailure(@NonNull Exception e) {
                 Toast.makeText(MainActivity.this , "Failed",Toast.LENGTH_LONG);

                }
                });
                 uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Toast.makeText(MainActivity.this , " image is uploaded",Toast.LENGTH_LONG);

                }

                 });
                startActivity(intent);

            }
        });

        click.setOnClickListener(new View.OnClickListener()  {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                //selectImage();
                click.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
                v = (View) findViewById(R.id.v1);
                v.setBackgroundResource(R.color.black);
                 cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,  "com.example.android.fileprovider", photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoURI);




                    }
                }
                startActivityForResult(cameraIntent, TAKE_PICTURE);


            }
        });



}

    String imageFilePath;
    private void createImageGallery() {
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        galleryFolder = new File(storageDir,IMAGE_GALLERY);
        if(!galleryFolder.exists()){
            galleryFolder.mkdirs();
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = null;
        try{
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                galleryFolder/* directory */

        );}
        catch(IOException e){
            Log.d("my log","Excep : "+e.toString());
        }

        imageFilePath = image.getAbsolutePath();
        return image;
    }
    public void addBitmapToMemoryCache(String key,Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }



    public void onActivityResult(int requestcode, int resultcode, Intent intent) {
        super.onActivityResult(requestcode, resultcode, intent);
        if (resultcode == RESULT_OK) {
            if (requestcode == TAKE_PICTURE) {
                //photo = (Bitmap) cameraIntent.getExtras().get("data");
                //Drawable drawable = new BitmapDrawable(photo);
                //captureImage.setBackgroundDrawable(drawable);
                //captureImage.setImageBitmap(photo);
                //Glide.with(this).load(imageFilePath).into(captureImage);
                //ImageShow.imageView.setBackgroundDrawable(drawable);
                //Bundle extras = cameraIntent.getExtras();
               // Bitmap imageBitmap = (Bitmap) extras.get("data");
                //captureImage.setImageBitmap(imageBitmap);
                //bitmap = mMemoryCache.get(imageFilePath);
                //bitmap = BitmapFactory.decodeFile(imageFilePath);
                //mMemoryCache.put(imageFilePath, bitmap);
                //captureImage.setImageBitmap(bitmap);
                setReducedImageSize();
               // mStorageRef = FirebaseStorage.getInstance().getReference("images");
                //UploadTask uploadTask = mStorageRef.putBytes(byteArray);
               /// uploadTask.addOnFailureListener(new OnFailureListener() {
                    //@Override
                   // public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(MainActivity.this , "Failed",Toast.LENGTH_LONG);

                    //}
               // });
               // uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    //@Override
                   // public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // Toast.makeText(MainActivity.this , " image is uploaded",Toast.LENGTH_LONG);

                    //}

           // });



        }
        }

    }
        void setReducedImageSize(){
        int targetImageViewWidth = captureImage.getWidth();
            int targetImageViewHieght = captureImage.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(imageFilePath,bmOptions);
            int cameraImageWidth = bmOptions.outWidth;
            int cameraImageHeight = bmOptions.outHeight;

            int scalefactor = Math.min(cameraImageWidth/targetImageViewWidth,cameraImageHeight/targetImageViewHieght);
            bmOptions.inSampleSize = scalefactor;
            bmOptions.inJustDecodeBounds = false;

            photoreducedSizeBitmap = BitmapFactory.decodeFile(imageFilePath,bmOptions);
            captureImage.setImageBitmap(photoreducedSizeBitmap);

        }
    }





