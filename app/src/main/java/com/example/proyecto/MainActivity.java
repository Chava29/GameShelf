package com.example.proyecto;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout cajonDeNavegacion;
    private NavigationView vistaDeNavegacion;
    private MaterialToolbar barraDeHerramientas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular las vistas
        cajonDeNavegacion = findViewById(R.id.drawer_layout);
        vistaDeNavegacion = findViewById(R.id.nav_view);
        barraDeHerramientas = findViewById(R.id.top_app_bar);

        // Configuración de la barra de herramientas (Toolbar)
        setSupportActionBar(barraDeHerramientas);
        ActionBarDrawerToggle alternador = new ActionBarDrawerToggle(
                this,
                cajonDeNavegacion,
                barraDeHerramientas,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        cajonDeNavegacion.addDrawerListener(alternador);
        alternador.syncState();

        vistaDeNavegacion.setNavigationItemSelectedListener(item -> {

            Fragment fragmento;
            String titulo;
            int idElemento = item.getItemId();

            if (idElemento == R.id.nav_inicio) {
                fragmento = new FragmentoInicio();
                titulo = getString(R.string.menu_inicio);

            } else if (idElemento == R.id.nav_videojuegos) {
                fragmento = new FragmentoVideojuegos();
                titulo = "Videojuegos";

            } else if (idElemento == R.id.nav_registro) {
                fragmento = new FragmentoRegistro();
                titulo = getString(R.string.menu_registro);

            } else {
                fragmento = new FragmentoInicio();
                titulo = getString(R.string.menu_inicio);
            }

            reemplazarFragmento(fragmento, titulo);
            cajonDeNavegacion.closeDrawer(GravityCompat.START);
            return true;
        });


        // Cargar el fragmento por defecto al iniciar la app (FragmentoInicio)
        if (savedInstanceState == null) {
            vistaDeNavegacion.setCheckedItem(R.id.nav_inicio);
            reemplazarFragmento(new FragmentoInicio(), getString(R.string.menu_inicio));
        }
    }

    private void reemplazarFragmento(Fragment fragmento, String titulo) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragmento)
                .commit();
        setTitle(titulo);
    }

    @Override
    public void onBackPressed() {
        if (cajonDeNavegacion.isDrawerOpen(GravityCompat.START)) {
            cajonDeNavegacion.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
