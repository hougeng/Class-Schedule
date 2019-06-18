package com.example.school.counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.school.R;

import java.util.LinkedList;
import java.util.List;



public class counterActivity extends Activity {

    private final String TAG = "counterActivity";
    private final int STATE_INIT=0;           //输入第一个数字
    private final int STATE_EDITING=1;        //数字编辑阶段
    private final int STATE_RESULT=2;         //返回结果状态 不可以对数字修改 只可以进行运算 或重新开始一次新的运算
    List<StringBuffer> list;

    private int state;                                //输入框的状态
    private double result;
    private double cacheCount;
    char operate;
    TextView tv_display;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter);   //显示
        init();
    }

    /**
     * 程序初始化
     */
    private void init(){
        tv_display=(TextView) findViewById(R.id.tv_display);  //电子屏显示（即数字显示）
        list=new LinkedList<StringBuffer>();  //重构，链表List只存储StringBuffer类型，StringBuffer是String的升级
        result=0;
        cacheCount=0;
        operate='+';
        state=STATE_INIT;
        sb=new StringBuffer();  //创建对象实例
        tv_display.setText(sb);  //显示屏显示
    }

    public void onClick(View arg){
        switch (arg.getId()){
            case R.id.bt_0:  //button 0
                updateData('0');
                break;
            case R.id.bt_1:
                updateData('1');
                break;
            case R.id.bt_2:
                updateData('2');
                break;
            case R.id.bt_3:
                updateData('3');
                break;
            case R.id.bt_4:
                updateData('4');
                break;
            case R.id.bt_5:
                updateData('5');
                break;
            case R.id.bt_6:
                updateData('6');
                break;
            case R.id.bt_7:
                updateData('7');
                break;
            case R.id.bt_8:
                updateData('8');
                break;
            case R.id.bt_9:
                updateData('9');
                break;
            case R.id.bt_point:
                updateData('.');
                break;
            /*case R.id.bt_delete:  //退一格
                updateData('d');
                break; */
            case R.id.bt_add:
                updateData('+');
                break;
            case R.id.bt_minus:
                updateData('-');
                break;
            case R.id.bt_multiply:
                updateData('*');
                break;
            case R.id.bt_dividi:
                updateData('/');
                break;
            case R.id.bt_equal:
                updateData('=');
                break;
            case R.id.tv_init:  //清屏
                init();
                break;
        }
    }

    /**
     * 每通过按钮　传入该方法一个字符
     * 实现对数据的更新
     */
    public void updateData(char ch){
        if ((ch<='9'&& ch>='0')||ch=='.'){
            sb.append(ch);  //append方法的作用是在一个StringBuffer对象后面追加字符串，这里即在显示屏显示的sb后再+一个数，如9+7为97
            tv_display.setText(sb);
        }
        switch (ch){
            case 'd':                                           //删除，退一位
                if (sb.length()==0)
                    break;
                sb.deleteCharAt(sb.length() - 1);  //用来删除StringBuffer字符串指定索引字符的方法
                tv_display.setText(sb);
                break;
            case '+':
                if(sb.length()==0){
                    init();
                    break;
                }
                cacheCount=Double.parseDouble(sb.toString()); //把数字类型的字符串，转换成double类型
                operator(); //调用运算方法
                operate='+';
                break;
            case '-':
                if(sb.length()==0){
                    init();
                    break;
                }
                cacheCount=Double.parseDouble(sb.toString());
                operator();
                operate='-';
                break;
            case '*':
                if(sb.length()==0){
                    init();
                    break;
                }
                cacheCount=Double.parseDouble(sb.toString());
                operator();
                operate='*';
                break;
            case '/':
                if(sb.length()==0){
                    init();
                    break;
                }
                cacheCount=Double.parseDouble(sb.toString());
                operator();
                operate='/';
                break;
            case '=':
                if(sb.length()==0){
                    init();
                    break;
                }
                cacheCount=Double.parseDouble(sb.toString());
                operator();
                operate='+';
                cacheCount=0;
                result=0;
                break;
        }
        Log.d(TAG,cacheCount+"#"+result);
    }

    private void operator(){  //运算方法
        String str;
        switch (operate){
            case '+':
                result+=cacheCount;  //cacheCount是上述变成double型的数
                str= String.valueOf(result);  //把result转换成Double类型，在返回的时候new Double()，所以返回的是Double
                sb=new StringBuffer();
                //       state=STATE_RESULT;
                tv_display.setText(str);
                break;
            case '-':
                result-=cacheCount;
                str=String.valueOf(result);
                sb=new StringBuffer();
                //       state=STATE_RESULT;
                tv_display.setText(str);
                break;
            case '/':
                if(cacheCount==0){
                    tv_display.setText("除零错误");
                    init();
                    break;
                }
                result/=cacheCount;
                str=String.valueOf(result);
                sb=new StringBuffer();
                //       state=STATE_RESULT;
                tv_display.setText(str);
                break;
            case '*':
                result*=cacheCount;
                str=String.valueOf(result);
                sb=new StringBuffer();
                //       state=STATE_RESULT;
                tv_display.setText(str);
                break;
            case '=':
                result+=cacheCount;
                str= String.valueOf(result);
                sb=new StringBuffer();
                //       state=STATE_RESULT;
                tv_display.setText(str);
                cacheCount=0;
                result=0;
                break;
        }
    }


}