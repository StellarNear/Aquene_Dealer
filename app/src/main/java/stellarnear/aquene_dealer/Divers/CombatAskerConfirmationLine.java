package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatAskerConfirmationLine {
    private LinearLayout lineStep;
    private Perso aquene = MainActivity.aquene;

    public CombatAskerConfirmationLine(final Activity mA, final Context mC, final Attack selectedAttack, final View.OnClickListener backToMainListner, final Boolean kistep) {
        lineStep = new LinearLayout(mC);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        Button valid = new Button(mC);
        if (selectedAttack == null) {
            valid.setText("Quitter");
            valid.setTextColor(mC.getColor(R.color.colorBackground));
            valid.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
        } else {
            valid.setText("Confirmation");
            valid.setTextColor(mC.getColor(R.color.colorBackground));
            valid.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

        }
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String txt;
                if (selectedAttack == null) {
                    txt = "Retour au menu principal";
                } else {
                    txt = "Lancement de : " + selectedAttack.getName();
                    if (aquene.getAllResources().getResource(selectedAttack.getId().replace("attack", "resource")) != null) {
                        aquene.getAllResources().getResource(selectedAttack.getId().replace("attack", "resource")).spend(1);
                    }
                    if (kistep) {
                        aquene.getAllResources().getResource("resource_ki").spend(aquene.getAllKiCapacities().getKicapacity("kicapacity_step").getCost());
                    }
                    CombatLauncher combatLauncher = new CombatLauncher(mA, mC, selectedAttack);
                    combatLauncher.showAlertDialog();
                    combatLauncher.setFinishEventListener(new CombatLauncher.OnFinishEventListener() {
                        public void onEvent() {
                            View toMain = new View(mC);
                            toMain.setOnClickListener(backToMainListner);
                            toMain.performClick();
                            Snackbar snackbar = Snackbar.make(view, "Retour au menu principal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                }
                Snackbar snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        lineStep.addView(valid);

    }

    public LinearLayout getConfirmationLine() {
        return lineStep;
    }

}

