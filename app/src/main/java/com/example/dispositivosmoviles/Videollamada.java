package com.example.dispositivosmoviles;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class Videollamada extends AppCompatActivity {


    EditText codigo;
    Button Entrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videollamada);
        codigo=findViewById(R.id.editTextTextPersonName);
        Entrar=findViewById(R.id.btn_Entrar);
        URL server;
        try {
            server =new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions
                    defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(server)
                    .setFeatureFlag("welcomepage.enabled", false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        }catch(MalformedURLException e){
            e.printStackTrace();

        }
        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(codigo.getText().toString())
                        .setFeatureFlag("welcomepage.enabled", false).build();
                JitsiMeetActivity.launch(Videollamada.this,options);
            }
        });

    }
}