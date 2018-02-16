package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 19/01/2018.
 */

public class QuadrantManager {
    private Context mC;
    private View mainView;
    private LinearLayout quadrantLine1;
    private LinearLayout quadrantLine2;
    private LinearLayout quadrant1;
    private LinearLayout quadrant2;
    private LinearLayout quadrant3;
    private LinearLayout quadrant4;
    private Perso aquene = MainActivity.aquene;
    private Map<LinearLayout,String> mapLayoutTextTitle =new HashMap<>();
    private List<LinearLayout> quadrantList;
    private QuadrantFiller quadrantFiller;
    public QuadrantManager(View mainView, Context mC, Activity mA) {
        this.mC=mC;
        this.mainView=mainView;
        quadrantLine1 = mainView.findViewById(R.id.main_frag_stats_quadrantLine1);
        quadrantLine2 = mainView.findViewById(R.id.main_frag_stats_quadrantLine2);

        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);

        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);

        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);

        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);

        quadrantList= Arrays.asList(quadrant1, quadrant2, quadrant3, quadrant4);
        hideQuadrant();
        setLayoutsListners();
        quadrantFiller =new QuadrantFiller(mainView,mC,mA);
    }

    private void setLayoutsListners() {

        for (final LinearLayout layout : quadrantList) {
            layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!quadrantFiller.isFullscreen()){
                        quadrantFiller.fullscreenQuadrant(layout);
                    }
                    layout.requestLayout();
                }
            });

        }
    }


    public List<LinearLayout> quadrantAsList() {
        return quadrantList;
    }
    public void hideQuadrant() {
        for (final LinearLayout layout : quadrantList) {
            layout.setVisibility(View.GONE);
        }
    }

    public void showQuadrant() {
        for (final LinearLayout layout : quadrantList) {
            layout.setVisibility(View.VISIBLE);
        }
    }
}
