package com.curofy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.curofy.internal.Paginate;
import com.curofy.internal.PaginateManager;
import com.curofy.internal.Paginator;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by nateshrelhan on 6/13/17.
 */

public class DemoPaginatorActivity extends AppCompatActivity {
    //Layout manager used through reflection in Paginator class
    @PaginateManager
    public LinearLayoutManager linearLayoutManager;
    //Paginator responsible for pagination in recyclerView
    @Inject
    Paginator paginator;
    @BindView(R.id.rv_news_list)
    RecyclerView recyclerView;

    private DemoPaginatorComponent demoPaginatorComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Create layout manager before in injecting the component because it is used in constructor of Paginator class which is
        instantiate at the time of injection*/
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Injecting paginator component and passind current activity as object (used in invoking and getting fields through reflection
        getDemoPaginatorComponent(this).inject(this);
        //Just attach it now
        recyclerView.addOnScrollListener(paginator);

    }

    /**
     * Do all the task inside this method that has to be called when user scrolls down the page
     *
     * @param page passed from paginator to this mehtod
     */
    @Paginate
    public void fetchNewsList(int page) {

    }

    /**
     * @param object is used in reflection api inside Paginator class
     * @return component of DemoPaginatorModule
     */
    private DemoPaginatorComponent getDemoPaginatorComponent(Object object) {
        this.injectDemoPaginatorInjector(object);
        return this.demoPaginatorComponent;
    }

    /**
     * to inject component of DemoPaginatorModule in activity/fragment or in any other class
     *
     * @param object is used in reflection api inside Paginator class
     */
    private void injectDemoPaginatorInjector(Object object) {
        if (this.demoPaginatorComponent == null) {
            this.demoPaginatorComponent = DaggerDemoPaginatorComponent.builder().demoPaginatorModule(new DemoPaginatorModule(object)).build();
        }
    }
}
