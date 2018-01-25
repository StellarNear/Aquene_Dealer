package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentCombat extends Fragment {
    Perso aquene = MainActivity.aquene;
    Map<RadioButton,String> mapModeButtonModKey=new HashMap<>();
    public MainActivityFragmentCombat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }


        View returnFragView= inflater.inflate(R.layout.fragment_main_combat, container, false);

        ImageButton buttonMain = (ImageButton) returnFragView.findViewById(R.id.button_frag_combat_to_main);

        moveTopRight(buttonMain);

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unlockOrient();
                Fragment fragment = new MainActivityFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_main_frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_normal),"normal");
        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_def),"def");
        mapModeButtonModKey.put((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_totaldef),"totaldef");

        RadioGroup combatModesRadio = new RadioGroup (getContext());
        combatModesRadio.addView((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_normal));
        combatModesRadio.addView((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_def));
        combatModesRadio.addView((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_totaldef));

        setCombatModeListner(combatModesRadio);




        return returnFragView;
    }

    private void setCombatModeListner(final RadioGroup radio) {

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("-State-","Changement de mode");
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    //do stuff with active mode
                    persoChange(mapModeButtonModKey.get(checkedRadioButton));
                }
            }
        });

    }

    private void persoChange(String mode) {
        aquene.setCombatMode(mode);
    }


    private void unlockOrient() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }


    private void moveTopRight(final ImageButton buttonMain) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Rect rect=new Rect();
                Rect rectParent=new Rect();
                buttonMain.getGlobalVisibleRect(rect);
                View parent = (View) buttonMain.getParent();
                parent.getGlobalVisibleRect(rectParent);
                float sizeFactor = 0.75f;
                int diffRescaleY = (int) ((rect.bottom-rect.top) - ((rect.bottom-rect.top)*sizeFactor) )/2;
                int diffRescaleX = (int) ((rect.right-rect.left) - ((rect.right-rect.left)*sizeFactor) )/2;
                buttonMain.animate().translationX( rectParent.right-rect.right+diffRescaleX).translationY(-diffRescaleY).setDuration(1000).scaleX(sizeFactor).scaleY(sizeFactor);

            }
        }, 25);
    }
}
