package stellarnear.aquene_dealer.Perso;

import android.graphics.drawable.Drawable;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 27/12/2017.
 */

public class Posture {
    String name;
    String descr;
    Drawable img;
    public Posture(String name,String descr, Drawable img){
        this.name=name;
        this.descr=descr;
        this.img=img;
    }

    public Drawable getImg(){
        return img;
    }
    public String getDescr(){
        return descr;
    }
    public String getName(){
        return name;
    }

}
