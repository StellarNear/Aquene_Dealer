package stellarnear.aquene_companion.Divers.Rolls;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.R;


public class ImgFactoryForDice {
    private Context mC;
    private View img;
    private Tools tools = Tools.getTools();

    public ImgFactoryForDice(Dice dice, Context mC) {
        this.mC = mC;
        this.img=makeImg(dice);
    }

    private ImageView makeImg(Dice dice){
        ImageView imgDice  = null;
        int drawableId;
        if (dice.getRandValue() > 0) {
            drawableId = mC.getResources().getIdentifier("d" + dice.getnFace() + "_" + dice.getRandValue() + (dice.getElement().equalsIgnoreCase("none") ? "" : dice.getElement()), "drawable", mC.getPackageName());
        } else {
            drawableId = mC.getResources().getIdentifier("d" + dice.getnFace() + "_main", "drawable", mC.getPackageName());
        }
        imgDice  = new ImageView(mC);
        if(drawableId!=0) {
            imgDice.setImageDrawable(mC.getDrawable(drawableId));
        } else {
            imgDice.setImageDrawable(mC.getDrawable(R.drawable.mire_test));
        }
        tools.resize(imgDice, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size));
        return imgDice;
    }

    public View getImg() {
        return this.img;
    }
}
