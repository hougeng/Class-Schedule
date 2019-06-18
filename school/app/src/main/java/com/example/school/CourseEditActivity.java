//package com.example.school;
//
//import android.content.Context;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//
//import com.example.school.dao.CourseDao;
//
//import butterknife.Bind;
//
//public class CourseEditActivity extends AppCompatActivity {
//
//    @Bind(R.id.toolbar_layout)
//    CollapsingToolbarLayout toolbarLayout;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;
//    private Context mContext = this;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
//    private FragmentManager fragmentManager;
//    private boolean isEditor = true;
//    boolean flag = true;
//    private CourseEditFramgment editFragment;
//    private static String id;
//    private CourseDao courseDao = new CourseDao(this);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}
