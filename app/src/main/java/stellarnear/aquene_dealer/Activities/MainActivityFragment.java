package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    Perso aquene = MainActivity.aquene;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        View returnFragView= inflater.inflate(R.layout.fragment_main, container, false);

        ImageButton fabCombat = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_combat);
        setButtonActivity(fabCombat,new MainActivityFragmentCombat());

        ImageButton fabKi = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_ki);
        setButtonActivity(fabKi, new MainActivityFragmentKi());

        ImageButton fabSkill = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_skill);
        setButtonActivity(fabSkill,new MainActivityFragmentSkill());

        return returnFragView;
    }

    private void setButtonActivity(ImageButton button, final Fragment ActivityFragment) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOrient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_main_frame_layout, ActivityFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    private void lockOrient() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
