package zollie.travelblogger.guidee.fragments;

import android.app.Application;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.io.IOException;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.activities.MainActivity;
import zollie.travelblogger.guidee.adapters.DataHandler;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by FuszeneckerZ on 2017.01.24..
 */

public class FaceLoginFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private ProfileFragment _profileFrag = new ProfileFragment();
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private AccessToken mAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //============================= Facebook Sign-in ==================================
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        mProfileTracker = new ProfileTracker(){
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
        //==================================================================================
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
/*        Profile prof = Profile.getCurrentProfile();
        FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firUser != null) { // User is already logged in
            FragmentManager fm;
            fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.contentContainer, _profileFrag).commit();
        }
*/


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facelogin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        VideoView videoView = (VideoView) getActivity().findViewById(R.id.login_video);
        playVideo(videoView);

        mCallbackManager = CallbackManager.Factory.create();
        //============================= Facebook Sign-in ==================================
        LoginButton facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        facebookLoginButton.setReadPermissions("user_friends", "email", "public_profile");
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                AccessToken accessToken = loginResult.getAccessToken();
                mAccessToken = accessToken;
                handleFacebookAccessToken(accessToken);
                ((MainActivity)getActivity()).setPreviousFragment("0");  // Set this to handle login after a logout
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        //==================================================================================
        //=============================== Google Sign-in ===================================
        SignInButton googleLoginButton = (SignInButton) view.findViewById(R.id.google_login_button);
        googleLoginButton.setOnClickListener((View.OnClickListener) this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder((MainActivity)getActivity())
                .enableAutoManage((MainActivity)getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //==================================================================================
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    if(((MainActivity)getActivity()).previousFragmentEquals("0")) { // Coming from MainActivity --> ExploreFragment
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        //Getting Firebase user ID
                        // ======================== Go to Profile Fragment ============================
                        Profile profile = Profile.getCurrentProfile();
                        FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (mAccessToken != null)
                            DataHandler.getInstance().createUserInFIR(firUser, mAccessToken);

                        FragmentManager fm;
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();
                        // ============================================================================
                    }
                }
                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage((MainActivity) getActivity());
        mGoogleApiClient.disconnect();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        FirebaseUser user = task.getResult().getUser();
                        FragmentManager fm;
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
   /*     mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            FragmentManager fm;
                            fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();

                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });*/

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        FirebaseUser user = task.getResult().getUser();
                        FragmentManager fm;
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
  /*      mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            FragmentManager fm;
                            fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();

                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
               // googleSignIn();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            default:
                return;
        }
    }
    private void googleSignIn() {
        FragmentManager fm;
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.contentContainer, (((MainActivity) getActivity()).getProfileFrag())).commit();
    }

    private void googleSignOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

    void playVideo(VideoView view) {

        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.onboardingvid;
        view.setVideoURI(Uri.parse(path));
        view.start();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Coming for google sign-out
       /* if(((MainActivity) getActivity()).getGoogleSignOut())
            googleSignOut();*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
