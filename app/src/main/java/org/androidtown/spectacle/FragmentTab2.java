package org.androidtown.spectacle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class FragmentTab2 extends Fragment {
    View myView;
    ImageButton campusActivitiesBtn, internationalActivitiesBtn, internBtn, certificateBtn, volunteerBtn, languageBtn;


    public static FragmentTab2 newInstance() {
        FragmentTab2 fragment = new FragmentTab2();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true); //메뉴 버튼 활성화


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_tab2, container, false);

        campusActivitiesBtn = (ImageButton) myView.findViewById(R.id.campus_activities_btn);
        internationalActivitiesBtn = (ImageButton) myView.findViewById(R.id.international_activities_btn);
        internBtn = (ImageButton) myView.findViewById(R.id.intern_btn);
        certificateBtn = (ImageButton) myView.findViewById(R.id.certificate_btn);
        volunteerBtn = (ImageButton) myView.findViewById(R.id.volunteer_activities_btn);
        languageBtn = (ImageButton) myView.findViewById(R.id.language_btn);

        campusActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CampusActivities.class);
                startActivity(intent);


            }
        });

        internationalActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InternationalActivities.class);
                startActivity(intent);

            }
        });

        internBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Intern.class);
                startActivity(intent);

            }
        });

        certificateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Certificate.class);
                startActivity(intent);

            }
        });
        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VolunteerActivities.class);
                startActivity(intent);
            }
        });

        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Language.class);
                startActivity(intent);
            }
        });


        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {

        }

        return true;
    }
}