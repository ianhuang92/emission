package monash.emission.CoffeeRoastingFuncV2;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.MotionEvent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import monash.emission.DateCalculation;
import monash.emission.R;

/**
 * Created by Ranger on 2017/8/27.
 */

public class CoffeeDurationFragment extends Fragment {

    private CoffeeRoastActivity c;
    private View vDisplayUnit;
    private EditText etStart;
    private EditText etEnd;
    private EditText etAvgHr;
    private Button butNext;
    private Date startDate;
    private Date endDate;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//inflate converts an XML layout file into corresponding ViewGroups and views
        vDisplayUnit = inflater.inflate(R.layout.fragment_coffee_duration, container, false);
        c = (CoffeeRoastActivity) getActivity();
        etStart = (EditText)vDisplayUnit.findViewById(R.id.et_start_fragment_coffee_duration);
        etEnd = (EditText)vDisplayUnit.findViewById(R.id.et_end_fragment_coffee_duration);
        etAvgHr = (EditText)vDisplayUnit.findViewById(R.id.et_avg_hr_fragment_coffee_duration) ;
        butNext = (Button)vDisplayUnit.findViewById(R.id.but_fragment_coffee_duration);

        etStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlgStart();
                    return true;
                }
                return false;
            }
        });
        etStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlgStart();
                }
            }
        });
    etEnd.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showDatePickDlgEnd();
                return true;
            }
            return false;
        }
    });
        etEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlgEnd();
                }
            }
        });

        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectData())
                {
                    //TODO
                    //Navigate to the following page here
                    //StartDate, EndDate, Duration, AvgHour have been saved in mother bundle
                    //Key StartDate, type String, format yyyy-MM-dd
                    //Key EndDate, type String, format yyyy-MM-dd
                    //Key Duration, type int, meaning number of days between end date and start date.
                    //Key AvgHour, type Double, meaning average running hour per day

                }

            }
        });






        return vDisplayUnit;
    }

    private boolean collectData()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {  //validate if start date has been selected
            startDate = sdf.parse(etStart.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(c,"Please enter Start Date.",Toast.LENGTH_SHORT).show();
            return false;
        }
        try {       //validate if end date has been selected
            endDate = sdf.parse(etEnd.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(c,"Please enter End Date.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etAvgHr.getText().toString().trim().isEmpty()) {  //validate if avg hour is empty
            Toast.makeText(c,"Please enter Average Machine Running Hours.",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startDate.compareTo(endDate)>=0)   //if startDate is not before endDate
        {
            Toast.makeText(c,"Start Date must be before End date. ",Toast.LENGTH_SHORT).show();
            return false;
        }
        Double avgHr;

       try {
           avgHr = Double.parseDouble(etAvgHr.getText().toString());
           if (avgHr>24)
           {
               Toast.makeText(c, "Daily operating hours shall not be greater than 24.", Toast.LENGTH_SHORT).show();
               return false;
           }
       }
       catch (NumberFormatException e)  //as input data has been limited as number decimal in xml file, this shall not happen
       {
           Toast.makeText(c,"Invalid Number Format. Please Contact Vendor to report this bug. ",Toast.LENGTH_SHORT).show();
           return false;
       }
       catch (NullPointerException e) //editText etAvgHr is null if this happens
       {
           Toast.makeText(c,"Null pointer exception. Please contact Vendor to report this bug.  ",Toast.LENGTH_SHORT).show();
           return false;
       }

        int duration = DateCalculation.getGapCount(startDate,endDate);


        c.sharedBundle.putString("StartDate",etStart.getText().toString());
        c.sharedBundle.putString("EndDate",etEnd.getText().toString());
        c.sharedBundle.putInt("Duration",duration);
        c.sharedBundle.putDouble("AvgHour",avgHr);
        return true;
    }


    /**
     * display date picker dialog for start date edit text
     */
    protected void showDatePickDlgStart() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etStart.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * display date picker dialog for end date edit text
     */
    protected void showDatePickDlgEnd() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etEnd.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }














    }
