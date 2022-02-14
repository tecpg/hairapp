package adapter;

import android.content.Context;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hairstyle.hairstyleapp.R;

import java.util.List;

/**
 * Created by PASCHAL GREEN on 10/18/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private List<Image> images;
    private Context mContext;
    private boolean isLoading;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
    public GalleryAdapter(Context context, List<Image> images) {
        mContext = context;
        this.images = images;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Image image = images.get(position);
        Glide.with(mContext).load(image.getUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.thumbnail);
    }
    @Override
    public int getItemCount() {
        return images.size();
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return images.get(position)== null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;
        public  LoadingViewHolder(View view){
            super(view);
          //  progressBar = (ProgressBar)view.findViewById(R.id.itemPbar);
        }
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
        this.onLoadMoreListener= mOnLoadMoreListener;

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
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