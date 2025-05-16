package com.appinsnap.aishrm.ui.fragments.leavesDetails;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private RecyclerView recyclerView;



    public CustomScrollListener(LinearLayoutManager layoutManager, RecyclerView recyclerView) {

        this.layoutManager = layoutManager;

        this.recyclerView = recyclerView;

    }
    @Override

    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (dx > 0) {

            // Scrolling towards the right

            if (lastVisibleItemPosition < layoutManager.getItemCount() - 1) {

                layoutManager.smoothScrollToPosition(recyclerView, null, lastVisibleItemPosition + 1);

            }

        } else if (dx < 0) {

            // Scrolling towards the left

            if (firstVisibleItemPosition > 0) {

                layoutManager.smoothScrollToPosition(recyclerView, null, firstVisibleItemPosition - 1);

            }

        }

    }

}