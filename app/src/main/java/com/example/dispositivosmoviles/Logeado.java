package com.example.dispositivosmoviles;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Logeado extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button;
    private TextView textview;
    private Locale locale;
    private Configuration config = new Configuration();
    TextView tvInfoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        tvInfoUser = findViewById(R.id.tv_info_user);

        initviews(user);





    }
    private void initviews(FirebaseUser user) {


        if (user!= null) {
            tvInfoUser.setText(user.getEmail());
        }
        else{
            tvInfoUser.setText("--");
        }

    }



    public void clickCerrarSesion2(View view) {
        mAuth.signOut();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Sesion Cerrada Vuelve Pronto!", Toast.LENGTH_SHORT).show();
        finish();

    }
    public void chat(View view) {
        Intent i = new Intent(getApplicationContext(), Chat.class);
        startActivity(i);
    }
    public void Videollamada(View view) {
        Intent i = new Intent(getApplicationContext(), Videollamada.class);
        startActivity(i);
    }








}