package ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.nkehm.databinding.FragmentColorBinding;

// Nam Hoang was here 
public class ColorFragment extends Fragment {
    private FragmentColorBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_color, container, false);
        binding =  FragmentColorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    private void updateContent(int color) {
        binding.textLabel.setTextColor(color);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get a reference to the SafeArgs object
        ColorFragmentArgs args = ColorFragmentArgs.fromBundle(getArguments());
        //Set the text color of the label. NOTE no need to cast
        binding.textLabel.setTextColor(args.getColor());
    }
}