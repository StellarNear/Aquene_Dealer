package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        question.setTextColor(mC.getColor(R.color.start_back_color));
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setWeightSum(2);

        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height=(int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        CheckBox yes = new CheckBox(mC);
        yes.setButtonDrawable(null);
        yes.setBackground(mC.getDrawable(R.drawable.chimpanzee_stance_selector));
        yes.setLayoutParams(params);

        CheckBox no = new CheckBox(mC);
        no.setButtonDrawable(null);
        no.setBackground(mC.getDrawable(R.drawable.bear_stance_selector));
        no.setLayoutParams(params);


        LinearLayout yesBox =new LinearLayout(mC);
        LinearLayout noBox =new LinearLayout(mC);
        yesBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        noBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        yesBox.setGravity(Gravity.CENTER);
        noBox.setGravity(Gravity.CENTER);

        yesBox.addView(yes);
        noBox.addView(no);

        answers.addView(yesBox);
        answers.addView(noBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);

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

        //setRadioListnerMoved(answers);

        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);
        answers.setWeightSum(2);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
    }

    private void clearStep(int rankStep) {

        while (stepsView.size()>rankStep) {
            stepsView.remove(stepsView.size()-1);
        }
    }

    private void setRadioListnerMoved(RadioGroup answers) {
        answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("-State-","Changement de bouton");
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    //do stuff with active stance
                    if(checkedRadioButton.getText().toString().contains("Oui")){
                        moved=true;
                    } else {
                        moved=false;
                    }
                }
                clearStep(1);
                buildRangeLine();
                getLayout();
            }
        });
    }

    private void buildRangeLine() {
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("L'ennemi est à quelle distance ?");
        question.setTextSize(20);
        question.setTextColor(mC.getColor(R.color.start_back_color));
        lineStep.addView(question);

        RadioGroup answers = new RadioGroup(mC);
        answers.setGravity(Gravity.CENTER);
        answers.setOrientation(LinearLayout.HORIZONTAL);

        RadioButton contact = new RadioButton(mC);
        contact.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        contact.setText("Il est au corps à corps (<3m)");
        contact.setGravity(Gravity.CENTER);
        RadioButton mid = new RadioButton(mC);
        mid.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        mid.setText("Il est entre 3m et 90m");
        mid.setGravity(Gravity.CENTER);
        RadioButton out = new RadioButton(mC);
        out.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        out.setText("Il est audela de 90m");
        out.setGravity(Gravity.CENTER);

        answers.addView(contact);
        answers.addView(mid);
        answers.addView(out);

        setRadioListnerRange(answers);


        lineStep.addView(answers);

        stepsView.add(lineStep);
    }

    private void setRadioListnerRange(RadioGroup answers) {
            answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("-State-","Changement de bouton");
                    RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    boolean isChecked = checkedRadioButton.isChecked();
                    // If the radiobutton that has changed in check state is now checked...
                    if (isChecked)
                    {
                        //do stuff with active stance
                        if(checkedRadioButton.getText().toString().contains("corps")){
                            range=true;
                            outrange=false;
                        } else if(checkedRadioButton.getText().toString().contains("entre")) {
                            range=false;
                            outrange=false;
                        } else {
                            range=false;
                            outrange=true;
                        }
                    }
                    clearStep(2);
                    buildResultLine();
                    getLayout();
                }
            });
    }

    private void buildResultLine() {

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

        stepsView.add(result);
        getLayout();
    }

    public void getLayout(){
        layout.removeAllViews();
        for (View view : stepsView){
            layout.addView(view);
        }
    }

}
