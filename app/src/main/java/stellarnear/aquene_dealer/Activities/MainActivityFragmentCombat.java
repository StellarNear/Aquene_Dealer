package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        activateMode();

        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_normal));
        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_def));
        setCombatModeListner((RadioButton) returnFragView.findViewById(R.id.button_combat_mode_totaldef));





        return returnFragView;
    }

    private void activateMode() {
        for (RadioButton radio : mapModeButtonModKey.keySet()){
            if (aquene.getAttacks().getCombatMode().equalsIgnoreCase(mapModeButtonModKey.get(radio))){radio.setChecked(true);}
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

                String modeTxt="";
                if (mapModeButtonModKey.get(button).equalsIgnoreCase("normal")){
                    modeTxt="normal";
                }
                if (mapModeButtonModKey.get(button).equalsIgnoreCase("def")){
                    modeTxt="défensif";
                }
                if (mapModeButtonModKey.get(button).equalsIgnoreCase("totaldef")){
                    modeTxt="défense totale";
                }

                toastIt("Mode "+modeTxt+" activé.");
            }
        });

    }

    private void toastIt(String s) {
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    private void persoChange(String mode) {
        aquene.getAttacks().setCombatMode(mode);
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
