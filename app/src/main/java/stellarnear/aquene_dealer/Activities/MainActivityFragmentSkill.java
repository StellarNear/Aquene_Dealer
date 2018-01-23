package stellarnear.aquene_dealer.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentSkill extends Fragment {
    Perso aquene = MainActivity.aquene;
    LinearLayout linearSkillScroll;
    View returnFragView;
    public MainActivityFragmentSkill() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        returnFragView= inflater.inflate(R.layout.fragment_main_skill, container, false);

        linearSkillScroll=returnFragView.findViewById(R.id.skillscrollLayout);

        for (Skill skill : aquene.getAllSkills().getSkillsList()) {
            addAllColumns(skill);
        }

        ImageButton buttonMain = (ImageButton) returnFragView.findViewById(R.id.button_frag_skill_to_main);

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

    private void addAllColumns(Skill skill) {
        LinearLayout line = new LinearLayout(getContext());
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.icon_skills_list_height)));
        line.setBackground(getResources().getDrawable(R.drawable.skill_bar_gradient));
        setNameListnerRollSkill(line,skill);

        TextView nameTxt = new TextView(getContext());
        TextView nameTitle = returnFragView.findViewById(R.id.skillNameTitle);
        nameTxt.setLayoutParams(nameTitle.getLayoutParams());
        nameTxt.setText(skill.getName());
        int imgId = getResources().getIdentifier(skill.getId(), "drawable", getContext().getPackageName());
        nameTxt.setCompoundDrawablesWithIntrinsicBounds(resize(getContext().getDrawable(imgId)),null,null,null);
        nameTxt.setPadding(getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
        nameTxt.setGravity(Gravity.CENTER);

        TextView abiTxt = new TextView(getContext());
        TextView abiTitle = returnFragView.findViewById(R.id.skillAbiTitle);
        abiTxt.setLayoutParams(abiTitle.getLayoutParams());
        String abScore;
        if(aquene.getAllAbilities().getMod(skill.getAbilityDependence())>=0){
            abScore = "+"+aquene.getAllAbilities().getMod(skill.getAbilityDependence());
        } else {
            abScore = String.valueOf(aquene.getAllAbilities().getMod(skill.getAbilityDependence()));
        }
        abiTxt.setText(skill.getAbilityDependence() + " : " +abScore );
        abiTxt.setGravity(Gravity.CENTER);

        TextView rankTxt = new TextView(getContext());
        TextView rankTitle = returnFragView.findViewById(R.id.skillRankTitle);
        rankTxt.setLayoutParams(rankTitle.getLayoutParams());
        rankTxt.setText(String.valueOf(skill.getRank()));
        rankTxt.setGravity(Gravity.CENTER);

        TextView bonusTxt = new TextView(getContext());
        TextView bonusTitle = returnFragView.findViewById(R.id.skillBonusTitle);
        bonusTxt.setLayoutParams(bonusTitle.getLayoutParams());
        bonusTxt.setText(String.valueOf(skill.getBonus()));
        bonusTxt.setGravity(Gravity.CENTER);

        line.addView(nameTxt);
        line.addView(abiTxt);
        line.addView(rankTxt);
        line.addView(bonusTxt);

        linearSkillScroll.addView(line);

    }

    private void setNameListnerRollSkill(LinearLayout line,final Skill skill) {
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialog skillDialog = new CustomAlertDialog(getActivity(),getContext(),skill,aquene.getAllAbilities().getMod(skill.getAbilityDependence()));
                skillDialog.showAlertDialog();
            }
        });
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        int pixel_size_icon = (int) (getResources().getDimensionPixelSize(R.dimen.icon_skills_list_height)*0.8);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(getResources(), bitmapResized);
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
