package com.chinhnc.covid_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.chinhnc.covid_app.API.ApiService;
import com.chinhnc.covid_app.Modul.Covid;
import com.google.android.material.navigation.NavigationView;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.service.AccountAuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    TextView text_canhiem, text_tuvong, text_hoiphuc, text_dieutri, textViewacc;
    ConstraintLayout layout1;
    Button suckhoe, yte, vietnam, thegioi;

    String check = "0";

    private AccountAuthService mAuthManager;
    private AccountAuthParams mAuthParam;
    String displayname_check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhxa();
        actionToolBar();

        //khi click vo list se doi mau
        navigationView.bringToFront();
        // hieejn tool bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_toolbar, R.string.close_toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //đặt sau syncstate mới hd.-> set icon toolbar moi co thể thay đôi đc icon và màu
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_menu);

        //khai báo sự kiện onclick từng item menu ->public boolean onNavigationItemSelected
        navigationView.setNavigationItemSelectedListener(this);


//        //chon mau thanh trang thai
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.bar_1));
//        }
//        // trong suot thanh trang thai
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        //API

        callApi(0);
        vietnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(0);
                check = "0";
            }
        });
        thegioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(1);
                check = "1";
            }
        });

        //click xem biểu đồ
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Chartcovid.class);
                intent.putExtra("check_input", check);
                startActivity(intent);
            }
        });
        //click xem chi tiet suc khoe
        suckhoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PreventionCovidActivity.class);
                startActivity(intent);
            }
        });

        yte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        //Toast.makeText(HomeActivity.this,"Phát Triển Bởi: Vương Quang Huy - 19A02",Toast.LENGTH_LONG).show();

        dulieuinten();

    }

    //lay gia tri tu api
    private void callApi(int i) {
        if (i == 0) {
            ApiService.apiService.convertapitovn("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    //ánh xạ
                    Covid covid = response.body();
                    if (covid != null) {


                        text_canhiem.setText(covid.getCases() + "");
                        text_dieutri.setText(covid.getActive() + "");
                        text_hoiphuc.setText(covid.getRecovered() + "");
                        text_tuvong.setText(covid.getDeaths() + "");
                        Toast.makeText(HomeActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Lỗi Lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ApiService.apiService.convertapitoall("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    Covid covid = response.body();
                    if (covid != null) {
                        text_canhiem.setText(covid.getCases() + "");
                        text_dieutri.setText(covid.getActive() + "");
                        text_hoiphuc.setText(covid.getRecovered() + "");
                        text_tuvong.setText(covid.getDeaths() + "");
                        Toast.makeText(HomeActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {

                }
            });
        }


    }


    //ham su kien thi an nut tro ve
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // khoi tao thu vien ho tro toolbar va  onclick
    private void actionToolBar() {
        // taoj thu vien ho tro
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set icon
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_menu);
        //toolbar.getNavigationIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

        //bat su kien onclick
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        text_canhiem = (TextView) findViewById(R.id.textView3);
        text_dieutri = (TextView) findViewById(R.id.textView12);
        text_hoiphuc = (TextView) findViewById(R.id.textView15);
        text_tuvong = (TextView) findViewById(R.id.textView9);
        layout1 = (ConstraintLayout) findViewById(R.id.lout_1);
        suckhoe = (Button) findViewById(R.id.buttonsuckhoe);
        yte = (Button) findViewById(R.id.buttonyte);
        vietnam = (Button) findViewById(R.id.vietnam_api);
        thegioi = (Button) findViewById(R.id.thegioi_api);
        textViewacc = (TextView) findViewById(R.id.textViewacc);
//        listView = (ListView) findViewById(R.id.listview_menu);
    }

    // hàm sử lí sự kiện khi click vào menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home_main) {
            //check
            if(displayname_check != null){
                logout();
            }
            else {
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }


        } else if (id == R.id.mask_main) {
            //Toast.makeText(HomeActivity.this,"mask click",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, PreventionCovidActivity.class);
            startActivity(intent);
        } else if (id == R.id.tintuc_main) {
            //Toast.makeText(HomeActivity.this,"mask click",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(HomeActivity.this, InformationRssActivity.class);
            Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
            startActivity(intent);

        } else if (id == R.id.map_main) {
            //Toast.makeText(HomeActivity.this,"mask click",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, MapActivity.class);
            startActivity(intent);
        }else if (id == R.id.sp_ins) {
            Toast.makeText(HomeActivity.this, "ins click", Toast.LENGTH_LONG).show();
            GoToURL("https://www.instagram.com/vqh.2602");
        } else if (id == R.id.sp_fb) {
            Toast.makeText(HomeActivity.this, "fb click", Toast.LENGTH_LONG).show();
            GoToURL("https://www.facebook.com/VQHuy2602");
        } else if (id == R.id.sp_mail) {
            Toast.makeText(HomeActivity.this, "mail click", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:vqh2602@gmail.com");
            intent.setData(data);
            startActivity(intent);
        }


        return true;
    }

    //open mang xa hoi
    private void GoToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //check du lieu intent
    private void dulieuinten() {
        Intent intent = getIntent();
        String displayname = intent.getStringExtra("Displayname");
        displayname_check = displayname;
        //check
        if(displayname_check != null) {
            Toast.makeText(HomeActivity.this,"Xin Chào: " + displayname, Toast.LENGTH_LONG).show();
            textViewacc.setText("Xin chào: " + displayname_check);
        }
        else {
            Toast.makeText(HomeActivity.this,"Bạn chưa đăng nhập ", Toast.LENGTH_LONG).show();
        }


    }

    //logout
    private void logout(){
        // tạo servier
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = AccountAuthManager.getService(HomeActivity.this, mAuthParam);
        // đăng xuất
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                //Processing after the sign-out.
                Log.i("MainActivitylogout", "signOut complete");
                Toast.makeText(HomeActivity.this,"Đã Đăng Xuất", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}