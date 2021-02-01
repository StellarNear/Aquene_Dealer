package stellarnear.aquene_companion.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

import stellarnear.aquene_companion.Divers.CombatPanel.CombatAsker;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentCombat extends Fragment {
    Perso aquene = MainActivity.aquene;
    View returnFragView;
    Map<RadioButton,String> mapModeButtonModKey=new HashMap<>();
    CombatAsker combatAsker;
    public MainActivityFragmentCombat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        returnFragView= inflater.inflate(R.layout.fragment_main_combat, container, false);

        ImageButton buttonMain = (ImageButton) returnFragView.findViewById(R.id.button_frag_combat_to_main);
        animate(buttonMain);
        View.OnClickListener listnerBackToMain = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unlockOrient();
                Fragment fragment = new MainActivityFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.infadefrag,R.animator.outtobotfrag);
                fragmentTransaction.replace(R.id.fragment_main_frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        buttonMain.setOnClickListener(listnerBackToMain);

        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_normal),"mode_normal");
        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_def),"mode_def");
        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_totaldef),"mode_totaldef");

        activateMode();

        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_normal));
        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_def));
        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_totaldef));

        combatAsker= new CombatAsker(getActivity(),getContext(),(LinearLayout) returnFragView.findViewById(R.id.scrollLinearCombat),listnerBackToMain);

        return returnFragView;
    }

    private void activateMode() {
        for (RadioButton radio : mapModeButtonModKey.keySet()){
            if (aquene.getAllAttacks().getCombatMode().equalsIgnoreCase(mapModeButtonModKey.get(radio))){radio.setChecked(true);}
        }
    }

    private void setCombatModeListner(final RadioButton button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RadioButton radio : mapModeButtonModKey.keySet()){
                    if (radio!=button){radio.setChecked(false);}
                }
                persoChange(mapModeButtonModKey.get(button));

                combatAsker.reset();
            }
        });

    }

    private void persoChange(String mode) {
        aquene.setCombatMode(getContext(),mode);
    }


    private void unlockOrient() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }


    private void animate(final ImageButton buttonMain) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation anim = new ScaleAnimation(1f,1.25f,1f,1.25f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setRepeatCount(1);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setDuration(666);

                buttonMain.startAnimation(anim);
            }
        }, getResources().getInteger(R.integer.translationFragDuration));
    }
}
