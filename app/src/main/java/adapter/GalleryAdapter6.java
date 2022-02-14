package adapter;

import android.content.Context;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hairstyle.hairstyleapp.R;

import java.util.List;

/**
 * Created by PASCHAL GREEN on 10/18/2016.
 */

public class GalleryAdapter6 extends RecyclerView.Adapter<GalleryAdapter6.MyViewHolder> {
    private List<Image6> images6;
    private Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView posttext;
        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail6);
            posttext = view.findViewById(R.id.posttext6);
        }
    }
    public GalleryAdapter6(Context context, List<Image6> images6) {
        mContext = context;
        this.images6 = images6;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail6, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image6 image6 = images6.get(position);
        Glide.with(mContext).load(image6.getImage_path())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.thumbnail);
        holder.posttext.setText( image6.getPostheading());
    }
    @Override
    public int getItemCount() {
        return images6.size();
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private GalleryAdapter6.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter6.ClickListener clickListener) {
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