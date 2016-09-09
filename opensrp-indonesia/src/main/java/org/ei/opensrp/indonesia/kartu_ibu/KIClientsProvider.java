package org.ei.opensrp.indonesia.kartu_ibu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import org.ei.opensrp.commonregistry.AllCommonsRepository;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.cursoradapter.SmartRegisterCLientsProviderForCursorAdapter;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.indonesia.R;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.service.AlertService;
import org.ei.opensrp.util.DateUtil;
import org.ei.opensrp.view.contract.AlertDTO;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.ei.opensrp.view.viewHolder.ECProfilePhotoLoader;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;
import org.ei.opensrp.view.viewHolder.ProfilePhotoLoader;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.ei.opensrp.util.StringUtil.humanize;
import static org.ei.opensrp.view.controller.ECSmartRegisterController.STATUS_DATE_FIELD;
import static org.ei.opensrp.view.controller.ECSmartRegisterController.STATUS_TYPE_FIELD;

/**
 * Created by Dimas Ciputra on 2/16/15.
 */
public class KIClientsProvider implements SmartRegisterCLientsProviderForCursorAdapter {
    private final LayoutInflater inflater;
    private final Context context;
    private final View.OnClickListener onClickListener;
    private Drawable iconPencilDrawable;
    private final int txtColorBlack;
    private final AbsListView.LayoutParams clientViewLayoutParams;

    protected CommonPersonObjectController controller;

    AlertService alertService;
    public KIClientsProvider(Context context,
                                         View.OnClickListener onClickListener,
                                         AlertService alertService) {
        this.onClickListener = onClickListener;
//        this.controller = controller;
        this.context = context;
        this.alertService = alertService;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        clientViewLayoutParams = new AbsListView.LayoutParams(MATCH_PARENT,
                (int) context.getResources().getDimension(org.ei.opensrp.R.dimen.list_item_height));
        txtColorBlack = context.getResources().getColor(org.ei.opensrp.R.color.text_black);

    }

    @Override
    public void getView(SmartRegisterClient smartRegisterClient, View convertView) {

        ViewHolder viewHolder;
     //      if (convertView == null){
     //          convertView = (ViewGroup) inflater().inflate(R.layout.smart_register_ki_client, null);
        viewHolder = new ViewHolder();
        viewHolder.profilelayout =  (LinearLayout)convertView.findViewById(R.id.profile_info_layout);
        viewHolder.wife_name = (TextView)convertView.findViewById(R.id.wife_name);
        viewHolder.husband_name = (TextView)convertView.findViewById(R.id.txt_husband_name);
        viewHolder.village_name = (TextView)convertView.findViewById(R.id.txt_village_name);
        viewHolder.wife_age = (TextView)convertView.findViewById(R.id.wife_age);
        viewHolder.no_ibu = (TextView)convertView.findViewById(R.id.no_ibu);
        viewHolder.unique_id = (TextView)convertView.findViewById(R.id.unique_id);

        viewHolder.gravida = (TextView)convertView.findViewById(R.id.txt_gravida);
               viewHolder.parity = (TextView)convertView.findViewById(R.id.txt_parity);
               viewHolder.number_of_abortus = (TextView)convertView.findViewById(R.id.txt_number_of_abortus);
               viewHolder.number_of_alive = (TextView)convertView.findViewById(R.id.txt_number_of_alive);

               viewHolder.edd = (TextView)convertView.findViewById(R.id.txt_edd);
               viewHolder.edd_due = (TextView)convertView.findViewById(R.id.txt_edd_due);
               viewHolder.children_age_left = (TextView)convertView.findViewById(R.id.txt_children_age_left);
            viewHolder.children_age_right = (TextView)convertView.findViewById(R.id.txt_children_age_right);

           viewHolder.anc_status_layout = (TextView)convertView.findViewById(R.id.mother_status);
            viewHolder.date_status = (TextView)convertView.findViewById(R.id.last_visit_status);
        viewHolder.visit_status = (TextView)convertView.findViewById(R.id.visit_status);
        viewHolder.profilepic =(ImageView)convertView.findViewById(R.id.img_profile);
        viewHolder.follow_up = (ImageButton)convertView.findViewById(R.id.btn_edit);
               viewHolder.profilepic.setImageDrawable(context.getResources().getDrawable(R.mipmap.woman_placeholder));
        convertView.setTag(viewHolder);
     //      }else{
    //           viewHolder = (ViewHolder) convertView.getTag();
     //          viewHolder.profilepic.setImageDrawable(context.getResources().getDrawable(R.mipmap.woman_placeholder));
    //       }
        viewHolder.follow_up.setOnClickListener(onClickListener);
        viewHolder.follow_up.setTag(smartRegisterClient);
        viewHolder.profilelayout.setOnClickListener(onClickListener);
        viewHolder.profilelayout.setTag(smartRegisterClient);
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        if (iconPencilDrawable == null) {
            iconPencilDrawable = context.getResources().getDrawable(R.drawable.ic_pencil);
        }
        viewHolder.follow_up.setImageDrawable(iconPencilDrawable);
        viewHolder.follow_up.setOnClickListener(onClickListener);
        //set image
        //  final ImageView childview = (ImageView)convertView.findViewById(R.id.profilepic);

        viewHolder.wife_name.setText(pc.getColumnmaps().get("namalengkap")!=null?pc.getColumnmaps().get("namalengkap"):"");
        viewHolder.husband_name.setText(pc.getColumnmaps().get("namaSuami")!=null?pc.getColumnmaps().get("namaSuami"):"");
        viewHolder.village_name.setText(pc.getDetails().get("desa")!=null?pc.getDetails().get("desa"):"");
        viewHolder.wife_age.setText(pc.getColumnmaps().get("umur")!=null?pc.getColumnmaps().get("umur"):"");
        viewHolder.no_ibu.setText(pc.getDetails().get("noIbu")!=null?pc.getDetails().get("noIbu"):"");
        viewHolder.unique_id.setText(pc.getDetails().get("unique_id")!=null?pc.getDetails().get("unique_id"):"");

        viewHolder.gravida.setText(pc.getDetails().get("gravida")!=null?pc.getDetails().get("gravida"):"-");
        viewHolder.parity.setText(pc.getDetails().get("partus")!=null?pc.getDetails().get("partus"):"-");
        viewHolder.number_of_abortus.setText(pc.getDetails().get("abortus")!=null?pc.getDetails().get("abortus"):"-");
        viewHolder.number_of_alive.setText(pc.getDetails().get("hidup")!=null?pc.getDetails().get("hidup"):"-");

        viewHolder.edd.setText(pc.getDetails().get("htp")!=null?pc.getDetails().get("htp"):"");


        String date = pc.getDetails().get("htp");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(pc.getDetails().get("htp")!=null) {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(date));
                c.add(Calendar.DATE, 2);  // number of days to add
                date = format.format(c.getTime());  // dt is now the new date
                Date dates = format.parse(date);
                Date currentDateandTime = new Date();
                long diff = Math.abs(dates.getTime() - currentDateandTime.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if(diffDays <1){
                    viewHolder.edd_due.setText("");

                }
                viewHolder.edd_due.setText(context.getString(R.string.due_status)+": "+diffDays+" "+context.getString(R.string.header_days_pp));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            viewHolder.edd_due.setText("-");
        }

        viewHolder.children_age_left.setText(pc.getColumnmaps().get("anak.namaBayi")!=null?"Name : "+pc.getColumnmaps().get("anak.namaBayi"):"");
        viewHolder.children_age_right.setText(pc.getColumnmaps().get("anak.tanggalLahirAnak")!=null?"DOB : "+pc.getColumnmaps().get("anak.tanggalLahirAnak"):"");

        if(pc.getColumnmaps().get("ibu.type")!=null){
            if(pc.getColumnmaps().get("ibu.type").equals("anc")){
                viewHolder.anc_status_layout.setText(context.getString(R.string.service_anc));
                String visit_date = pc.getColumnmaps().get("ibu.ancDate")!=null?context.getString(R.string.hh_last_visit_date) +" " +pc.getColumnmaps().get("ibu.ancDate"):"";
                String visit_stat = pc.getColumnmaps().get("ibu.ancKe")!=null?context.getString(R.string.anc_ke) +" " + pc.getColumnmaps().get("ibu.ancKe"):"";
                viewHolder.date_status.setText( visit_date);
                viewHolder.visit_status.setText(visit_stat);

            }
            if(pc.getColumnmaps().get("ibu.type").equals("pnc")){
                viewHolder.anc_status_layout.setText(context.getString(R.string.service_pnc));
                String hariKeKF = pc.getColumnmaps().get("ibu.hariKeKF")!=null?context.getString(R.string.hari_ke_kf)+" " +pc.getColumnmaps().get("ibu.hariKeKF"):"";
                viewHolder.visit_status.setText( hariKeKF);
            }
        }

     //   viewHolder.anc_status_layout.setText(pc.getColumnmaps().get("ibu.type")!=null?pc.getColumnmaps().get("ibu.type"):"--");

        convertView.setLayoutParams(clientViewLayoutParams);
      //  return convertView;
    }
    CommonPersonObjectController householdelcocontroller;
    

    //    @Override
    public SmartRegisterClients getClients() {
        return controller.getClients();
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption,
                                              FilterOption searchFilter, SortOption sortOption) {
        return getClients().applyFilter(villageFilter, serviceModeOption, searchFilter, sortOption);
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        // do nothing.
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public View inflatelayoutForCursorAdapter() {
        View View = (ViewGroup) inflater().inflate(R.layout.smart_register_ki_client, null);
        return View;
    }

    class ViewHolder {

        TextView wife_name ;
        TextView husband_name ;
        TextView village_name;
        TextView wife_age;
        LinearLayout profilelayout;
        ImageView profilepic;
        TextView gravida;
        Button warnbutton;
        ImageButton follow_up;
        TextView parity;
        TextView number_of_abortus;
        TextView number_of_alive;
        TextView no_ibu;
        TextView unique_id;
        TextView edd;
        TextView edd_due;
        TextView children_age_left;
        TextView anc_status_layout;
        public TextView visit_status;
        public TextView date_status;
        public TextView children_age_right;
    }


}