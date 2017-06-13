package com.curofy.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by natesh on 6/1/16.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    // The first item to be visible on top of current state
    private int firstVisibleItem;
    // All the items visible in current state
    private int visibleItemCount;
    // Total number of items present in the data set
    private int totalItemCount;
    // Total number of items in the data set after the last load
    private int previousTotal = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 4;
    // Specify the page on which the user is
    private int current_page = 0;


    public void setmLinearLayoutManager(LinearLayoutManager mLinearLayoutManager) {
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    //Reset variables
    public void resetData() {
        this.previousTotal = 0;
        this.loading = true;
        this.current_page = 0;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        boolean reverseLayout = mLinearLayoutManager.getReverseLayout();
        if ((!reverseLayout && dy >= 0) || (reverseLayout && dy <= 0)) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (loading) {
                int diff = totalItemCount - visibleThreshold - visibleItemCount;
                if (firstVisibleItem > diff) {
                    //User has scrolled and passed the visible threshold
                    previousTotal = totalItemCount;
                    loading = false;
                    current_page++;
                    onLoadMore(current_page);
                }
            } else {
                if (totalItemCount > previousTotal)
                    //Data fetched
                    loading = true;
                else if (mLinearLayoutManager.findLastVisibleItemPosition() == mLinearLayoutManager.getItemCount() - 1)
                    //End of the list
                    onLoadMore(-100);

            }
        }
    }

    public abstract void onLoadMore(int current_page);
}