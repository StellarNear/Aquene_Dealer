package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

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
    private SharedPreferences settings;
    private List<Roll> atksRolls;
    private CombatLauncherHitCritLines combatLauncherHitCritLines;
    private CombatLauncherDamageLines combatLauncherDamageLines;
    private ImageButton addAtkButton;
    private OnFinishEventListener mListener;
    private Tools tools=new Tools();

    public CombatLauncher(Activity mA, Context mC, Attack attack) {
        this.mA = mA;
        this.mC = mC;
        this.attack = attack;
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.combat_launcher_dialog, null);
        addAtkButton = dialogView.findViewById(R.id.fab_add_atk);
        medusa = dialogView.findViewById(R.id.add_atk_medusa);
        ki = dialogView.findViewById(R.id.add_atk_ki);
        boots = dialogView.findViewById(R.id.add_atk_boots);
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        buildCombatDialog();
    }

    private void buildCombatDialog() {
        ImageView img = dialogView.findViewById(R.id.combat_dialog_attack_img);
        int imgId = mC.getResources().getIdentifier(attack.getId(), "drawable", mC.getPackageName());
        img.setImageDrawable(mC.getDrawable(imgId));
        TextView title = dialogView.findViewById(R.id.combat_dialog_attack_name);
        title.setText(attack.getName());
        ImageButton summary = dialogView.findViewById(R.id.fab_info_summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt=attack.getDescr();
                if(attack.hasSave()){
                    int val = 10+(int)(aquene.getAbilityScore(mC,"ability_lvl")/2.0)+aquene.getAbilityMod(mC,"ability_sagesse");
                    txt+="\n\nJet de sauvegarde (vigueur) que l'ennemi doit égaler : "+val;}
                tools.customToast(mC,txt,"center");
            }
        });
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
                                    startAttack();
                                    combatLauncherDamageLines.hideStatLine();
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
                    startDamage();
                }else {
                        new android.app.AlertDialog.Builder(mC)
                                .setIcon(R.drawable.ic_warning_black_24dp)
                                .setTitle("Nouveau jet de dégats")
                                .setMessage("Veux-tu lancer d'autres dégats ?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDamage();
                                        combatLauncherDamageLines.hideStatLine();
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
                dmgMoved = true;
                fabDetail.setVisibility(View.VISIBLE);
            }
        });

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mListener.onEvent();
            }
        });
        alertDialog = dialogBuilder.create();
    }

    public interface OnFinishEventListener {
        void onEvent();
    }

    public void setFinishEventListener(OnFinishEventListener eventListener) {
        mListener = eventListener;
    }

    private void setAddAtkPanel() {
        addAtkPanelIsVisible=false;
        final Animation inFromTop = AnimationUtils.loadAnimation(mC,R.anim.infromtopaddatkpanel);
        final Animation outToTop = AnimationUtils.loadAnimation(mC,R.anim.outtotopaddatkpanel);
        final LinearLayout linearAddAtk=dialogView.findViewById(R.id.add_atk_linear);
        addAtkButton.setVisibility(View.VISIBLE);
        addAtkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFromResource();
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

    private void refreshFromResource() {
        if(aquene.getAllResources().getResource("resource_ki").getCurrent()<1){ki.setChecked(false);ki.setVisibility(View.GONE);}
        if(aquene.getAllResources().getResource("resource_boot_add_atk").getCurrent()<1){boots.setChecked(false);boots.setVisibility(View.GONE);}
    }

    private void startAttack() {
        clearLinear();
        buildAtksList();
        if(addAtkPanelIsVisible){addAtkButton.performClick();} //pour le refermer
        if(ki.isChecked()){aquene.getAllResources().getResource("resource_ki").spend(1);}
        if(boots.isChecked()){aquene.getAllResources().getResource("resource_boot_add_atk").spend(1);}
        combatLauncherHitCritLines.getPreRandValues();
        combatLauncherHitCritLines.getRandValues();
    }

    private void clearLinear() {
        LinearLayout linear = dialogView.findViewById(R.id.combat_dialog_LinearLayoutMain);
        for(int i=0;i<linear.getChildCount();i++){
            linear.getChildAt(i).setVisibility(View.GONE);
        }
    }
    private void buildAtksList() {
        atksRolls = new ArrayList<>();
        String bonus_epic_att = settings.getString("attack_att_epic", String.valueOf(mC.getResources().getInteger(R.integer.attack_att_epic_DEF)));
        if (attack.getId().equalsIgnoreCase("attack_flurry")) {
            String att_base = settings.getString("jet_att_flurry", mC.getString(R.string.jet_att_flurry_DEF));
            String delim = ",";
            List<String> list_att_base_string = Arrays.asList(att_base.split(delim));
            List<Integer> list_att_base = tools.toInt(list_att_base_string);
            int prowess=tools.toInt(settings.getString("attack_prouesse_epic",String.valueOf( mC.getResources().getInteger(R.integer.attack_prouesse_epic_DEF))));
            if(prowess>0){
                list_att_base.set(list_att_base.size()-1,list_att_base.get(list_att_base.size()-1)+prowess);  //TODO:faire un truc d'assignation inteligent avec les parametre etc
            }
            if (medusa.isChecked()){  atksRolls.add(new Roll(mA,mC, list_att_base.get(0))); atksRolls.add(new Roll(mA,mC, list_att_base.get(0)));      }
            if (ki.isChecked()){      atksRolls.add(new Roll(mA,mC, list_att_base.get(0)));     }
            if (boots.isChecked()){   atksRolls.add(new Roll(mA,mC, list_att_base.get(0)));     }
            if ( settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))) {  atksRolls.add(new Roll(mA,mC, list_att_base.get(0)));  }

            for (Integer each : list_att_base) {
                    atksRolls.add(new Roll(mA,mC, each+tools.toInt(bonus_epic_att)));
            }
        } else {
            String att_base = settings.getString("jet_att", mC.getString(R.string.jet_att_DEF));
            String delim = ",";
            String[] list_att_base_string = att_base.split(delim);
            atksRolls.add(new Roll(mA,mC, tools.toInt(list_att_base_string[0]) +tools.toInt(bonus_epic_att)));
        }
        if(attack.isFromCharge()){
            for (Roll roll : atksRolls){ roll.setFromCharge();}
        }
        combatLauncherHitCritLines =new CombatLauncherHitCritLines(mA,mC,dialogView,atksRolls);
    }

    private void startDamage() {
        if(!combatLauncherHitCritLines.isMegaFail()){
            combatLauncherDamageLines = new CombatLauncherDamageLines(mA, mC, dialogView, atksRolls);
            combatLauncherDamageLines.getDamageLine();
            checkHighscore(combatLauncherDamageLines.getSum());
        }
        changeCancelButtonToOk();
    }

    private void checkHighscore(int sum) {
        TextView highscore = dialogView.findViewById(R.id.combat_dialog_highscore);
        highscore.setText("Précédent Record : "+String.valueOf(attack.getHighscore()));
            if(attack.isHighscore(sum)){
                tools.playVideo(mA,mC,"/raw/explosion");
                tools.customToast(mC, String.valueOf(sum) + " dégats !\nC'est un nouveau record !", "center");
            }
    }

    private void displayDetail() {
        combatLauncherDamageLines.showDialogDetail();
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
}