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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Konfyt on 2016/9/14.
 */
public abstract class LoadMoreLinearBase2Adapter<T> extends RecyclerView.Adapter<LoadMoreLinearBase2Adapter.ViewHolder> implements View.OnClickListener {

    // 数据源
    private List<T> mDatas;
    // 布局填充器
    private LayoutInflater mInflater;
    // Attach的RecyclerView
    private RecyclerView mRecyclerView;
    // 上下文对象
    private Context mContext;
    // item的点击事件
    private OnItemClickListener mListener;
    // error的点击事件
    private OnFooterErrorListener mErrorListener;


    // 脚布局
    private final int TYPE_FOOTER = Integer.MAX_VALUE;


    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    // 正在加载
    private final int LOADING = 1;
    // 加载完成
    private final int LOAD_COMPLETE = 2;
    // 加载到底
    private final int LOAD_END = 3;
    // 加载出错
    private final int LOAD_ERROR = 4;
    // 加载无数据
    private final int LOAD_NODATA = 5;


    public LoadMoreLinearBase2Adapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatas = new ArrayList<>();
        mContext = context;
    }


    public LoadMoreLinearBase2Adapter(Context context, List<T> list) {
        this(context);
        addData(list);
    }


    // 添加数据源
    public void addData(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position == mDatas.size()) {
            return TYPE_FOOTER;
        } else {
            return getCustomItemViewType(position);
        }
    }

    // 多布局的ViewType
    public abstract int getCustomItemViewType(int position);

    // 获取item的总数量(数据源+脚布局)
    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    // 创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_FOOTER) {
            View view = mInflater.inflate(R.layout.layout_refresh_footer, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mErrorListener != null && loadState == LOAD_ERROR) {
                        mErrorListener.onClick();
                    }
                }
            });
            return new FootViewHolder(view);
        } else {
            ViewHolder viewHolder = onCreateCustomViewHolder(parent, viewType);
            viewHolder.itemView.setOnClickListener(this);
            return viewHolder;
        }

    }

    // 创建多布局的ViewHolder
    public abstract ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);

    // 绑定ViewHolder 需要定义抽象方法来实现里面的操作，
    // 所以LoadMoreLinearBaseAdapter需要声明成抽象类
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载

                    footViewHolder.getProgressBar(R.id.pb_loading).setVisibility(View.VISIBLE);
                    footViewHolder.getTextView(R.id.tv_loading).setVisibility(View.VISIBLE);
                    footViewHolder.getLinearLayout(R.id.ll_end).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_no_data).setVisibility(View.GONE);
                    footViewHolder.getRelativeLayout(R.id.ll_error).setVisibility(View.GONE);
                    break;

                case LOAD_COMPLETE: // 加载完成
                    footViewHolder.getProgressBar(R.id.pb_loading).setVisibility(View.INVISIBLE);
                    footViewHolder.getTextView(R.id.tv_loading).setVisibility(View.INVISIBLE);
                    footViewHolder.getLinearLayout(R.id.ll_end).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_no_data).setVisibility(View.GONE);
                    footViewHolder.getRelativeLayout(R.id.ll_error).setVisibility(View.GONE);
                    break;

                case LOAD_END: // 加载到底
                    footViewHolder.getProgressBar(R.id.pb_loading).setVisibility(View.GONE);
                    footViewHolder.getTextView(R.id.tv_loading).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_end).setVisibility(View.VISIBLE);
                    footViewHolder.getLinearLayout(R.id.ll_no_data).setVisibility(View.GONE);
                    footViewHolder.getRelativeLayout(R.id.ll_error).setVisibility(View.GONE);
                    break;
                case LOAD_ERROR: // 加载出错
                    footViewHolder.getProgressBar(R.id.pb_loading).setVisibility(View.GONE);
                    footViewHolder.getTextView(R.id.tv_loading).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_end).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_no_data).setVisibility(View.GONE);
                    footViewHolder.getRelativeLayout(R.id.ll_error).setVisibility(View.VISIBLE);
                    break;
                case LOAD_NODATA:
                    footViewHolder.getProgressBar(R.id.pb_loading).setVisibility(View.GONE);
                    footViewHolder.getTextView(R.id.tv_loading).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_end).setVisibility(View.GONE);
                    footViewHolder.getLinearLayout(R.id.ll_no_data).setVisibility(View.VISIBLE);
                    footViewHolder.getRelativeLayout(R.id.ll_error).setVisibility(View.GONE);
                    break;
            }
        } else {

            // 需要子类去实现 具体操作
            onBindCustomViewHolder(holder, position);
        }
    }

    public abstract void onBindCustomViewHolder(ViewHolder holder, int position);


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    // 清空数据源
    public void clearAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        T t = mDatas.get(position);
        if (mListener != null) {
            mListener.onClick(t, position);
        }
    }


    // 脚布局ViewHoldr类
    private static class FootViewHolder extends LoadMoreLinearBase2Adapter.ViewHolder {

        FootViewHolder(View itemView) {
            super(itemView);
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

        public RelativeLayout getRelativeLayout(int id) {
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


    /**
     * 设置加载的状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(LoadingState loadState) {
        this.loadState = loadState.getmState();
        notifyDataSetChanged();
    }

    // 对外提供获取数据源的方法
    public List<T> getmDatas() {
        return mDatas;
    }

    // 对外提供获取context的方法
    public Context getmContext() {
        return mContext;
    }

    // 对外提供获取布局填充器的方法
    public LayoutInflater getmInflater() {
        return mInflater;
    }

    // 对外提供设置item的监听器的方法
    public void setOnItemClickListener(OnItemClickListener<T> mListener) {
        this.mListener = mListener;
    }

    // 对外提供设置footer的监听器的方法
    public void setOnFooterErrorListener(OnFooterErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }


    public interface OnItemClickListener<T> {
        // 传递当前点击的对象（List对应位置的数据）与位置
        void onClick(T t, int position);
    }

    // foot 错误的事件
    public interface OnFooterErrorListener {
        void onClick();
    }
}
