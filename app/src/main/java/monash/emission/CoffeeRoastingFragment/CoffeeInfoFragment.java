package monash.emission.CoffeeRoastingFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import monash.emission.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoffeeInfoFragment extends Fragment {

    private View vInfo;
    private ExpandableListView infoExpandLV;
    private CoffeeListViewAdapter infoLVAdapter;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail;

    public CoffeeInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vInfo = inflater.inflate(R.layout.fragment_coffee_info, container, false);
        infoExpandLV = (ExpandableListView) vInfo.findViewById(R.id.inforExpandLV);
        expandableListDetail = new LinkedHashMap<String, List<String>>();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        initializeInfo();
        infoLVAdapter = new CoffeeListViewAdapter(getActivity(), expandableListTitle,expandableListDetail,2);
        infoExpandLV.setAdapter(infoLVAdapter);
        infoExpandLV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 8 && childPosition == 0){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://npi.gov.au/substances/fact-sheets"));
                    startActivity(browserIntent);
                }
                return false;
            }
        });
        return vInfo;
    }

    private void initializeInfo() {
        List<String> detail = new ArrayList<>();
        detail.add("(1) Sampling Data\n    Using your sampling test reports data to calculate annual emission.\n   *Emissions of ammonia/Chlorine may be determined by considering the losses from the refrigeration system in any given year.");
        detail.add("(2) Continuous Emission Monitoring System (CEMS) Data\n    CEMS usually provides pollutant concentration. Emission rate can be obtained by multiplying pollutant concentration by the volumetric gas or flow liquid rate.\n" +
                "Most food processing industries however, will not be required to use this method of EET\n" +
                "for the reporting of substances.\n");
        expandableListDetail.put("1.Direct Measurement",detail);
        detail = new ArrayList<>();
        detail.add("  A mass balance identifies the quantity of substance going in and out of an entire facility," +
                "process, or piece of equipment.\n   Emissions can be calculated as the difference between" +
                "input and output of each listed substance. Accumulation or depletion of the substance" +
                "within the equipment should be accounted for in your calculation.");
        expandableListDetail.put("2. Mass Balance",detail);
        detail = new ArrayList<>();
        detail.add("An engineering calculation is an estimation method based on physical/chemical" +
                "properties (eg. vapour pressure) of the substance and mathematical relationships (eg. ideal" +
                "gas law).\n" +
                "\tWe provide you with a fuel analysis calculator for Sulfur Dioxide (SO2) and Carbon Monoxide (CO).");
        expandableListDetail.put("3.Engineering Calculations",detail);
        detail = new ArrayList<>();
        detail.add("Emission factor relates the quantity of substances emitted from a source to some common " +
                "activity associated with those emissions.You are required to have the emission factor reviewed and approved by State or Territory environment agencies prior to its use for NPI estimations.\n" +
                "We provide you with a calculator using Industry-wide factor for Particulate Matter, VOC and Carbon Monoxide.\n");
        expandableListDetail.put("4.Emission Factors",detail);
        detail = new ArrayList<>();
        detail.add("Predictive emission monitoring is based on developing a correlation between pollutant " +
                "emission rates and process parameters. You can use your own PEM to develop site-specific emission factor more relevant to your particular process.");
        expandableListDetail.put("*Predictive Emission Monitoring (PEM)",detail);
        detail = new ArrayList<>();
        detail.add("Volatile Organic Compounds (VOC)\n" +
                "Sulfur Dioxide (SO2)\n" +
                "Particulate Matter (PM10)\n" +
                "Carbon Monoxide (CO)\n" +
                "Oxides of Nitrogen (Nox)\n" +
                "Ammonia (NH3)\n" +
                "Chlorine (Cl2)");
        expandableListDetail.put("*Emission to Air",detail);
        detail = new ArrayList<>();
        detail.add("Using monitoring data or mass balance/emission factor to estimate emission to water.\n" +
                "*Discharge to sewers require no reports. However,leakage and other emissions from tailing facility are reportable.");
        expandableListDetail.put("*Emission to Water",detail);
        detail = new ArrayList<>();
        detail.add("Solid wastes, slurries, sediments, spills and leaks, storage and distribution of liquids and may contain listed substances.\n" +
                "\tFor the coffee roasting industry, emissions to land may occur as a result of irrigating with " +
                "treated wasted water. It is only necessary to report emissions to land from irrigation if it " +
                "contains an NPI-listed substance.");
        expandableListDetail.put("*Emission to Land",detail);
        detail = new ArrayList<>();
        detail.add("Click lead to => http://npi.gov.au/substances/fact-sheets");
        expandableListDetail.put("Find out more about NPI-listed substance",detail);

        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
    }

}
