package stellarnear.aquene_dealer.Divers.CombatPanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class CombatAsker {
    private Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private LinearLayout layout;
    private ArrayList<View> stepsView = new ArrayList<View>();
    private Map<RadioButton, Attack> mapRadioAtkAtk = new HashMap<>();
    private Attack selectedAttack = null;
    private boolean moved;
    private boolean canCharge;
    private boolean chargeRange;

    private boolean range;
    private boolean farRange;
    private boolean outrange;

    private boolean kistep; //pour la dépense à la fin en confirmation
    private View.OnClickListener backToMainListner;
    private Tools tools= new Tools();

    public CombatAsker(Activity mA,Context mC, LinearLayout layout, View.OnClickListener backToMainListner) {
        this.mA=mA;
        this.mC = mC;
        this.layout = layout;
        this.backToMainListner = backToMainListner;
        buildRangeLine();
    }

    public void reset() {
        layout.removeAllViews();
        stepsView = new ArrayList<View>();
        mapRadioAtkAtk = new HashMap<>();
        selectedAttack = null;
        buildRangeLine();
    }

    private void buildRangeLine() {
        clearStep(0);
        CombatAskerRangeLine lineRange = new CombatAskerRangeLine(mC);
        setRadioButtonListnerRange(lineRange.getContactButton(), lineRange.getMidButton(), lineRange.getOutButton());
        stepsView.add(lineRange.getRangeLine());
        getLayout();
    }

    private void setRadioButtonListnerRange(final RadioButton contact, final RadioButton mid, final RadioButton out) {
        final List<RadioButton> listRadio = Arrays.asList(contact, mid, out);
        for (final RadioButton check : listRadio) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton checkToUnselect : listRadio) {
                            if (!checkToUnselect.equals(check)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if (check.equals(contact)) {
                            range = true;
                            farRange = false;
                            outrange = false;

                            chargeRange=false;
                            buildMovedLine();
                        } else if (check.equals(mid)) {
                            range = false;
                            farRange = false;
                            outrange = false;

                            chargeRange = true;
                            buildMovedLine();
                        } else {
                                farRange = true;
                                askForMovementFar();
                            }
                        }
                    }
                });
        }
    }

    private void askForMovementFar() {
        clearStep(1);
        final CombatAskerMovementFar lineMovementFar = new CombatAskerMovementFar(mC);

        setRadioButtonListnerFarRange(lineMovementFar.getChargeButton(),lineMovementFar.getKiRangeButton(),lineMovementFar.getOutrangeButton());

        Animation outLeft = AnimationUtils.loadAnimation(mC,R.anim.outtoleftfilled);
        final Animation inRight = AnimationUtils.loadAnimation(mC,R.anim.infromrightfilled);
        inRight.setFillAfter(true);
        outLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        clearStep(0);
                        stepsView.add(lineMovementFar.getKiMovementLine());
                        lineMovementFar.getKiMovementLine().startAnimation(inRight);
                        getLayout();
                    }
                }, 50);
            }
        });
        stepsView.get(0).startAnimation(outLeft);
    }

    private void setRadioButtonListnerFarRange(final RadioButton chargeRangeButton,final RadioButton kiRangeButton, final RadioButton outrangeButton) {
        final List<RadioButton> listRadio = Arrays.asList(chargeRangeButton,kiRangeButton, outrangeButton);
        for (final RadioButton check : listRadio) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton checkToUnselect : listRadio) {
                            if (!checkToUnselect.equals(check)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if (check.equals(chargeRangeButton)) {
                            range = true;
                            outrange = false;
                            chargeRange = true;
                        }
                        if (check.equals(kiRangeButton)) {
                            if (aquene.getAllResources().getResource("resource_ki").getCurrent()>=aquene.getAllKiCapacities().getKicapacity("kicapacity_step").getCost()) {
                                outrange = false;
                            } else { outrange = true; }
                            range = false;
                            chargeRange = false;
                        }
                        if (check.equals(outrangeButton)) {
                            range = false;
                            chargeRange = false;
                            outrange=true;
                        }
                        buildMovedLine();
                    }
                }
            });
        }
    }

    private void buildMovedLine() {
        clearStep(1);
        CombatAskerMovedLine lineMoved = new CombatAskerMovedLine(mC,chargeRange);
        setRadioButtonListnerMoved(lineMoved.getChargeButton(),lineMoved.getWalkButton(), lineMoved.getNoButton());
        stepsView.add(lineMoved.getMovedLine());
        getLayout();
    }

    private void setRadioButtonListnerMoved(final RadioButton charge,final RadioButton walk, final RadioButton no) {
        final List<RadioButton> listRadio = Arrays.asList(charge,walk, no);
        for (final RadioButton check : listRadio) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton checkToUnselect : listRadio) {
                            if (!checkToUnselect.equals(check)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if (check.equals(charge)) {
                            moved = false;  //j'ai pas encore bougé et je charge
                            canCharge =true;
                            tools.customToast(mC,"Tu auras -2 CA pendant un round.","center");
                        }
                        if (check.equals(walk)) {
                            moved = false;  //j'ai pas encore bougé
                            canCharge =false;
                        }
                        if (check.equals(no)) {
                            moved = true;   //je peux plus bouger
                            canCharge =false;
                        }
                        buildResultLine();
                    }
                }
            });
        }
    }

    private void clearStep(int rankStep) {
        while (stepsView.size() > rankStep) {
            stepsView.remove(stepsView.size() - 1);
        }
        getLayout();
    }


    private void buildResultLine() {
        clearStep(2);
        selectedAttack = null;
        CombatAskerResultLine lineResult = new CombatAskerResultLine(mA,mC,moved,range,farRange, chargeRange, canCharge,outrange);
        kistep=lineResult.getKistep();
        mapRadioAtkAtk=lineResult.getMapRadioAtkAtk();
        if( lineResult.attackToLaunch()){
            setRadioButtonListnerResult(lineResult.getListRadioAtk());
            stepsView.add(lineResult.getResultLine());
            getLayout();
        } else {
            stepsView.add(lineResult.getResultLine());
            addConfirmationLine();
        }
    }

    private void setRadioButtonListnerResult(final List<RadioButton> listRadioAtk) {
        for (final RadioButton atkButton : listRadioAtk) {
            atkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (atkButton.isChecked()) {
                        atkButton.getBackground().clearColorFilter();
                        for (RadioButton checkToUnselect : listRadioAtk) {
                            if (!checkToUnselect.equals(atkButton)) {
                                checkToUnselect.setChecked(false);
                                ColorMatrix matrix = new ColorMatrix();
                                matrix.setSaturation(0);
                                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                                checkToUnselect.getBackground().setColorFilter(filter);
                            }
                        }
                        selectedAttack = mapRadioAtkAtk.get(atkButton);
                        addConfirmationLine();
                    }
                }
            });
        }
    }

    private void addConfirmationLine() {
        clearStep(3);
        CombatAskerConfirmationLine lineConfirm = new CombatAskerConfirmationLine(mA,mC,selectedAttack,backToMainListner,kistep);
        stepsView.add(lineConfirm.getConfirmationLine());
        getLayout();
    }

    public void getLayout() {
        for (int i=0;i <layout.getChildCount();i++){
            View viewToRemove=layout.getChildAt(i);
            if (!stepsView.contains(viewToRemove)){
                layout.removeView(viewToRemove);
            }
        }

        for (View view : stepsView) {
            if (view.getParent()==null){layout.addView(view);}
        }
    }
}
