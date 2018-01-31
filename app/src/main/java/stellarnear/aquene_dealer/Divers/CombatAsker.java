package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class CombatAsker {
    private Perso aquene= MainActivity.aquene;
    private Context mC;
    private int lastStep;
    private LinearLayout layout;
    ArrayList<View> stepsView = new ArrayList<View>();

    private boolean moved;
    private boolean range;
    private boolean outrange;

    public CombatAsker(Context mC, LinearLayout layout) {
        this.mC=mC;
        this.layout=layout;
        buildMovedLine();
    }

    private void buildMovedLine() {

        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("As tu bougé ?");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));

        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setWeightSum(2);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);
        answers.setPadding(0,(int) mC.getResources().getDimension(R.dimen.general_margin),0,0);

        LinearLayout yesBox =new LinearLayout(mC);
        LinearLayout noBox =new LinearLayout(mC);
        yesBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        noBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        yesBox.setGravity(Gravity.CENTER);
        noBox.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        CheckBox yes = new CheckBox(mC);
        yes.setButtonDrawable(null);
        yes.setBackground(mC.getDrawable(R.drawable.moving_selector));
        yes.setLayoutParams(params);

        CheckBox no = new CheckBox(mC);
        no.setButtonDrawable(null);
        no.setBackground(mC.getDrawable(R.drawable.notmoving_selector));
        no.setLayoutParams(params);

        setCheckboxListnerMoved(yes,no);
        yesBox.addView(yes);
        noBox.addView(no);

        answers.addView(yesBox);
        answers.addView(noBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0,0,0,(int) mC.getResources().getDimension(R.dimen.general_margin));

        TextView yesTxt=new TextView(mC);
        yesTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        yesTxt.setGravity(Gravity.CENTER);
        yesTxt.setText("Oui j'ai bougé");
        TextView noTxt=new TextView(mC);
        noTxt.setText("Non j'ai rien fais");
        noTxt.setGravity(Gravity.CENTER);
        noTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        buttonTxt.addView(yesTxt);
        buttonTxt.addView(noTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
    }

    private void setCheckboxListnerMoved(final CheckBox yes,final  CheckBox no) {
        final List<CheckBox> listCheck = Arrays.asList(yes,no);
        for(final CheckBox check : listCheck)
        {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (isChecked){
                        for(CheckBox checkToUnselect : listCheck){
                            if(!checkToUnselect.equals(check)){
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if(check.equals(yes)){
                            moved=true;
                        } else {
                            moved=false;
                        }
                        buildRangeLine();
                    }
                }
            });
        }
    }

    private void clearStep(int rankStep) {

        while (stepsView.size()>rankStep) {
            stepsView.remove(stepsView.size()-1);
        }
    }

    private void buildRangeLine() {
        clearStep(1);
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("A quelle distance est l'ennemi ?");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setWeightSum(3);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);
        answers.setPadding(0,(int) mC.getResources().getDimension(R.dimen.general_margin),0,0);

        LinearLayout contactBox =new LinearLayout(mC);
        LinearLayout midBox =new LinearLayout(mC);
        LinearLayout outrangeBox =new LinearLayout(mC);
        contactBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        midBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        outrangeBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        contactBox.setGravity(Gravity.CENTER);
        midBox.setGravity(Gravity.CENTER);
        outrangeBox.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        CheckBox contact = new CheckBox(mC);
        contact.setButtonDrawable(null);
        contact.setBackground(mC.getDrawable(R.drawable.contact_selector));
        contact.setLayoutParams(params);

        CheckBox mid = new CheckBox(mC);
        mid.setButtonDrawable(null);
        mid.setBackground(mC.getDrawable(R.drawable.mid_range_selector));
        mid.setLayoutParams(params);

        CheckBox out = new CheckBox(mC);
        out.setButtonDrawable(null);
        out.setBackground(mC.getDrawable(R.drawable.out_range_selector));
        out.setLayoutParams(params);

        setCheckboxListnerRange(contact,mid,out);
        contactBox.addView(contact);
        midBox.addView(mid);
        outrangeBox.addView(out);

        answers.addView(contactBox);
        answers.addView(midBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0,0,0,(int) mC.getResources().getDimension(R.dimen.general_margin));

        TextView contactTxt=new TextView(mC);
        contactTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        contactTxt.setGravity(Gravity.CENTER);
        contactTxt.setText("Moins de 6m");
        TextView midTxt=new TextView(mC);
        midTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        midTxt.setGravity(Gravity.CENTER);
        midTxt.setText("Entre 6m et 90m");
        TextView outTxt=new TextView(mC);
        outTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        outTxt.setGravity(Gravity.CENTER);
        outTxt.setText("Plus de 90m");

        buttonTxt.addView(contactTxt);
        buttonTxt.addView(midTxt);
        buttonTxt.addView(outTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
    }

    private void setCheckboxListnerRange(final CheckBox contact,final CheckBox mid,final CheckBox out) {
        final List<CheckBox> listCheck = Arrays.asList(contact,mid,out);
        for(final CheckBox check : listCheck)
        {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (isChecked){
                        for(CheckBox checkToUnselect : listCheck){
                            if(!checkToUnselect.equals(check)){
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if(check.equals(contact)){
                            range=true;
                            outrange=false;
                        } else if(check.equals(mid)) {
                            range=false;
                            outrange=false;
                        } else {
                            range=false;
                            outrange=true;
                        }
                        buildResultLine();
                    }
                }
            });
        }
    }

    private void buildResultLine() {
        clearStep(2);
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("Action(s) possible(s) :");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        lineStep.addView(question);

        //pas oublier le padding top du prochain element linestep
        TextView result =new TextView(mC);
        String resultTxt="-";
        if (outrange){
            resultTxt="laisse tomber il est trop loin";
        }else if (!moved&&range&&!outrange){
            resultTxt="FULL ATTACK";
        } else if (!moved&&!range&&!outrange){
            resultTxt="bouge et attaque";
        }else if (moved&&range&&!outrange){
            resultTxt="attaque normal";
        } else if (moved&&!range&&!outrange){
            resultTxt="t'as bougé mais il est pas trop loin pour la prochaine fois";
        }
        result.setText(resultTxt);

        lineStep.addView(result);

        LinearLayout lineStep2 = new LinearLayout(mC);
        TextView confirm = new TextView(mC);
        confirm.setText("Confirmation");
        lineStep2.setBackgroundColor(mC.getColor(R.color.validation));
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1); //le weight est là pour que ca remplisse le restant du layout

        lineStep2.addView(confirm);
        lineStep2.setLayoutParams(para);

        stepsView.add(lineStep);
        stepsView.add(lineStep2);

        getLayout();
    }

    public void getLayout(){
        layout.removeAllViews();
        for (View view : stepsView){
            layout.addView(view);
        }
    }

}
