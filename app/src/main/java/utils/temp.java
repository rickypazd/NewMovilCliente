package utils;

import android.content.Context;

import com.ricardopazdemiquel.moviles.Adapter.Adaptador_mis_viajes;

public class temp {
     new asdasd(<PARAMETROS>).execute();
}

public class asdasd extends AsyncTask<Void, String, String>{


    private int id_usuario;
    private double precio;
    private int cant_dormitorios;
    private int cant_banhos;
    private int metros2;
    private String descripcion;
    private double lat;
    private double lng;
    private String direccion;
    private int tipo_public;

    public asdasd(int id_usuario, double precio, int cant_dormitorios, int cant_banhos, int metros2, String descripcion, double lat, double lng, String direccion, int tipo_public) {
        this.id_usuario = id_usuario;
        this.precio = precio;
        this.cant_dormitorios = cant_dormitorios;
        this.cant_banhos = cant_banhos;
        this.metros2 = metros2;
        this.descripcion = descripcion;
        this.lat = lat;
        this.lng = lng;
        this.direccion = direccion;
        this.tipo_public = tipo_public;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "get_carrera_cliente");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("id_usuario", id_usuario+"");
        parametros.put("precio", precio+"");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("tokenAcceso", "servi12sis3");
        parametros.put("id_usr",id+"");

        String respuesta = HttpConnection.sendRequest(new StandarRequestConfiguration(getString(R.string.url_servlet_index), MethodType.POST, parametros));
        return respuesta;
    }
    @Override
    protected void onPostExecute(String resp) {
        super.onPostExecute(resp);

        if(resp==null){
            Log.e(Contexto.APP_TAG, "Hubo un error al conectarse al servidor.");
        }else{
            if(resp.length()<=0){
                Log.e(Contexto.APP_TAG, "Hubo un error al conectarse al servidor.");
            }else {
                try {
                    JSONObject obj = new JSONObject(resp);
                    if(obj.getInt("estado")!=1){
                        Log.e(Contexto.APP_TAG, obj.getString("error"));
                        //show mensaje
                        // Toast, alert, etc  para mostrar error al usuario=  obj.getString("mensaje");
                        //
                    }else{
                        // Toast, alert, etc  para mostrar error al usuario=  obj.getString("mensaje");
                        // TRABAJAR LA RESPUESTA=  JSONObject objresp = new JSONObject(obj.getString("resp"))
                        // TRABAJAR LA RESPUESTA=  JSONArray arrresp = new JSONArray(obj.getString("resp"));
                        btn_nav_formaspago.setText(objresp.getString("nombre"));
                        if(obj.getInt("id")==3){
                            btn_nav_formaspago.setVisivity(View.gone);
                        }

                        //Trabajar lista
                        Adaptador_mis_viajes adapter = new Adaptador_mis_viajes(Context ,arrresp);
                        lv.setadapter(adapter);
                    }
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