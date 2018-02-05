package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Ability;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.R;


public class HealthDialog {
    Perso aquene= MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private View dialogView;
    private int modBonus;
    String mode;

    public HealthDialog(Activity mA, Context mC) {
        this.mA=mA;
        this.mC=mC;
        buildHealthDialog();
    }

    private void buildHealthDialog() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.health_dialog, null);

        setHealthHeight();

        Button heal = dialogView.findViewById(R.id.button_healthDialog_heal);
        heal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveEditText("De combien de dégâts as-tu été soigné ?","heal");
            }
        });

        Button regen = dialogView.findViewById(R.id.button_healthDialog_regen);
        regen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int regen=aquene.getResourceValue("resource_regen");
                aquene.getAllResources().getResource("resource_hp").earn(regen);
                setHealthHeight();
            }
        });

        Button dmg = dialogView.findViewById(R.id.button_healthDialog_dmg);
        dmg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveEditText("Combien de dégâts as-tu subi ?","dmg");
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

    private void giveEditText(String askText, final String mode) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mC);
        alert.setTitle(askText);
        final EditText inputEdit = new EditText(mC);
        inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        inputEdit.setFocusableInTouchMode(true);
        alert.setView(inputEdit);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int val = toInt(inputEdit.getText().toString());
                if(mode.equalsIgnoreCase("dmg")){
                    aquene.getAllResources().getResource("resource_hp").spend(val);
                    setHealthHeight();
                } else {
                    aquene.getAllResources().getResource("resource_hp").earn(val);
                    setHealthHeight();
                }
            }
        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
        inputEdit.post(new Runnable() {
            public void run() {
                inputEdit.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(inputEdit, 0);
            }
        });
    }

    private void setHealthHeight() {
        TextView textLife = dialogView.findViewById(R.id.healthDialogTestTitle);
        textLife.setText(aquene.getResourceValue("resource_hp")+"/"+aquene.getAllResources().getResource("resource_hp").getMax());

        ImageView imgHealth = dialogView.findViewById(R.id.health_dialog_back_health);
        ImageView imgHealthBase = dialogView.findViewById(R.id.health_dialog_back_health_base);
        ViewGroup.LayoutParams paraBase= (ViewGroup.LayoutParams) imgHealthBase.getLayoutParams();
        ViewGroup.LayoutParams para= (ViewGroup.LayoutParams) imgHealth.getLayoutParams();
        int oriHeight=paraBase.height;
        Double coef = (double) aquene.getResourceValue("resource_hp")/aquene.getAllResources().getResource("resource_hp").getMax();
        para.height=(int) (coef*oriHeight);
        imgHealth.setLayoutParams(para);
    }

    public void showAlertDialog(){
        alertDialog.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_customdialog)/100f;
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