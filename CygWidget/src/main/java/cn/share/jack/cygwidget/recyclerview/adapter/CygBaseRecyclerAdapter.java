package cn.share.jack.cygwidget.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 对RecyclerView的Adapter的封装
 */
public abstract class CygBaseRecyclerAdapter<DATA, VH extends CygBaseRecyclerViewHolder<DATA>> extends RecyclerView.Adapter<VH> {

    private List<DATA> mDataList;

    private final Context mContext;

    protected OnItemClickListener<VH> listener;

    public CygBaseRecyclerAdapter(Context context, OnItemClickListener<VH> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public CygBaseRecyclerAdapter(Context context) {
        this(context, null);
    }

    protected Context getContext() {
        return mContext;
    }

    /**
     * 给holder设置数据
     *
     * @param vh
     * @param position
     */
    @Override
    public void onBindViewHolder(VH vh, final int position) {
        vh.setData(mDataList.get(position));
        if (listener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    /**
     * 每一个位置的item都作为单独一项来设置
     * viewType 设置为position
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    /**
     * 填充数据,此方法会清空以前的数据
     *
     * @param dataList 需要显示的数据
     */
    public void setDataList(List<DATA> dataList, boolean notifyDataSetChanged) {
        if (mDataList != null) {
            mDataList.clear();
        }
        if (dataList != null) {
            if (mDataList == null) {
                mDataList = new ArrayList<>();
            }
            mDataList.addAll(dataList);
        }
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public void removeAllDataList() {
        if (mDataList != null) {
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 在列表某个位置插入数据
     *
     * @param position 需要在哪个位置上插入
     * @param data     插入的数据
     */
    public void insertItem(int position, DATA data) {
        mDataList.add(position, data);
        notifyItemInserted(position);  //插入更新操作
        notifyItemRangeChanged(position, mDataList.size() - position);  //更新position后面的数据的位置
    }

    /**
     * 移除某个位置的数据
     *
     * @param position
     */
    public void removeItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);  //删除操作更新
        notifyItemRangeChanged(position, mDataList.size() - position);  //更新position后面的数据的位置
    }

    public void setDataList(List<DATA> dataList) {
        setDataList(dataList, true);
    }

    public List<DATA> getDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        return mDataList;
    }

    /**
     * 更新局部数据
     *
     * @param position item的位置
     * @param data     item的数据
     */
    public void updateItem(int position, DATA data) {
        mDataList.set(position, data);
        notifyDataSetChanged();
    }

    /**
     * 获取一条数据
     *
     * @param position item的位置
     * @return item对应的数据
     */
    public DATA getItem(int position) {
        return mDataList.get(position);
    }

    /**
     * 追加一条数据
     *
     * @param data 追加的数据
     */
    public void appendItem(DATA data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 追加一个集合数据
     *
     * @param list 要追加的数据集合
     */
    public void appendList(List<DATA> list) {
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 在最顶部前置数据
     *
     * @param data 要前置的数据
     */
    public void frontItem(DATA data) {
        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    /**
     * 在顶部前置数据集合
     *
     * @param list 要前置的数据集合
     */
    public void frontList(List<DATA> list) {
        mDataList.addAll(0, list);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener<VH> {
        void onItemClick(int position);
    }

}
