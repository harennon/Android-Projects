package neilj.navdrawerexperiment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Setup DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null){
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    /**
     * Handles the back button: close nav drawer
     */
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null){
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *
     * @param item  Item in the navigation drawer
     * @return      Returns true after closing nav drawer
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //handle nav view clicks here
        switch (item.getItemId()){
            case R.id.nav_camera:
                //Handle camera import action
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_camera));
                return  true;
            case R.id.nav_gallery:
                //Handle gallery actions
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_gallery));
                return true;
            case R.id.nav_manage:
                //Handle manage actions
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_tools));
                return true;
            case R.id.nav_slideshow:
                //Handle slideshow actions
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_slideshow));
                return true;
            case R.id.nav_share:
                //Handle share actions
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_share));
                return true;
            case R.id.nav_send:
                //Handle send actions
                drawer.closeDrawer(GravityCompat.START);
                displayToast(getString(R.string.chose_send));
                return true;
            default: return false;
        }
    }

    /**
     * Displays a toast message.
     * @param message   Message to display in toast
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
