package com.ricardopazdemiquel.moviles;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ricardopazdemiquel.moviles.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clienteHTTP.HttpConnection;
import clienteHTTP.MethodType;
import clienteHTTP.StandarRequestConfiguration;
import utiles.Token;


public class Iniciar_cuenta_fb_Activity extends AppCompatActivity implements View.OnClickListener{


    private EditText edit_nombre;
    private EditText edit_apellidoP;
    private EditText edit_telefono;
    private EditText edit_correo;
    private com.mikhaellopez.circularimageview.CircularImageView img_photo;
    private RadioButton radio_hombre;
    private RadioButton radio_mujer;
    private Button btn_siguiente;
    private String sexo;
    private String id;

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_iniciar_cuenta_fb);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_nombre = findViewById(R.id.edit_nombre);
        edit_apellidoP= findViewById(R.id.edit_apellidoP);
        edit_telefono = findViewById(R.id.edit_telefono);
        radio_hombre = findViewById(R.id.radioHombre);

        radio_mujer = findViewById(R.id.radioMujer);
        edit_correo = findViewById(R.id.edit_correo);
        img_photo= findViewById(R.id.img_photo);
        btn_siguiente = findViewById(R.id.btn_siguiente);

        btn_siguiente.setOnClickListener(this);

        Radio();

        Intent it = getIntent();
        if (it != null) {
            try {
                JSONObject obj = new JSONObject(it.getStringExtra("usr_face"));
                if(obj.has("id")){
                    id = obj.getString("id");
                }
                    String url="https://graph.facebook.com/" + id+ "/picture?type=large";
                    new AsyncTaskLoadImage(img_photo).execute(url);
//                    Bitmap bm = getFacebookProfilePicture(id);
//                        img_photo.setImageBitmap(bm);
                String nombre1="";
                String nombre2="";
                if(obj.has("first_name")){
                     nombre1 = obj.getString("first_name");
                }
                if(obj.has("middle_mane")){
                     nombre2 = obj.getString("middle_mane");
                }
                edit_nombre.setText(nombre1+" "+nombre2);
                if(obj.has("last_name")){
                    edit_apellidoP.setText(obj.getString("last_name"));
                }
                if(obj.has("email")){
                    edit_correo.setText(obj.getString("email"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void Radio() {
        radio_hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_hombre.isChecked() == true) {
                    setSexo("Hombre");
                    radio_mujer.setChecked(false);
                    //radioButton_trabajar.setError(null);
                    //radioButton_trabajar.setChecked(false);
                }
            }
        });
        radio_mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_mujer.isChecked() == true) {
                    setSexo("Mujer");
                    radio_hombre.setChecked(false);
                    //radioButton_contrato.setError(null);
                    //radioButton_contrato.setChecked(false);
                }
            }
        });
    }
    // Opcion para ir atras sin reiniciar el la actividad anterior de nuevo
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_siguiente:
                Guardar();
                break;
        }
    }

    public static boolean validarEmailSimple(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void Guardar() {
        String nombreV = edit_nombre.getText().toString().trim();
        String apellidoV = edit_apellidoP.getText().toString().trim();
        String correoV = edit_correo.getText().toString().trim();
        String telefonoV = edit_telefono.getText().toString().trim();

        boolean isValid = true;

        if (nombreV.isEmpty()) {
            edit_nombre.setError("campo obligatorio");
            isValid = false;
        }
        if (apellidoV.isEmpty()) {
            edit_apellidoP.setError("campo obligatorio");
            isValid = false;
        }
        if (correoV.isEmpty()) {
            edit_correo.setError("campo obligatorio");
            isValid = false;
        }else if (validarEmailSimple(correoV) == false) {
            edit_correo.setError("email no valido");
            isValid = false;
        }if(getSexo().isEmpty()){
            Toast.makeText(Iniciar_cuenta_fb_Activity.this,"Elija su sexo.",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (!isValid) {
            return;
        }
        new Registrar(id , nombreV , apellidoV , correoV ,telefonoV , getSexo()).execute();
    }

    private class Registrar extends AsyncTask<Void, String, String> {
        private ProgressDialog progreso;
        private String  id , nombre ,  apellidos, correo , telefono ,  sexo ;
        public Registrar(String id ,String nombre ,String apellidos , String correo , String telefono , String sexo) {
            this.id= id;
            this.nombre= nombre;
            this.apellidos = apellidos;
            this.correo = correo;
            this.telefono = telefono;
            this.sexo = sexo;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso = new ProgressDialog(Iniciar_cuenta_fb_Activity.this);
            progreso.setIndeterminate(true);
            progreso.setTitle("Esperando Respuesta");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Hashtable<String,String> param = new Hashtable<>();
            param.put("evento","registrar_usuario_face");
            param.put("id",id);
            param.put("nombre",nombre);
            param.put("apellidos",apellidos);
            param.put("correo",correo);
            param.put("telefonos", telefono);
            param.put("Sexo",sexo);
            param.put("token", Token.currentToken);
            String respuesta = HttpConnection.sendRequest(new StandarRequestConfiguration(getString(R.string.url_servlet_admin), MethodType.POST, param));
            return respuesta;
        }

        @Override
        protected void onPostExecute(String Cliente) {
            super.onPostExecute(Cliente);
            progreso.dismiss();
            if(Cliente==null){
                Toast.makeText(Iniciar_cuenta_fb_Activity.this,"Error al conectarse con el servidor.",Toast.LENGTH_SHORT).show();
            }else{
                if (Cliente.equals("falso")) {
                    Toast.makeText(Iniciar_cuenta_fb_Activity.this,"Error al obtner datos.",Toast.LENGTH_SHORT).show();
                }else{
                    try {

                        JSONObject objs = new JSONObject(Cliente);
                        if(objs.getString("exito").equals("si")) {
                            SharedPreferences preferencias = getSharedPreferences("myPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("usr_log", objs.toString());
                            editor.commit();

                            SharedPreferences preferencias2 = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = preferencias2.edit();
                            JSONArray arrayAe =new JSONArray();
                            JSONObject objAe = new JSONObject();
                            objAe.put("nombre_favorito" , "Aeropuerto");
                            Double lat = -17.6481;
                            Double lng = -63.1404;
                            objAe.put("latFin" , lat);
                            objAe.put("lngFin" , lng);
                            arrayAe.put(objAe);
                            editor2.putString("lista_favoritos", arrayAe.toString());
                            editor2.commit();

                            Intent intent = new Intent(Iniciar_cuenta_fb_Activity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Iniciar_cuenta_fb_Activity.this,"Error al registrar usuario..",Toast.LENGTH_SHORT).show();

                        }

                    SharedPreferences preferencias2 = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = preferencias2.edit();
                    JSONArray array =new JSONArray();
                    JSONObject obj = new JSONObject();
                    obj.put("nombre_favorito" , "Aeropuerto");
                    Double lat = -17.6481;
                    Double lng = -63.1404;
                    obj.put("latFin" , lat);
                    obj.put("lngFin" , lng);
                    array.put(obj);
                    editor2.putString("lista_favoritos", array.toString());
                    editor2.commit();

                    Intent inte = new Intent(Iniciar_cuenta_fb_Activity.this,MainActivity.class);
                    inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte);
                    finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;
        public AsyncTaskLoadImage(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}

