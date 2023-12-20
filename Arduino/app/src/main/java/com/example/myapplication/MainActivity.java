package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private TextView canalHumedad;
    private TextView canalLuz;

    private static final String URL_READHumedad = "https://api.thingspeak.com/channels/2377846/fields/2.json?results=2";
    private static final String URL_READLuz = "https://api.thingspeak.com/channels/2377846/fields/3.json?results=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canalHumedad=findViewById(R.id.canalHumedad);
        canalLuz=findViewById(R.id.canalLuz);
        startPeriodicUpdate();
    }

    public void BombaOn(View view) {
        String url = "https://api.thingspeak.com/update?api_key=X90JS4LNJF6XSSTL&field4=1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String response = new String(responseBody);
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void BombaOff(View view) {
        String url = "https://api.thingspeak.com/update?api_key=X90JS4LNJF6XSSTL&field4=0";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String response = new String(responseBody);
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void readJSONhumedad() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_READHumedad, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String response = new String(responseBody);
                    HumedadJSON(response);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void readJSONluz() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_READLuz, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String response = new String(responseBody);
                    LuzJSON(response);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void HumedadJSON(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray feeds = root.getJSONArray("feeds");
            String valor, texto = "";

            for (int i = 0; i < feeds.length(); i++) {
                valor = feeds.getJSONObject(i).getString("field2");
                texto = texto + valor + "\n";
            }
            canalHumedad.setText(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LuzJSON(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray feeds = root.getJSONArray("feeds");
            String valor, texto = "";

            for (int i = 0; i < feeds.length(); i++) {
                valor = feeds.getJSONObject(i).getString("field3");
                texto = texto + valor + "\n";
            }
            canalLuz.setText(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void startPeriodicUpdate() {
        // Crear un temporizador que ejecuta la tarea cada cierto tiempo
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Actualizar la interfaz de usuario en el hilo principal
                runOnUiThread(() -> readJSONhumedad());
                runOnUiThread(() -> readJSONluz());
            }
        }, 0, 1000); // Actualizar cada 5000 milisegundos (5 segundos), ajusta según tus necesidades
    }



}