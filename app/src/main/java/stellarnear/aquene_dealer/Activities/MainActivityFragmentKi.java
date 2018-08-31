package stellarnear.aquene_dealer.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Equipment;
import stellarnear.aquene_dealer.Perso.KiCapacity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentKi extends Fragment {
    private Perso aquene=MainActivity.aquene;
    private View returnFragView;
    private List<LinearLayout> allKiCapa=new ArrayList<>();
    private KiCapacity kiCapaSelected;
    private Button valid;
    private Tools tools=new Tools();
    public MainActivityFragmentKi() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        returnFragView= inflater.inflate(R.layout.fragment_main_ki, container, false);

        ImageButton buttonMain = (ImageButton) returnFragView.findViewById(R.id.button_frag_ki_to_main);

        animate(buttonMain);

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

        LinearLayout contentLinear = returnFragView.findViewById(R.id.kiFragmentContentLinear);
        addContent(contentLinear);


        return returnFragView;
    }

    private void backToMain() {
        unlockOrient();
        Fragment fragment = new MainActivityFragment();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.infadefrag,R.animator.outtoleftfrag);
        fragmentTransaction.replace(R.id.fragment_main_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addContent(LinearLayout contentLinear) {

        for (KiCapacity kiCapa : aquene.getAllKiCapacities().getAllKiCapacitiesList()){
            LinearLayout lineCapa = new LinearLayout(getContext());
            lineCapa.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            lineCapa.setOrientation(LinearLayout.HORIZONTAL);
            lineCapa.setGravity(Gravity.CENTER);
            lineCapa.setBackground(getResources().getDrawable(R.drawable.ki_bar_gradient));

            TextView nameTxt = new TextView(getContext());
            TextView nameTitle = returnFragView.findViewById(R.id.kiNameTitle);
            nameTxt.setLayoutParams(nameTitle.getLayoutParams());
            nameTxt.setText(kiCapa.getName());

            int imgId = getResources().getIdentifier(kiCapa.getId(), "drawable", getContext().getPackageName());
            nameTxt.setCompoundDrawablesWithIntrinsicBounds(tools.resize(getContext(),getContext().getDrawable(imgId),(int) (getResources().getDimensionPixelSize(R.dimen.icon_kicapacities_list))),null,null,null);
            nameTxt.setPadding(getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
            nameTxt.setGravity(Gravity.CENTER);

            lineCapa.addView(nameTxt);

            TextView summary = new TextView(getContext());
            TextView summaryTitle = returnFragView.findViewById(R.id.kiEffectTitle);
            summary.setLayoutParams(summaryTitle.getLayoutParams());
            summary.setGravity(Gravity.CENTER);
            summary.setTextSize(12);
            summary.setPadding(getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
            summary.setText(kiCapa.getDescr());
            lineCapa.addView(summary);

            TextView cost = new TextView(getContext());
            TextView costTitle = returnFragView.findViewById(R.id.kiCostTitle);
            cost.setLayoutParams(costTitle.getLayoutParams());
            cost.setGravity(Gravity.CENTER);
            cost.setText(String.valueOf(kiCapa.getCost()));
            lineCapa.addView(cost);

            setCapaLineListner(lineCapa,kiCapa);
            allKiCapa.add(lineCapa);

            contentLinear.addView(lineCapa);
        }

        LinearLayout lineStep = new LinearLayout(getContext());
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        valid = new Button(getContext());
        valid.setText("Confirmation");
        valid.setTextColor(getContext().getColor(R.color.colorBackground));
        valid.setBackground(getContext().getDrawable(R.drawable.button_basic_gradient));
        lineStep.addView(valid);

        contentLinear.addView(lineStep);

    }

    private void setCapaLineListner(final LinearLayout lineCapa, final KiCapacity kiCapa) {
        lineCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiCapaSelected=kiCapa;
                setLinearCapaColor(lineCapa);
            }
        });
    }

    private void setLinearCapaColor(LinearLayout lineCapa) {
        for (LinearLayout lin : allKiCapa){
            if (lin.equals(lineCapa)){
                lin.setBackground(getResources().getDrawable(R.drawable.ki_capacity_selected_bar_gradient));
            } else {
                lin.setBackground(getResources().getDrawable(R.drawable.ki_bar_gradient));
            }
        }

        if (aquene.getAllResources().getResource("resource_ki").getCurrent()-kiCapaSelected.getCost()>=0){
            valid.setBackground(getContext().getDrawable(R.drawable.button_ok_gradient));
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(kiCapaSelected.getId().equalsIgnoreCase("kicapacity_ki_armor")){
                        setTempArmor();
                    } else {
                        aquene.getAllResources().getResource("resource_ki").spend(kiCapaSelected.getCost());
                        String txt = "Lancement de : " + kiCapaSelected.getName();
                        if (kiCapaSelected.getId().equalsIgnoreCase("kicapacity_heal")) {
                            int heal = aquene.getAbilityScore(getContext(), "ability_lvl");
                            aquene.getAllResources().getResource("resource_hp").earn(heal);
                            txt += " (+" + heal + "pv)";
                        }

                        Snackbar snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        backToMain();
                    }
                }
            });
        } else {
            valid.setBackground(getContext().getDrawable(R.drawable.button_cancel_gradient));
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tools.customToast(getContext(),"Tu n'as pas assez de points de Ki pour faire cela","center");
                }
            });
        }


    }

    private void setTempArmor() {
        //TODO demander combien de lancement et ajotuer à l'armure temp (check le cap)

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View askSpendKi = inflater.inflate(R.layout.custom_toast_asker, null);
        LinearLayout mainLin = askSpendKi.findViewById(R.id.toast_asker_linear);
        TextView question = new TextView(getContext());
        question.setText("Combien de points de Ki souhaites tu dépenser ?");
        question.setGravity(Gravity.CENTER);
        mainLin.addView(question);

        final NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(1);

        int lvl = aquene.getAbilityScore(getContext(),"ability_lvl");
        int kiPoint = aquene.getResourceValue(getContext(),"resource_ki");
        if(lvl/4<kiPoint) {
            numberPicker.setMaxValue(lvl / 4);
        }else{
            numberPicker.setMaxValue(kiPoint);
        }
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(1);

        mainLin.addView(numberPicker);

        final TextView armorResult = new TextView(getContext());
        armorResult.setText("Ca te fera "+2*numberPicker.getValue()+" de CA");
        armorResult.setGravity(Gravity.CENTER);

        mainLin.addView(armorResult);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                armorResult.setText("Ca te fera "+2*numberPicker.getValue()+" de CA");
            }
        });

        CustomAlertDialog askerPopup = new CustomAlertDialog(getActivity(), getContext(), askSpendKi);
        askerPopup.setPermanent(true);
        askerPopup.addConfirmButton("Accepter");
        askerPopup.addCancelButton("Annuler");
        askerPopup.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                String txt = "Lancement de : " + kiCapaSelected.getName();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());;
                int bonusKiArmor= tools.toInt(settings.getString("bonus_ki_armor", String.valueOf(getContext().getResources().getInteger(R.integer.bonus_ki_armor_DEF))));

                if (bonusKiArmor>=2*numberPicker.getValue()){
                    tools.customToast(getContext(), "Tu as une Armure Ki plus puissante !","center");
                    backToMain();
                } else {
                    aquene.getAllResources().getResource("resource_ki").spend(numberPicker.getValue());
                    settings.edit().putString("bonus_ki_armor",String.valueOf(2*numberPicker.getValue())).apply();
                    tools.customToast(getContext(), "Tu as gagné " + String.valueOf(2 * numberPicker.getValue()) + " de CA temporaire !", "center");
                    Snackbar snackbar = Snackbar.make(getView(), txt, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    backToMain();
                }
            }
        });
        askerPopup.showAlert();
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
