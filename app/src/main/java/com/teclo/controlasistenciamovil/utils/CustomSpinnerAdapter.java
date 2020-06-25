package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.teclo.controlasistenciamovil.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private ArrayList<String> asr;
    private ArrayList<Long> asrId;
    private Context context;
    List<ImagenSpinner> listImge = null;

    public CustomSpinnerAdapter(ArrayList<String> asr,ArrayList<Long> asrId, Context context) {
        this.context = context;
        this.asr = asr;
        this.asrId= asrId;
    }

    public CustomSpinnerAdapter(ArrayList<String> asr, Context context) {
        this.context = context;
        this.asr = asr;
    }

    public CustomSpinnerAdapter(ArrayList<String> asr, List<ImagenSpinner> listImge, Context context) {
        this.context = context;
        this.asr = asr;
        this.listImge=listImge;
    }

    public int getCount() {
        if(listImge != null)
            return listImge.size();
        else
            return asr.size();
    }

    public Object getItem(int i) {
        if(listImge != null)
            return listImge.get(i);
        else{
            return asr.get(i);
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getIndexById(Long idElement){
        int i=0;
        int index=0;
        if(asrId != null && asrId.size() > 0){
            for(i=0; i<asrId.size(); i++){
                if(asrId.get(i).equals(idElement))
                    index= i;
            }
        }
        return index;
    }

    public int getIndexByString(String idElement){
        int i=0;
        int index=0;
        if(asr != null && asr.size() > 0){
            for(i=0; i<asr.size(); i++){
                if(asr.get(i).equals(idElement))
                    index= i;
            }
        }
        return index;
    }

    public int getIndexByImgString(String idElement){
        int i=0;
        int index=0;
        if(listImge != null && listImge.size() > 0){
            for(i=0; i<listImge.size(); i++){
                if(listImge.get(i).getName().equals(idElement))
                    index= i;
            }
        }
        return index;
    }

    public String getDescriptionFilterImg(int position){
        return listImge.get(position).getName();
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if(listImge != null){

            View row = convertView;
            if (row == null)
            {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.spinner_list_item, parent, false);
            }

            if (row.getTag() == null) {
                SocialNetworkHolder redSocialHolder = new SocialNetworkHolder();
                redSocialHolder.setIcono((ImageView) row.findViewById(R.id.icono));
                ImageView imgView =redSocialHolder.getIcono();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(48,48);
                imgView.setLayoutParams(params);
                redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
                row.setTag(redSocialHolder);
            }

            //rellenamos el layout con los datos de la fila que se está procesando
            ImagenSpinner imSpinner = listImge.get(position);
            ((SocialNetworkHolder) row.getTag()).getIcono().setImageResource(imSpinner.getIcon());
            ((SocialNetworkHolder) row.getTag()).getTextView().setText(imSpinner.getName());

            return row;

        }

        TextView txt = new TextView(context);
        txt.setPadding(10, 15, 10, 15);
        txt.setTextSize(16);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position));
        if (Build.VERSION.SDK_INT >= 23) {
            txt.setTextColor(ContextCompat.getColor(context, R.color.blue));
        } else {
            txt.setTextColor(context.getResources().getColor(R.color.blue));
        }

        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {

        if(listImge != null){

            if (view == null){
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_selected_item,null);
            }

            ((TextView) view.findViewById(R.id.texto)).setText(listImge.get(i).getName());
            ImageView img=((ImageView) view.findViewById(R.id.icono));
            img.setBackgroundResource(listImge.get(i).getIcon());

            return view;

        }

        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(3, 5, 5, 5);
        txt.setTextSize(18);
        txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dropdown, 0, 0, 0);
        txt.setText(asr.get(i));

        if(asrId!=null)
            txt.setTag(asrId.get(i));

        txt.setCompoundDrawablePadding(5);
        txt.setTextColor(ContextCompat.getColor(context, R.color.base_black_light_50));

        return txt;
    }


    private static class SocialNetworkHolder
    {

        private ImageView icono;

        private TextView textView;

        public ImageView getIcono()
        {
            return icono;
        }

        public void setIcono(ImageView icono)
        {
            this.icono = icono;
        }

        public TextView getTextView()
        {
            return textView;
        }

        public void setTextView(TextView textView)
        {
            this.textView = textView;
        }

    }

}
