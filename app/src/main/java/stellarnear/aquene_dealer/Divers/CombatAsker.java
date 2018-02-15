package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class CombatAsker {
    private Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private int lastStep;
    private LinearLayout layout;
    private ArrayList<View> stepsView = new ArrayList<View>();
    private Map<RadioButton, Attack> mapRadioAtkAtk = new HashMap<>();
    private Attack selectedAttack = null;
    private boolean moved;
    private boolean range;
    private boolean kistep;
    private boolean outrange;
    View.OnClickListener backToMainListner;

    public CombatAsker(Activity mA,Context mC, LinearLayout layout, View.OnClickListener backToMainListner) {
        this.mA=mA;
        this.mC = mC;
        this.layout = layout;
        this.backToMainListner = backToMainListner;
        buildMovedLine();
    }

    public void reset() {
        layout.removeAllViews();
        stepsView = new ArrayList<View>();
        mapRadioAtkAtk = new HashMap<>();
        selectedAttack = null;
        buildMovedLine();
    }

    private void buildMovedLine() {
        clearStep(0);
        CombatAskerMovedLine lineMoved = new CombatAskerMovedLine(mC);
        setRadioButtonListnerMoved(lineMoved.getYesButton(), lineMoved.getNoButton());
        stepsView.add(lineMoved.getMovedLine());
        getLayout();
    }

    private void setRadioButtonListnerMoved(final RadioButton yes, final RadioButton no) {
        final List<RadioButton> listRadio = Arrays.asList(yes, no);
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
                        if (check.equals(yes)) {
                            moved = true;
                        } else {
                            moved = false;
                        }
                        buildRangeLine();
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

    private void buildRangeLine() {
        clearStep(1);
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
                            kistep=false;
                            outrange = false;
                            buildResultLine();
                        } else if (check.equals(mid)) {
                            range = false;
                            kistep=false;
                            outrange = false;
                            buildResultLine();
                        } else {
                            if (!moved && aquene.getAllResources().getResource("resource_ki").getCurrent()>=aquene.getAllKiCapacities().getKicapacity("kicapacity_step").getCost()) {
                                range = false;
                                askForKiMovement();
                            } else {
                                range = false;
                                kistep=false;
                                outrange = true;
                                buildResultLine();
                            }
                        }
                    }
                }
            });
        }
    }

    private void askForKiMovement() {
        clearStep(2);
        final CombatAskerKiMovment lineKiMovement = new CombatAskerKiMovment(mC);

        setRadioButtonListnerKiRange(lineKiMovement.getContactButton(),lineKiMovement.getOutButton());

        Animation outLeft = AnimationUtils.loadAnimation(mC,R.anim.outtoleft);
        final Animation inRight = AnimationUtils.loadAnimation(mC,R.anim.infromright);
        inRight.setFillAfter(true);
        outLeft.setAnimationListener(new Animation.AnimationListener() {
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
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        clearStep(1);
                        stepsView.add(lineKiMovement.getKiMovementLine());
                        lineKiMovement.getKiMovementLine().startAnimation(inRight);
                        getLayout();
                    }
                }, 50);
            }
        });
        stepsView.get(1).startAnimation(outLeft);
    }

    private void setRadioButtonListnerKiRange(final RadioButton contact, final RadioButton out) {
        final List<RadioButton> listRadio = Arrays.asList(contact, out);
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
                            kistep=true;
                            outrange=false;
                        } else {
                            kistep=false;
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
        selectedAttack = null;
        CombatAskerResultLine lineResult = new CombatAskerResultLine(mA,mC,moved,range,outrange,kistep);

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
