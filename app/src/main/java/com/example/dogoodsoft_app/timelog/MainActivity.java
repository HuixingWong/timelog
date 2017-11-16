package com.example.dogoodsoft_app.timelog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.dogoodsoft_app.timelog.adapter.MyItemRecyclerViewAdapter2;
import com.example.dogoodsoft_app.timelog.adapter.MyItemTouchHelperCallBack;
import com.example.dogoodsoft_app.timelog.dummy.DummyContent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.list);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        MyItemRecyclerViewAdapter2 mAdapter =
                new MyItemRecyclerViewAdapter2(DummyContent.ITEMS);
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
}
