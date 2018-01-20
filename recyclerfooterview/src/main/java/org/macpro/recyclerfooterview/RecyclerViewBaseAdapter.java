package org.macpro.recyclerfooterview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Konfyt on 2016/9/14.
 */
public abstract class RecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<RecyclerViewBaseAdapter.ViewHolder> implements View.OnClickListener {

    // 因为是个基类所以不能用private
    private List<T> mDatas;
    private LayoutInflater mInflater;
    private int mLayoutResId;
    private RecyclerView mRecyclerView;
    private OnItemClickListener listener;
    private Context mContext;


    public RecyclerViewBaseAdapter(Context context, int layoutResId) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResId = layoutResId;
        mDatas = new ArrayList<>();
        mContext = context;
    }


    public RecyclerViewBaseAdapter(Context context, int layoutResId, List<T> list) {
        this(context, layoutResId);
        addData(list);
    }

    // 获取数据源个数
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    // 创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(mLayoutResId, parent, false);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    // 绑定ViewHolder 需要定义抽象方法来实现里面的操作，
    // 所以RecyclerViewBaseAdapter需要声明成抽象类
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 需要子类去实现 具体操作
        bind(holder, mDatas.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    // 添加数据源
    public void addData(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    // 清空数据源
    public void clearAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }


    public abstract void bind(ViewHolder holder, T t);

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        T t = mDatas.get(position);
        if (listener != null) {
            listener.onClick(t, position);
        }
    }

    // ViewHoldr类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Map<Integer, View> mCacheViews;

        public ViewHolder(View itemView) {
            super(itemView);
            mCacheViews = new HashMap<>();
        }


        //获得常用控件
        public ImageView getImageView(int id) {
            return getView(id);
        }

        public TextView getTextView(int id) {
            return getView(id);
        }

        public EditText getEditText(int id) {
            return getView(id);
        }

        public Button getButton(int id) {
            return getView(id);
        }

        public ImageButton getImageButton(int id) {
            return getView(id);
        }

        public CheckBox getCheckBox(int id) {
            return getView(id);
        }

        public ProgressBar getProgressBar(int id) {
            return getView(id);
        }

        public LinearLayout getLinearLayout(int id) {
            return getView(id);
        }

        public <T extends View> T getView(int resId) {
            View view = null;
            if (mCacheViews.containsKey(resId)) {
                view = mCacheViews.get(resId);
            } else {
                view = itemView.findViewById(resId);
                mCacheViews.put(resId, view);
            }
            return (T) view;
        }
    }


    // 对外提供获取数据源的方法
    public List<T> getmDatas() {
        return mDatas;
    }

    // 对外提供获取context的方法
    public Context getmContext() {
        return mContext;
    }

    public int getmLayoutResId() {
        return mLayoutResId;
    }

    // 设置Item的点击事件
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener<T> {
        // 传递当前点击的对象（List对应位置的数据）与位置
        void onClick(T t, int position);
    }
}
