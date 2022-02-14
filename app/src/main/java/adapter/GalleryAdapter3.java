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

public class GalleryAdapter3 extends RecyclerView.Adapter<GalleryAdapter3.MyViewHolder> {
    private List<Image3> images3;
    private Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail3);
        }
    }
    public GalleryAdapter3(Context context, List<Image3> images3) {
        mContext = context;
        this.images3 = images3;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail3, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image3 image3 = images3.get(position);
        Glide.with(mContext).load(image3.getUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.thumbnail);
    }
    @Override
    public int getItemCount() {
        return images3.size();
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private GalleryAdapter3.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter3.ClickListener clickListener) {
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