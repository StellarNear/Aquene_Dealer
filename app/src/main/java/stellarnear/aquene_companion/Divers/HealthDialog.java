package stellarnear.aquene_companion.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.preference.PreferenceManager;
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

import androidx.appcompat.app.AlertDialog;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;


public class HealthDialog {
    Perso aquene= MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private View dialogView;
    private OnRefreshEventListener mListener;
    private Tools tools=Tools.getTools();

    public HealthDialog(Activity mA, Context mC) {
        this.mA=mA;
        this.mC=mC;
        buildHealthDialog();
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
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
        regen.setText("régén (+"+aquene.getCurrentResourceValue("resource_regen")+")" );
        regen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int regen=aquene.getCurrentResourceValue("resource_regen");
                aquene.getAllResources().getResource("resource_hp").earn(regen);
                animateText(regen);
                setHealthWidth();
                refreshHpPanel();
                changeCancelButtonToOk();
            }
        });
        Button shield = dialogView.findViewById(R.id.button_healthDialog_shield);
        shield.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveEditText("Combien de points de vie temporaires as-tu gagné ?","shield");
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
            public void onClick(DialogInterface dialog, int id) { }
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
            out = AnimationUtils.loadAnimation(mA, R.anim.outtoleft_health);
        } else {
            numberTxt.setText("+"+number);
            numberTxt.setTextColor(mC.getColor(R.color.validation));
            in = AnimationUtils.loadAnimation(mA, R.anim.infromleft_health);
            out = AnimationUtils.loadAnimation(mA, R.anim.outtoright_health);
        }
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {   }

            @Override
            public void onAnimationEnd(Animation animation) {
                numberTxt.startAnimation(out);
            }
        });
        numberTxt.startAnimation(in);
        numberTxt.setVisibility(View.INVISIBLE);

    }

    private void giveEditText(String askText, final String mode) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mC);
        alert.setTitle(askText);
        final EditText inputEdit = new EditText(mC);
        inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        inputEdit.setFocusableInTouchMode(true);
        final InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);

        alert.setView(inputEdit);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final int val =  tools.toInt(inputEdit.getText().toString());
                if(mode.equalsIgnoreCase("shield")){
                    aquene.getAllResources().getResource("resource_hp").shield(val);
                    refreshHpPanel();
                }
                if(mode.equalsIgnoreCase("dmg")){
                    if(aquene.getCurrentResourceValue("resource_mythic_points")>0){
                        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
                        final int valReduc = 5*tools.toInt(settings.getString("mythic_tier", String.valueOf(mC.getResources().getInteger(R.integer.mythic_tier_def))));
                        new android.app.AlertDialog.Builder(mA)
                                .setIcon(R.drawable.ic_warning_black_24dp)
                                .setTitle("Absoprtion des coups")
                                .setMessage("Veux-tu utiliser un point mythique pour réduire ces dégats de "+valReduc+" ?"
                                +"\n\nRessources :\nPoint(s) mythique restant(s) : "+aquene.getCurrentResourceValue("resource_mythic_points"))
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        aquene.getAllResources().getResource("resource_hp").spend(val-valReduc);
                                        aquene.getAllResources().getResource("resource_mythic_points").spend(1);
                                        new PostData(mC,new PostDataElement("Utilisation d'absoprtion des coups","-1pt mythique, absorbe "+valReduc+"pts de dégats"));
                                        int currentAbsorption = tools.toInt(settings.getString("mythiccapacity_absorption_value",String.valueOf(mC.getResources().getInteger(R.integer.mythiccapacity_absorption_value_DEF))));
                                        settings.edit().putString("mythiccapacity_absorption_value", String.valueOf(valReduc+currentAbsorption)).apply();
                                        animateText(-val+valReduc);
                                        summaryText(-val+valReduc,0);
                                        refreshHpPanel();
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        aquene.getAllResources().getResource("resource_hp").spend(val);
                                        animateText(-val);
                                        summaryText(-val,0);
                                        refreshHpPanel();
                                    }
                                }).show();
                    } else {
                        aquene.getAllResources().getResource("resource_hp").spend(val);
                        animateText(-val);
                        summaryText(-val,0);
                        refreshHpPanel();
                    }
                }
                if(mode.equalsIgnoreCase("heal")){
                    int over = aquene.getAllResources().getResource("resource_hp").getCurrent()+val-aquene.getAllResources().getResource("resource_hp").getMax();
                    aquene.getAllResources().getResource("resource_hp").earn(val);
                    animateText(val);
                    if (over > 0){
                        summaryText(val,over);
                    } else {
                        summaryText(val,0);
                    }
                    refreshHpPanel();
                }

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

    private void summaryText(int number,int over) {
        TextView numberSummary = dialogView.findViewById(R.id.healthDialogSummary);
        numberSummary.setVisibility(View.VISIBLE);
        if (number <= 0){
            numberSummary.setText("Aie !\nTu as subi "+Math.abs(number)+" dégâts");
        } else {
            String texte = "Bravo !\nTu as été soigné de "+number+" points de vie";

            if(over > 0){
                texte+="\n("+over+" en excès)";
                makeShield(over);
            }
            numberSummary.setText(texte);
        }
        refreshHpPanel();
    }

    private void refreshHpPanel() {
        setHealthWidth();
        if(mListener!=null){mListener.onEvent();}
    }

    private void makeShield(final int over) {
        new android.app.AlertDialog.Builder(mC)
                .setIcon(R.drawable.ic_info_black_24dp)
                .setTitle("Ajout de points de vie temporaire")
                .setMessage("On ajoute "+over+" aux points de vie temporaires ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aquene.getAllResources().getResource("resource_hp").shield(over);
                        setHealthWidth();
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

    private void setHealthWidth() {
        TextView textLife = dialogView.findViewById(R.id.healthDialogHealth);
        TextView textLifeTitle = dialogView.findViewById(R.id.healthDialogHealthTitle);
        //Double percent = 100.0*aquene.getCurrentResourceValue("resource_hp")/aquene.getAllResources().getResource("resource_hp").getMax();
        String txt;
        String txtTitle;
        int shield = aquene.getAllResources().getResource("resource_hp").getShield();
        if(shield>0){
            txt=aquene.getCurrentResourceValue("resource_hp")+"/"+aquene.getAllResources().getResource("resource_hp").getMax();
            txt+= " ("+shield+")";
            txtTitle="Vie restante (points de vie temporaires) :";
        } else {
            txt=aquene.getCurrentResourceValue("resource_hp")+"/"+aquene.getAllResources().getResource("resource_hp").getMax();
            txtTitle="Vie restante :";
        }
        textLife.setText(txt);
        textLifeTitle.setText(txtTitle);

        final ImageView imgHealthBase = dialogView.findViewById(R.id.health_dialog_back_health_base);
        imgHealthBase.post(new Runnable() {
            @Override
            public void run() {
                ImageView imgHealth = dialogView.findViewById(R.id.health_dialog_back_health);
                ViewGroup.LayoutParams para= (ViewGroup.LayoutParams) imgHealth.getLayoutParams();
                int oriWidth=imgHealthBase.getMeasuredWidth();
                int oriHeight=imgHealthBase.getMeasuredHeight();
                int height=(int) (oriHeight*0.355); //c'est le rapport entre le haut gargouille et la barre
                Double coef = (double) aquene.getCurrentResourceValue("resource_hp")/aquene.getAllResources().getResource("resource_hp").getMax();
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