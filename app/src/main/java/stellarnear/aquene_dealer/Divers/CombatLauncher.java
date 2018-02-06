package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;


public class CombatLauncher {
    Perso aquene= MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private View dialogView;
    private Attack attack;
    private boolean fabMoved=false;
    private boolean dmgMoved=false;
    private int nAtks;
    private List<Integer> atks=new ArrayList<>();


    public CombatLauncher(Activity mA,Context mC, Attack attack) {
        this.mA=mA;
        this.mC=mC;
        this.attack=attack;
        buildCombatDialog();
    }

    private void buildCombatDialog() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.combat_launcher_dialog, null);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);

        ImageView img = dialogView.findViewById(R.id.combat_dialog_attack_img);
        int imgId = mC.getResources().getIdentifier(attack.getId(), "drawable", mC.getPackageName());
        img.setImageDrawable(mC.getDrawable(imgId));
        TextView title = dialogView.findViewById(R.id.combat_dialog_attack_name);
        title.setText(attack.getName());


        if (attack.getId().equalsIgnoreCase("attack_flurry")){
            String att_base = settings.getString("jet_att_flurry",mC.getString(R.string.jet_att_flurry_def));
            String delim = ",";
            String[] list_att_base_string = att_base.split(delim);
            for (String each : list_att_base_string) {
                atks.add(toInt(each));
            }
            nAtks=list_att_base_string.length;
            buildAtksBonus();
        } else {
            String att_base = settings.getString("jet_att",mC.getString(R.string.jet_att_def));
            String delim = ",";
            String[] list_att_base_string = att_base.split(delim);
            atks.add(toInt(list_att_base_string[0]));
            nAtks=1;
            buildAtkBonus();
        }

        final FloatingActionButton fab = dialogView.findViewById(R.id.fab);
        final FloatingActionButton fabDamage = dialogView.findViewById(R.id.fab_damage);
        final FloatingActionButton fabDetail = dialogView.findViewById(R.id.fab_detail);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fabMoved){fab.animate().translationX(-mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();}
                fabMoved=true;
                fabDamage.setVisibility(View.VISIBLE);
                startAttack();
            }
        });

        fabDamage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dmgMoved){fabDamage.animate().translationX(mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();}
                dmgMoved=true;
                fabDetail.setVisibility(View.VISIBLE);
                startDamage();
            }
        });

        fabDetail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dmgMoved){fabDamage.animate().translationX(mC.getResources().getDimension(R.dimen.comabt_launcher_fab_mouvement)).start();}
                displayDetail();
            }
        });

        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked cancel button
            }
        });
        alertDialog = dialogBuilder.create();
    }

    private void startAttack() {
    }
    private void displayDetail() {
    }
    private void startDamage() {

        changeCancelButtonToOk();
    }

    private void buildAtksBonus() {
        LinearLayout mainLayout = dialogView.findViewById(R.id.combat_dialog_LinearLayoutMain);
        LinearLayout line = new LinearLayout(mC);
        line.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int generalMargin=(int)mC.getResources().getDimension(R.dimen.general_margin);
        params.setMargins(generalMargin,generalMargin,generalMargin,generalMargin);
        line.setLayoutParams(params);
        for (int i=0; i <atks.size();i++) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("+"+atks.get(i));
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            line.addView(atkTxt);
            if(i<atks.size()-1){
                TextView sep = new TextView(mC);
                sep.setText("/");
                sep.setGravity(Gravity.CENTER);
                sep.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
                line.addView(sep);
            }
        }
        mainLayout.addView(line);
    }

    private void buildAtkBonus() {
        LinearLayout mainLayout = dialogView.findViewById(R.id.combat_dialog_LinearLayoutMain);

    }



    public void showAlertDialog(){
        alertDialog.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_combat_launcher_dialog)/100f;
        alertDialog.getWindow().setLayout((int) (factor*size.x), (int)(factor*size.y));
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width=ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }

    private void changeCancelButtonToOk(){
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        onlyButton.setText("Ok");
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
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