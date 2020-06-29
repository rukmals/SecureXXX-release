package com.example.securex.passwordupdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.securex.R;
import com.example.securex.databinding.FragmentUpdateGraphicalPhaseOneBinding;
import com.example.securex.databinding.FragmentUpdateGraphicalPhaseTwoBinding;
import com.example.securex.registration.ImageAdapter;
import com.example.securex.viewmodel.PasswordUpdateSharedViewModel;
import com.example.securex.viewmodel.RegistrationSharedViewModel;

import java.util.ArrayList;


public class UpdateGraphicalPhaseTwoFragment extends Fragment implements View.OnClickListener{

    private FragmentUpdateGraphicalPhaseTwoBinding binding;
    private NavController navController;
    private PasswordUpdateSharedViewModel model;


    ImageAdapter imageAdapter;
    ArrayList<Integer> FruitsArray;
    ArrayList<Integer> SelectedPositions;
    private String Password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateGraphicalPhaseTwoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(PasswordUpdateSharedViewModel.class);
        SelectedPositions = new ArrayList<>();
        setAdapter();
        binding.updategraphicalphasetwonext.setOnClickListener(this);

        binding.updategridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!SelectedPositions.contains(position)){
                        view.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_800));
                        SelectedPositions.add(position);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        binding.updatefruitremove1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updategraphicalphasetwonext:
                Password = "";
                int PasswordSize = SelectedPositions.size();
                for(int i=0;i<SelectedPositions.size();i++){

                    Password+=getActivity().getResources().getResourceEntryName(FruitsArray.get(SelectedPositions.get(i)));

                }
                if (!Password.equals("")){
                    model.setPassword(Password);
                    navController.navigate(R.id.action_updateGraphicalPhaseTwoFragment_to_updateGraphicalPhaseThreeFragment);
                }
                else {
                    showError();
                }

                break;
            case R.id.fruitremove1:
                if(SelectedPositions.size()>0) {
                    int end = SelectedPositions.size() - 1;
                    v = binding.updategridview.getChildAt(SelectedPositions.get(SelectedPositions.size()-1));
                    SelectedPositions.remove(SelectedPositions.get(SelectedPositions.size() - 1));
                    v.setBackgroundColor(getResources().getColor(R.color.trans));
                }
                break;


        }
    }

    public void setAdapter() {
        imageAdapter = new ImageAdapter(getActivity().getApplication(),model.getSize().getValue().intValue());
        binding.updategridview.setAdapter(imageAdapter);
        getFruitsArray(imageAdapter);
    }
    public void getFruitsArray(ImageAdapter imageAdapter) {
        FruitsArray = imageAdapter.getImageID();
    }

    public void showError(){
        Toast.makeText(getActivity().getApplicationContext(),"SELECT IMAGES",Toast.LENGTH_SHORT).show();
    }
}
