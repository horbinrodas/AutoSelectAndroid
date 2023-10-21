package com.umg.autoselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehiculoActivity extends AppCompatActivity {
    ViewFlipper imageSlider;
    ImageView imageView;
    TextView vehiculoDetailsText, vehiculoMotorText, vehiculoTransmisionText;
    EditText nombreContactInput, emailContactInput, telefonoContactInput, mensajeContactInput;
    Spinner razonSpinner;
    Button buttonShowContact, buttonSendContact;
    CardView contactCardView;
    int idVehiculo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        buttonShowContact = findViewById(R.id.buttonShowContact);
        buttonSendContact = findViewById(R.id.buttonSendContact);
        contactCardView = findViewById(R.id.contactCardView);
        buttonShowContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactCardView.setVisibility(contactCardView.VISIBLE);
                contactCardView.animate().alpha(1.0f);
                contactCardView.animate().translationY(0);
            }
        });
        getVehiculo();
    }
    public void Contact(View view){
        nombreContactInput = findViewById(R.id.nombreContactInput);
        emailContactInput = findViewById(R.id.emailContactInput);
        mensajeContactInput = findViewById(R.id.mensajeContactInput);
        telefonoContactInput = findViewById(R.id.telefonoContactInput);
        razonSpinner = findViewById(R.id.razonSpinner);
        String url = "https://api.autoselect.online/contact/"+idVehiculo;

        Map<String, String> params = new HashMap();
        params.put("nombre", nombreContactInput.getText().toString());
        params.put("email", emailContactInput.getText().toString());
        params.put("razon", razonSpinner.getSelectedItem().toString());
        params.put("mensaje", mensajeContactInput.getText().toString());
        params.put("telefono", telefonoContactInput.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(VehiculoActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                contactCardView.animate()
                        .translationY(contactCardView.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                contactCardView.setVisibility(View.GONE);
                            }
                        });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(VehiculoActivity.this, "Ocurrio Un Error", Toast.LENGTH_SHORT).show();
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void getVehiculo(){
        imageSlider = findViewById(R.id.imageSlider);
        vehiculoDetailsText = findViewById(R.id.vehiculoDetailsText);
        vehiculoMotorText = findViewById(R.id.vehiculoMotorText);
        vehiculoTransmisionText = findViewById(R.id.vehiculoTransmisionText);
        idVehiculo = getIntent().getIntExtra("idVehiculo",0);
        String url = "https://api.autoselect.online/vehiculos/"+idVehiculo;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject vehiculo = response.getJSONObject("vehiculo");
                    String vehiculoDetails = vehiculo.getString("marca")+" "+
                            vehiculo.getString("linea")+" "+
                            vehiculo.getInt("modelo");
                    String motorDetails = "Motor: " + vehiculo.getString("motor");
                    String transmisionDetails = "Transmision: " + vehiculo.getString("transmision");
                    vehiculoDetailsText.setText(vehiculoDetails);
                    vehiculoMotorText.setText(motorDetails);
                    vehiculoTransmisionText.setText(transmisionDetails);
                    JSONArray imgArray = vehiculo.getJSONArray("img_links");
                    for(int j = 0; j < imgArray.length(); j++){
                        JSONObject img = imgArray.getJSONObject(j);
                        imageView = new ImageView(VehiculoActivity.this);
                        Glide.with(VehiculoActivity.this).load(img.getString("img_link")).into(imageView);
                        imageSlider.addView(imageView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(VehiculoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VehiculoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    public void Compartir(View view){
        String urlShare = "https://autoselect.online/vehiculo.php?id="+idVehiculo;
        Toast.makeText(this, urlShare, Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("url", urlShare);
        clipboard.setPrimaryClip(clip);
    }
    public void showNextImage(View view){
        imageSlider.showNext();
    }
    public void showPreviousImage(View view){
        imageSlider.showPrevious();
    }
}