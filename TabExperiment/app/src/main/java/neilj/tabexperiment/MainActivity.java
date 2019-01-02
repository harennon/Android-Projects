package neilj.tabexperiment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code to inflate toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create instance of the tab layout from the view
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        //set text for each tab
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_label1)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_label2)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_label3)));
        //set tabs to fill entire layout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //use PageAdapter to manage views in fragments
        //each page is represented by its own fragment
        final ViewPager viewPager = findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //Set up a listener for clicks
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
