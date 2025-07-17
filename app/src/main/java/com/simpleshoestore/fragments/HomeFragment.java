package com.simpleshoestore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simpleshoestore.R;
import com.simpleshoestore.adapters.ShoeAdapter;
import com.simpleshoestore.models.Shoe;
import com.simpleshoestore.activities.AddShoeActivity;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShoeAdapter adapter;
    private SearchView searchView; // 搜索栏组件

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_shoes);
        searchView = view.findViewById(R.id.search_view); // 绑定搜索栏（需在布局中定义）

        setupRecyclerView();
        setupSearchFunction(); // 初始化搜索功能
        loadShoes();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ShoeAdapter(getContext(), AddShoeActivity.getAllShoes());
        recyclerView.setAdapter(adapter);
    }

    // 新增：搜索功能实现
    private void setupSearchFunction() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterShoes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterShoes(newText); // 实时过滤
                return true;
            }
        });
    }

    // 根据关键词过滤鞋子
    private void filterShoes(String keyword) {
        List<Shoe> allShoes = AddShoeActivity.getAllShoes();
        List<Shoe> filteredShoes = new ArrayList<>();
        for (Shoe shoe : allShoes) {
            if (shoe.getName().toLowerCase().contains(keyword.toLowerCase())
                    || shoe.getBrand().toLowerCase().contains(keyword.toLowerCase())) {
                filteredShoes.add(shoe);
            }
        }
        adapter.updateShoes(filteredShoes);
    }

    private void loadShoes() {
        AddShoeActivity.initializeDefaultShoes();
        List<Shoe> shoes = AddShoeActivity.getAllShoes();
        adapter.updateShoes(shoes);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadShoes();
    }
}