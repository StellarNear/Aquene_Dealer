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
    private Map<LinearLayout,Boolean> mapLayoutFull =new HashMap<>();
    private List<LinearLayout> quadrantList;
    private QuadrantFiller quadrantFiller;
    public QuadrantManager(View mainView,Context mC) {
        this.mC=mC;
        this.mainView=mainView;
        quadrantLine1 = mainView.findViewById(R.id.main_frag_stats_quadrantLine1);
        quadrantLine2 = mainView.findViewById(R.id.main_frag_stats_quadrantLine2);
        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);
        mapLayoutFull.put(quadrant1,false);
        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);
        mapLayoutFull.put(quadrant2,false);
        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);
        mapLayoutFull.put(quadrant3,false);
        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);
        mapLayoutFull.put(quadrant4,false);
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
                    clearAllAnimation();
                    if(mapLayoutFull.get(layout)){
                        beGone(layout);
                        mapLayoutFull.put(layout,false);
                    } else {
                        setOtherGone(layout);
                        mapLayoutFull.put(layout,true);
                    }
                    layout.requestLayout();
                }
            });

        }
    }

    private void clearAllAnimation() {
        quadrant1.clearAnimation();
        quadrant2.clearAnimation();
        quadrant3.clearAnimation();
        quadrant4.clearAnimation();
        quadrantLine1.clearAnimation();
        quadrantLine2.clearAnimation();
    }

    private void setAllVisible() {
        quadrant1.setVisibility(View.VISIBLE);
        quadrant2.setVisibility(View.VISIBLE);
        quadrant3.setVisibility(View.VISIBLE);
        quadrant4.setVisibility(View.VISIBLE);
        quadrantLine1.setVisibility(View.VISIBLE);
        quadrantLine2.setVisibility(View.VISIBLE);
        Animation animation1  = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(150);
        animation1.setFillAfter(true);
        quadrantLine1.startAnimation(animation1);
        quadrantLine2.startAnimation(animation1);

    }

    private void setOtherGone(final LinearLayout layoutSafe) {
        Animation animation1  = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(200);
        animation1.setFillAfter(true);
        animation1.setAnimationListener( new  Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                quadrantLine1.setVisibility(View.GONE);
                quadrantLine2.setVisibility(View.GONE);
                quadrant1.setVisibility(View.GONE);
                quadrant2.setVisibility(View.GONE);
                quadrant3.setVisibility(View.GONE);
                quadrant4.setVisibility(View.GONE);
                quadrantFiller.fullscreenQuadrant(layoutSafe);
                beThere(layoutSafe);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        quadrantLine1.startAnimation(animation1);
        quadrantLine2.startAnimation(animation1);
    }

    private void beThere(LinearLayout layoutSafe) {
        if(layoutSafe.equals(quadrant1)||layoutSafe.equals(quadrant2)){
            quadrantLine1.setVisibility(View.VISIBLE);
        }
        if(layoutSafe.equals(quadrant3)||layoutSafe.equals(quadrant4)){
            quadrantLine2.setVisibility(View.VISIBLE);
        }


        layoutSafe.setVisibility(View.VISIBLE);
        Animation animation1  = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(200);
        animation1.setFillAfter(true);
        layoutSafe.startAnimation(animation1);
    }
    private void beGone(final LinearLayout subQuadrant){
        Animation animation1  = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(200);
        animation1.setFillAfter(true);
        animation1.setAnimationListener( new  Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                subQuadrant.setVisibility(View.GONE);
                quadrantFiller.minimizeQuadrant(subQuadrant);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setAllVisible();
                    }
                }, 50);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        subQuadrant.startAnimation(animation1);
    }
}
