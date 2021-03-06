package com.example.dell.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.myapplication.LitePal.Pass;
import com.jrummyapps.android.animations.Technique;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.LinkedList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    //加入数据库版本
//    private MydatabaseHelper dbHelper;

    //改用litepal
    /*正式版需用文件存储这些数据，在该版本中回到主菜单后下面四个数据会重置*/
    private boolean keyboardPassed;
    private boolean memoryPassed;
    private Pass passKeyboard;
    private Pass passMemory;

    //创建数据库及pass表和words表，并初始化
    private void operateData(Context context) {
        SQLiteDatabase db = LitePal.getDatabase();
        Toast.makeText(this, "创建数据库及表", Toast.LENGTH_LONG).show();
        passKeyboard = new Pass();
        passKeyboard.setName("Keyboard");
        passKeyboard.setPassed(false);
        passKeyboard.save();
        passMemory = new Pass();
        passMemory.setName("Memory");
        passMemory.setPassed(false);
        passMemory.save();
        Toast.makeText(this, "初始化完毕", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    keyboardPassed = data.getBooleanExtra("data_return1", false);
                    passKeyboard.setPassed(keyboardPassed);
                    passKeyboard.save();
//                    pass.updateAll("name = ?", "Keyboard");
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    memoryPassed = data.getBooleanExtra("data_return2", false);
                    passMemory.setPassed(memoryPassed);
                    passMemory.save();
                }
                break;

            default:
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //操作数据提取为方法
        operateData(this);

        //创建数据库Helper,并通过它创建数据库
//        dbHelper = new MydatabaseHelper(this,"PassStore.db",null,5);
//        dbHelper.getWritableDatabase();

        Button cpu = (Button) findViewById(R.id.CPU);
        Button keyboard = (Button) findViewById(R.id.keyboard);
        Button memory = (Button) findViewById(R.id.memory);
        Button printer = (Button) findViewById(R.id.printer);
        Button returnMain = (Button) findViewById(R.id.returnToMain);

        //导入开源库后的测试
//        cpu.setOnTouchListener(new Rebound.SpringyTouchListener() {
//
//            @Override
//            public void onClick(View v) {
//                Technique.BOUNCE.playOn(v);
//                Toast.makeText(MapActivity.this,"hhh",Toast.LENGTH_LONG).show();
//                Technique.FADE_IN.getComposer().duration(2500).delay(1000).playOn(v);
//            }
//        });


        cpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一个动画
                Technique.BOUNCE.playOn(v);
                Intent intent0 = new Intent(MapActivity.this, CPU.class);
                startActivityForResult(intent0, 0);
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapActivity.this, Keyboard.class);
                startActivityForResult(intent1, 1);
            }
        });

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Pass> pass = LitePal.select("passed")
                        .where("name = ?", "Keyboard")
                        .find(Pass.class);
                boolean passed = pass.get(0).isPassed();
                if (passed) {
                    Intent intent2 = new Intent(MapActivity.this, Memory.class);
                    startActivityForResult(intent2, 2);
                } else {
                    Toast.makeText(MapActivity.this, "请先完成键盘关卡", Toast.LENGTH_SHORT).show();
                }
            }
        });

        printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Pass> pass = LitePal.select("passed")
                        .where("name = ?","Memory")
                        .find(Pass.class);
                boolean passed = pass.get(0).isPassed();
                if (passed) {
                    Intent intent3 = new Intent(MapActivity.this, Printer.class);
                    startActivityForResult(intent3, 3);
                } else {
                    Toast.makeText(MapActivity.this, "请先完成存储器关卡", Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}