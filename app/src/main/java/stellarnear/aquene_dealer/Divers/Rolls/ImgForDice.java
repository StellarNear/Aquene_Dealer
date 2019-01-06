package stellarnear.aquene_dealer.Divers.Rolls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;
import stellarnear.aquene_dealer.Divers.Tools;

public class ImgForDice {
    private Activity mA;
    private Context mC;
    private Dice dice;
    private Dice surgeDice;
    private ImageView img;
    private Tools tools = new Tools();
    private Perso aquene = MainActivity.aquene;

    public ImgForDice(Dice dice, Activity mA, Context mC) {
        this.mA = mA;
        this.mC = mC;
        this.dice = dice;
    }

    public ImageView getImg() {
        if (this.img == null) {
            int drawableId;
            if (dice.getRandValue() > 0) {
                drawableId = mC.getResources().getIdentifier("d" + dice.getnFace() + "_" + String.valueOf(dice.getRandValue()) + dice.getElement(), "drawable", mC.getPackageName());
            } else {
                drawableId = mC.getResources().getIdentifier("d" + dice.getnFace() + "_main", "drawable", mC.getPackageName());
            }
            this.img = new ImageView(mC);
            this.img.setImageDrawable(tools.resize(mC, drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_wheel_size)));

            if (dice.getnFace() == 20) {
                setMythicSurge(); //on assigne un lsitener pour creer le des mythique si clic sur l'image du dès
            }
        }
        return this.img;
    }

      /*

    Partie Mythique !

     */

    private void setMythicSurge() {
        this.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mC)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setTitle("Montée en puissance")
                        .setMessage("Ressources :\n\n" +
                                "Point(s) mythique restant(s) : "+aquene.getResourceValue(mC,"mythic_points")+"\n" +
                                "Point(s) légendaire restant(s) : "+aquene.getResourceValue(mC,"legendary_points"))
                        .setNeutralButton("Aucune", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("Mythique", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                launchingMythicDice("mythique");
                            }
                        })
                        .setNegativeButton("Legendaire", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                launchingMythicDice("légendaire");
                            }
                        }).show();
            }
        });
    }

    private void launchingMythicDice(String mode) {
        int points=0;
        if(mode.equalsIgnoreCase("légendaire")){
            points = MainActivity.aquene.getResourceValue(mC,"legendary_points");
        } else {
            points = MainActivity.aquene.getResourceValue(mC,"mythic_points");
        }

        if (points > 0 && dice.getMythicDice()==null) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            if(mode.equalsIgnoreCase("légendaire")){
                surgeDice=new Dice(mC, tools.toInt(settings.getString("legendary_dice",String.valueOf(mC.getResources().getInteger(R.integer.legendary_dice_def)))));
                MainActivity.aquene.getAllResources().getResource("legendary_points").spend(1);
            } else {
                surgeDice=new Dice(mC, tools.toInt(settings.getString("mythic_dice",String.valueOf(mC.getResources().getInteger(R.integer.mythic_dice_def)))));
                MainActivity.aquene.getAllResources().getResource("mythic_points").spend(1);
            }

            if (settings.getBoolean("switch_manual_diceroll",mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF))){
                surgeDice.rand(true);
                surgeDice.setRefreshEventListener(new Dice.OnRefreshEventListener() {
                    @Override
                    public void onEvent() {
                        dice.setMythicDice(surgeDice);
                        toastResultDice();
                        newImgWithSurge();
                    }
                });
            } else {
                surgeDice.rand(false);
                dice.setMythicDice(surgeDice);
                toastResultDice();
                newImgWithSurge();
            }
        } else if (dice.getMythicDice()!=null) {
            tools.customToast(mC, "Tu as déjà fais une montée en puissance sur ce dès", "center");
        } else {
            tools.customToast(mC, "Tu n'as plus de point "+mode, "center");
        }
    }

    private void newImgWithSurge() {
        int subSize = mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size_double_dice_sub);
        LayerDrawable finalDrawable = new LayerDrawable(new Drawable[]{tools.resize(mC, this.img.getDrawable(), subSize), tools.resize(mC, surgeDice.getImg().getDrawable(), subSize)});

        int splitSize = mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size_double_dice_split);
        finalDrawable.setLayerInsetTop(1, splitSize);
        finalDrawable.setLayerInsetStart(1, splitSize);
        finalDrawable.setLayerGravity(0, Gravity.START | Gravity.TOP);
        finalDrawable.setLayerGravity(1, Gravity.END | Gravity.BOTTOM);

        this.img.setImageDrawable(finalDrawable);
    }

    private void toastResultDice() {
        LinearLayout linear = new LinearLayout(mC);
        int marge = 2 * mC.getResources().getDimensionPixelSize(R.dimen.general_margin);
        linear.setPadding(marge, marge, marge, marge);
        linear.setBackground(mC.getDrawable(R.drawable.background_border_infos));
        linear.setOrientation(LinearLayout.VERTICAL);

        TextView text = new TextView(mC);
        text.setText("Résultat du dès :");
        linear.addView(text);
        linear.addView(surgeDice.getImg());
        Toast toast = new Toast(mC);
        toast.setView(linear);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
