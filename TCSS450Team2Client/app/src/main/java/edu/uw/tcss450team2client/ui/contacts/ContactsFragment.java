package edu.uw.tcss450team2client.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ContactsViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
//        mModel.connectGet();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());

        mModel.addBlogListObserver(getViewLifecycleOwner(), contacts -> {
            if (!contacts.isEmpty()) {
                binding.listRoot.setAdapter(
//                        new ui.contacts.ContactsRecyclerViewAdapter(blogList)
                        new edu.uw.tcss450team2client.ui.contacts.ContactsRecyclerViewAdapter(contacts)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }
}