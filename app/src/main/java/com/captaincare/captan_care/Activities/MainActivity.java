package com.captaincare.captan_care.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.captaincare.captan_care.Fragments.AddCredit.AddCreditFragment;
import com.captaincare.captan_care.Fragments.ChangePasswordFragment;
import com.captaincare.captan_care.Fragments.ContactUsFragment;
import com.captaincare.captan_care.Fragments.CurrentOfferFragment;
import com.captaincare.captan_care.Fragments.FavoriteOfferFragment;
import com.captaincare.captan_care.Fragments.MyRequestFragment;
import com.captaincare.captan_care.Fragments.Notification.NotificationDetailsFragment;
import com.captaincare.captan_care.Fragments.Notification.NotificationFragment;
import com.captaincare.captan_care.Fragments.PaymentFragment;
import com.captaincare.captan_care.Fragments.Profile.EditProfileFragment;
import com.captaincare.captan_care.Fragments.Profile.ProfileFragment;
import com.captaincare.captan_care.Language.LocaleHelpar;
import com.captaincare.captan_care.Fragments.SettingFragment;
import com.captaincare.captan_care.Fragments.HomeFragment;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, txtState, txtRate, txtPoint;
    private CircleImageView imgProfile;

    LinearLayout linearPoint;
    View navigationHeader;
    Menu navigationMenu;
    NavigationView navigationView;

    int menuPosition;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        String Language = Paper.book().read("language");
        if (Language == null) {
            Paper.book().write("language", "en");
            updateLanguage(Paper.book().read("language"));
        } else if (!getResources().getConfiguration().locale.toString().equals(Language)) {
            Paper.book().write("language", Language);
            updateLanguage(Paper.book().read("language"));
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationHeader = navigationView.getHeaderView(0);
        navigationMenu = navigationView.getMenu();

        textName = navigationHeader.findViewById(R.id.txtName);
        txtPoint = navigationHeader.findViewById(R.id.txtPoint);
        txtState = navigationHeader.findViewById(R.id.txtState);
        linearPoint = navigationHeader.findViewById(R.id.linearPoint);
        txtRate = navigationHeader.findViewById(R.id.txtRate);
        imgProfile = navigationHeader.findViewById(R.id.imgPersonal);

        navigationMenu.findItem(R.id.nav_my_offer).setTitle(R.string.requests_history);
        navigationMenu.findItem(R.id.nav_favorite_offers).setTitle(R.string.favorite_offer);
        navigationMenu.findItem(R.id.nav_captain_home).setChecked(true);
        getUserInformation();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }

    private void updateLanguage(String language) {
        Context context = LocaleHelpar.setLocal(MainActivity.this, language);
        Resources resources = context.getResources();

        MainActivity.this.finish();
        startActivity(new Intent(context, MainActivity.class));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getUserInformation();

        int id = menuItem.getItemId();
        if (id == R.id.nav_my_profile) {
            menuPosition = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        } else if (id == R.id.nav_captain_home) {
            menuPosition = 1;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        } else if (id == R.id.nav_my_offer) {
            menuPosition = 2;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyRequestFragment()).commit();
        } else if (id == R.id.nav_notifications) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
            menuPosition = 3;
        } else if (id == R.id.nav_favorite_offers) {
            menuPosition = 4;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FavoriteOfferFragment()).commit();
        } else if (id == R.id.nav_current_offers_captain) {
            menuPosition = 5;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CurrentOfferFragment()).commit();
        } else if (id == R.id.nav_payment) {
            menuPosition = 6;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new PaymentFragment()).commit();
        } else if (id == R.id.nav_payment1) {
            menuPosition = 7;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddCreditFragment()).commit();
        } else if (id == R.id.nav_setting) {
            menuPosition = 8;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
        } else if (id == R.id.nav_contact_us) {
            menuPosition = 9;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ContactUsFragment()).commit();
        } else if (id == R.id.nav_sign_out) {
            menuPosition = 10;
            new AlertDialog.Builder(this)
                    .setTitle(R.string.titel_dialog_log_out)
                    .setIcon(R.drawable.ic_logout)
                    .setPositiveButton(getString(R.string.log_out), (dialog, which) -> {
                        signOutUser();
                    })
                    .setNegativeButton(getString(R.string.Cancel), null)
                    .create().show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SetNavItemChecked(int id) {
        getUserInformation();
        MenuItem menuItem = navigationMenu.getItem(id);
        menuItem.setChecked(true);
    }

    public void setToolbarVisibility(int visibility) {
        navigationView.setVisibility(visibility);
        toolbar.setVisibility(visibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUserInformation();

    }

    private void signOutUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SignOutTokenCaptain,
                response -> {
                    try {
                        ReadJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    NotFoundResponseFromServer();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(MainActivity.this).getKEYID());
                return params;
            }
        };
        RequestHand.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    private void ReadJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString("error").equals("false")) {

            ShareProfileData.getInstance(MainActivity.this).Logout();
            ShareProfileData.getInstance(MainActivity.this).LogoutPROFILE();
            ShareProfileData.getInstance(MainActivity.this).LogoutSTATE();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_LONG).show();
        }
    }

    private void NotFoundResponseFromServer() {
        Toast.makeText(MainActivity.this, "Not Conection", Toast.LENGTH_LONG).show();
    }

    private void getUserInformation() {
        if (ShareProfileData.getInstance(this).getKeyprofile_URL() != null) {
            Glide.with(Objects.requireNonNull(this))
                    .load(ShareProfileData.getInstance(this).getKeyprofile_URL())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_profile_default).into(imgProfile);

        }
        textName.setText(ShareProfileData.getInstance(this).getKeyName());

        if (ShareProfileData.getInstance(this).getKeyRate().equals("null")) {
            txtRate.setText("5");
        } else {
            txtRate.setText(ShareProfileData.getInstance(this).getKeyRate());

        }
        if (ShareProfileData.getInstance(this).ISLOGIN()) {
            txtState.setText(getString(R.string.online));
        } else {
            txtState.setText(getString(R.string.offline));
        }

        txtPoint.setText(ShareProfileData.getInstance(this).getKeyPoint());
    }

    @Override
    public void onBackPressed() {
        getUserInformation();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof HomeFragment) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            while (count > 0) {
                getSupportFragmentManager().popBackStack();
                count--;
            }
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(R.string.exit_app);
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                finish();
            }).setNegativeButton(R.string.cancel, null)
                    .show();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof NotificationDetailsFragment ||
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof EditProfileFragment ||
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof ChangePasswordFragment) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }
    }

}
