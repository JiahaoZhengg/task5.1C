package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewsFragment extends Fragment {

    private View inflate;
    private RecyclerView rvNews;
    private BaseQuickAdapter<NewsBean.ResultBean.DataBean, BaseViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_news, container, false);
        initView();
        return inflate;
    }

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private float dip2px(float dip) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());

    }

    private float sp2px(int sp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());

    }

    public void initView() {
        rvNews = inflate.findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvNews.addItemDecoration(new SpacesItemDecoration((int) dip2px(10)));
        adapter = new BaseQuickAdapter<NewsBean.ResultBean.DataBean, BaseViewHolder>(R.layout.item_news) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsBean.ResultBean.DataBean dataBean) {
                baseViewHolder.setText(R.id.tv_auth_name, dataBean.getAuthor_name());
                baseViewHolder.setText(R.id.tv_time, dataBean.getDate());
                baseViewHolder.setText(R.id.tv_title, dataBean.getTitle());
                baseViewHolder.setText(R.id.tv_category, dataBean.getCategory());
                Glide.with(getActivity()).load(dataBean.getThumbnail_pic_s()).into((ImageView) baseViewHolder.getView(R.id.iv_news));
            }


        };
        rvNews.setAdapter(adapter);
        String json = getJson(getContext(), "News.json");
        NewsBean newsBean = new Gson().fromJson(json, NewsBean.class);
        adapter.setNewInstance(newsBean.getResult().getData().subList(0, 4));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                NewsBean.ResultBean.DataBean o = (NewsBean.ResultBean.DataBean) adapter.getData().get(position);
                bundle.putSerializable("news", o);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_secondFragment, bundle);

            }
        });
    }

    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
