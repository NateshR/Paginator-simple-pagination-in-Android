package com.curofy.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by natesh on 6/1/16.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    int firstVisibleItem, visibleItemCount, totalItemCount, fixSize, firstVisibleItemFix = 0;
    int mLastFirstVisibleItem = 0, firstVisibleItemB;
    public int previousTotal = 0; // The total number of items in the dataset after the last load
    public boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 4; // The minimum amount of items to have below your current scroll position before loading more.
    public int current_page = 0;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean callLoadMore = true;

    public EndlessRecyclerOnScrollListener() {
    }

    public EndlessRecyclerOnScrollListener(@Nullable Context context, LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.fixSize = mLinearLayoutManager.getItemCount();
    }

    public void setmLinearLayoutManager(LinearLayoutManager mLinearLayoutManager) {
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public int getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void refreshData() {
        this.previousTotal = 0;
        this.loading = true;
        this.current_page = 0;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        firstVisibleItemB = mLinearLayoutManager.findFirstVisibleItemPosition();
        boolean reverseLayout = mLinearLayoutManager.getReverseLayout();
        if ((!reverseLayout && dy >= 0) || (reverseLayout && dy<=0)) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            /*Log.d(TAG, "total - " + totalItemCount + "  prev total - " + previousTotal);*/
            if (callLoadMore) {
                if (loading) {
                    int diff = totalItemCount - visibleThreshold - visibleItemCount;
                    if (firstVisibleItem > diff) {
                        previousTotal = totalItemCount;
                        loading = false;
                        current_page++;
                        Log.d(TAG, "page" + current_page);
                        onLoadMore(current_page);
                    }
                } else {
                    if (totalItemCount > previousTotal) {
                        Log.d(TAG, "total" + totalItemCount);
                        loading = true;
                    } else {
                        //At the end of the list.
                        if (mLinearLayoutManager.findLastVisibleItemPosition() == mLinearLayoutManager.getItemCount() - 1) {
                            //Its at bottom ..
                            onLoadMore(-100);
                        }
                    }
                }
            }
        }
        if (firstVisibleItemB > mLastFirstVisibleItem) {
            hideFilter(true);
        } else if (mLastFirstVisibleItem > firstVisibleItemB) {
            hideFilter(false);
        }
        mLastFirstVisibleItem = firstVisibleItemB;


    }

    public abstract void onLoadMore(int current_page);

    public abstract void hideFilter(boolean toHide);

    public void setCallLoadMore(boolean callLoadMore) {
        this.callLoadMore = callLoadMore;
    }
}