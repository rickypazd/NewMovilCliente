package com.ricardopazdemiquel.moviles.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ricardopazdemiquel.moviles.EsperandoConductor;
import com.ricardopazdemiquel.moviles.Inicio_viaje_togo;
import com.ricardopazdemiquel.moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edson on 02/12/2017.
 */

@SuppressLint("ValidFragment")
public class Info_Viajes_Dialog extends DialogFragment implements View.OnClickListener {

    private Button btn_cancelar;
    private Button btn_confirmar_cancelacion;
    private TextView text_mesaje;

    public static String APP_TAG = "registro";

    private static final String TAG = Info_Viajes_Dialog.class.getSimpleName();
    private int tipo_siete;

    private static final int SIETE_ESTANDAR = 1;
    private static final int SUPER_SIETE = 2;
    private static final int SIETE_MARAVILLA = 3;
    private static final int SIETE_TOGO = 4;

    @SuppressLint("ValidFragment")
    public Info_Viajes_Dialog(int tipo) {
        this.tipo_siete = tipo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createLoginDialogo();
    }

    public AlertDialog createLoginDialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmanetstyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_info_viaje, null);
        builder.setView(v);

        btn_confirmar_cancelacion = v.findViewById(R.id.btn_cancelar);
        btn_cancelar = v.findViewById(R.id.btn_confirmar_cancelacion);
        text_mesaje = v.findViewById(R.id.text_mensaje);

        btn_cancelar.setOnClickListener(this);
        btn_confirmar_cancelacion.setOnClickListener(this);

        Informacion_siete();

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancelar:

                dismiss();
                break;
            case R.id.btn_confirmar_cancelacion:
                dismiss();
                break;
        }
    }


    private void Informacion_siete() {
        switch (tipo_siete) {
            case SIETE_ESTANDAR:
                text_mesaje.setText("7 viajes rápidos y econimicos, donde podras realiazar pedidos de nuestras diferentes categorias: viaje estandar, camionetas, 4x4 , vehiculos de 3 filas.");
                break;
            case SUPER_SIETE:
                text_mesaje.setText("Conductor designado, ustedes preocupense de la diversión super siete se encarga de lo demas.");
                break;
            case SIETE_MARAVILLA:
                text_mesaje.setText("Siete maravilla , estar juntas y seguras es una maravilla, una chofer altamente capacitada sera responsable de llevarte a tu destino.");
                break;
            case SIETE_TOGO:
                text_mesaje.setText("Siete TO GO, delivery y encomiendas las 24 horas los 7 dias de la semana.");
                break;
        }
    }

}


