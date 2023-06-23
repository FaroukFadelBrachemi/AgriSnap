package com.example.agrisnap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Controller.DatabaseHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;

import Models.Land;
import Models.Site;


public class AdditionDialog extends AppCompatDialogFragment {

    private EditText dialoget;
    private TextView dialogtv, dialogtitle;
    String dialogText;
    private Button saveBtn, cancelBtn;
    private DatabaseHelper databaseHelper;
    public static Activity activity;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        dialoget = view.findViewById(R.id.dialoget);
        dialogtv = view.findViewById(R.id.dialogtv);
        dialogtitle = view.findViewById(R.id.dialogtitle);
        saveBtn = view.findViewById(R.id.savebtn);
        cancelBtn = view.findViewById(R.id.cancelbtn);
        databaseHelper = new DatabaseHelper(getContext());

        activity = getActivity();

        if (activity != null) {
            switch (activity.getLocalClassName()) {
                case "SitesActivity":
                    dialogtitle.setText(R.string.newsiteaddition);
                    dialogtv.setText(R.string.entersitename);
                    dialoget.setHint(R.string.sitename);
                    break;
                case "LandsActivity":
                    dialogtitle.setText(R.string.newlandaddition);
                    dialogtv.setText(R.string.enterlandname);
                    dialoget.setHint(R.string.landname);
                    break;
            }
        }

        builder.setView(view);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogText = dialoget.getText().toString();

                if (!dialogText.isEmpty()) {
                    switch (activity.getLocalClassName()) {
                        case "SitesActivity":
                            int id1=SitesActivity.townId;
                            Site site=new Site();
                            site.setSiteName(dialogText);
                            databaseHelper.addSite(site, id1);
                            File siteDir = new File(SitesActivity.getPath()+"/"+site.getSiteName());
                            siteDir.mkdirs();
                            SitesActivity act = (SitesActivity) getActivity();
                            if (act != null) {
                                act.notifyAdapter(site);
                            }
                            dismiss();
                            break;
                        case "LandsActivity":
                            int id2=LandsActivity.siteId;
                            Land land=new Land();
                            land.setLandName(dialogText);
                            databaseHelper.addLand(land, id2);
                            File landDir = new File(LandsActivity.getPath()+"/"+land.getLandName());
                            landDir.mkdirs();
                            LandsActivity acti = (LandsActivity) getActivity();
                            if (acti != null) {
                                acti.notifyAdapter(land);
                            }
                            dismiss();
                            break;
                    }

                }

            }
        });

        return builder.create();
    }


}






