package stellarnear.aquene_dealer.Divers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public QuadrantManager(View mainView,Context mC) {
        this.mC=mC;
        this.mainView=mainView;
        quadrantLine1 = mainView.findViewById(R.id.main_frag_stats_quadrantLine1);
        quadrantLine2 = mainView.findViewById(R.id.main_frag_stats_quadrantLine2);

        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);

        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);

        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);

        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);

        quadrantList= Arrays.asList(quadrant1, quadrant2, quadrant3, quadrant4);

        setLayoutsListners();
        quadrantFiller =new QuadrantFiller(mainView,mC);
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


}