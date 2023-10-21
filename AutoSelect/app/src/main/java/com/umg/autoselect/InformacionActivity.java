package com.umg.autoselect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

public class InformacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        ImageView facebookIcon = findViewById(R.id.facebookIcon);

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre el enlace de Facebook en un navegador web
                String url = "https://www.facebook.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        ImageView instagramIcon = findViewById(R.id.instagramIcon);

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre el enlace de Instagram en un navegador web
                String url = "https://www.instagram.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void openWhatsAppLink(View view) {
        // Abre el enlace de WhatsApp en un navegador web
        String url = "https://chat.whatsapp.com/LYdu5hAApcz06lRXqkfSeH";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void openAutoSelectWebsite(View view) {
        // Abre el enlace de AutoSelect en línea en un navegador web
        String url = "https://autoselect.online/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}