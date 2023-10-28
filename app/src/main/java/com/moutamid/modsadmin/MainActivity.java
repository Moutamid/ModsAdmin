package com.moutamid.modsadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moutamid.modsadmin.databinding.ActivityMainBinding;
import com.moutamid.modsadmin.fragments.MapsFragment;
import com.moutamid.modsadmin.fragments.MixedFragment;
import com.moutamid.modsadmin.fragments.ModsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.toolbar.back.setVisibility(View.GONE);
        binding.toolbar.title.setText(getString(R.string.app_name));

        binding.addMod.setOnClickListener(v -> startActivity(new Intent(this, AddNewActivity.class)));

        setupViewPager();

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ModsFragment(), "Mods");
        adapter.addFragment(new MapsFragment(), "Maps");
        adapter.addFragment(new MixedFragment(), "Mixed");
        binding.viewPager.setAdapter(adapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }


}