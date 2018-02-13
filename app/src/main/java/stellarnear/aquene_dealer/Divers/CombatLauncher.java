package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;


public class CombatLauncher {
    Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private View dialogView;
    private Attack attack;
    private CheckBox medusa;
    private CheckBox ki;
    private CheckBox boots;
    private boolean fabMoved = false;
    private boolean dmgMoved = false;
    private boolean addAtkPanelIsVisible;
    private boolean manualDice;
    private SharedPreferences settings;
    private List<Roll> atksRolls;
    private CombatLauncherHitCritLines combatLauncherHitCritLines;

    public CombatLauncher(Activity mA, Context mC, Attack attack) {
        this.mA = mA;
        this.mC = mC;
        this.attack = attack;
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.combat_launcher_dialog, null);
        medusa = dialogView.findViewById(R.id.add_atk_medusa);
        ki = dialogView.findViewById(R.id.add_atk_ki);
        boots = dialogView.findViewById(R.id.add_atk_boots);

        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        buildCombatDialog();
    }

    private void buildCombatDialog() {
        ImageView img = dialogView.findViewById(R.id.combat_dialog_attack_img);
        int imgId = mC.getResources().getIdentifier(attack.getId(), "drawable", mC.getPackageName());
        img.setImageDrawable(mC.getDrawable(imgId));
        TextView title = dialogView.findViewById(R.id.combat_dialog_attack_name);
        title.setText(attack.getName());
        TextView summary = dialogView.findViewById(R.id.combat_dialog_attack_summary);
        summary.setText(attack.getDescr());

        final FloatingActionButton fab = dialogView.findViewById(R.id.fab);
        final FloatingActionButton fabDamage = dialogView.findViewById(R.id.fab_damage);
        final FloatingActionButton fabDetail = dialogView.findViewById(R.id.fab_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fabMoved) {
                    fab.animate().translationX(-mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();
                    startAttack();
                } else {
                    new android.app.AlertDialog.Builder(mC)
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Attaque déjà en cours...")
                            .setMessage("Es-tu sûre de vouloir relancer ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    buildPreRandValues();
                                    startAttack();
                                }

                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }

                            })
                            .show();
                }
                fabMoved = true;
                fabDamage.setVisibility(View.VISIBLE);
            }
        });

        fabDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dmgMoved) {
                    fabDamage.animate().translationX(mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();
                }
                dmgMoved = true;
                fabDetail.setVisibility(View.VISIBLE);
                startDamage();
            }
        });

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dmgMoved) {
                    fabDamage.animate().translationX(mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();
                }
                displayDetail();
            }
        });

        if (attack.getId().equalsIgnoreCase("attack_flurry")) {
            setAddAtkPanel();
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                unlockOrient();
            }
        });
        alertDialog = dialogBuilder.create();
    }

    private void setAddAtkPanel() {
        addAtkPanelIsVisible=false;
        final ImageButton addAtkButton = dialogView.findViewById(R.id.fab_add_atk);
        final Animation inFromTop = AnimationUtils.loadAnimation(mC,R.anim.infromtopaddatkpanel);
        final Animation outToTop = AnimationUtils.loadAnimation(mC,R.anim.outtotopaddatkpanel);
        final LinearLayout linearAddAtk=dialogView.findViewById(R.id.add_atk_linear);
        addAtkButton.setVisibility(View.VISIBLE);
        addAtkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addAtkPanelIsVisible){
                    linearAddAtk.setVisibility(View.VISIBLE);
                    linearAddAtk.startAnimation(inFromTop);
                    addAtkPanelIsVisible=true;
                } else {
                    linearAddAtk.startAnimation(outToTop);
                    linearAddAtk.setVisibility(View.GONE);
                    addAtkPanelIsVisible=false;
                }
            }
        });
    }

    private void buildPreRandValues()
    {
        clearLinear();
        combatLauncherHitCritLines.getPreRandValues();
    }

    private void clearLinear() {
        LinearLayout linear = dialogView.findViewById(R.id.combat_dialog_LinearLayoutMain);
        for(int i=0;i<linear.getChildCount();i++){
            linear.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void startAttack() {
        buildAtksList();
        combatLauncherHitCritLines.getRandValues();
    }

    private void buildAtksList() {
        atksRolls = new ArrayList<>();
        if (attack.getId().equalsIgnoreCase("attack_flurry")) {
            String att_base = settings.getString("jet_att_flurry", mC.getString(R.string.jet_att_flurry_DEF));
            String delim = ",";
            String[] list_att_base_string = att_base.split(delim);
            if (medusa.isChecked()){
                atksRolls.add(new Roll(mA,mC, toInt(list_att_base_string[0]))); atksRolls.add(new Roll(mA,mC, toInt(list_att_base_string[0])));
            }
            if (ki.isChecked()){
                atksRolls.add(new Roll(mA,mC, toInt(list_att_base_string[0])));
            }
            if (boots.isChecked()){
                atksRolls.add(new Roll(mA,mC, toInt(list_att_base_string[0])));
            }
            for (String each : list_att_base_string) {
                atksRolls.add(new Roll(mA,mC, toInt(each)));
            }
        } else {
            String att_base = settings.getString("jet_att", mC.getString(R.string.jet_att_DEF));
            String delim = ",";
            String[] list_att_base_string = att_base.split(delim);
            atksRolls.add(new Roll(mA,mC, toInt(list_att_base_string[0])));
        }
        combatLauncherHitCritLines =new CombatLauncherHitCritLines(mA,mC,dialogView,atksRolls);
        buildPreRandValues();
    }

    private void startDamage() {
        if(combatLauncherHitCritLines.isMegaFail()){

        } else {
            CombatLauncherDamageLines combatLauncherDamageLines = new CombatLauncherDamageLines(mA, mC, dialogView, atksRolls);
            combatLauncherDamageLines.getDamageLine();
        }
        changeCancelButtonToOk();
    }

    private void displayDetail() {
    }

    public void showAlertDialog() {
        alertDialog.show();
        lockOrient();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_combat_launcher_dialog) / 100f;
        alertDialog.getWindow().setLayout((int) (factor * size.x), (int) (factor * size.y));
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }

    private void changeCancelButtonToOk() {
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        onlyButton.setText("Ok");
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
    }
    private void lockOrient() {
        mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void unlockOrient() {
        mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private Integer toInt(String key) {
        Integer value = 0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e) {
        }
        return value;
    }
}