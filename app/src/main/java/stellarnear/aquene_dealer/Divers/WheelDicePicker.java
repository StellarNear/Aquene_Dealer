package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import stellarnear.aquene_dealer.R;


public class WheelDicePicker extends AppCompatActivity {
    private Context mC;
    private int valueSelected;
    private int dice;
    private Tools tools=new Tools();
    public WheelDicePicker(RelativeLayout relativeCenter, int dice, Context mC) {
        this.mC = mC;
        this.dice=dice;
        double angle_part = 360.0/dice;
        double time_delay_anim=1000/dice;
        int dist=mC.getResources().getDimensionPixelSize(R.dimen.distance_dice_wheel);
        final ImageButton mainDice = new ImageButton(mC);

        for (int i = 0; i < dice; i++) {

            ImageButton imgButton = new ImageButton(mC);
            int drawableId=mC.getResources().getIdentifier("d"+dice+"_"+String.valueOf(i+1), "drawable", mC.getPackageName());
            imgButton.setImageDrawable( tools.resize(mC,drawableId,mC.getResources().getDimensionPixelSize(R.dimen.icon_dices_wheel_size)));

            setPara(imgButton);

            final int val_dice=i+1;
            imgButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valueSelected =val_dice;
                    changeMainDice(val_dice,mainDice);
                }
            });

            relativeCenter.addView(imgButton);

            double angle=-90d+angle_part*i;
            int distX = (int) (dist*Math.cos(Math.toRadians(angle)));
            int distY = (int) (dist*Math.sin(Math.toRadians(angle)));

            imgButton.animate().setDuration(1000).setInterpolator(new OvershootInterpolator(3.0f)).translationX(distX).translationY(distY).setStartDelay((int) (i*time_delay_anim)).start();
        }

        int drawableIdMain=mC.getResources().getIdentifier("d"+dice+"_main", "drawable", mC.getPackageName());
        mainDice.setImageDrawable( tools.resize(mC,drawableIdMain,mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size)));
        setPara(mainDice);
        relativeCenter.addView(mainDice);
    }

    private void setPara(ImageButton imgButton) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgButton.setLayoutParams(layoutParams);
        imgButton.setBackgroundColor(mC.getColor(R.color.transparent));
    }

    private void changeMainDice(int val_dice,ImageButton mainDice) {
        int drawableId=mC.getResources().getIdentifier("d"+dice+"_"+String.valueOf(val_dice), "drawable", mC.getPackageName());
        mainDice.setImageDrawable( tools.resize(mC,drawableId,mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size)));
    }

    public int getValueSelected(){
        return this.valueSelected;
    }
}