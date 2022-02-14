package adapter;

import android.content.Context;


import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hairstyle.hairstyleapp.R;

import java.util.List;

/**
 * Created by PASCHAL GREEN on 10/18/2016.
 */

public class GalleryAdapter2 extends RecyclerView.Adapter<GalleryAdapter2.MyViewHolder> {
    private List<Image2> images2;
    private Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail2);
        }
    }
    public GalleryAdapter2(Context context, List<Image2> images) {
        mContext = context;
        this.images2 = images;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail2, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image2 image2 = images2.get(position);
        Glide.with(mContext).load(image2.getUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return images2.size();
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private GalleryAdapter2.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter2.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}