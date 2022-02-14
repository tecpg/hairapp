package adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import com.hairstyle.hairstyleapp.R;

/**
 * Created by PASCHAL GREEN on 1/30/2017.
 */
public class MyToast {
    //myToast customized
    @TargetApi(Build.VERSION_CODES.M)
    public static void showToast(Context ct, String message){

        Toast myToast = new Toast(ct);
        myToast.setDuration(Toast.LENGTH_LONG);
        TextView textView = new TextView(ct);
        textView.setBackgroundResource(R.drawable.my_toast);
        textView.setTextColor(Color.WHITE);


        textView.setText(message);
        myToast.setView(textView);
        myToast.show();


    }
}
