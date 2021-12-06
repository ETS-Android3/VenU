package com.example.venu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.adapters.BadgeAdapter;
import com.example.venu.adapters.FriendAdapter;
import com.example.venu.models.Badge;
import com.example.venu.models.Friend;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = "EditProfileActivity";
    public static final int PICK_PIC_CODE = 100;
    private BottomNavigationView bottomNavigationView;
    EditText etBio;
    ImageView ivTempPic;
    Button btnSubmit;
    Button btnGetPic;
    private ParseFile photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        etBio = findViewById(R.id.etEditBio);
        ivTempPic = findViewById(R.id.ivTempPicture);
        btnGetPic = findViewById(R.id.btnGetImage);
        btnSubmit = findViewById(R.id.btnSubmit);


        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.include(Profile.KEY_USERNAME);
        query.whereEqualTo(Profile.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting profile", e);
                    return;
                }
                for (ParseUser profile : profiles) {
                    if (profile.get("profilepicture") != null ){
                        Glide.with(EditProfileActivity.this).load(profile.getParseFile("profilepicture").getUrl()).into(ivTempPic);
                    }
                    etBio.setText(String.valueOf(profile.get("bio")));
                }
            }
        });
        btnGetPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPic();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

    }

    private void updateInfo() {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("bio", etBio.getText().toString());
        user.put("profilepicture", photoFile);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG,"saved successfully!");
                } else {
                    Log.e(TAG,"failed", e);
                }
            }
        });
    }

    private void getPic() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_PIC_CODE);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PIC_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] byteArray = stream.toByteArray();
                    photoFile = new ParseFile("image.png",byteArray);
                    ivTempPic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't uploaded!", Toast.LENGTH_SHORT).show();
            }
        }

    }



}
