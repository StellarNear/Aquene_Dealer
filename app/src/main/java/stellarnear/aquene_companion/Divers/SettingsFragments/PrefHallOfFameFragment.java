package stellarnear.aquene_companion.Divers.SettingsFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Divers.CustomAlertDialog;
import stellarnear.aquene_companion.Divers.FameEntry;
import stellarnear.aquene_companion.Divers.Stats.Stat;
import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;

public class PrefHallOfFameFragment extends Preference {
    private Perso aquene= MainActivity.aquene;
    private Context mC;
    private View mainView;
    private LinearLayout fameList;
    private Tools tools=Tools.getTools();

    public PrefHallOfFameFragment(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public PrefHallOfFameFragment(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
    public PrefHallOfFameFragment(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(ViewGroup parent)
    {
        super.onCreateView(parent);
        this.mC=getContext();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        mainView = inflater.inflate(R.layout.hall_of_fame, null);

        setSave();
        refreshHall();

        return mainView;
    }


    private void setSave() {
        ((LinearLayout)mainView.findViewById(R.id.hall_of_fame_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLast();
                refreshHall();
            }
        });
    }

    private void refreshHall() {
        fameList=(LinearLayout)mainView.findViewById(R.id.hall_of_frame_list);
        fameList.removeAllViews();
        for(final FameEntry fame : aquene.getHallOfFame().getHallOfFameList()){
            LinearLayout statLine = new LinearLayout(mC);
            statLine.setOrientation(LinearLayout.HORIZONTAL);
            statLine.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams para =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            para.setMargins(10,10,10,10);
            para.gravity=Gravity.CENTER;
            statLine.setLayoutParams(para);
            statLine.setMinimumHeight(150);
            statLine.setBackground(mC.getDrawable(R.drawable.background_border_fame));

            statLine.addView(iconAttack(fame.getAttackID()));
            statLine.addView(newTextInfo(fame.getSumDmg()+" dégâts"));
            statLine.addView(newTextInfo(fame.getFoeName()));
            statLine.addView(newTextInfo(fame.getLocation()));

            statLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
                    tools.customToast(mC,formater.format(fame.getStat().getDate())+"\n"+fame.getDetails(),"center");
                }
            });

            statLine.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    updateFame(fame);
                    return false;
                }
            });

            fameList.addView(statLine,0);
        }
    }

    private View iconAttack(String attackID) {
        LinearLayout iconLine = new LinearLayout(mC);
        iconLine.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        iconLine.setPadding(10,10,10,10);
        iconLine.setGravity(Gravity.CENTER);
        ImageView img = new ImageView(mC);
        try {
            int imgId = mC.getResources().getIdentifier(attackID, "drawable", mC.getPackageName());
            img.setImageDrawable(mC.getDrawable(imgId));
            tools.resize(img,120);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iconLine.addView(img);
        return iconLine;
    }

    private TextView newTextInfo(String txt) {
        TextView text = new TextView(mC);
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,2));
        text.setGravity(Gravity.CENTER);
        text.setText(txt);
        text.setTextColor(Color.DKGRAY);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTypeface(null, Typeface.BOLD);
        text.setPadding(10,10,10,10);
        return text;
    }


    private void saveLast() {
        Stat lastStat = aquene.getStats().getStatsList().getLastStat();
        if(lastStat==null){
            tools.customToast(mC, "Aucune attaque à enregistrer...", "center");
        } else {
            if (aquene.getHallOfFame().containsStat(lastStat)) {
                tools.customToast(mC, "Entrée déjà présente", "center");
            } else {
                addFameEntry(lastStat);
            }
        }
    }


    public void addFameEntry(final Stat lastStat) {
        LayoutInflater inflater = LayoutInflater.from(mC);
        final View addHallEntry = inflater.inflate(R.layout.custom_toast_hall_of_fame, null);

        CustomAlertDialog creationItemAlert = new CustomAlertDialog(null, mC, addHallEntry);
        creationItemAlert.setPermanent(true);
        creationItemAlert.addConfirmButton("Ajouter");
        creationItemAlert.addCancelButton("Annuler");
        creationItemAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                String foeName = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_foe_name)).getText().toString();
                String location = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_location)).getText().toString();
                String details = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_details)).getText().toString();
                aquene.getHallOfFame().addToHallOfFame(new FameEntry(lastStat,foeName,location,details));
                tools.customToast(mC,  "Entrée ajoutée !");
                refreshHall();
            }
        });
        creationItemAlert.showAlert();
        final EditText foe = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_foe_name));
        foe.post(new Runnable() {
            public void run() {
                foe.setFocusableInTouchMode(true);
                foe.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) mC.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(foe, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    private void updateFame(final FameEntry fame) {
        LayoutInflater inflater = LayoutInflater.from(mC);
        final View addHallEntry = inflater.inflate(R.layout.custom_toast_hall_of_fame, null);

        ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_foe_name)).setText(fame.getFoeName());
        ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_location)).setText(fame.getLocation());
        ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_details)).setText(fame.getDetails());

        CustomAlertDialog creationItemAlert = new CustomAlertDialog(null, mC, addHallEntry);
        creationItemAlert.setPermanent(true);
        creationItemAlert.addConfirmButton("Actualiser");
        creationItemAlert.addCancelButton("Annuler");
        creationItemAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                String foeName = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_foe_name)).getText().toString();
                String location = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_location)).getText().toString();
                String details = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_details)).getText().toString();
                fame.updateInfos(foeName,location,details);
                aquene.getHallOfFame().refreshSave();
                tools.customToast(mC,  "Entrée changée !");
                refreshHall();
            }
        });
        creationItemAlert.showAlert();
        final EditText foe = ((EditText) addHallEntry.findViewById(R.id.hall_of_fame_foe_name));
        foe.post(new Runnable() {
            public void run() {
                foe.setFocusableInTouchMode(true);
                foe.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) mC.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(foe, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

}
