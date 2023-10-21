package com.umg.autoselect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.umg.multipartrequest.AppHelper;
import com.umg.multipartrequest.VolleyMultipartRequest;
import com.umg.multipartrequest.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehiculoCreateActivity extends AppCompatActivity {
    EditText createMarcaEdit, createLineaEdit, createModeloEdit, createMotorEdit, createTransmisionEdit;
    ImageView createImageView;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo_create);
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "0");
        createImageView = findViewById(R.id.createImageView);
        createMarcaEdit = findViewById(R.id.createMarcaEdit);
        createLineaEdit = findViewById(R.id.createLineaEdit);
        createModeloEdit = findViewById(R.id.createModeloEdit);
        createMotorEdit = findViewById(R.id.createMotorEdit);
        createTransmisionEdit = findViewById(R.id.createTransmisionEdit);
    }
    public void Crear(View view){
        String url = "https://api.autoselect.online/vehiculos";
        try{
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);;
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        Toast.makeText(VehiculoCreateActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent misVehiculos = new Intent(VehiculoCreateActivity.this, MisVehiculosActivity.class);
                    startActivity(misVehiculos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(VehiculoCreateActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    params.put("condicion","USADO");
                    params.put("marca", createMarcaEdit.getText().toString());
                    params.put("linea", createLineaEdit.getText().toString());
                    params.put("modelo", createModeloEdit.getText().toString());
                    params.put("motor", createMotorEdit.getText().toString());
                    params.put("transmision", createTransmisionEdit.getText().toString());
                    params.put("vendedor", user_id);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("file[]", new DataPart("autoselect.jpeg", AppHelper.getFileDataFromDrawable(getBaseContext(), createImageView.getDrawable()), "image/jpeg"));
                    //params.put("file[]", new DataPart("autoselect.jpeg", AppHelper.getFileDataFromDrawable(getBaseContext(), imageUpload.getDrawable()), "image/jpeg"));

                    return params;
                }
            };
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
        }catch(Exception E){
            Toast.makeText(this, E.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}