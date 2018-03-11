package com.example.singha02.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.singha02.navigation.accounts.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by SinghA02 on 03/03/2018.
 */

public class account extends Fragment{
    private static final String TAG= "Account";

    //firebase
    private FirebaseAuth.AuthStateListener mauthStateListener;
    //widgets(Button)
    private Button mSignOut;
    private Button addBusiness;
    private FragmentActivity myContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account,container,false);
        mSignOut= (Button) view.findViewById(R.id.sign_out);
        addBusiness= (Button) view.findViewById(R.id.add_business);
        setupFirebaseListener();
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Attempting to sign out User");
                FirebaseAuth.getInstance().signOut();
            }
        });
        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_business business=new add_business();
                FragmentManager manager= myContext.getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.viewpager_container, business).commit();
            }
        });
        return view;

    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    private void setupFirebaseListener(){
        Log.d(TAG,"Setting up the auth state listener");
        mauthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG,"signed_in"+user.getUid());
                }else{
                    Log.d(TAG,"onAuthstateChanged: signed_out");
                    Toast.makeText(getActivity(),"Signed out",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mauthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mauthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mauthStateListener);
        }
    }
}
