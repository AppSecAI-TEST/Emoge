package com.emoge.app.emoge.ui.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emoge.app.emoge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 8.
 * 갤러리 사진 View Holder
 */

class GalleryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.gallery_item_image)
    ImageView image;
    @BindView(R.id.gallery_item_type)
    TextView type;

    GalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
