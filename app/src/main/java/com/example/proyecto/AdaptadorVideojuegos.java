package com.example.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdaptadorVideojuegos extends BaseAdapter {

    private final Context context;
    private final List<Videojuego> lista;

    public AdaptadorVideojuegos(Context context, List<Videojuego> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).idJuego;
    }

    // Por ahorita: mapeo simple (portada 1-5 -> misma imagen temporal)
    private int portadaToDrawable(int portada) {

        switch (portada) {
            case 1: return R.drawable.p1;
            case 2: return R.drawable.p2;
            case 3: return R.drawable.p3;
            case 4: return R.drawable.p4;
            case 5: return R.drawable.p5;
            default: return R.drawable.ic_launcher_foreground;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_videojuego, parent, false);
        }

        ImageView img = v.findViewById(R.id.imgPortada);
        TextView tvTitulo = v.findViewById(R.id.tvTitulo);
        TextView tvInfo = v.findViewById(R.id.tvInfo);

        Videojuego juego = lista.get(position);

        img.setImageResource(portadaToDrawable(juego.portada));
        tvTitulo.setText(juego.titulo);
        tvInfo.setText(juego.plataforma + " • " + juego.estado);

        return v;
    }
}
