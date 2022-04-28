package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvHor;
    private ViewPager vpNews;
    private View inflate;
    private BaseQuickAdapter adapter;
    private LinearLayout llRight;
    private LinearLayout llLeft;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private int currentPosition = 0;

    private float dip2px(float dip) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());

    }


    private void initView() {
        rvHor = (RecyclerView) inflate.findViewById(R.id.rv_hor);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHor.setLayoutManager(layout);
        rvHor.addItemDecoration(new SpacesItemDecoration(10));
        adapter = new BaseQuickAdapter<NewsBean.ResultBean.DataBean, BaseViewHolder>(R.layout.item_top) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsBean.ResultBean.DataBean dataBean) {
                ImageView view = baseViewHolder.getView(R.id.iv_news);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                layoutParams.width = (int) ((screenWidth - dip2px(30)) / 3);
                Glide.with(getActivity()).load(dataBean.getThumbnail_pic_s()).into(view);
            }


        };

        rvHor.setAdapter(adapter);
        String json = getJson(getContext(), "News.json");
        NewsBean newsBean = new Gson().fromJson(json, NewsBean.class);
        adapter.setNewInstance(newsBean.getResult().getData());

        vpNews = (ViewPager) inflate.findViewById(R.id.vp_news);
        BaseFragmentAdapter<Fragment> adapter = new BaseFragmentAdapter<>(getActivity());
        adapter.addFragment(NewsFragment.newInstance());
        adapter.addFragment(NewsFragment.newInstance());
        adapter.addFragment(NewsFragment.newInstance());
        adapter.addFragment(NewsFragment.newInstance());

        vpNews.setAdapter(adapter);

        rvHor.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPosition = dx;
            }
        });
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        llRight = (LinearLayout) inflate.findViewById(R.id.ll_right);
        llLeft = (LinearLayout) inflate.findViewById(R.id.ll_left);
        llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvHor.scrollBy(currentPosition + (int) ((screenWidth - dip2px(30)) / 3), 0);
            }
        });
        llLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvHor.scrollBy(currentPosition - (int) ((screenWidth - dip2px(30)) ), 0);
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