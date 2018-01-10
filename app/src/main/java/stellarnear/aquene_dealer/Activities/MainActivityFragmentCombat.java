package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentCombat extends Fragment {
    Perso aquene = MainActivity.aquene;
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

        TextView testTextFor=returnFragView.findViewById(R.id.textCombat);
        testTextFor.setText("La valeur de la force est :"+aquene.getAbilities().getFOR());

        LinearLayout linearComabatFrag=returnFragView.findViewById(R.id.linearCombatFrag);

        for (Feat feat : aquene.getAllFeats().getFeatsList()) {
            TextView test = new TextView(getActivity());
            test.setText(feat.getName() + " : "+aquene.featIsActive(feat.getId()));
            linearComabatFrag.addView(test);
        }

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


        return returnFragView;
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
