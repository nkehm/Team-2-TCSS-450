//package edu.uw.tcss450team2client.ui.contacts;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//public class ContactStateAdapter extends FragmentStateAdapter {
//    private final int mTabCount = 3;
//
//    public ContactStateAdapter(@NonNull Fragment fragment) {
//        super(fragment);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        Fragment fragment = new ContactListFragment();
//        Bundle args = new Bundle();
//        args.putInt(ContactListFragment.TAB_POSITION, position + 1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mTabCount;
//    }
//}
