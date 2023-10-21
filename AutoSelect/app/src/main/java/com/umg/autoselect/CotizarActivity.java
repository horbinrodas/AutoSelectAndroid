package com.umg.autoselect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CotizarActivity extends AppCompatActivity {
    EditText cotizarNombreEdit, cotizarEmailEdit, cotizarMarcaEdit, cotizarLineaEdit, cotizarModeloEdit, cotizarMensajeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizar);
        cotizarNombreEdit = findViewById(R.id.cotizarNombreEdit);
        cotizarEmailEdit = findViewById(R.id.cotizarEmailEdit);
        cotizarMarcaEdit = findViewById(R.id.cotizarMarcaEdit);
        cotizarLineaEdit = findViewById(R.id.cotizarLineaEdit);
        cotizarModeloEdit = findViewById(R.id.cotizarModeloEdit);
        cotizarMensajeEdit = findViewById(R.id.cotizarMensajeEdit);
    }
    public void enviarCotizacion(View view){
        String url = "https://api.autoselect.online/contact";

        Map<String, String> params = new HashMap();
        params.put("nombre", cotizarNombreEdit.getText().toString());
        params.put("email", cotizarEmailEdit.getText().toString());
        params.put("marca", cotizarMarcaEdit.getText().toString());
        params.put("linea", cotizarLineaEdit.getText().toString());
        params.put("modelo", cotizarModeloEdit.getText().toString());
        params.put("mensaje", cotizarMensajeEdit.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getString("message").equals("Correo Enviado")){
                        Toast.makeText(CotizarActivity.this, "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(CotizarActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                    clearEdits();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CotizarActivity.this, "Ocurrio Un Error", Toast.LENGTH_SHORT).show();
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void clearEdits() {
        cotizarNombreEdit.setText("");
        cotizarEmailEdit.setText("");
        cotizarMarcaEdit.setText("");
        cotizarLineaEdit.setText("");
        cotizarModeloEdit.setText("");
        cotizarMensajeEdit.setText("");
    }
}