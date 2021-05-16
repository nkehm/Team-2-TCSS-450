package edu.uw.tcss450team2client.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentChatListBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mModel;
    private ChatListRecyclerViewAdapter mAdapter;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mModel.connectGet(model.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

//        binding.buttonAddChat.setOnClickListener(button -> Navigation.findNavController(getView())
//                .navigate(ChatListFragmentDirections.actionChatListFragmentToAddChatFragment()));

        mModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            // if (!chatList.isEmpty()) {
            mAdapter = new ChatListRecyclerViewAdapter(chatList, getActivity().getSupportFragmentManager());
            binding.listRoot.setAdapter(mAdapter);
            binding.layoutWait.setVisibility(View.GONE);
        });
    }
}