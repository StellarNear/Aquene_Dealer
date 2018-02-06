package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.Perso.KiCapacity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentKi extends Fragment {
    Perso aquene=MainActivity.aquene;
    View returnFragView;
    List<LinearLayout> allKiCapa=new ArrayList<>();
    KiCapacity kiCapaSelected;
    Button valid;
    public MainActivityFragmentKi() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        returnFragView= inflater.inflate(R.layout.fragment_main_ki, container, false);

        ImageButton buttonMain = (ImageButton) returnFragView.findViewById(R.id.button_frag_ki_to_main);

        animate(buttonMain);

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

        LinearLayout contentLinear = returnFragView.findViewById(R.id.kiFragmentContentLinear);
        addContent(contentLinear);


        return returnFragView;
    }

    private void backToMain() {
        unlockOrient();
        Fragment fragment = new MainActivityFragment();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.infadefrag,R.animator.outtoleftfrag);
        fragmentTransaction.replace(R.id.fragment_main_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addContent(LinearLayout contentLinear) {

        for (KiCapacity kiCapa : aquene.getAllKiCapacities().getAllKiCapacitiesList()){
            LinearLayout lineCapa = new LinearLayout(getContext());
            lineCapa.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            lineCapa.setOrientation(LinearLayout.HORIZONTAL);
            lineCapa.setGravity(Gravity.CENTER);
            lineCapa.setBackground(getResources().getDrawable(R.drawable.ki_bar_gradient));

            TextView nameTxt = new TextView(getContext());
            TextView nameTitle = returnFragView.findViewById(R.id.kiNameTitle);
            nameTxt.setLayoutParams(nameTitle.getLayoutParams());
            nameTxt.setText(kiCapa.getName());
            int imgId = getResources().getIdentifier(kiCapa.getId(), "drawable", getContext().getPackageName());
            nameTxt.setCompoundDrawablesWithIntrinsicBounds(resize(getContext().getDrawable(imgId)),null,null,null);
            nameTxt.setPadding(getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
            nameTxt.setGravity(Gravity.CENTER);

            lineCapa.addView(nameTxt);

            TextView summary = new TextView(getContext());
            TextView summaryTitle = returnFragView.findViewById(R.id.kiEffectTitle);
            summary.setLayoutParams(summaryTitle.getLayoutParams());
            summary.setGravity(Gravity.CENTER);
            summary.setTextSize(12);
            summary.setPadding(getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
            summary.setText(kiCapa.getDescr());
            lineCapa.addView(summary);

            TextView cost = new TextView(getContext());
            TextView costTitle = returnFragView.findViewById(R.id.kiCostTitle);
            cost.setLayoutParams(costTitle.getLayoutParams());
            cost.setGravity(Gravity.CENTER);
            cost.setText(String.valueOf(kiCapa.getCost()));
            lineCapa.addView(cost);

            setCapaLineListner(lineCapa,kiCapa);
            allKiCapa.add(lineCapa);

            contentLinear.addView(lineCapa);
        }

        LinearLayout lineStep = new LinearLayout(getContext());
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        valid = new Button(getContext());
        valid.setText("Confirmation");
        valid.setTextColor(getContext().getColor(R.color.colorBackground));
        valid.setBackground(getContext().getDrawable(R.drawable.button_basic_gradient));
        lineStep.addView(valid);

        contentLinear.addView(lineStep);

    }

    private void setCapaLineListner(final LinearLayout lineCapa, final KiCapacity kiCapa) {
        lineCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiCapaSelected=kiCapa;
                setLinearCapaColor(lineCapa);
            }
        });
    }

    private void setLinearCapaColor(LinearLayout lineCapa) {
        for (LinearLayout lin : allKiCapa){
            if (lin.equals(lineCapa)){
                lin.setBackground(getResources().getDrawable(R.drawable.ki_capacity_selected_bar_gradient));
            } else {
                lin.setBackground(getResources().getDrawable(R.drawable.ki_bar_gradient));
            }
        }

        if (aquene.getAllResources().getResource("resource_ki").getCurrent()-kiCapaSelected.getCost()>=0){
            valid.setBackground(getContext().getDrawable(R.drawable.button_ok_gradient));
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aquene.getAllResources().getResource("resource_ki").spend(kiCapaSelected.getCost());
                    Snackbar snackbar = Snackbar.make(view, "Lancement de : "+kiCapaSelected.getName(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    backToMain();
                }
            });
        } else {
            valid.setBackground(getContext().getDrawable(R.drawable.button_cancel_gradient));
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Toast toast = Toast.makeText(getContext(), "Tu n'as pas assez de points de Ki pour faire cela", Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        if( v != null) v.setGravity(Gravity.CENTER);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                }
            });
        }


    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        int pixel_size_icon = (int) (getResources().getDimensionPixelSize(R.dimen.icon_kicapacities_list));
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    private void unlockOrient() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void animate(final ImageButton buttonMain) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation anim = new ScaleAnimation(1f,1.25f,1f,1.25f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setRepeatCount(1);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setDuration(666);

                buttonMain.startAnimation(anim);
            }
        }, getResources().getInteger(R.integer.translationFragDuration));
    }
}
