package com.example.proyecto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentoDetalleVideojuego extends Fragment {

    private ImageView imgPortadaDetalle;
    private TextView tvTituloDetalle, tvInfoDetalle, tvPrecioHoras, tvIdJuego;
    private Button btnEliminar, btnEditar;
    private String tituloActual, plataformaActual, generoActual, estadoActual;
    private int precioActual, horasActual;


    private int idJuegoRecibido = -1;
    private int portadaActual = 1; // para pintar la imagen

    public FragmentoDetalleVideojuego() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_detalle_videojuego, container, false);

        imgPortadaDetalle = v.findViewById(R.id.imgPortadaDetalle);
        tvTituloDetalle = v.findViewById(R.id.tvTituloDetalle);
        tvInfoDetalle = v.findViewById(R.id.tvInfoDetalle);
        tvPrecioHoras = v.findViewById(R.id.tvPrecioHoras);
        tvIdJuego = v.findViewById(R.id.tvIdJuego);



        btnEliminar = v.findViewById(R.id.btnEliminar);
        btnEditar = v.findViewById(R.id.btnEditar);

        // Leer el ID que mandamos desde la lista
        if (getArguments() != null) {
            idJuegoRecibido = getArguments().getInt("idJuego", -1);
        }

        if (idJuegoRecibido == -1) {
            Toast.makeText(getContext(), "No se recibió el ID del juego", Toast.LENGTH_SHORT).show();
        } else {
            cargarDetalle(idJuegoRecibido);
        }

        btnEliminar.setOnClickListener(view -> eliminarJuego());

        btnEditar.setOnClickListener(view -> {
            FragmentoRegistro frag = new FragmentoRegistro();
            Bundle b = new Bundle();

            b.putInt("idJuego", idJuegoRecibido);
            b.putString("titulo", tituloActual);
            b.putString("plataforma", plataformaActual);
            b.putString("genero", generoActual);
            b.putString("estado", estadoActual);
            b.putInt("precio", precioActual);
            b.putInt("horas", horasActual);
            b.putInt("portada", portadaActual);

            frag.setArguments(b);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, frag)
                    .addToBackStack(null)
                    .commit();
        });


        return v;
    }

    private void cargarDetalle(int idJuego) {
        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM videojuegos WHERE idJuego = ?",
                new String[]{String.valueOf(idJuego)});

        if (c.moveToFirst()) {

            // Guardamos en variables globales (para Editar)
            tituloActual = c.getString(c.getColumnIndexOrThrow("titulo"));
            plataformaActual = c.getString(c.getColumnIndexOrThrow("plataforma"));
            generoActual = c.getString(c.getColumnIndexOrThrow("genero"));
            estadoActual = c.getString(c.getColumnIndexOrThrow("estado"));
            precioActual = c.getInt(c.getColumnIndexOrThrow("precio"));
            horasActual = c.getInt(c.getColumnIndexOrThrow("horas"));
            portadaActual = c.getInt(c.getColumnIndexOrThrow("portada"));

            // Pintamos UI
            tvTituloDetalle.setText(tituloActual);
            tvInfoDetalle.setText(plataformaActual + " • " + generoActual + " • " + estadoActual);
            tvPrecioHoras.setText("Precio: $" + precioActual + " • Horas: " + horasActual);
            tvIdJuego.setText("ID: " + idJuego);
            imgPortadaDetalle.setImageResource(portadaToDrawable(portadaActual));

        } else {
            Toast.makeText(getContext(), "No se encontró el juego", Toast.LENGTH_SHORT).show();
        }

        c.close();
        db.close();
    }


    private void eliminarJuego() {
        if (idJuegoRecibido == -1) return;

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getWritableDatabase();

        int res = db.delete("videojuegos", "idJuego = ?", new String[]{String.valueOf(idJuegoRecibido)});
        db.close();

        if (res > 0) {
            Toast.makeText(getContext(), "Juego eliminado", Toast.LENGTH_SHORT).show();

            // Volver a la lista
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new FragmentoVideojuegos())
                    .commit();

        } else {
            Toast.makeText(getContext(), "No se pudo eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    private int portadaToDrawable(int portada) {
        switch (portada) {
            case 1: return R.drawable.p1;
            case 2: return R.drawable.p2;
            case 3: return R.drawable.p3;
            case 4: return R.drawable.p4;
            case 5: return R.drawable.p5;
            default: return R.drawable.p1;
        }
    }
}
