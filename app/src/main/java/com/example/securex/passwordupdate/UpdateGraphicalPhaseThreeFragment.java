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
import com.example.securex.databinding.FragmentRegistrationPhaseFourBinding;
import com.example.securex.databinding.FragmentUpdateGraphicalPhaseThreeBinding;
import com.example.securex.registration.ImageAdapter;
import com.example.securex.viewmodel.PasswordUpdateSharedViewModel;
import com.example.securex.viewmodel.RegistrationSharedViewModel;

import java.util.ArrayList;


public class UpdateGraphicalPhaseThreeFragment extends Fragment implements View.OnClickListener {

    private FragmentUpdateGraphicalPhaseThreeBinding binding;
    private NavController navController;
    PasswordUpdateSharedViewModel model;
    ImageAdapter imageAdapter;
    ArrayList<Integer> FruitsArray;
    ArrayList<Integer> SelectedPositions;
    private String ConfirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateGraphicalPhaseThreeBinding.inflate(inflater, container, false);
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
        binding.updategraphicalphasethreenext.setOnClickListener(this);

        binding.updategridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        binding.updatefruitremove2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updategraphicalphasethreenext:
                ConfirmPassword = "";
                int PasswordSize = SelectedPositions.size();
                for(int i=0;i<SelectedPositions.size();i++){

                    ConfirmPassword+=getActivity().getResources().getResourceEntryName(FruitsArray.get(SelectedPositions.get(i)));

                }
                if(!ConfirmPassword.equals("")){
                    if(ConfirmPassword.equals(model.getPassword().getValue().toString())){
                        model.updateGraphical(getContext());
                        navController.navigate(R.id.action_updateGraphicalPhaseThreeFragment_to_loginActivity);
                        getActivity().finish();

                    }
                    else {
                        showError();;
                    }
                }
                else showError();
                break;
            case R.id.updatefruitremove2:
                if(SelectedPositions.size()>0) {
                    int end = SelectedPositions.size() - 1;
                    v = binding.updategridview1.getChildAt(SelectedPositions.get(SelectedPositions.size()-1));
                    SelectedPositions.remove(SelectedPositions.get(SelectedPositions.size() - 1));
                    v.setBackgroundColor(getResources().getColor(R.color.trans));
                }
                break;
        }

    }
    public void setAdapter() {
        imageAdapter = new ImageAdapter(getActivity().getApplication(),model.getSize().getValue().intValue());
        binding.updategridview1.setAdapter(imageAdapter);
        getFruitsArray(imageAdapter);
    }
    public void getFruitsArray(ImageAdapter imageAdapter) {
        FruitsArray = imageAdapter.getImageID();
    }

    public void showError(){
        Toast.makeText(getActivity().getApplicationContext(),"NOT MATCH",Toast.LENGTH_SHORT).show();
    }
}
