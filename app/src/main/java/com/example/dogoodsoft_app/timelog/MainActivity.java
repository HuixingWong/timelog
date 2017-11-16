package com.example.dogoodsoft_app.timelog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.example.dogoodsoft_app.timelog.adapter.MyItemRecyclerViewAdapter2;
import com.example.dogoodsoft_app.timelog.adapter.MyItemTouchHelperCallBack;
import com.example.dogoodsoft_app.timelog.modols.Log;
import com.example.dogoodsoft_app.timelog.utils.SharepreferencesUtils;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String [] logNames = new String[]{"编程","读书","魔术"};
    private List<Log> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isfirstIn();

        RecyclerView recyclerView = findViewById(R.id.list);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        MyItemRecyclerViewAdapter2 mAdapter =
                new MyItemRecyclerViewAdapter2(logs,this);
//            recyclerView.addItemDecoration(new SpaceItemDecoration(10,2));
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mAdapter);

        /**
         * 设置RecycleView可以拖动交换位置的实现
         */
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallBack(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);

        helper.attachToRecyclerView(recyclerView);
    }

    private void isfirstIn(){
        boolean isFirstIn = SharepreferencesUtils.getIsFirstIn(this, "my_share", "isfirstin");
        if (isFirstIn) {

            long l = System.currentTimeMillis();

            for (int i = 1; i <= logNames.length+1; i++) {

                Log log = new Log();
                log.setCounTime(0l);
                log.setName(logNames[i-1]);
                log.setStartTime(l);
                log.save();

                logs.add(log);

            }

        }else {

           logs = DataSupport.findAll(Log.class);

        }
    }

}
