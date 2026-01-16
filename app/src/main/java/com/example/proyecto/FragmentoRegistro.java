package com.example.proyecto;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentoRegistro extends Fragment {

    private EditText etIdJuego, etTitulo, etPlataforma, etGenero, etEstado, etPrecio, etHoras, etPortada;
    private Button btnAlta, btnBaja, btnCambio, btnConsulta;
    private TextView tvResultado;

    public FragmentoRegistro() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_registro, container, false);

        // Inputs
        etIdJuego = v.findViewById(R.id.etIdJuego);
        etTitulo = v.findViewById(R.id.etTitulo);
        etPlataforma = v.findViewById(R.id.etPlataforma);
        etGenero = v.findViewById(R.id.etGenero);
        etEstado = v.findViewById(R.id.etEstado);
        etPrecio = v.findViewById(R.id.etPrecio);
        etHoras = v.findViewById(R.id.etHoras);
        etPortada = v.findViewById(R.id.etPortada);

        // Botones
        btnAlta = v.findViewById(R.id.btnAlta);
        btnBaja = v.findViewById(R.id.btnBaja);
        btnCambio = v.findViewById(R.id.btnCambio);
        btnConsulta = v.findViewById(R.id.btnConsulta);

        // Resultado
        tvResultado = v.findViewById(R.id.tvResultado);

        // Si viene desde Detalle para editar, llenamos los campos
        if (getArguments() != null && getArguments().containsKey("idJuego")) {

            etIdJuego.setText(String.valueOf(getArguments().getInt("idJuego")));
            etTitulo.setText(getArguments().getString("titulo", ""));
            etPlataforma.setText(getArguments().getString("plataforma", ""));
            etGenero.setText(getArguments().getString("genero", ""));
            etEstado.setText(getArguments().getString("estado", ""));
            etPrecio.setText(String.valueOf(getArguments().getInt("precio", 0)));
            etHoras.setText(String.valueOf(getArguments().getInt("horas", 0)));
            etPortada.setText(String.valueOf(getArguments().getInt("portada", 1)));

            // Tip: evita que cambien el ID por accidente
            etIdJuego.setEnabled(false);

            tvResultado.setText("Modo edición: cambia lo que quieras y dale 'Cambio'");
        }


        // Listeners
        btnAlta.setOnClickListener(view -> alta());
        btnBaja.setOnClickListener(view -> baja());
        btnCambio.setOnClickListener(view -> cambio());
        btnConsulta.setOnClickListener(view -> consulta());

        return v;
    }

    // Valida que no falte ningún campo (para alta/cambio)
    private boolean camposVacios() {
        return etIdJuego.getText().toString().trim().isEmpty()
                || etTitulo.getText().toString().trim().isEmpty()
                || etPlataforma.getText().toString().trim().isEmpty()
                || etGenero.getText().toString().trim().isEmpty()
                || etEstado.getText().toString().trim().isEmpty()
                || etPrecio.getText().toString().trim().isEmpty()
                || etHoras.getText().toString().trim().isEmpty()
                || etPortada.getText().toString().trim().isEmpty();
    }

    // ===== ALTA =====
    private void alta() {
        if (camposVacios()) {
            Toast.makeText(getContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int idJuego = Integer.parseInt(etIdJuego.getText().toString().trim());
        int precio = Integer.parseInt(etPrecio.getText().toString().trim());
        int horas = Integer.parseInt(etHoras.getText().toString().trim());
        int portada = Integer.parseInt(etPortada.getText().toString().trim());

        // Validación simple para portada
        if (portada < 1 || portada > 5) {
            Toast.makeText(getContext(), "Portada debe ser un número del 1 al 5", Toast.LENGTH_SHORT).show();
            return;
        }

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getWritableDatabase();

        // Verificamos si el ID ya existe
        Cursor c = db.rawQuery("SELECT idJuego FROM videojuegos WHERE idJuego = ?",
                new String[]{String.valueOf(idJuego)});

        if (c.moveToFirst()) {
            c.close();
            db.close();
            tvResultado.setText("Ya existe un juego con ese ID");
            return;
        }
        c.close();

        ContentValues values = new ContentValues();
        values.put("idJuego", idJuego);
        values.put("titulo", etTitulo.getText().toString().trim());
        values.put("plataforma", etPlataforma.getText().toString().trim());
        values.put("genero", etGenero.getText().toString().trim());
        values.put("estado", etEstado.getText().toString().trim());
        values.put("precio", precio);
        values.put("horas", horas);
        values.put("portada", portada);

        long res = db.insert("videojuegos", null, values);
        db.close();

        if (res != -1) {
            tvResultado.setText("Juego agregado: " + etTitulo.getText().toString().trim());
            limpiarCampos();
        } else {
            tvResultado.setText("Error al insertar");
        }
    }

    // ===== BAJA =====
    private void baja() {
        String idStr = etIdJuego.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(getContext(), "Pon el ID del juego para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getWritableDatabase();

        int res = db.delete("videojuegos", "idJuego = ?", new String[]{idStr});
        db.close();

        if (res > 0) {
            tvResultado.setText("Juego eliminado (ID " + idStr + ")");
            limpiarCampos();
        } else {
            tvResultado.setText("No existe ese ID");
        }
    }

    // ===== CAMBIO (EDITAR) =====
    private void cambio() {
        if (camposVacios()) {
            Toast.makeText(getContext(), "Para editar, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int portada = Integer.parseInt(etPortada.getText().toString().trim());
        if (portada < 1 || portada > 5) {
            Toast.makeText(getContext(), "Portada debe ser un número del 1 al 5", Toast.LENGTH_SHORT).show();
            return;
        }

        String idStr = etIdJuego.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put("titulo", etTitulo.getText().toString().trim());
        values.put("plataforma", etPlataforma.getText().toString().trim());
        values.put("genero", etGenero.getText().toString().trim());
        values.put("estado", etEstado.getText().toString().trim());
        values.put("precio", Integer.parseInt(etPrecio.getText().toString().trim()));
        values.put("horas", Integer.parseInt(etHoras.getText().toString().trim()));
        values.put("portada", portada);

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getWritableDatabase();

        int res = db.update("videojuegos", values, "idJuego = ?", new String[]{idStr});
        db.close();

        if (res > 0) {
            tvResultado.setText("Juego actualizado (ID " + idStr + ")");
            limpiarCampos();
        } else {
            tvResultado.setText("No existe ese ID");
        }
    }

    // ===== CONSULTA =====
    private void consulta() {
        String idStr = etIdJuego.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(getContext(), "Pon el ID del juego para consultar", Toast.LENGTH_SHORT).show();
            return;
        }

        BaseDatos bd = new BaseDatos(getContext());
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM videojuegos WHERE idJuego = ?",
                new String[]{idStr});

        if (c.moveToFirst()) {
            etTitulo.setText(c.getString(c.getColumnIndexOrThrow("titulo")));
            etPlataforma.setText(c.getString(c.getColumnIndexOrThrow("plataforma")));
            etGenero.setText(c.getString(c.getColumnIndexOrThrow("genero")));
            etEstado.setText(c.getString(c.getColumnIndexOrThrow("estado")));
            etPrecio.setText(String.valueOf(c.getInt(c.getColumnIndexOrThrow("precio"))));
            etHoras.setText(String.valueOf(c.getInt(c.getColumnIndexOrThrow("horas"))));
            etPortada.setText(String.valueOf(c.getInt(c.getColumnIndexOrThrow("portada"))));

            tvResultado.setText("Encontrado: " + c.getString(c.getColumnIndexOrThrow("titulo")));
        } else {
            tvResultado.setText("No existe ese ID");
        }

        c.close();
        db.close();
    }

    private void limpiarCampos() {
        etIdJuego.setText("");
        etTitulo.setText("");
        etPlataforma.setText("");
        etGenero.setText("");
        etEstado.setText("");
        etPrecio.setText("");
        etHoras.setText("");
        etPortada.setText("");
        etIdJuego.setEnabled(true);
    }
}
