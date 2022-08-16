package com.smartherd.suntrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.smartherd.suntrip.databinding.ActivityProfileBinding;

public class Profile extends UtilitiesClass {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        get_request(base_url_profile + "token=" + Hash.getInstance().get_token(), new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                ProfileResponse profileResponse = responseToJSON(response, ProfileResponse.class);
                if(profileResponse == null)
                {
                    Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    return;
                }
                setDetail(profileResponse);
            }

            @Override
            public void onError(String response) {
                Toast.makeText(Profile.this, "An error happened", Toast.LENGTH_LONG).show();
            }
        });

        binding.btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivity(MainActivity.class);
                finish();
            }
        });


    }

    private void logOut() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        Hash.getInstance().set_token("");
        switchActivity(MainActivity.class);
        finish();
    }

    private void setDetail(ProfileResponse profileResponse)
    {
        binding.tvName.setText(profileResponse.getNume_complet().split(" ")[0]);
        binding.tvSurname.setText(profileResponse.getNume_complet().split(" ")[1]);
        binding.tvEmail.setText(profileResponse.getEmail());
        Hash.getInstance().set_token(profileResponse.getNew_token());
    }



}