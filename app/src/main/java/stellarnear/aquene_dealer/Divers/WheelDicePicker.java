package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import stellarnear.aquene_dealer.R;


public class WheelDicePicker extends AppCompatActivity {
    Context mC;
    int value_selected;
    public WheelDicePicker(RelativeLayout relativeCenter, int n_dice, Context mC) {
        this.mC = mC;
        double angle_part = 360.0/n_dice;
        double time_delay_anim=1000/n_dice;
        int dist=mC.getResources().getDimensionPixelSize(R.dimen.distance_dice_wheel);
        final ImageButton mainDice = new ImageButton(mC);

        for (int i = 0; i < n_dice; i++) {

            ImageButton imgButton = new ImageButton(mC);
            int drawableId=mC.getResources().getIdentifier("d20_"+String.valueOf(i+1), "drawable", mC.getPackageName());
            imgButton.setImageDrawable(resize(drawableId,mC.getResources().getDimensionPixelSize(R.dimen.icon_dices_wheel_size)));

            setPara(imgButton);

            final int val_dice=i+1;
            imgButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value_selected=val_dice;
                    changeMainDice(val_dice,mainDice);
                }
            });

            relativeCenter.addView(imgButton);

            double angle=-90d+angle_part*i;
            int distX = (int) (dist*Math.cos(Math.toRadians(angle)));
            int distY = (int) (dist*Math.sin(Math.toRadians(angle)));

            imgButton.animate().setDuration(1000).setInterpolator(new OvershootInterpolator(3.0f)).translationX(distX).translationY(distY).setStartDelay((int) (i*time_delay_anim)).start();
        }


        if (n_dice==20){
            mainDice.setImageDrawable(resize(R.drawable.d20_main,mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size)));
            setPara(mainDice);
            relativeCenter.addView(mainDice);
        }


    }

    private void setPara(ImageButton imgButton) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgButton.setLayoutParams(layoutParams);
        imgButton.setBackgroundColor(mC.getColor(R.color.transparent));
    }

    private void changeMainDice(int val_dice,ImageButton mainDice) {
        int drawableId=mC.getResources().getIdentifier("d20_"+String.valueOf(val_dice), "drawable", mC.getPackageName());
        mainDice.setImageDrawable(resize(drawableId,mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size)));
    }


    private Drawable resize(int imageId,int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
    }



    public int getValue_selected(){
        return this.value_selected;
    }
}