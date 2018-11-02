package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRest {
    private static String url_string = "http://172.16.0.2:1234/categorias";
    //private static String url_string = "http://192.168.42.57:1234/categorias";
    //private static String url_string = "http://192.168.0.13:1234/categorias";

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
            URL url = new URL(url_string);
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

    public List<Categoria> listarTodas() {
        // inicializar variables
        List<Categoria> resultado = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            // gestionar la conexion
            URL url = new URL(url_string);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-type", "application/json");
            urlConnection.setRequestMethod("GET");

            // leer la respuesta
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();

            // verificar el codigo de respuesta
            if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }
                // ver los datos recibidos
                Log.d("LAB_04", sb.toString());

                // transformar respuesta a JSON
                JSONTokener tokener = new JSONTokener(sb.toString());
                JSONArray listaCategorias = (JSONArray) tokener.nextValue();

                // iterar todas las entradas del arreglo
                for (int i = 0; i < listaCategorias.length(); i++) {
                    JSONObject row = listaCategorias.getJSONObject(i);
                    Categoria c = new Categoria(row.getInt("id"), row.getString("nombre"));

                    resultado.add(c);
                }
            }

        } catch (ProtocolException e) {
            Log.e("LAB_04", String.format("%s: Request method inválido", e.toString()));

        } catch (IOException e) {
            Log.e("LAB_04", String.format("%s: Error al abrir la conexión con el servidor", e.toString()));

        } catch (JSONException e) {
            Log.e("LAB_04", String.format("%s: Error tratando de obtener el siguiente valor del objeto JSON", e.toString()));

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("LAB_04", String.format("%s: Error al tratar de cerrar InputStream", e.toString()));
                }
            }

            if (urlConnection != null) urlConnection.disconnect();
        }

        return resultado;
    }
}
