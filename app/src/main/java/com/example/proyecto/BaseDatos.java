package com.example.proyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {

    // Súbelo para que se ejecute onUpgrade y se regenere la BD
    private static final int DB_VERSION = 3;

    public BaseDatos(@Nullable Context context) {
        super(context, "proyecto", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE videojuegos (" +
                "idJuego INTEGER PRIMARY KEY," +
                "titulo TEXT NOT NULL," +
                "plataforma TEXT NOT NULL," +
                "genero TEXT NOT NULL," +
                "estado TEXT NOT NULL," +
                "precio INTEGER NOT NULL," +
                "horas INTEGER NOT NULL," +
                "portada INTEGER NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS videojuegos");
        onCreate(db);
    }
}
