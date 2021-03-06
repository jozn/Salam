package com.shamchat.androidclient;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.androidclient.data.MomentDatabaseHelper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AndroidDatabaseManagerMoment extends Activity implements OnItemClickListener {
    MomentDatabaseHelper dbm;
    HorizontalScrollView hsv;
    indexInfo info;
    LinearLayout mainLayout;
    ScrollView mainscrollview;
    Button next;
    Button previous;
    Spinner select_table;
    TableLayout tableLayout;
    LayoutParams tableRowParams;
    TextView tv;
    TextView tvmessage;

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.1 */
    class C10371 implements OnClickListener {
        final /* synthetic */ Button val$customQuery;
        final /* synthetic */ EditText val$customquerytext;
        final /* synthetic */ TextView val$help;
        final /* synthetic */ LinearLayout val$secondrow;
        final /* synthetic */ Spinner val$spinnertable;
        final /* synthetic */ Button val$submitQuery;

        C10371(LinearLayout linearLayout, Spinner spinner, TextView textView, EditText editText, Button button, Button button2) {
            this.val$secondrow = linearLayout;
            this.val$spinnertable = spinner;
            this.val$help = textView;
            this.val$customquerytext = editText;
            this.val$submitQuery = button;
            this.val$customQuery = button2;
        }

        public final void onClick(View v) {
            indexInfo.isCustomQuery = true;
            this.val$secondrow.setVisibility(8);
            this.val$spinnertable.setVisibility(8);
            this.val$help.setVisibility(8);
            this.val$customquerytext.setVisibility(0);
            this.val$submitQuery.setVisibility(0);
            AndroidDatabaseManagerMoment.this.select_table.setSelection(0);
            this.val$customQuery.setVisibility(8);
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.2 */
    class C10382 implements OnClickListener {
        final /* synthetic */ Button val$customQuery;
        final /* synthetic */ EditText val$customquerytext;

        C10382(Button button, EditText editText) {
            this.val$customQuery = button;
            this.val$customquerytext = editText;
        }

        public final void onClick(View v) {
            AndroidDatabaseManagerMoment.this.tableLayout.removeAllViews();
            this.val$customQuery.setVisibility(8);
            String Query10 = this.val$customquerytext.getText().toString();
            Log.d("query", Query10);
            ArrayList<Cursor> alc2 = AndroidDatabaseManagerMoment.this.dbm.getData(Query10);
            Cursor c4 = (Cursor) alc2.get(0);
            Cursor Message2 = (Cursor) alc2.get(1);
            Message2.moveToLast();
            if (Message2.getString(0).equalsIgnoreCase("Success")) {
                AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                if (c4 != null) {
                    AndroidDatabaseManagerMoment.this.tvmessage.setText("Queru Executed successfully.Number of rows returned :" + c4.getCount());
                    if (c4.getCount() > 0) {
                        indexInfo.maincursor = c4;
                        AndroidDatabaseManagerMoment.this.refreshTable(1);
                        return;
                    }
                    return;
                }
                AndroidDatabaseManagerMoment.this.tvmessage.setText("Queru Executed successfully");
                AndroidDatabaseManagerMoment.this.refreshTable(1);
                return;
            }
            AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
            AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + Message2.getString(0));
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.3 */
    class C10393 extends ArrayAdapter<String> {
        C10393(Context x0, List x2) {
            super(x0, 17367048, x2);
        }

        public final View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            v.setBackgroundColor(-1);
            TextView adap = (TextView) v;
            adap.setTextSize(20.0f);
            return adap;
        }

        public final View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);
            v.setBackgroundColor(-1);
            return v;
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4 */
    class C10514 implements OnItemSelectedListener {
        final /* synthetic */ Cursor val$c;
        final /* synthetic */ Button val$customQuery;
        final /* synthetic */ EditText val$customquerytext;
        final /* synthetic */ TextView val$help;
        final /* synthetic */ LinearLayout val$secondrow;
        final /* synthetic */ Spinner val$spinnertable;
        final /* synthetic */ Button val$submitQuery;
        final /* synthetic */ LinearLayout val$thirdrow;

        /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.1 */
        class C10401 extends ArrayAdapter<String> {
            C10401(Context x0, List x2) {
                super(x0, 17367048, x2);
            }

            public final View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setBackgroundColor(-1);
                TextView adap = (TextView) v;
                adap.setTextSize(20.0f);
                return adap;
            }

            public final View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(-1);
                return v;
            }
        }

        /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2 */
        class C10502 implements OnItemSelectedListener {

            /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.1 */
            class C10431 implements Runnable {

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.1.1 */
                class C10411 implements DialogInterface.OnClickListener {
                    C10411() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.1.2 */
                class C10422 implements DialogInterface.OnClickListener {
                    C10422() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        Cursor tempc = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData("Drop table " + indexInfo.table_name).get(1);
                        tempc.moveToLast();
                        Log.d("Drop table Mesage", tempc.getString(0));
                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                            AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                            AndroidDatabaseManagerMoment.this.tvmessage.setText(indexInfo.table_name + "Dropped successfully");
                            AndroidDatabaseManagerMoment androidDatabaseManagerMoment = AndroidDatabaseManagerMoment.this;
                            androidDatabaseManagerMoment.finish();
                            androidDatabaseManagerMoment.startActivity(androidDatabaseManagerMoment.getIntent());
                            return;
                        }
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + tempc.getString(0));
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                C10431() {
                }

                public final void run() {
                    if (!AndroidDatabaseManagerMoment.this.isFinishing()) {
                        new Builder(AndroidDatabaseManagerMoment.this).setTitle("Are you sure ?").setMessage("Pressing yes will remove " + indexInfo.table_name + " table from database").setPositiveButton("yes", new C10422()).setNegativeButton("No", new C10411()).create().show();
                    }
                }
            }

            /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.2 */
            class C10462 implements Runnable {

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.2.1 */
                class C10441 implements DialogInterface.OnClickListener {
                    C10441() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.2.2 */
                class C10452 implements DialogInterface.OnClickListener {
                    C10452() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        String Query7 = "Delete  from " + indexInfo.table_name;
                        Log.d("delete table query", Query7);
                        Cursor tempc = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData(Query7).get(1);
                        tempc.moveToLast();
                        Log.d("Delete table Mesage", tempc.getString(0));
                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                            AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                            AndroidDatabaseManagerMoment.this.tvmessage.setText(indexInfo.table_name + " table content deleted successfully");
                            indexInfo.isEmpty = true;
                            AndroidDatabaseManagerMoment.this.refreshTable(0);
                            return;
                        }
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + tempc.getString(0));
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                C10462() {
                }

                public final void run() {
                    if (!AndroidDatabaseManagerMoment.this.isFinishing()) {
                        new Builder(AndroidDatabaseManagerMoment.this).setTitle("Are you sure?").setMessage("Clicking on yes will delete all the contents of " + indexInfo.table_name + " table from database").setPositiveButton("yes", new C10452()).setNegativeButton("No", new C10441()).create().show();
                    }
                }
            }

            /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.3 */
            class C10493 implements Runnable {
                final /* synthetic */ LinkedList val$addnewrownames;
                final /* synthetic */ LinkedList val$addnewrowvalues;
                final /* synthetic */ ScrollView val$addrowsv;

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.3.1 */
                class C10471 implements DialogInterface.OnClickListener {
                    C10471() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.4.2.3.2 */
                class C10482 implements DialogInterface.OnClickListener {
                    C10482() {
                    }

                    public final void onClick(DialogInterface dialog, int which) {
                        int i;
                        indexInfo.index = 10;
                        String Query4 = "Insert into " + indexInfo.table_name + " (";
                        for (i = 0; i < C10493.this.val$addnewrownames.size(); i++) {
                            TextView tv = (TextView) C10493.this.val$addnewrownames.get(i);
                            tv.getText().toString();
                            if (i == C10493.this.val$addnewrownames.size() - 1) {
                                Query4 = Query4 + tv.getText().toString();
                            } else {
                                Query4 = Query4 + tv.getText().toString() + ", ";
                            }
                        }
                        Query4 = Query4 + " ) VALUES ( ";
                        for (i = 0; i < C10493.this.val$addnewrownames.size(); i++) {
                            EditText et = (EditText) C10493.this.val$addnewrowvalues.get(i);
                            et.getText().toString();
                            if (i == C10493.this.val$addnewrownames.size() - 1) {
                                Query4 = Query4 + "'" + et.getText().toString() + "' ) ";
                            } else {
                                Query4 = Query4 + "'" + et.getText().toString() + "' , ";
                            }
                        }
                        Log.d("Insert Query", Query4);
                        Cursor tempc = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData(Query4).get(1);
                        tempc.moveToLast();
                        Log.d("Add New Row", tempc.getString(0));
                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                            AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                            AndroidDatabaseManagerMoment.this.tvmessage.setText("New Row added succesfully to " + indexInfo.table_name);
                            AndroidDatabaseManagerMoment.this.refreshTable(0);
                            return;
                        }
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + tempc.getString(0));
                        C10514.this.val$spinnertable.setSelection(0);
                    }
                }

                C10493(ScrollView scrollView, LinkedList linkedList, LinkedList linkedList2) {
                    this.val$addrowsv = scrollView;
                    this.val$addnewrownames = linkedList;
                    this.val$addnewrowvalues = linkedList2;
                }

                public final void run() {
                    if (!AndroidDatabaseManagerMoment.this.isFinishing()) {
                        new Builder(AndroidDatabaseManagerMoment.this).setTitle("values").setCancelable(false).setView(this.val$addrowsv).setPositiveButton("Add", new C10482()).setNegativeButton("close", new C10471()).create().show();
                    }
                }
            }

            C10502() {
            }

            public final void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));
                if (C10514.this.val$spinnertable.getSelectedItem().toString().equals("Drop this table")) {
                    AndroidDatabaseManagerMoment.this.runOnUiThread(new C10431());
                }
                if (C10514.this.val$spinnertable.getSelectedItem().toString().equals("Delete this table")) {
                    AndroidDatabaseManagerMoment.this.runOnUiThread(new C10462());
                }
                if (C10514.this.val$spinnertable.getSelectedItem().toString().equals("Add row to this table")) {
                    int i;
                    LinkedList<TextView> addnewrownames = new LinkedList();
                    LinkedList<EditText> addnewrowvalues = new LinkedList();
                    ScrollView addrowsv = new ScrollView(AndroidDatabaseManagerMoment.this);
                    Cursor c4 = indexInfo.maincursor;
                    String cname;
                    TextView textView;
                    if (indexInfo.isEmpty) {
                        AndroidDatabaseManagerMoment.this.getcolumnnames();
                        for (i = 0; i < indexInfo.emptytablecolumnnames.size(); i++) {
                            cname = (String) indexInfo.emptytablecolumnnames.get(i);
                            textView = new TextView(AndroidDatabaseManagerMoment.this.getApplicationContext());
                            textView.setText(cname);
                            addnewrownames.add(textView);
                        }
                        for (i = 0; i < addnewrownames.size(); i++) {
                            addnewrowvalues.add(new EditText(AndroidDatabaseManagerMoment.this.getApplicationContext()));
                        }
                    } else {
                        for (i = 0; i < c4.getColumnCount(); i++) {
                            cname = c4.getColumnName(i);
                            textView = new TextView(AndroidDatabaseManagerMoment.this.getApplicationContext());
                            textView.setText(cname);
                            addnewrownames.add(textView);
                        }
                        for (i = 0; i < addnewrownames.size(); i++) {
                            addnewrowvalues.add(new EditText(AndroidDatabaseManagerMoment.this.getApplicationContext()));
                        }
                    }
                    RelativeLayout addnewlayout = new RelativeLayout(AndroidDatabaseManagerMoment.this);
                    new RelativeLayout.LayoutParams(-2, -2).addRule(10);
                    for (i = 0; i < addnewrownames.size(); i++) {
                        View tv = (TextView) addnewrownames.get(i);
                        EditText et = (EditText) addnewrowvalues.get(i);
                        int k = i + 500;
                        int lid = i + 600;
                        tv.setId(i + 400);
                        tv.setTextColor(Color.parseColor("#000000"));
                        et.setBackgroundColor(Color.parseColor("#F2F2F2"));
                        et.setTextColor(Color.parseColor("#000000"));
                        et.setId(k);
                        LinearLayout ll = new LinearLayout(AndroidDatabaseManagerMoment.this);
                        ViewGroup.LayoutParams tvl = new LinearLayout.LayoutParams(0, 100);
                        tvl.weight = 1.0f;
                        ll.addView(tv, tvl);
                        ll.addView(et, tvl);
                        ll.setId(lid);
                        Log.d("Edit Text Value", et.getText().toString());
                        RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(-1, -2);
                        rll.addRule(3, ll.getId() - 1);
                        rll.setMargins(0, 20, 0, 0);
                        addnewlayout.addView(ll, rll);
                    }
                    addnewlayout.setBackgroundColor(-1);
                    addrowsv.addView(addnewlayout);
                    Log.d("Button Clicked", BuildConfig.VERSION_NAME);
                    AndroidDatabaseManagerMoment.this.runOnUiThread(new C10493(addrowsv, addnewrownames, addnewrowvalues));
                }
            }

            public final void onNothingSelected(AdapterView<?> adapterView) {
            }
        }

        C10514(LinearLayout linearLayout, LinearLayout linearLayout2, Spinner spinner, TextView textView, EditText editText, Button button, Button button2, Cursor cursor) {
            this.val$secondrow = linearLayout;
            this.val$thirdrow = linearLayout2;
            this.val$spinnertable = spinner;
            this.val$help = textView;
            this.val$customquerytext = editText;
            this.val$submitQuery = button;
            this.val$customQuery = button2;
            this.val$c = cursor;
        }

        public final void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            if (pos == 0 && !indexInfo.isCustomQuery) {
                this.val$secondrow.setVisibility(8);
                AndroidDatabaseManagerMoment.this.hsv.setVisibility(8);
                this.val$thirdrow.setVisibility(8);
                this.val$spinnertable.setVisibility(8);
                this.val$help.setVisibility(8);
                AndroidDatabaseManagerMoment.this.tvmessage.setVisibility(8);
                this.val$customquerytext.setVisibility(8);
                this.val$submitQuery.setVisibility(8);
                this.val$customQuery.setVisibility(8);
            }
            if (pos != 0) {
                this.val$secondrow.setVisibility(0);
                this.val$spinnertable.setVisibility(0);
                this.val$help.setVisibility(0);
                this.val$customquerytext.setVisibility(8);
                this.val$submitQuery.setVisibility(8);
                this.val$customQuery.setVisibility(0);
                AndroidDatabaseManagerMoment.this.hsv.setVisibility(0);
                AndroidDatabaseManagerMoment.this.tvmessage.setVisibility(0);
                this.val$thirdrow.setVisibility(0);
                this.val$c.moveToPosition(pos - 1);
                indexInfo.cursorpostion = pos - 1;
                Log.d("selected table name is", this.val$c.getString(0));
                indexInfo.table_name = this.val$c.getString(0);
                AndroidDatabaseManagerMoment.this.tvmessage.setText("Error Messages will be displayed here");
                AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(-1);
                AndroidDatabaseManagerMoment.this.tableLayout.removeAllViews();
                ArrayList<String> spinnertablevalues = new ArrayList();
                spinnertablevalues.add("Click here to change this table");
                spinnertablevalues.add("Add row to this table");
                spinnertablevalues.add("Delete this table");
                spinnertablevalues.add("Drop this table");
                new ArrayAdapter(AndroidDatabaseManagerMoment.this.getApplicationContext(), 17367049, spinnertablevalues).setDropDownViewResource(17367048);
                ArrayAdapter<String> adapter = new C10401(AndroidDatabaseManagerMoment.this, spinnertablevalues);
                adapter.setDropDownViewResource(17367049);
                this.val$spinnertable.setAdapter(adapter);
                String Query2 = "select * from " + this.val$c.getString(0);
                Log.d(BuildConfig.VERSION_NAME, Query2);
                Cursor c2 = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData(Query2).get(0);
                indexInfo.maincursor = c2;
                LinearLayout cell;
                TextView tableheadercolums;
                if (c2 != null) {
                    int counts = c2.getCount();
                    indexInfo.isEmpty = false;
                    Log.d("counts", String.valueOf(counts));
                    AndroidDatabaseManagerMoment.this.tv.setText(String.valueOf(counts));
                    this.val$spinnertable.setOnItemSelectedListener(new C10502());
                    TableRow tableheader = new TableRow(AndroidDatabaseManagerMoment.this.getApplicationContext());
                    tableheader.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    tableheader.setPadding(0, 2, 0, 2);
                    for (int k = 0; k < c2.getColumnCount(); k++) {
                        cell = new LinearLayout(AndroidDatabaseManagerMoment.this);
                        cell.setBackgroundColor(-1);
                        cell.setLayoutParams(AndroidDatabaseManagerMoment.this.tableRowParams);
                        tableheadercolums = new TextView(AndroidDatabaseManagerMoment.this.getApplicationContext());
                        tableheadercolums.setPadding(0, 0, 4, 3);
                        tableheadercolums.setText(c2.getColumnName(k));
                        tableheadercolums.setTextColor(Color.parseColor("#000000"));
                        cell.addView(tableheadercolums);
                        tableheader.addView(cell);
                    }
                    AndroidDatabaseManagerMoment.this.tableLayout.addView(tableheader);
                    c2.moveToFirst();
                    AndroidDatabaseManagerMoment androidDatabaseManagerMoment = AndroidDatabaseManagerMoment.this;
                    c2.getCount();
                    androidDatabaseManagerMoment.paginatetable$13462e();
                    return;
                }
                this.val$help.setVisibility(8);
                AndroidDatabaseManagerMoment.this.tableLayout.removeAllViews();
                AndroidDatabaseManagerMoment.this.getcolumnnames();
                TableRow tableheader2 = new TableRow(AndroidDatabaseManagerMoment.this.getApplicationContext());
                tableheader2.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                tableheader2.setPadding(0, 2, 0, 2);
                cell = new LinearLayout(AndroidDatabaseManagerMoment.this);
                cell.setBackgroundColor(-1);
                cell.setLayoutParams(AndroidDatabaseManagerMoment.this.tableRowParams);
                tableheadercolums = new TextView(AndroidDatabaseManagerMoment.this.getApplicationContext());
                tableheadercolums.setPadding(0, 0, 4, 3);
                tableheadercolums.setText("   Table   Is   Empty   ");
                tableheadercolums.setTextSize(30.0f);
                tableheadercolums.setTextColor(SupportMenu.CATEGORY_MASK);
                cell.addView(tableheadercolums);
                tableheader2.addView(cell);
                AndroidDatabaseManagerMoment.this.tableLayout.addView(tableheader2);
                AndroidDatabaseManagerMoment.this.tv.setText("0");
            }
        }

        public final void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.5 */
    class C10525 extends ArrayAdapter<String> {
        C10525(Context x0, List x2) {
            super(x0, 17367048, x2);
        }

        public final View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            v.setBackgroundColor(-1);
            TextView adap = (TextView) v;
            adap.setTextSize(20.0f);
            return adap;
        }

        public final View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);
            v.setBackgroundColor(-1);
            return v;
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.6 */
    class C10556 implements Runnable {
        final /* synthetic */ LinkedList val$columnames;
        final /* synthetic */ LinkedList val$columvalues;
        final /* synthetic */ Spinner val$crud_dropdown;
        final /* synthetic */ ScrollView val$updaterowsv;
        final /* synthetic */ ArrayList val$value_string;

        /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.6.1 */
        class C10531 implements DialogInterface.OnClickListener {
            C10531() {
            }

            public final void onClick(DialogInterface dialog, int which) {
            }
        }

        /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.6.2 */
        class C10542 implements DialogInterface.OnClickListener {
            C10542() {
            }

            public final void onClick(DialogInterface dialog, int which) {
                int i;
                TextView tvc;
                Cursor tempc;
                String spinner_value = C10556.this.val$crud_dropdown.getSelectedItem().toString();
                if (spinner_value.equalsIgnoreCase("Update this row")) {
                    indexInfo.index = 10;
                    String Query3 = "UPDATE " + indexInfo.table_name + " SET ";
                    for (i = 0; i < C10556.this.val$columnames.size(); i++) {
                        tvc = (TextView) C10556.this.val$columnames.get(i);
                        EditText etc = (EditText) C10556.this.val$columvalues.get(i);
                        if (!etc.getText().toString().equals("null")) {
                            Query3 = Query3 + tvc.getText().toString() + " = ";
                            if (i == C10556.this.val$columnames.size() - 1) {
                                Query3 = Query3 + "'" + etc.getText().toString() + "'";
                            } else {
                                Query3 = Query3 + "'" + etc.getText().toString() + "' , ";
                            }
                        }
                    }
                    Query3 = Query3 + " where ";
                    for (i = 0; i < C10556.this.val$columnames.size(); i++) {
                        tvc = (TextView) C10556.this.val$columnames.get(i);
                        if (!((String) C10556.this.val$value_string.get(i)).equals("null")) {
                            Query3 = Query3 + tvc.getText().toString() + " = ";
                            if (i == C10556.this.val$columnames.size() - 1) {
                                Query3 = Query3 + "'" + ((String) C10556.this.val$value_string.get(i)) + "' ";
                            } else {
                                Query3 = Query3 + "'" + ((String) C10556.this.val$value_string.get(i)) + "' and ";
                            }
                        }
                    }
                    Log.d("Update Query", Query3);
                    tempc = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData(Query3).get(1);
                    tempc.moveToLast();
                    Log.d("Update Mesage", tempc.getString(0));
                    if (tempc.getString(0).equalsIgnoreCase("Success")) {
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText(indexInfo.table_name + " table Updated Successfully");
                        AndroidDatabaseManagerMoment.this.refreshTable(0);
                    } else {
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + tempc.getString(0));
                    }
                }
                if (spinner_value.equalsIgnoreCase("Delete this row")) {
                    indexInfo.index = 10;
                    String Query5 = "DELETE FROM " + indexInfo.table_name + " WHERE ";
                    for (i = 0; i < C10556.this.val$columnames.size(); i++) {
                        tvc = (TextView) C10556.this.val$columnames.get(i);
                        if (!((String) C10556.this.val$value_string.get(i)).equals("null")) {
                            Query5 = Query5 + tvc.getText().toString() + " = ";
                            if (i == C10556.this.val$columnames.size() - 1) {
                                Query5 = Query5 + "'" + ((String) C10556.this.val$value_string.get(i)) + "' ";
                            } else {
                                Query5 = Query5 + "'" + ((String) C10556.this.val$value_string.get(i)) + "' and ";
                            }
                        }
                    }
                    Log.d("Delete Query", Query5);
                    AndroidDatabaseManagerMoment.this.dbm.getData(Query5);
                    tempc = (Cursor) AndroidDatabaseManagerMoment.this.dbm.getData(Query5).get(1);
                    tempc.moveToLast();
                    Log.d("Update Mesage", tempc.getString(0));
                    if (tempc.getString(0).equalsIgnoreCase("Success")) {
                        AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                        AndroidDatabaseManagerMoment.this.tvmessage.setText("Row deleted from " + indexInfo.table_name + " table");
                        AndroidDatabaseManagerMoment.this.refreshTable(0);
                        return;
                    }
                    AndroidDatabaseManagerMoment.this.tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                    AndroidDatabaseManagerMoment.this.tvmessage.setText("Error:" + tempc.getString(0));
                }
            }
        }

        C10556(ScrollView scrollView, Spinner spinner, LinkedList linkedList, LinkedList linkedList2, ArrayList arrayList) {
            this.val$updaterowsv = scrollView;
            this.val$crud_dropdown = spinner;
            this.val$columnames = linkedList;
            this.val$columvalues = linkedList2;
            this.val$value_string = arrayList;
        }

        public final void run() {
            if (!AndroidDatabaseManagerMoment.this.isFinishing()) {
                new Builder(AndroidDatabaseManagerMoment.this).setTitle("values").setView(this.val$updaterowsv).setCancelable(false).setPositiveButton("Ok", new C10542()).setNegativeButton("close", new C10531()).create().show();
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.7 */
    class C10567 implements OnClickListener {
        final /* synthetic */ Cursor val$c3;
        final /* synthetic */ TableRow val$tableRow;

        C10567(Cursor cursor, TableRow tableRow) {
            this.val$c3 = cursor;
            this.val$tableRow = tableRow;
        }

        public final void onClick(View v) {
            ArrayList<String> value_string = new ArrayList();
            for (int i = 0; i < this.val$c3.getColumnCount(); i++) {
                value_string.add(((TextView) ((LinearLayout) this.val$tableRow.getChildAt(i)).getChildAt(0)).getText().toString());
            }
            indexInfo.value_string = value_string;
            AndroidDatabaseManagerMoment.this.updateDeletePopup$13462e();
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.8 */
    class C10578 implements OnClickListener {
        final /* synthetic */ Cursor val$c3;

        C10578(Cursor cursor) {
            this.val$c3 = cursor;
        }

        public final void onClick(View v) {
            int tobestartindex = (indexInfo.currentpage - 2) * 10;
            if (indexInfo.currentpage == 1) {
                Toast.makeText(AndroidDatabaseManagerMoment.this.getApplicationContext(), "This is the first page", 1).show();
                return;
            }
            indexInfo.currentpage--;
            this.val$c3.moveToPosition(tobestartindex);
            boolean decider = true;
            for (int i = 1; i < AndroidDatabaseManagerMoment.this.tableLayout.getChildCount(); i++) {
                TableRow tableRow = (TableRow) AndroidDatabaseManagerMoment.this.tableLayout.getChildAt(i);
                if (decider) {
                    tableRow.setVisibility(0);
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        ((TextView) ((LinearLayout) tableRow.getChildAt(j)).getChildAt(0)).setText(this.val$c3.getString(j));
                    }
                    if (this.val$c3.isLast()) {
                        decider = false;
                    } else {
                        decider = true;
                    }
                    if (!this.val$c3.isLast()) {
                        this.val$c3.moveToNext();
                    }
                } else {
                    tableRow.setVisibility(8);
                }
            }
            indexInfo.index = tobestartindex;
            Log.d("index =", indexInfo.index);
        }
    }

    /* renamed from: com.shamchat.androidclient.AndroidDatabaseManagerMoment.9 */
    class C10589 implements OnClickListener {
        final /* synthetic */ Cursor val$c3;

        C10589(Cursor cursor) {
            this.val$c3 = cursor;
        }

        public final void onClick(View v) {
            if (indexInfo.currentpage >= indexInfo.numberofpages) {
                Toast.makeText(AndroidDatabaseManagerMoment.this.getApplicationContext(), "This is the last page", 1).show();
                return;
            }
            indexInfo.currentpage++;
            boolean decider = true;
            for (int i = 1; i < AndroidDatabaseManagerMoment.this.tableLayout.getChildCount(); i++) {
                TableRow tableRow = (TableRow) AndroidDatabaseManagerMoment.this.tableLayout.getChildAt(i);
                if (decider) {
                    tableRow.setVisibility(0);
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        ((TextView) ((LinearLayout) tableRow.getChildAt(j)).getChildAt(0)).setText(this.val$c3.getString(j));
                    }
                    if (this.val$c3.isLast()) {
                        decider = false;
                    } else {
                        decider = true;
                    }
                    if (!this.val$c3.isLast()) {
                        this.val$c3.moveToNext();
                    }
                } else {
                    tableRow.setVisibility(8);
                }
            }
        }
    }

    static class indexInfo {
        public static int currentpage;
        public static int cursorpostion;
        public static ArrayList<String> emptytablecolumnnames;
        public static int index;
        public static boolean isCustomQuery;
        public static boolean isEmpty;
        public static Cursor maincursor;
        public static int numberofpages;
        public static String table_name;
        public static ArrayList<String> value_string;

        indexInfo() {
        }

        static {
            index = 10;
            numberofpages = 0;
            currentpage = 0;
            table_name = BuildConfig.VERSION_NAME;
            cursorpostion = 0;
        }
    }

    public AndroidDatabaseManagerMoment() {
        this.info = new indexInfo();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbm = MomentDatabaseHelper.getMomentDatabaseHelper(this);
        this.mainscrollview = new ScrollView(this);
        this.mainLayout = new LinearLayout(this);
        this.mainLayout.setOrientation(1);
        this.mainLayout.setBackgroundColor(-1);
        this.mainLayout.setScrollContainer(true);
        this.mainscrollview.addView(this.mainLayout);
        setContentView(this.mainscrollview);
        View linearLayout = new LinearLayout(this);
        linearLayout.setPadding(0, 10, 0, 20);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 150);
        layoutParams.weight = 1.0f;
        linearLayout = new TextView(this);
        linearLayout.setText("Select Table");
        linearLayout.setTextSize(22.0f);
        linearLayout.setLayoutParams(layoutParams);
        this.select_table = new Spinner(this);
        this.select_table.setLayoutParams(layoutParams);
        linearLayout.addView(linearLayout);
        linearLayout.addView(this.select_table);
        this.mainLayout.addView(linearLayout);
        this.hsv = new HorizontalScrollView(this);
        this.tableLayout = new TableLayout(this);
        this.tableLayout.setHorizontalScrollBarEnabled(true);
        this.hsv.addView(this.tableLayout);
        LinearLayout secondrow = new LinearLayout(this);
        secondrow.setPadding(0, 20, 0, 10);
        layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.weight = 1.0f;
        linearLayout = new TextView(this);
        linearLayout.setText("No. Of Records : ");
        linearLayout.setTextSize(20.0f);
        linearLayout.setLayoutParams(layoutParams);
        this.tv = new TextView(this);
        this.tv.setTextSize(20.0f);
        this.tv.setLayoutParams(layoutParams);
        secondrow.addView(linearLayout);
        secondrow.addView(this.tv);
        this.mainLayout.addView(secondrow);
        EditText customquerytext = new EditText(this);
        customquerytext.setVisibility(8);
        customquerytext.setHint("Enter Your Query here and Click on Submit Query Button .Results will be displayed below");
        this.mainLayout.addView(customquerytext);
        Button submitQuery = new Button(this);
        submitQuery.setVisibility(8);
        submitQuery.setText("Submit Query");
        submitQuery.setBackgroundColor(Color.parseColor("#BAE7F6"));
        this.mainLayout.addView(submitQuery);
        TextView help = new TextView(this);
        help.setText("Click on the row below to update values or delete the tuple");
        help.setPadding(0, 5, 0, 5);
        Spinner spinnertable = new Spinner(this);
        this.mainLayout.addView(spinnertable);
        this.mainLayout.addView(help);
        this.hsv.setPadding(0, 10, 0, 10);
        this.hsv.setScrollbarFadingEnabled(false);
        this.hsv.setScrollBarStyle(50331648);
        this.mainLayout.addView(this.hsv);
        LinearLayout thirdrow = new LinearLayout(this);
        this.previous = new Button(this);
        this.previous.setText("Previous");
        this.previous.setBackgroundColor(Color.parseColor("#BAE7F6"));
        this.previous.setLayoutParams(layoutParams);
        this.next = new Button(this);
        this.next.setText("Next");
        this.next.setBackgroundColor(Color.parseColor("#BAE7F6"));
        this.next.setLayoutParams(layoutParams);
        View tvblank = new TextView(this);
        tvblank.setLayoutParams(layoutParams);
        thirdrow.setPadding(0, 10, 0, 10);
        thirdrow.addView(this.previous);
        thirdrow.addView(tvblank);
        thirdrow.addView(this.next);
        this.mainLayout.addView(thirdrow);
        this.tvmessage = new TextView(this);
        this.tvmessage.setText("Error Messages will be displayed here");
        this.tvmessage.setTextSize(18.0f);
        this.mainLayout.addView(this.tvmessage);
        Button customQuery = new Button(this);
        customQuery.setText("Custom Query");
        customQuery.setBackgroundColor(Color.parseColor("#BAE7F6"));
        this.mainLayout.addView(customQuery);
        customQuery.setOnClickListener(new C10371(secondrow, spinnertable, help, customquerytext, submitQuery, customQuery));
        submitQuery.setOnClickListener(new C10382(customQuery, customquerytext));
        this.tableRowParams = new LayoutParams(-2, -2);
        this.tableRowParams.setMargins(0, 0, 2, 0);
        ArrayList<Cursor> alc = this.dbm.getData("SELECT name _id FROM sqlite_master WHERE type ='table'");
        Cursor c = (Cursor) alc.get(0);
        Cursor Message = (Cursor) alc.get(1);
        Message.moveToLast();
        Log.d("Message from sql = ", Message.getString(0));
        ArrayList<String> tablenames = new ArrayList();
        if (c != null) {
            c.moveToFirst();
            tablenames.add("click here");
            do {
                tablenames.add(c.getString(0));
            } while (c.moveToNext());
        }
        ArrayAdapter<String> c10393 = new C10393(this, tablenames);
        c10393.setDropDownViewResource(17367049);
        this.select_table.setAdapter(c10393);
        this.select_table.setOnItemSelectedListener(new C10514(secondrow, thirdrow, spinnertable, help, customquerytext, submitQuery, customQuery, c));
    }

    public final void getcolumnnames() {
        Cursor c5 = (Cursor) this.dbm.getData("PRAGMA table_info(" + indexInfo.table_name + ")").get(0);
        indexInfo.isEmpty = true;
        if (c5 != null) {
            indexInfo.isEmpty = true;
            ArrayList<String> emptytablecolumnnames = new ArrayList();
            c5.moveToFirst();
            do {
                emptytablecolumnnames.add(c5.getString(1));
            } while (c5.moveToNext());
            indexInfo.emptytablecolumnnames = emptytablecolumnnames;
        }
    }

    public final void updateDeletePopup$13462e() {
        int i;
        Cursor c2 = indexInfo.maincursor;
        ArrayList<String> spinnerArray = new ArrayList();
        spinnerArray.add("Click Here to Change this row");
        spinnerArray.add("Update this row");
        spinnerArray.add("Delete this row");
        ArrayList<String> value_string = indexInfo.value_string;
        LinkedList<TextView> columnames = new LinkedList();
        LinkedList<EditText> columvalues = new LinkedList();
        for (i = 0; i < c2.getColumnCount(); i++) {
            String cname = c2.getColumnName(i);
            TextView textView = new TextView(getApplicationContext());
            textView.setText(cname);
            columnames.add(textView);
        }
        for (i = 0; i < columnames.size(); i++) {
            String cv = (String) value_string.get(i);
            EditText et = new EditText(getApplicationContext());
            value_string.add(cv);
            et.setText(cv);
            columvalues.add(et);
        }
        View relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(-1);
        new RelativeLayout.LayoutParams(-2, -2).addRule(10);
        ScrollView updaterowsv = new ScrollView(this);
        relativeLayout = new LinearLayout(this);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(0, 20, 0, 0);
        Spinner crud_dropdown = new Spinner(getApplicationContext());
        ArrayAdapter<String> crudadapter = new C10525(this, spinnerArray);
        crudadapter.setDropDownViewResource(17367049);
        crud_dropdown.setAdapter(crudadapter);
        relativeLayout.setId(299);
        relativeLayout.addView(crud_dropdown, layoutParams);
        layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(3, 0);
        relativeLayout.addView(relativeLayout, layoutParams);
        for (i = 0; i < columnames.size(); i++) {
            View tv = (TextView) columnames.get(i);
            et = (EditText) columvalues.get(i);
            int k = i + 200;
            int lid = i + 300;
            tv.setId(i + 100);
            tv.setTextColor(Color.parseColor("#000000"));
            et.setBackgroundColor(Color.parseColor("#F2F2F2"));
            et.setTextColor(Color.parseColor("#000000"));
            et.setId(k);
            Log.d("text View Value", tv.getText().toString());
            relativeLayout = new LinearLayout(this);
            relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            relativeLayout.setId(lid);
            layoutParams = new LinearLayout.LayoutParams(0, 100);
            layoutParams.weight = 1.0f;
            tv.setLayoutParams(layoutParams);
            et.setLayoutParams(layoutParams);
            relativeLayout.addView(tv);
            relativeLayout.addView(et);
            Log.d("Edit Text Value", et.getText().toString());
            layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(3, relativeLayout.getId() - 1);
            layoutParams.setMargins(0, 20, 0, 0);
            relativeLayout.getId();
            relativeLayout.addView(relativeLayout, layoutParams);
        }
        updaterowsv.addView(relativeLayout);
        runOnUiThread(new C10556(updaterowsv, crud_dropdown, columnames, columvalues, value_string));
    }

    public final void refreshTable(int d) {
        Cursor cursor = null;
        this.tableLayout.removeAllViews();
        if (d == 0) {
            cursor = (Cursor) this.dbm.getData("select * from " + indexInfo.table_name).get(0);
            indexInfo.maincursor = cursor;
        }
        if (d == 1) {
            cursor = indexInfo.maincursor;
        }
        if (cursor != null) {
            int counts = cursor.getCount();
            Log.d("counts", String.valueOf(counts));
            this.tv.setText(String.valueOf(counts));
            TableRow tableheader = new TableRow(getApplicationContext());
            tableheader.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            tableheader.setPadding(0, 2, 0, 2);
            for (int k = 0; k < cursor.getColumnCount(); k++) {
                LinearLayout cell = new LinearLayout(this);
                cell.setBackgroundColor(-1);
                cell.setLayoutParams(this.tableRowParams);
                TextView tableheadercolums = new TextView(getApplicationContext());
                tableheadercolums.setPadding(0, 0, 4, 3);
                tableheadercolums.setText(cursor.getColumnName(k));
                tableheadercolums.setTextColor(Color.parseColor("#000000"));
                cell.addView(tableheadercolums);
                tableheader.addView(cell);
            }
            this.tableLayout.addView(tableheader);
            cursor.moveToFirst();
            cursor.getCount();
            paginatetable$13462e();
            return;
        }
        TableRow tableheader2 = new TableRow(getApplicationContext());
        tableheader2.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        tableheader2.setPadding(0, 2, 0, 2);
        cell = new LinearLayout(this);
        cell.setBackgroundColor(-1);
        cell.setLayoutParams(this.tableRowParams);
        tableheadercolums = new TextView(getApplicationContext());
        tableheadercolums.setPadding(0, 0, 4, 3);
        tableheadercolums.setText("   Table   Is   Empty   ");
        tableheadercolums.setTextSize(30.0f);
        tableheadercolums.setTextColor(SupportMenu.CATEGORY_MASK);
        cell.addView(tableheadercolums);
        tableheader2.addView(cell);
        this.tableLayout.addView(tableheader2);
        this.tv.setText("0");
    }

    public final void paginatetable$13462e() {
        Cursor c3 = indexInfo.maincursor;
        indexInfo.numberofpages = (c3.getCount() / 10) + 1;
        indexInfo.currentpage = 1;
        c3.moveToFirst();
        int currentrow = 0;
        do {
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            tableRow.setPadding(0, 2, 0, 2);
            for (int j = 0; j < c3.getColumnCount(); j++) {
                LinearLayout cell = new LinearLayout(this);
                cell.setBackgroundColor(-1);
                cell.setLayoutParams(this.tableRowParams);
                TextView columsView = new TextView(getApplicationContext());
                columsView.setText(c3.getString(j));
                columsView.setTextColor(Color.parseColor("#000000"));
                columsView.setPadding(0, 0, 4, 3);
                cell.addView(columsView);
                tableRow.addView(cell);
            }
            tableRow.setVisibility(0);
            currentrow++;
            tableRow.setOnClickListener(new C10567(c3, tableRow));
            this.tableLayout.addView(tableRow);
            if (!c3.moveToNext()) {
                break;
            }
        } while (currentrow < 10);
        indexInfo.index = currentrow;
        this.previous.setOnClickListener(new C10578(c3));
        this.next.setOnClickListener(new C10589(c3));
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
    }
}
