package com.example.venu.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.venu.EditProfileActivity;
import com.example.venu.Profile;
import com.example.venu.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {

    public static final String TAG = "ProfileEditFragment";
    public static final int PICK_PIC_CODE = 100;
    EditText etBio;
    ImageView ivTempPic;
    Button btnSubmit;
    Button btnGetPic;
    private ParseFile photoFile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        etBio = v.findViewById(R.id.etEditBio);
        ivTempPic = v.findViewById(R.id.ivTempPicture);
        btnGetPic = v.findViewById(R.id.btnGetImage);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                        Glide.with(getContext()).load(profile.getParseFile("profilepicture").getUrl()).into(ivTempPic);
                    }
                    etBio.setText(String.valueOf(profile.get("bio")));
                    photoFile = (ParseFile) profile.getParseFile("profilepicture");
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
                    Fragment nextFrag= new ProfileFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContainer, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PIC_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] byteArray = stream.toByteArray();
                    photoFile = new ParseFile("image.png",byteArray);
                    ivTempPic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't uploaded!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}