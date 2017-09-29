package com.example.owner.myconnect4;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    ImageView img1,img2;
    boolean turno_jugador1;
    Animation ani = new AlphaAnimation(1,0);



    String board[][]={  {"B","B","B","B","B","B","B"},
            {"B","B","B","B","B","B","B"},
            {"B","B","B","B","B","B","B"},
            {"B","B","B","B","B","B","B"},
            {"B","B","B","B","B","B","B"},
            {"B","B","B","B","B","B","B"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ani.setDuration(50);
        ani.setInterpolator(new LinearInterpolator());
        ani.setRepeatCount(5);
        ani.setRepeatMode(Animation.REVERSE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int fila, col, resid;

        //Asignar turno del jugador
        turno_jugador1 = true;

        //asignar ImageView que indica turno del jugador
        img1= (ImageView) findViewById(R.id.img1);
        img1.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.circulorojo));

        //Para setear o activar OnclickListener a todos los ImageView dinamicamente
        for(fila=1; fila <=6; fila++) {
            for(col=1;col<=7;col++) {
                //Seteando evento click a la imagen1
                resid = getResources().getIdentifier("img" + fila+col,"id", getPackageName());
                findViewById(resid).setOnClickListener(this);
            }
        }



    }

    private  boolean determinarGanador(int posfila, int poscol){
        int fil, col, f, c, cantidad=0;

        //posicionarnos al inicio de la primera ficha del mismo color en direccion Vertical
        //desde la posicion recorremos  y contamos las fichas iguales
        for(f=posfila; f<6;f++){
            if(board[f][poscol]==board[posfila][poscol])
                cantidad++;
            else
                break;
        }

        //si la cantidad es igual a 4 indicamos ganador
        if(cantidad==4)
            return true;

        //si no ha ganado en forma Vertical reseteamos el valor de cantidad
        cantidad = 0;
        //posicionarnos al inicio de la primera ficha del mismo color en direccion horizontal
        for(col = poscol; col>0; col--){
            if(board[posfila][col] != board[posfila][col-1]){
                break;
            }
        }

        //desde la posicion recorremos  y contamos las fichas iguales
        for(c=col; c<7;c++){
            if(board[posfila][c]==board[posfila][poscol])
                cantidad++;
            else
                break;
        }

        //si la cantidad es igual a 4 indicamos ganador
        if(cantidad==4)
            return true;

        //si no ha ganado en forma Vertical reseteamos el valor de cantidad
        cantidad = 0;

        //posicionarnos en la primera posicion diagonal der-izquierda
        for(f=posfila, c=poscol; (f > 0) && (c>0); f--,c--){
            if(board[f][c]!=board[f-1][c-1])
                break;
        }

        //recorremos la diagonal y contamos
        for(; (f < 6) && (c < 7); f++,c++){
            if(board[f][c]==board[posfila][poscol])
                cantidad++;
            else
                break;
        }

        //si la cantidad es igual a 4 indicamos ganador
        if(cantidad==4)
            return true;

        //si no ha ganado  reseteamos el valor de cantidad
        cantidad = 0;

        //posicionarnos en la primera posicion diagonal izq-der
        for(f=posfila, c=poscol; (f > 0 ) && (c<6); f--,c++){
            if(board[f][c]!=board[f-1][c+1])
                break;
        }

        //recorremos la diagonal y contamos
        for(; (f < 6) && (c >= 0); f++,c--){
            if(board[f][c]==board[posfila][poscol])
                cantidad++;
            else
                break;
        }

        //si la cantidad es igual a 4 indicamos ganador
        if(cantidad==4)
            return true;

        //si no hay ganador retornamos false
        return  false;


    }

    //Funcion para decir quien ganó y obtener respuesta de si desea seguir jugando.
    public void mostrarGanador(int idficha) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.connect_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);

        final ImageView img = (ImageView) dialogView.findViewById(R.id.imgganador);
        img.setImageDrawable(ContextCompat.getDrawable(this, idficha));
        dialogBuilder.setTitle(R.string.app_name);

        dialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent inten = getIntent();
                finish();
                startActivity(inten);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }

        });
        AlertDialog resp = dialogBuilder.create();
        resp.show();
    }

    @Override
    public void onClick(View view) {
        int fila, col, cfila, ccol, resid, idficha;
        boolean  ganador=false;

        String cel, colorganador;
        cel=  view.getTag().toString();
        fila= Integer.parseInt(cel.substring(0,1))-1;
        col= Integer.parseInt(cel.substring(1,2))-1;

        for(cfila=5; cfila>= 0; cfila--){

            if(board[cfila][col]=="B") {
                resid = getResources().getIdentifier("img" + (cfila + 1) + (col + 1), "id", getPackageName());
                img2 = (ImageView) findViewById(resid);

                //si el turno es del jugador entonces asignamos color rojo
                if (turno_jugador1) {
                    board[cfila][col] = "R";
                    img2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circulorojo));
                    img1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circuloamarillo));
                } else {
                    board[cfila][col] = "A";
                    img2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circuloamarillo));
                    img1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circulorojo));
                }

                //hacer animacion
                img2.startAnimation(ani);
                //Verificar si gana
                ganador = determinarGanador(cfila, col);
                //si hay ganador preguntar si desea un nuevo juego
                if (ganador) {
                    if (turno_jugador1)
                        idficha = R.drawable.circulorojo;
                    else
                        idficha = R.drawable.circuloamarillo;
                    //mensaje de ganador
                    mostrarGanador(idficha);
//                    Dialog alerta = new Dialog
//                    alerta.setTitle(R.string.app_name);
//                    alerta.setIcon(R.mipmap.ic_launcher);
//                    alerta.setConte
//                    alerta.setCancelable(true);
//                    alerta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent inten = getIntent();
//                            finish();
//                            startActivity(inten);
//                        }
//                    })
//                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    finish();
//                                }
//                            });
//                    alerta.show();
                    break;

                }

                else {
                    turno_jugador1 = !turno_jugador1;
                    break;
                }

            }

        }

    }

}
