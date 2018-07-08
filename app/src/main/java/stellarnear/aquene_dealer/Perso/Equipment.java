package stellarnear.aquene_dealer.Perso;
import android.content.Context;
import android.graphics.drawable.Drawable;


/**
 * Created by jchatron on 04/01/2018.
 */

public class Equipment {
    private String name;
    private String descr;
    private String slotId;
    private Context mC;
    private Drawable img;

    public Equipment(String name,String descr, String imgIdTxt,String slotId, Context mC) {
        this.name = name;
        this.descr = descr;
        this.slotId = slotId;
        this.mC = mC;
        int imgId=mC.getResources().getIdentifier(imgIdTxt, "drawable", mC.getPackageName());
        this.img = mC.getDrawable(imgId);
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public String getSlotId() {
        return slotId;
    }

    public Drawable getImg(){
        return this.img;
    }

}

