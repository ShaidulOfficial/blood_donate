package com.shaidulsoftstudio.roktobindu.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private View view;
    ArrayAdapter<CharSequence> bloodAdapter, districtAdapter_search_fragment, upzillaAdapter_search_fragment;
    String querysearch_frag = "", blood_group_search_fragment = "", districtSpin_search_fragment1 = "", upzilaSpin_search_fragment1 = "";
    String districtSpin_search_fragment, upzilaSpin_search_fragment;
    Spinner district_search_Spinner, upzila_search_Spinner, blood_group_search_Spinner;
    RecyclerView recyclerView;
    AppCompatButton okSearchBtn;
    DatabaseReference dbReference;
    List<Users> userList = new ArrayList<>();
    UserAdapter userAdapterSearch;
    LottieAnimationView progressbar;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_search);
        progressbar = view.findViewById(R.id.progressbar_lottie_searFm);
        district_search_Spinner = view.findViewById(R.id.district_search_Spinner);
        upzila_search_Spinner = view.findViewById(R.id.upzila_search_Spinner);
        blood_group_search_Spinner = view.findViewById(R.id.blood_group_search_Spinner);


        okSearchBtn = view.findViewById(R.id.okSearchBtn);

        dbReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        okSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUsers();
                getUpzilaDonorUser();

                userAdapterSearch = new UserAdapter(getContext(), userList);
                userAdapterSearch.notifyDataSetChanged();
                recyclerView.setAdapter(userAdapterSearch);
                recyclerView.setHasFixedSize(true);
            }
        });


        bloodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.blood_groups, R.layout.spinner_layout_blood);
        districtAdapter_search_fragment = ArrayAdapter.createFromResource(getContext(),
                R.array.allDistrict, R.layout.spinner_layout_blood);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtAdapter_search_fragment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_group_search_Spinner.setAdapter(bloodAdapter);
        district_search_Spinner.setAdapter(districtAdapter_search_fragment);
        district_search_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                districtSpin_search_fragment = district_search_Spinner.getSelectedItem().toString().trim();

                int parentId = parent.getId();
                if (parentId == R.id.district_search_Spinner) {
                    switch (districtSpin_search_fragment) {
                        case "Select Your District":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.defaultupzilla, R.layout.spinner_layout_blood);
                            break;
                        case "Bandarban(বান্দরবন)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Bandarban, R.layout.spinner_layout_blood);
                            break;
                        case "Barguna(বরগুনা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Barguna, R.layout.spinner_layout_blood);
                            break;
                        case "Bagura(বগুড়া)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Bogra, R.layout.spinner_layout_blood);
                            break;
                        case "Brahmanbaria(ব্রাহ্মণবাড়িয়া)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Brahmanbaria, R.layout.spinner_layout_blood);
                            break;
                        case "Cumilla(কুমিল্লা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.cumilla, R.layout.spinner_layout_blood);
                            break;
                        case "Chandpur(চাঁদপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Chandpur, R.layout.spinner_layout_blood);
                            break;
                        case "Bagerhat(বাগেরহাট)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Bagerhat, R.layout.spinner_layout_blood);
                            break;
                        case "Barisal(বরিশাল)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Barisal, R.layout.spinner_layout_blood);
                            break;
                        case "Bhola(ভোলা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Bhola, R.layout.spinner_layout_blood);
                            break;
                        case "Chittagong(চিটাগাং)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Chittagong, R.layout.spinner_layout_blood);
                            break;
                        case "Chuadanga(চুয়াডাঙ্গা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Chuadanga, R.layout.spinner_layout_blood);
                            break;
                        case "Cox\'s Bazar(কক্সবাজার)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.CoxsBazar, R.layout.spinner_layout_blood);
                            break;
                        case "Dhaka(ঢাকা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Dhaka, R.layout.spinner_layout_blood);
                            break;
                        case "Faridpur(ফরিদপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Faridpur, R.layout.spinner_layout_blood);
                            break;
                        case "Feni(ফেনী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.feni, R.layout.spinner_layout_blood);
                            break;
                        case "Gaibandha(গাইবান্ধা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Gaibandha, R.layout.spinner_layout_blood);
                            break;
                        case "Gazipur(গাজীপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Gazipur, R.layout.spinner_layout_blood);
                            break;
                        case "Gopalganj(গোপালগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Gopalganj, R.layout.spinner_layout_blood);
                            break;
                        case "Habiganj(হবিগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Habiganj, R.layout.spinner_layout_blood);
                            break;
                        case "Joypurhat(জয়পুরহাট)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Joypurhat, R.layout.spinner_layout_blood);
                            break;
                        case "Jamalpur(জামালপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Jamalpur, R.layout.spinner_layout_blood);
                            break;
                        case "Jessore(যশোর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Jessore, R.layout.spinner_layout_blood);
                            break;
                        case "Kurigram(কুড়িগ্রাম)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Kurigram, R.layout.spinner_layout_blood);
                            break;
                        case "Kushtia(কুষ্টিয়া)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Kushtia, R.layout.spinner_layout_blood);
                            break;
                        case "Lakshmipur(লক্ষ্মীপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Lakshmipur, R.layout.spinner_layout_blood);
                            break;
                        case "Jhalakathi(ঝালকাঠী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Jhalokati, R.layout.spinner_layout_blood);
                            break;
                        case "Jhinaidah(ঝিনাইদাহ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Jhenaidah, R.layout.spinner_layout_blood);
                            break;
                        case "Khagrachari(খাগড়াছড়ি)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Khagrachhari, R.layout.spinner_layout_blood);
                            break;
                        case "Khulna(খুলনা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Khulna, R.layout.spinner_layout_blood);
                            break;
                        case "Kishoreganj(কিশোরগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Kishoreganj, R.layout.spinner_layout_blood);
                            break;
                        case "Lalmonirhat(লালমনিরহাট)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Lalmonirhat, R.layout.spinner_layout_blood);
                            break;
                        case "Madaripur(মাদারীপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Madaripur, R.layout.spinner_layout_blood);
                            break;
                        case "Magura(মাগুরা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Magura, R.layout.spinner_layout_blood);
                            break;
                        case "Manikganj(মানিকগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Manikganj, R.layout.spinner_layout_blood);
                            break;
                        case "Meherpur(মেহেরপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Meherpur, R.layout.spinner_layout_blood);
                            break;
                        case "Moulavibazar(মৌলভীবাজার)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Moulvibazar, R.layout.spinner_layout_blood);
                            break;
                        case "Munshiganj(মুন্সীগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Munshiganj, R.layout.spinner_layout_blood);
                            break;
                        case "Mymensingh(ময়মনসিংহ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Mymensingh, R.layout.spinner_layout_blood);
                            break;
                        case "Naogaon(নওগাঁ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Naogaon, R.layout.spinner_layout_blood);
                            break;
                        case "Narayanganj(নারায়ণগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Narayanganj, R.layout.spinner_layout_blood);
                            break;
                        case "Narsingdi(নরসিংদী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Narsingdi, R.layout.spinner_layout_blood);
                            break;
                        case "Natore(নাটোর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Natore, R.layout.spinner_layout_blood);
                            break;
                        case "Chapainawabganj(চাঁপাইনবাবগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Chapainawabganj, R.layout.spinner_layout_blood);
                            break;
                        case "Netrokona(নেত্রকোনা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Netrokona, R.layout.spinner_layout_blood);
                            break;
                        case "Nilphamari(নীলফামারী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Nilphamari, R.layout.spinner_layout_blood);
                            break;
                        case "Noakhali(নোয়াখালী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Noakhali, R.layout.spinner_layout_blood);
                            break;
                        case "Norail(নড়াইল)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Narail, R.layout.spinner_layout_blood);
                            break;
                        case "Pabna(পাবনা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Pabna, R.layout.spinner_layout_blood);
                            break;
                        case "Panchagarh(পঞ্চগড়)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Panchagarh, R.layout.spinner_layout_blood);
                            break;
                        case "Patuakhali(পটুয়াখালী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Patuakhali, R.layout.spinner_layout_blood);
                            break;
                        case "Pirojpur(পিরোজপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Pirojpur, R.layout.spinner_layout_blood);
                            break;
                        case "Rajbari(রাজবাড়ী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Rajbari, R.layout.spinner_layout_blood);
                            break;
                        case "Rajshahi(রাজশাহী)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Rajshahi, R.layout.spinner_layout_blood);
                            break;
                        case "Rangamati(রাঙ্গামাটি)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Rangamati, R.layout.spinner_layout_blood);
                            break;
                        case "Rangpur(রংপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Rangpur, R.layout.spinner_layout_blood);
                            break;
                        case "Shariyatpur(শরীয়তপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Shariatpur, R.layout.spinner_layout_blood);
                            break;
                        case "Satkhira(সাতক্ষীরা)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Satkhira, R.layout.spinner_layout_blood);
                            break;
                        case "Sherpur(শেরপুর)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Sherpur, R.layout.spinner_layout_blood);
                            break;
                        case "Sirajganj(সিরাজগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Sirajganj, R.layout.spinner_layout_blood);
                            break;
                        case "Sunamganj(সুনামগঞ্জ)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Sunamganj, R.layout.spinner_layout_blood);
                            break;
                        case "Sylhet(সিলেট)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Sylhet, R.layout.spinner_layout_blood);
                            break;
                        case "Thakurgaon(ঠাকুরগাঁও)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Thakurgaon, R.layout.spinner_layout_blood);
                            break;
                        case "Tangail(টাঙ্গাইল)":
                            upzillaAdapter_search_fragment = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.Tangail, R.layout.spinner_layout_blood);
                            break;
                        default:
                            break;
                    }
                    upzillaAdapter_search_fragment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    upzila_search_Spinner.setAdapter(upzillaAdapter_search_fragment);
                    upzila_search_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            upzilaSpin_search_fragment = upzila_search_Spinner.getSelectedItem().toString().trim();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void checkSearch() {
        if (blood_group_search_fragment.equals("Select Blood Group") || districtSpin_search_fragment.equals("Select Your District")
                || upzilaSpin_search_fragment.equals("Select Your Upzila")) {
            customDialog();
        } else {
            Toast.makeText(getContext(), "..", Toast.LENGTH_SHORT).show();
        }
    }

    private void customDialog1() {

        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.custom_fail_dialog);

        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.setCancelable(false);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog1.show();
        TextView oopstxV = dialog1.findViewById(R.id.oopstextView);
        TextView msgtextView = dialog1.findViewById(R.id.msgtextView);
        oopstxV.setText(R.string.oops1);
        msgtextView.setText(R.string.notfound);
        Button ok = dialog1.findViewById(R.id.okbtn1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void customDialog() {

        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.custom_fail_dialog);

        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.setCancelable(false);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog1.show();
        TextView msgtextView = dialog1.findViewById(R.id.msgtextView);
        msgtextView.setText(R.string.searchMsg);
        Button ok = dialog1.findViewById(R.id.okbtn1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void getUpzilaDonorUser() {
        DatabaseReference refDb = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressbar.setVisibility(View.VISIBLE);
                String result = "";
                String type = snapshot.child("Usertype").getValue().toString();
                if (type.equals("Donor")) {
                    result = "Receiver";
                } else {
                    result = "Donor";
                }


                blood_group_search_fragment = blood_group_search_Spinner.getSelectedItem().toString().trim();
                districtSpin_search_fragment1 = district_search_Spinner.getSelectedItem().toString().trim();
                upzilaSpin_search_fragment1 = upzila_search_Spinner.getSelectedItem().toString().trim();

                querysearch_frag = blood_group_search_fragment + districtSpin_search_fragment1
                        + upzilaSpin_search_fragment1;
                checkSearch();
                DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                Query query = dbrefer.orderByChild("Upzillasearch").equalTo(result + querysearch_frag);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users usersmodel = ds.getValue(Users.class);
                            userList.add(usersmodel);

                        }
                        userAdapterSearch.notifyDataSetChanged();
                        progressbar.setVisibility(View.GONE);
                        if (userList.isEmpty()) {

                            progressbar.setVisibility(View.GONE);
                            customDialog1();
                        } else {
                            Toast.makeText(getContext(), " hey Donor", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {

        DatabaseReference refDb = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String result = "";
                String type = snapshot.child("Usertype").getValue().toString().trim();
                if (type.equals("Donor")) {
                    result = "Receiver";
                } else {
                    result = "Donor";
                }
                blood_group_search_fragment = blood_group_search_Spinner.getSelectedItem().toString().trim();
                districtSpin_search_fragment1 = district_search_Spinner.getSelectedItem().toString().trim();
                upzilaSpin_search_fragment1 = upzila_search_Spinner.getSelectedItem().toString().trim();

                querysearch_frag = blood_group_search_fragment + districtSpin_search_fragment1
                        + upzilaSpin_search_fragment1;

                DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                Query query = dbrefer.orderByChild("Upzillasearch").equalTo(result + querysearch_frag);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users usersmodel = ds.getValue(Users.class);
                            userList.add(usersmodel);
                        }
                        userAdapterSearch.notifyDataSetChanged();
                        progressbar.setVisibility(View.GONE);
                        if (userList.isEmpty()) {
                            progressbar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(), " hey Donor", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}


