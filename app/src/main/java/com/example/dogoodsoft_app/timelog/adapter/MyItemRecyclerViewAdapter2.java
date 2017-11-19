package com.example.dogoodsoft_app.timelog.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.dogoodsoft_app.timelog.MyApp;
import com.example.dogoodsoft_app.timelog.R;
import com.example.dogoodsoft_app.timelog.modols.Log;
import com.example.dogoodsoft_app.timelog.utils.TimeUtils;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 */
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHellperAdapter, View.OnClickListener,View.OnLongClickListener{

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

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    @Override
    public boolean onLongClick(View view) {
        if (mOnItemLongClickListener != null) {
            //使用getTag方法获取position
            mOnItemLongClickListener.onItemlongClick(view, (int) view.getTag());
        }
        return false;
    }


    public static enum ITEM_TYPE {
        ITEM_TYPE_NORMAL,
        ITEM_TYPE_END,
    }


    public MyItemRecyclerViewAdapter2(List<Log> items,Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getAdapterPosition() == mRecordPosition
                && mRecordPosition != -1 && holder instanceof NormalHolder) {

            /**
             * 每次滑动都会出现时间差，原因是每次滑动都会执行这个方法每次执行的时候都会出现
             * 差值。这个差值是每次销毁的时候的时间值与得到当前Chronometor上面显示的值之间的差值。
             *
             */

            ((NormalHolder)holder).mView.flipTheView();

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal()){
            EasyFlipView view = (EasyFlipView) LayoutInflater.from(context)
                    .inflate(R.layout.recycleview_item, parent, false);
            return new NormalHolder(view);
        }else {

            EasyFlipView view = (EasyFlipView) LayoutInflater.from(context).inflate(R.layout.item_add,parent,false);

            return new empteyHolder(view);
        }


    }


    @Override
    public int getItemViewType(int position) {

        if (position < mValues.size()){

            return ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal();

        }else {
            return ITEM_TYPE.ITEM_TYPE_END.ordinal();

        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof NormalHolder) {

            final Log log = mValues.get(position);
            ((NormalHolder)holder).mIdView.setText(mValues.get(position).getName());

            ((NormalHolder)holder).mTvCountTime.setText(TimeUtils.format(log.getCounTime()));

            ((NormalHolder)holder).mTVStartTime.setText(TimeUtils.parseTime(log.getStartTime()));

            if (log.isstart && position == mRecordPosition) {
                if ( ((NormalHolder)holder).mView.isFrontSide()) {
                    ((NormalHolder)holder).mView.flipTheView();

                    if (isChronometerRunning) {


                        systemtime12 = System.currentTimeMillis() - systemtime1;
                        /**
                         * 因为longtime记录的是走过的时间，所以出了监听cheronometor的方法里面自增之外，当恢复
                         * 显示的时候，如果是正在计时的情况，还要加上时间差
                         */
                        logtime += systemtime12;
                        ((NormalHolder)holder).chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2 - systemtime12);
                        ((NormalHolder)holder).chronometer.start();


                    } else {
                        ((NormalHolder)holder).chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2);
                        ((NormalHolder)holder).chronometer.stop();
                    }

                }
            } else {
                if ( ((NormalHolder)holder).mView.isBackSide()) {

                    ((NormalHolder)holder).mView.flipTheView();

                }
            }


            ((NormalHolder)holder).mImgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRecordPosition == -1) {
                        ((NormalHolder)holder).mView.flipTheView();
                        mRecordPosition = position;
                        log.isstart = true;

                        mStarttime = SystemClock.elapsedRealtime();

                        ((NormalHolder)holder).chronometer.setBase(mStarttime);
                        ((NormalHolder)holder).chronometer.start();
                        isChronometerRunning = true;
                        mRecordPosition = position;


                    }

                }
            });

            ((NormalHolder)holder).mBtnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (isChronometerRunning) {
                        ((NormalHolder)holder).chronometer.stop();
                        isChronometerRunning = false;
                        mRecordtime = SystemClock.elapsedRealtime();
                    } else {

                        if (mIsStartFromRecycleed) {

                            ((NormalHolder)holder).chronometer.setBase(SystemClock.elapsedRealtime() - mRecordtime2);
                            mIsStartFromRecycleed = false;

                        } else {

                            ((NormalHolder)holder).chronometer.setBase( ((NormalHolder)holder).chronometer.getBase()
                                    + SystemClock.elapsedRealtime() - mRecordtime);

                        }


                        ((NormalHolder)holder).chronometer.start();

                        isChronometerRunning = true;


                    }


                }
            });


            ((NormalHolder)holder).mBtnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    log.isstart = false;



                    String s = ((NormalHolder) holder).chronometer.getText().toString();

                    String[] my =s.split(":");
                    int hour = 0;
                    if (my.length > 2){

                       hour  =Integer.parseInt(my[my.length-3]);

                    }
                    int min =Integer.parseInt(my[my.length-2]);
                    int sec =Integer.parseInt(my[my.length-1]);

                    long totalSec =(hour*3600+min*60+sec)*1000;

                    log.setCounTime(log.getCounTime() + totalSec);

                    /**
                     * 每次计时结束以后，修改countime的值。
                     */
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("counTime", log.getCounTime());
                    DataSupport.update(Log.class, contentValues, log.getId());

                    ((NormalHolder)holder).chronometer.stop();
                    ((NormalHolder)holder).chronometer.setBase(SystemClock.elapsedRealtime());
                    ((NormalHolder)holder).mView.flipTheView();

                    notifyDataSetChanged();

                    clearData();


                }
            });


            ((NormalHolder)holder).chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {


                    if (position == mRecordPosition) {

                        android.util.Log.e("123", chronometer.toString());
                        logtime += 1000;

                    }
                }
            });


        }else {

            ((empteyHolder)holder).addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mRecordPosition == -1){

                        ((empteyHolder) holder).flipView.flipTheView();

                    }else {

                        Toast.makeText(context, "专注结束以后添加", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            ((empteyHolder)holder).btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String text = ((empteyHolder) holder).etName.getText().toString();
                    if (TextUtils.isEmpty(text)){


                        Toast.makeText(context, "请输入分类名后添加", Toast.LENGTH_SHORT).show();
                        ((empteyHolder) holder).flipView.flipTheView();

                    }else {

                        Log log = new Log();
                        log.setStartTime(System.currentTimeMillis());
                        log.setCounTime(0l);
                        log.setName(text);

                        boolean save = log.save();

                        if (save){

                            mValues.add(log);
                            notifyDataSetChanged();


                        }else {

                            Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        }

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
        return mValues.size()+1;
    }



    public class NormalHolder extends RecyclerView.ViewHolder {
        public final EasyFlipView mView;
        public final TextView mIdView;
        public final Button mImgbtn;
        public final Chronometer chronometer;

        public final Button mBtnPause;
        public final Button mBtnStop;

        public final  TextView mTvCountTime;

        public final TextView mTVStartTime;


        public NormalHolder(EasyFlipView view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tv_project_name);
            mImgbtn = (Button) view.findViewById(R.id.start_btn);

            chronometer = view.findViewById(R.id.chrometor);

            mBtnPause = view.findViewById(R.id.btn_pause);
            mBtnStop = view.findViewById(R.id.btn_stop);

            mTvCountTime = view.findViewById(R.id.tv_counttime);

            mTVStartTime = view.findViewById(R.id.tv_starttime);
        }


    }


    public class empteyHolder extends RecyclerView.ViewHolder{

        public final  EasyFlipView flipView;
        public  final  ImageView addImg;

        public final EditText etName;

        public final  Button btnSure;

        public empteyHolder(EasyFlipView itemView) {
            super(itemView);
            flipView = itemView;
            addImg = itemView.findViewById(R.id.add_img);
            btnSure = itemView.findViewById(R.id.btn_add_sure);
            etName = itemView.findViewById(R.id.type_name_et);

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

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public  interface OnItemLongClickListener {
        void onItemlongClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setmOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
