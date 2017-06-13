package com.curofy.internal;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.curofy.utils.EndlessRecyclerOnScrollListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Inject;

/**
 * Created by nateshrelhan on 08/05/17.
 */
@PerActivity
public class Paginator extends EndlessRecyclerOnScrollListener {
    private Method paginationMethod;
    private Object object;

    @Inject
    public Paginator(@NonNull Object object) {
        this.object = object;
        init();
    }

    private void init() {
        try {
            Method[] allMethods = this.object.getClass().getDeclaredMethods();
            for (Method method : allMethods) {
                Paginate paginate = method.getAnnotation(Paginate.class);
                if (paginate != null) {
                    this.paginationMethod = method;
                    break;
                }
            }
            Field[] fields = this.object.getClass().getFields();
            for (Field field : fields) {
                PaginateManager paginateManager = field.getAnnotation(PaginateManager.class);
                if (paginateManager != null && field.getType().equals(LinearLayoutManager.class)) {
                    setmLinearLayoutManager((LinearLayoutManager) field.get(this.object));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadMore(int current_page) {
        try {
            if (current_page > 0)
                this.paginationMethod.invoke(this.object, current_page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideFilter(boolean toHide) {

    }
}
