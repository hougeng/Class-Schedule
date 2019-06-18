package com.example.school;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.school.adapter.CourseAdapter;
import com.example.school.bean.CourseBean;
import com.example.school.dao.BaseInfoDao;
import com.example.school.dao.CourseDao;
import com.example.school.event.UpdateDataEvent;
import com.flyco.systembar.SystemBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.couse_list)
    RecyclerView couseList;
    @Bind(R.id.tv_null_course)
    TextView tvNullCourse;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navView;

    private Toolbar toolbar;
    private Context mContext = this;
    private DrawerLayout drawer;
    private String stuXH;
    private String stuName;
    private TextView header_xh;
    private TextView header_name;
    private BaseInfoDao baseInfoDao;
    private CourseDao courseDao;
    private CourseAdapter adapter;
    private List<CourseBean> allCourse;
    private static List<CourseBean> startList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_gallery);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        int color = getResources().getColor(R.color.colorPrimary);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            SystemBarHelper.tintStatusBar(this, color);
        SystemBarHelper.tintStatusBarForDrawer(this, drawerLayout, color);
//        EventBus.getDefault().register(this);


//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        View view = navigationView.getHeaderView(0);
//        header_xh = (TextView) findViewById(R.id.header_xh);
//        header_name = (TextView) findViewById(R.id.header_name);
        initView();
        initData();
        initUI();
    }

    @Subscribe
    public void onEvent(UpdateDataEvent event) {
        initData();
        initView();
        initUI();
    }
    /*
    布局
     */
    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        header_xh = (TextView) findViewById(R.id.header_xh);
        header_name = (TextView) findViewById(R.id.header_name);
    }
    /*
    从数据库中获取数据
     */
    private void initData() {
        baseInfoDao = new BaseInfoDao(mContext);
        courseDao = new CourseDao(mContext);
        Map<String, String> infosMap = baseInfoDao.queryAll();
        stuXH = infosMap.get("stuXH");
        stuName = infosMap.get("stuName");
        allCourse = courseDao.queryAll();
        initDay();
    }

    /*
    初始化课程表数据
     */

    public void initDay() {
        Calendar calendar = Calendar.getInstance();
        int flag = calendar.get(Calendar.DAY_OF_WEEK);
        startList = new ArrayList<>();
        if (flag == 1) {
            flag = 7;
        } else
            flag = flag - 1;
        for (int j = flag; j <= 7; j++) {
            List<CourseBean> list = courseDao.query(j + "");
            for (CourseBean item : list) {
                startList.add(item);
            }
        }
        for (CourseBean course : allCourse) {
            startList.add(course);
        }
    }

    private void initUI() {
//            header_name.setText("stuName");
 //           header_xh.setText("stuXH");

        if (allCourse.size() == 0) {
            tvNullCourse.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        couseList.setLayoutManager(manager);
        adapter = new CourseAdapter(startList);
        couseList.setAdapter(adapter);
//        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                int id = startList.get(position).getId();
//                Intent intent = new Intent(mContext, CourseEditActivity.class);
//                intent.putExtra("id", id + "");
//                startActivity(intent);
//            }
//        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Button button1=(Button)this.findViewById(R.id.login);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,com.example.school.login.LoginActivity.class);
                startActivity(intent);
            }
        });
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.user_counter) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,com.example.school.counter.counterActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
