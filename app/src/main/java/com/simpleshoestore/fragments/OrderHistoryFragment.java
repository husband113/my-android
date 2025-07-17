package com.simpleshoestore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simpleshoestore.R;
import com.simpleshoestore.adapters.OrderAdapter;
import com.simpleshoestore.models.Order;
import com.simpleshoestore.utils.OrderManager;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout llEmptyView;  // 改为LinearLayout类型
    private OrderAdapter adapter;
    private OrderManager orderManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderManager = OrderManager.getInstance();
        initViews(view);
        setupRecyclerView();
        loadOrders();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_orders);
        llEmptyView = view.findViewById(R.id.tv_empty_view);  // 注意：布局中这个ID对应的是LinearLayout
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        List<Order> orders = orderManager.getUserOrders();

        if (orders.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            llEmptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llEmptyView.setVisibility(View.GONE);
            adapter.updateOrders(orders);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}