package com.umg.autoselect;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText emailText, pwdText;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailText = findViewById(R.id.emailText);
        pwdText = findViewById(R.id.pwdText);
    }

    //Metodo Boton Registrar
    public void Registrar (View view){
        Intent register = new Intent(this, RegistroActivity.class);
        startActivity(register);
    }
    //Metodo Boton Login
    public void Login (View view){
        checkLogin();
    }

    public void checkLogin(){
        String url = "https://api.autoselect.online/usuarios";

        Map<String, String> params = new HashMap();
        params.put("action", "LOGIN");
        params.put("email", emailText.getText().toString());
        params.put("pwd", pwdText.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("usuario");
                    if(jsonArray.length()<1){
                        Toast.makeText(MainActivity.this, "Credenciales Invalidos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject usuario = jsonArray.getJSONObject(i);
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USER_ID, String.valueOf(usuario.getInt("UserID")));
                        editor.putString(USER_NAME, usuario.getString("nombre"));
                        editor.apply();
                    }
                    Intent login = new Intent(MainActivity.this, PrincipalActivity.class);
                    startActivity(login);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Ocurrio Un Error", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }
}