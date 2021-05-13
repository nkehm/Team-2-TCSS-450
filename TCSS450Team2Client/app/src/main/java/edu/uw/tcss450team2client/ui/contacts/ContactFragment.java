package edu.uw.tcss450team2client.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.tcss450team2client.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ContactListViewModel mContactListViewModel;

    // For the tab view content
    private ViewPager2 mViewPager;
    private ContactStateAdapter contactStateAdapter;
    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactListViewModel = new ViewModelProvider(getActivity())
                .get(ContactListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactStateAdapter = new ContactStateAdapter(this);
        mViewPager = view.findViewById(R.id.contact_viewpager);
        mViewPager.setAdapter(contactStateAdapter);
        mView = view;
        TabLayout tabLayout = view.findViewById(R.id.contact_tabLayout);
        String[] tabNames = {"Friend", "Pending", "Invite"};
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) ->
                tab.setText(tabNames[position])).attach();
    }

    //TODO: SEARCHING FUNCTIONALITY
}