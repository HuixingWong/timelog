package com.example.dogoodsoft_app.timelog.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dogoodsoft_app.timelog.MyApp;
import com.example.dogoodsoft_app.timelog.R;
import com.example.dogoodsoft_app.timelog.modols.Log;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 */
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder>
        implements ItemTouchHellperAdapter {

    private final List<Log> mValues;

    /**
     * 保存点击暂停时候的时间值
     */
    private long mRecordtime = 0l;
    /**
     * 回显的时候用到的时间段长度值，也就是从开始时间到回显之间的时间值。
     */
    private long mRecordtime2 = 0l;

    /**
     * 保存点击开始时候的时间值
     */
    private long mStarttime = 0l;

    private long logtime = 0l;

    private Context context;


    /**
     * chronometor
     * 是否正在执行任务
     */
    boolean isChronometerRunning = false;

    private Log mLog;

    /**
     * 正在记录的view的position
     */
    private int mRecordPosition = -1;

    /**
     * 控件被回收的系统时间值。
     */
    private long systemtime1 = 0l;
    /**
     * 控件回显的时候的时间值与回收时刻的时间值之差
     */
    private long systemtime12 = 0l;

    /**
     * 点击暂停与点击开始之间有没有被销毁过
     */
    private boolean mIsStartFromRecycleed = false;


    public MyItemRecyclerViewAdapter2(List<Log> items,Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getAdapterPosition() == mRecordPosition && mRecordPosition != -1) {

            /**
             * 每次滑动都会出现时间差，原因是每次滑动都会执行这个方法每次执行的时候都会出现
             * 差值。这个差值是每次销毁的时候的时间值与得到当前Chronometor上面显示的值之间的差值。
             *
             */

            holder.mView.flipTheView();

            if (!isChronometerRunning) {

                /**
                 * 这个basetime就是所谓的开始时间，不是停止的时间
                 */

                mRecordtime2 = logtime;// 保存这次记录了的时间
                Toast.makeText(MyApp.getContext(), "" + mRecordtime2, Toast.LENGTH_SHORT).show();

                mIsStartFromRecycleed = true;


            } else {


                mRecordtime2 = logtime;
                systemtime1 = System.currentTimeMillis();


            }


        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EasyFlipView view = (EasyFlipView) LayoutInflater.from(context)
                .inflate(R.layout.recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Log log = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());

        if (log.isstart && position == mRecordPosition) {
            if (holder.mView.isFrontSide()) {
                holder.mView.flipTheView();

                if (isChronometerRunning) {


                    systemtime12 = System.currentTimeMillis() - systemtime1;
                    /**
                     * 因为longtime记录的是走过的时间，所以出了监听cheronometor的方法里面自增之外，当恢复
                     * 显示的时候，如果是正在计时的情况，还要加上时间差
                     */
                    logtime += systemtime12;
                    holder.chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2 - systemtime12);
                    holder.chronometer.start();


                } else {
                    holder.chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2);
                    holder.chronometer.stop();
                }

            }
        }else {
            if (holder.mView.isBackSide()){

                holder.mView.flipTheView();

            }
        }


        holder.mImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordPosition == -1) {
                    holder.mView.flipTheView();
                    mRecordPosition = position;
                    log.isstart = true;

                    mStarttime = SystemClock.elapsedRealtime();

                    holder.chronometer.setBase(mStarttime);
                    holder.chronometer.start();
                    isChronometerRunning = true;
                    mRecordPosition = position;


                }

            }
        });

        holder.mBtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isChronometerRunning) {
                    holder.chronometer.stop();
                    isChronometerRunning = false;
                    mRecordtime = SystemClock.elapsedRealtime();
                } else {

                    if (mIsStartFromRecycleed) {

                        holder.chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2);
                        mIsStartFromRecycleed = false;

                    } else {

                        holder.chronometer.setBase(holder.chronometer.getBase()
                                + SystemClock.elapsedRealtime() - mRecordtime);

                    }


                    holder.chronometer.start();

                    isChronometerRunning = true;


                }


            }
        });


        holder.mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.chronometer.stop();
                holder.chronometer.setBase(SystemClock.elapsedRealtime());
                holder.mView.flipTheView();
                log.isstart = false;

                clearData();


            }
        });


        holder.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {


                if (position == mRecordPosition) {

                    android.util.Log.e("123", chronometer.toString());
                    logtime += 1000;

                }
            }
        });

    }

    /**
     * 一次计时结束后清除数据
     */
    private void clearData() {


        mRecordtime = 0l;
        mRecordtime2 = 0l;

        systemtime1 = 0l;
        systemtime12 = 0l;

        logtime = 0;

        mRecordPosition = -1;
        isChronometerRunning = false;
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final EasyFlipView mView;
        public final TextView mIdView;
        public final Button mImgbtn;
        public final Chronometer chronometer;

        public final Button mBtnPause;
        public final Button mBtnStop;


        public ViewHolder(EasyFlipView view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tv_project_name);
            mImgbtn = (Button) view.findViewById(R.id.start_btn);

            chronometer = view.findViewById(R.id.chrometor);

            mBtnPause = view.findViewById(R.id.btn_pause);
            mBtnStop = view.findViewById(R.id.btn_stop);

        }


    }



    /**
     * 拖拽移动的方法+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * @param source
     * @param target
     */
    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

        int frompostion = source.getAdapterPosition();
        int toposition = target.getAdapterPosition();
        if (frompostion < mValues.size() && toposition < mValues.size()) {

            updateWhenMove(frompostion,toposition);

            /**
             * 因为这个swap方法交换的时候，将id值一起修改了随着position
             * 但是下面那个更新方法里面只修改了除了id以外的其他值。
             */
            Collections.swap(mValues, frompostion, toposition);

            notifyItemMoved(frompostion, toposition);

            /**
             * 添加这一句可以解决移动之后，数据交换没错位置出错的问题
             * 但是当item数量少的时候，这句会出现问题。
             */
            this.notifyDataSetChanged();



        }

    }

    public void updateWhenMove(int fromposition,int toPosition){

        Log logfrom = mValues.get(fromposition);

        int idFrom = logfrom.getId();

        ContentValues valuesFrom = new ContentValues();
        valuesFrom.put("name",logfrom.getName());
        valuesFrom.put("counTime",logfrom.getCounTime());
        valuesFrom.put("startTime",logfrom.getStartTime());

        Log logTo = mValues.get(toPosition);

        int idTo = logTo.getId();

        ContentValues valuesTo = new ContentValues();
        valuesTo.put("name",logTo.getName());
        valuesTo.put("counTime",logTo.getCounTime());
        valuesTo.put("startTime",logTo.getStartTime());

        DataSupport.update(Log.class,valuesFrom,idTo);
        DataSupport.update(Log.class,valuesTo,idFrom);

        /**
         * 在这里把From和To的id值再交换一下
         * 这样swap的时候id的位置就不会变化了
         */
        logfrom.setId(idTo);
        logTo.setId(idFrom);


    }


    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {

    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source) {

        source.itemView.setScaleX(1.2f);
        source.itemView.setScaleY(1.2f);

    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {

        source.itemView.setScaleX(1.0f);
        source.itemView.setScaleY(1.0f);

    }

    @Override
    public boolean canDrag() {

        if (mRecordPosition == -1) {
            return true;
        }

        return false;

    }

    /**
     * 拖拽移动的方法+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
}
