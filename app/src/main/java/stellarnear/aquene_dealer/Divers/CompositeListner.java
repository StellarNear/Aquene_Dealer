package stellarnear.aquene_dealer.Divers;

import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;

public class CompositeListner implements OnClickListener {
    private ArrayList<OnClickListener> listeners=new ArrayList<>();

    public void addOnclickListener(OnClickListener listener){
        listeners.add(listener);
    }

    @Override
    public void onClick(View v) {
        for(OnClickListener l : listeners){
            l.onClick(v);
        }
    }
}