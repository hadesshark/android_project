package helloandroid.textreader4;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton faBtn_up, faBtn_down;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private String bookList_str[];

    private ActionBarDrawerToggle toggle;

    private SharedPreferences config;
    private boolean nightMode;

//    private UiModeManager uiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        recyclerViewSetting();
        FloatingActionButtonSetting();
    }

    private void FloatingActionButtonSetting() {
        faBtn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        faBtn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(recyclerView.getBottom());
            }
        });
    }

    class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

        private List<Book> bookList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvName;
            private CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
            }
        }

        public BookAdapter(List<Book> bookList) {
            this.bookList = bookList;
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reader_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Book book = bookList.get(position);
            holder.tvName.setText(book.getBook_name());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", book.getBook_name());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private void recyclerViewSetting() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Book> bookList = new ArrayList<>();
        try {
            bookList_str = getAssets().list("JsonBookstore");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String book_name : bookList_str) {
            bookList.add(new Book(book_name.substring(0, book_name.indexOf("."))));
        }

        recyclerView.setAdapter(new BookAdapter(bookList));
    }


    private void initView() {

        config = getSharedPreferences("config", Context.MODE_PRIVATE);
        nightMode = config.getBoolean("dayNightMode", false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("小說閱讀器");
        setSupportActionBar(toolbar);

        faBtn_up = (FloatingActionButton) findViewById(R.id.faBtn_up);
        faBtn_down = (FloatingActionButton) findViewById(R.id.faBtn_down);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.setDrawerListener(toggle);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        drawerSetting();
    }

    private void disableNightMode() {
        config.edit().putBoolean("dayNightMode", false).apply();
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    private void enableNightMode() {
        config.edit().putBoolean("dayNightMode", true).apply();
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.taggle:
                drawerSetting();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        return true;
    }

    private void drawerSetting() {
        MenuItem dayNightItem = navigationView.getMenu().findItem(R.id.night_mode);
        SwitchCompat dayNightSwitch = (SwitchCompat) MenuItemCompat.getActionView(dayNightItem).findViewById(R.id.taggle);

        if(nightMode) {
            dayNightSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    enableNightMode();
                } else {
                    disableNightMode();
                }
            }
        });
    }
}
