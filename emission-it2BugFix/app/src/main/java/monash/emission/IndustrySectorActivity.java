package monash.emission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;

/**
 * For user to choose the industry that the user belongs to, from the area to specific industry.
 */
public class IndustrySectorActivity extends AppCompatActivity {

    private Spinner industryAreaSpinner, industrySpinner;
    private Button calculateBtn;
    private Button infoBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_sector);
        industryAreaSpinner = (Spinner) findViewById(R.id.industry_area_spinner);
        industrySpinner = (Spinner) findViewById(R.id.industry_spinner);
        calculateBtn = (Button) findViewById(R.id.calculateBtn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (industrySpinner.getSelectedItem().toString().equals("Coffee Roasting")){
                    //modified, redirected to v2 activity
                    Intent i = new Intent(getApplicationContext(),CoffeeRoastActivity.class);
                    i.putExtra("IndustryName","Coffee Roasting");
                    i.putExtra("entrance","fuel");
                    startActivity(i);
                }
            }
        });

        /*infoBtn = (Button) findViewById(R.id.reportBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (industrySpinner.getSelectedItem().toString().equals("Coffee Roasting")){
                    Intent i = new Intent(getApplicationContext(),CalculationActivity.class);
                    i.putExtra("IndustryName","Coffee Roasting");
                    i.putExtra("entrance","info");
                    startActivity(i);
                }
            }
        });*/

    }
}
