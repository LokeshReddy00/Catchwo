package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddReels extends AppCompatActivity {

    private VideoView video_lay;
    private TextView text;
    private TextView post;
    Uri selectedUri;
    private ImageView image;
    private ProgressDialog progressDialog;
    MediaController mediaController;
    String videoData;
    private static final int PICK_FILE = 1;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reels);

        post = findViewById(R.id.post);
        image = findViewById(R.id.image);
        video_lay = findViewById(R.id.video_lay);
        text = findViewById(R.id.text);progressDialog = new ProgressDialog(this);
        mediaController = new MediaController(AddReels.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        video_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUri != null && !TextUtils.isEmpty(text.getText().toString())){
                    UploadToserver();
                }else {
                    Toast.makeText(AddReels.this, "Post Something", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void UploadToserver() {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


        if(!TextUtils.isEmpty(text.getText().toString()) && selectedUri != null) {

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("time", time);
                        hashMap.put("desc", text.getText().toString());
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post", DownloadUrl.toString());
                        hashMap.put("id", String.valueOf(times));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("reels").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddReels.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddReels.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

        }


    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(AddReels.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(AddReels.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void ChooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        //  intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE || resultCode == RESULT_OK || data != null
                || data.getData() != null){

            selectedUri = data.getData();

            video_lay.setMediaController(mediaController);
            video_lay.setVideoURI(selectedUri);
            video_lay.start();

            image.setVisibility(View.GONE);

                selectedUri = Objects.requireNonNull(data).getData();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(), selectedUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilli = Long.parseLong(time);
                retriever.release();

                if (timeInMilli > 600000){
//                    Snackbar.make(findViewById(R.id.main), "Video must be of 10 minutes or less", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(AddReels.this, "Video must be of 10 minutes or less", Toast.LENGTH_SHORT).show();
                }else {
                    vedio(selectedUri);
                }

//                reel_lay.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(AddReels.this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void vedio(Uri selectedUri) {

        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = managedQuery(selectedUri, projection, null, null, null);

        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        Log.d("File Name:",filePath);

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        // Setting the thumbnail of the video in to the image view
//        msImage.setImageBitmap(thumb);
//        imageStore(thumb);
        InputStream inputStream = null;
// Converting the video in to the bytes
        try
        {
            inputStream = getContentResolver().openInputStream(selectedUri);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int len = 0;
        try
        {
            while ((len = inputStream.read(buffer)) != -1)
            {
                byteBuffer.write(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("converted!");


        //Converting bytes into base64
        videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
        Log.d("VideoData**>  " , videoData);

        String sinSaltoFinal2 = videoData.trim();
        String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
        Log.d("VideoData**>  " , sinsinSalto2);
    }

}