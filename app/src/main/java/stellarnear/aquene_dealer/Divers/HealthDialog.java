package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Display;
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

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;


public class HealthDialog {
    Perso aquene= MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private View dialogView;
    private TextView miniHealth,fullHealth;
    private Tools tools=new Tools();

    public HealthDialog(Activity mA, Context mC,TextView miniHealth,TextView fullHealth) {
        this.mA=mA;
        this.mC=mC;
        this.miniHealth=miniHealth;
        this.fullHealth=fullHealth;
        buildHealthDialog();
    }

    private void buildHealthDialog() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.health_dialog, null);

        setHealthWidth();

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
                animateText(regen);
                setHealthWidth();
                changeCancelButtonToOk();
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

    private void animateText(int number) {
        final TextView numberTxt = dialogView.findViewById(R.id.health_dialog_floating_numbers);
        numberTxt.setVisibility(View.VISIBLE);
        final Animation in,out;
        if (number <= 0){
            numberTxt.setText(String.valueOf(number));
            numberTxt.setTextColor(mC.getColor(R.color.cancel));
            in = AnimationUtils.loadAnimation(mA, R.anim.infromright_health);
            //in.setInterpolator(null);
            out = AnimationUtils.loadAnimation(mA, R.anim.outtoleft_health);
            //out.setInterpolator(null);
        } else {
            numberTxt.setText("+"+number);
            numberTxt.setTextColor(mC.getColor(R.color.validation));
            in = AnimationUtils.loadAnimation(mA, R.anim.infromleft_health);
            //in.setInterpolator(null);
            out = AnimationUtils.loadAnimation(mA, R.anim.outtoright_health);
            //out.setInterpolator(null);
        }

        in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                numberTxt.startAnimation(out);

            }
        });

        numberTxt.startAnimation(in);
        numberTxt.setVisibility(View.INVISIBLE);

        //le message résumé
        TextView numberSummary = dialogView.findViewById(R.id.healthDialogSummary);
        numberSummary.setVisibility(View.VISIBLE);
        if (number <= 0){
            numberSummary.setText("Aie !\nTu as subi "+Math.abs(number)+" dégâts.");
        } else {
            numberSummary.setText("Bravo !\nTu as été soigné de "+number+" points de vie.");
        }

        //refresh les hp dans main frag
        miniHealth.setText(String.valueOf(aquene.getResourceValue("resource_hp")));
        fullHealth.setText(String.valueOf(aquene.getResourceValue("resource_hp")));
    }

    private void giveEditText(String askText, final String mode) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mC);
        alert.setTitle(askText);
        final EditText inputEdit = new EditText(mC);
        inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        inputEdit.setFocusableInTouchMode(true);
        final InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);

        alert.setView(inputEdit);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int val =  tools.toInt(inputEdit.getText().toString());
                if(mode.equalsIgnoreCase("dmg")){
                    aquene.getAllResources().getResource("resource_hp").spend(val);
                    animateText(-val);
                } else {
                    aquene.getAllResources().getResource("resource_hp").earn(val);
                    animateText(val);
                }
                setHealthWidth();
                changeCancelButtonToOk();
                lManager.hideSoftInputFromWindow(inputEdit.getWindowToken(), 0);
            }
        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
        inputEdit.post(new Runnable() {
            public void run() {
                inputEdit.requestFocusFromTouch();
                lManager.showSoftInput(inputEdit, 0);
            }
        });
    }

    private void setHealthWidth() {
        TextView textLife = dialogView.findViewById(R.id.healthDialogTestTitle);
        Double percent = 100.0*aquene.getResourceValue("resource_hp")/aquene.getAllResources().getResource("resource_hp").getMax();
        String txt = aquene.getResourceValue("resource_hp")+"/"+aquene.getAllResources().getResource("resource_hp").getMax() +" ("+percent.intValue()+"%)";
        textLife.setText(txt);

        final ImageView imgHealthBase = dialogView.findViewById(R.id.health_dialog_back_health_base);
        imgHealthBase.post(new Runnable() {
            @Override
            public void run() {
                ImageView imgHealth = dialogView.findViewById(R.id.health_dialog_back_health);
                ViewGroup.LayoutParams paraBase= (ViewGroup.LayoutParams) imgHealthBase.getLayoutParams();
                ViewGroup.LayoutParams para= (ViewGroup.LayoutParams) imgHealth.getLayoutParams();

                int oriWidth=imgHealthBase.getMeasuredWidth();
                int oriHeight=imgHealthBase.getMeasuredHeight();

                int height=(int) (oriHeight*0.355); //c'est le rapport entre le haut gargouille et la barre

                Double coef = (double) aquene.getResourceValue("resource_hp")/aquene.getAllResources().getResource("resource_hp").getMax();
                if(coef<0d){coef=0d;} //pour les hp negatif
                if(coef>1d){coef=1d;}
                para.width=(int) (coef*oriWidth);
                para.height=height;
                imgHealth.setLayoutParams(para);

                if(coef>=0.75){
                    imgHealth.setImageDrawable(mC.getDrawable(R.drawable.bar_gradient_health_ok));
                } else if (coef <0.75 && coef >=0.5){
                    imgHealth.setImageDrawable(mC.getDrawable(R.drawable.bar_gradient_health_abovehalf));
                } else if (coef <0.5 && coef >=0.25){
                    imgHealth.setImageDrawable(mC.getDrawable(R.drawable.bar_gradient_health_underhalf));
                } else {
                    imgHealth.setImageDrawable(mC.getDrawable(R.drawable.bar_gradient_health_notok));
                }
            }
        });

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
}