package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import stellarnear.aquene_dealer.R;

public class Dice {
    private int nFace;
    private int randValue;
    private String element;
    private ImageView img = null;
    private Context mC;
    private OnRefreshEventListener mListener;
    private boolean rolled=false;
    private boolean canCrit=false;
    private Tools tools=new Tools();

    public Dice(Context mC, Integer nFace,String... elementArg) {
        this.nFace=nFace;
        this.element = elementArg.length > 0 ? elementArg[0] : "";
        this.mC=mC;
        int drawableId = mC.getResources().getIdentifier("d" + nFace + "_main", "drawable", mC.getPackageName());
        this.img=new ImageView(mC);
        this.img.setImageDrawable( tools.resize(mC,drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
    }

    public void rand(Boolean manual){
        if (manual){
            setDiceImgListner();
        } else {
            Random rand = new Random();
            this.randValue = 1 + rand.nextInt(nFace);
            int drawableId = mC.getResources().getIdentifier("d" + nFace + "_" + String.valueOf(this.randValue) + this.element, "drawable", mC.getPackageName());
            this.img.setImageDrawable(tools.resize(mC, drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            this.rolled=true;
        }
    }

    private void setDiceImgListner() {
        this.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DiceDealerDialog(mC, Dice.this);
            }
        });
    }

    public void setRand(int randFromWheel) { // le retour depuis wheelpicker
        this.randValue = randFromWheel;
        int drawableId = mC.getResources().getIdentifier("d" + nFace + "_" + String.valueOf(this.randValue) + this.element, "drawable", mC.getPackageName());
        this.img.setImageDrawable(tools.resize(mC, drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        this.img.setOnClickListener(null);
        this.rolled=true;
        mListener.onEvent();
    }

    public boolean isRolled() {
        return rolled;
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }

    public int getnFace() {
        return nFace;
    }

    public ImageView getImg() {
        return this.img;
    }

    public int getRandValue() {
        return this.randValue;
    }

    public String getElement() {
        return this.element;
    }

    public void makeCritable(){
        this.canCrit=true;
    }

    public boolean canCrit(){
        return this.canCrit;
    }
}
