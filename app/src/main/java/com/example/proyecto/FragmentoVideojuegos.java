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
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentoVideojuegos extends Fragment {

    private ListView lvVideojuegos;
    private ArrayList<Videojuego> lista;

    public FragmentoVideojuegos() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_videojuegos, container, false);

        lvVideojuegos = v.findViewById(R.id.lvVideojuegos);
        lista = new ArrayList<>();

        cargarDesdeBD();

        AdaptadorVideojuegos adapter = new AdaptadorVideojuegos(getContext(), lista);
        lvVideojuegos.setAdapter(adapter);

        lvVideojuegos.setOnItemClickListener((parent, view, position, id) -> {
            Videojuego juego = lista.get(position);

            // aquí vamos a abrir el detalle con el id
            FragmentoDetalleVideojuego frag = new FragmentoDetalleVideojuego();
            Bundle b = new Bundle();
            b.putInt("idJuego", juego.idJuego);
            frag.setArguments(b);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, frag)
                    .addToBackStack(null)
                    .commit();
        });


        return v;
    }

    private void cargarDesdeBD() {
        lista.clear();

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT idJuego, titulo, plataforma, estado, portada FROM videojuegos ORDER BY titulo",
                null
        );

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("idJuego"));
                String titulo = c.getString(c.getColumnIndexOrThrow("titulo"));
                String plataforma = c.getString(c.getColumnIndexOrThrow("plataforma"));
                String estado = c.getString(c.getColumnIndexOrThrow("estado"));
                int portada = c.getInt(c.getColumnIndexOrThrow("portada"));

                lista.add(new Videojuego(id, titulo, plataforma, estado, portada));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
    }
}
