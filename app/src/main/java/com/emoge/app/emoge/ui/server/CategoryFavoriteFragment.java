package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.MyStoreGif;
import com.emoge.app.emoge.model.StoreGif;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jh on 17. 8. 7.
 * 내부 저장소에서 불러오기 ( Realm 이용 )
 */

public class CategoryFavoriteFragment extends Fragment {
    private final String LOG_TAG = CategoryFavoriteFragment.class.getSimpleName();

    @BindView(R.id.server_gif_list)
    RecyclerView mGifList;
    @BindView(R.id.server_no_image)
    ImageView mNoImage;

    private StoreGifAdapter mGifAdapter;
    private Realm realm;


    public CategoryFavoriteFragment() {
    }

    public static CategoryFavoriteFragment newInstance() {
        return new CategoryFavoriteFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        // RecyclerView 설정
        mGifAdapter = new StoreGifAdapter(this, new ArrayList<StoreGif>());
        mGifList.setHasFixedSize(true);
        mGifList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGifList.setAdapter(mGifAdapter);

        loadGifImages();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mGifAdapter != null) {
            mGifAdapter.closeRealm();
        }
    }

    // 새로고침
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mGifAdapter != null) {
            if(isVisibleToUser) {
                if(!mGifAdapter.isEmpty()) {
                    mGifAdapter.clear();
                }
                loadGifImages();
            } else {
                mGifAdapter.clear();
            }
        }
    }

    // Realm 에서 불러오기
    private void loadGifImages() {
        RealmResults<MyStoreGif> myStoreGifs = realm.where(MyStoreGif.class).findAll();
        for(MyStoreGif myStoreGif : myStoreGifs) {
            mGifAdapter.addItem(new StoreGif(myStoreGif.getTitle(), myStoreGif.getDownloadUrl(), -1));
        }
        if(mGifAdapter.isEmpty()) {
            Glide.with(getContext()).load(R.drawable.img_no_image).into(mNoImage);
            mNoImage.setVisibility(View.VISIBLE);
        } else {
            mNoImage.setVisibility(View.GONE);
        }
    }
}
