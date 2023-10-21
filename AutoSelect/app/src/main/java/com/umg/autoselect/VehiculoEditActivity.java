package com.umg.autoselect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.umg.multipartrequest.AppHelper;
import com.umg.multipartrequest.VolleyMultipartRequest;
import com.umg.multipartrequest.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehiculoEditActivity extends AppCompatActivity {
    ViewFlipper imageSlider;
    ImageView imageView, imageUpload;
    EditText vehiculoMarcaEdit, vehiculoLineaEdit, vehiculoModeloEdit, vehiculoMotorEdit, vehiculoTransmisionEdit;
    int idVehiculo;
    String user_id, imgs;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo_edit);
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "0");
        idVehiculo = getIntent().getIntExtra("idVehiculo",0);
        vehiculoMarcaEdit = findViewById(R.id.vehiculoMarcaEdit);
        vehiculoLineaEdit = findViewById(R.id.vehiculoLineaEdit);
        vehiculoModeloEdit = findViewById(R.id.vehiculoModeloEdit);
        vehiculoMotorEdit = findViewById(R.id.vehiculoMotorEdit);
        vehiculoTransmisionEdit = findViewById(R.id.vehiculoTransmisionEdit);
        imageUpload = findViewById(R.id.imageUpload);
        builder = new AlertDialog.Builder(this);
        getVehiculo();
    }
    public void Editar(View view){
        builder.setTitle("Confirmacion")
                .setMessage("Esta seguro de los cambios que se realizaran?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editVehiculo();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }
    public void Delete(View view){
        builder.setTitle("Confirmacion")
                .setMessage("Esta seguro? El listado se eliminara permanentemente")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVehiculo();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }
    private void editVehiculo(){
        String url = "https://api.autoselect.online/vehiculos/"+idVehiculo;
        try{
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);;
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        Toast.makeText(VehiculoEditActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent misVehiculos = new Intent(VehiculoEditActivity.this, MisVehiculosActivity.class);
                    startActivity(misVehiculos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message+" Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message+ " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message+" Something is getting wrong";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("marca", vehiculoMarcaEdit.getText().toString());
                    params.put("linea", vehiculoLineaEdit.getText().toString());
                    params.put("modelo", vehiculoModeloEdit.getText().toString());
                    params.put("motor", vehiculoMotorEdit.getText().toString());
                    params.put("transmision", vehiculoTransmisionEdit.getText().toString());
                    params.put("userID", user_id);
                    params.put("imgs","");
                    return params;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("file[]", new DataPart("autoselect.jpeg", AppHelper.getFileDataFromDrawable(getBaseContext(), imageUpload.getDrawable()), "image/jpeg"));
                    //params.put("file[]", new DataPart("autoselect.jpeg", AppHelper.getFileDataFromDrawable(getBaseContext(), imageUpload.getDrawable()), "image/jpeg"));
                    return params;
                }
            };
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
        }catch(Exception E){
            Toast.makeText(this, E.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteVehiculo(){
        String url = "https://api.autoselect.online/vehiculos/"+idVehiculo;

        Map<String, String> params = new HashMap();
        params.put("userID", user_id);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PATCH, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(VehiculoEditActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(VehiculoEditActivity.this, "ocurrio un error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
                Intent misVehiculos = new Intent(VehiculoEditActivity.this, MisVehiculosActivity.class);
                startActivity(misVehiculos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    error.printStackTrace();
                }catch(Exception E){
                    Toast.makeText(VehiculoEditActivity.this, E.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void getVehiculo(){
        imageSlider = findViewById(R.id.imageEditSlider);
        idVehiculo = getIntent().getIntExtra("idVehiculo",0);
        String url = "https://api.autoselect.online/vehiculos/"+idVehiculo;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject vehiculo = response.getJSONObject("vehiculo");
                    vehiculoMarcaEdit.setText(vehiculo.getString("marca"));
                    vehiculoLineaEdit.setText(vehiculo.getString("linea"));
                    vehiculoModeloEdit.setText(String.valueOf(vehiculo.getInt("modelo")));
                    vehiculoMotorEdit.setText(vehiculo.getString("motor"));
                    vehiculoTransmisionEdit.setText(vehiculo.getString("transmision"));
                    JSONArray imgArray = vehiculo.getJSONArray("img_links");
                    for(int j = 0; j < imgArray.length(); j++){
                        JSONObject img = imgArray.getJSONObject(j);
                        imageView = new ImageView(VehiculoEditActivity.this);
                        Glide.with(VehiculoEditActivity.this).load(img.getString("img_link")).into(imageView);
                        imageSlider.addView(imageView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(VehiculoEditActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VehiculoEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    public void showEditNext(View view){
        imageSlider.showNext();
    }
    public void showEditPrevious(View view){
        imageSlider.showPrevious();
    }
}