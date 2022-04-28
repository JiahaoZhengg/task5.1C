package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class SecondFragment extends Fragment {
    private ImageView ivNews;
    private TextView tvCategory;
    private TextView tvTitle;
    private TextView tvAuthName;
    private TextView tvTime;
    private RecyclerView rvNews;
    private View inflate;
    private BaseQuickAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_second, container, false);
        initView();
        return inflate;
    }

    private void initView() {
        NewsBean.ResultBean.DataBean news = (NewsBean.ResultBean.DataBean) getArguments().getSerializable("news");

        ivNews = (ImageView) inflate.findViewById(R.id.iv_news);
        tvCategory = (TextView) inflate.findViewById(R.id.tv_category);
        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        tvAuthName = (TextView) inflate.findViewById(R.id.tv_auth_name);
        tvTime = (TextView) inflate.findViewById(R.id.tv_time);
        rvNews = (RecyclerView) inflate.findViewById(R.id.rv_news);
        tvTime.setText(news.getDate());
        tvTitle.setText(news.getTitle());
        tvAuthName.setText(news.getAuthor_name());
        tvCategory.setText(news.getCategory());
        Glide.with(this).load(news.getThumbnail_pic_s()).into(ivNews);

        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseQuickAdapter<NewsBean.ResultBean.DataBean, BaseViewHolder>(R.layout.item_second_news) {
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
