package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CategoriaRest {
    public static void crearCategoria (Categoria c) {
        // variables de conexion y stream de escritura y lectura
        HttpURLConnection urlConnection = null;
        DataOutputStream printout = null;
        InputStream in = null;

        try {
            // crear el objecto json que representa una categoria
            JSONObject categoriaJson = new JSONObject();
            categoriaJson.put("nombre", c.getNombre());

            // Abrir una conexión al servidor para enviar un POST
            URL url = new URL("http://192.168.42.57:1234/categorias");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            // obtener el outputStream para escribir el JSON
            printout = new DataOutputStream(urlConnection.getOutputStream());
            String str = categoriaJson.toString();
            byte[] jsonData = str.getBytes("UTF-8");
            printout.write(jsonData);
            printout.flush();

            // leer la respuesta
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            int data = isw.read();

            // analizar el código de la respuesta
            if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }

                Log.d("LAB_04", sb.toString());
            } else {
                Log.e("LAB_04", "No se pudo conectar con el servidor");
            }
        } catch (JSONException e) {
            Log.e("LAB_04", String.format("%s: Problemas al agregar valores al objecto JSON", e.toString()));

        } catch (MalformedURLException e) {
            Log.e("LAB_04", String.format("%s: URL del servidor malformada", e.toString()));

        } catch (IOException e) {
            Log.e("LAB_04", String.format("%s: Error al tratar de abrir conexion con el servidor", e.toString()));

        } finally {
            if (printout != null) {
                try {
                    printout.close();
                } catch(IOException e) {
                    Log.e("LAB_04", String.format("%s: Error al cerrar DataOutputStream", e.toString()));
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch(IOException e) {
                    Log.e("LAB_04", String.format("%s: Error al cerrar DataInputStream", e.toString()));
                }
            }

            if (urlConnection != null) urlConnection.disconnect();
        }
    }
}
