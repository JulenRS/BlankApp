package com.jross.blankapp.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jross.blankapp.R;
import com.jross.blankapp.adapters.AuthAdapter;
import com.jross.blankapp.fragments.LogInFragment;
import com.jross.blankapp.fragments.SignUpFragment;
import com.jross.blankapp.utils.SharedPrefManager;
import com.jross.blankapp.views.AnimatedViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    private static String TAG = "Authentication";
    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    @BindView(R.id.pager)
    AnimatedViewPager pager;
    @BindView(R.id.scrolling_background)
    ImageView background;
    @BindView(R.id.logo)
    TextView txtLogo;

    //defining firebaseauth object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        int[] screenSize = screenSize();

        //initializing firebase auth object
        mAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(mAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //load a very big image and resize it, so it fits our needs
        Glide.with(this)
                .load(R.drawable.busy)
                .into(new ImageViewTarget<Drawable>(background) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.busy);
                        background.setImageBitmap(icon);
                        background.post(() -> {
                            //we need to scroll to the very left edge of the image
                            //fire the scale animation
                            background.scrollTo(-background.getWidth() / 2, 0);
                            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(background, View.SCALE_X, 4f, background.getScaleX());
                            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(background, View.SCALE_Y, 4f, background.getScaleY());
                            AnimatorSet set = new AnimatorSet();
                            set.playTogether(xAnimator, yAnimator);
                            set.setDuration(getResources().getInteger(R.integer.duration));
                            set.start();
                        });
                        pager.post(() -> {
                            AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, txtLogo);
                            pager.setAdapter(adapter);
                        });
                    }
                });
    }

    private int[] screenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    @Override
    public void onLoginRequested(String email, String password) {
        Toast.makeText(this, "Log in requested for email: " + email, Toast.LENGTH_LONG).show();
        logIn(email, password);
    }

    @Override
    public void onSignupRequested(String email, String password) {
        Toast.makeText(this, email + " requested!", Toast.LENGTH_LONG).show();
        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Save Data to SharedPreference
                        sharedPrefManager = new SharedPrefManager(mContext);
                        sharedPrefManager.saveIsLoggedIn(mContext, true);

                        sharedPrefManager.saveEmail(mContext, email);
                        sharedPrefManager.saveName(mContext, user.getDisplayName());
                        sharedPrefManager.savePhoto(mContext, user.getPhotoUrl().toString());

                        sharedPrefManager.saveToken(mContext, user.getIdToken(true).toString());
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Save Data to SharedPreference
                        sharedPrefManager = new SharedPrefManager(mContext);
                        sharedPrefManager.saveIsLoggedIn(mContext, true);

                        sharedPrefManager.saveEmail(mContext, email);
                        sharedPrefManager.saveName(mContext, user.getDisplayName());
                        //sharedPrefManager.savePhoto(mContext, user.getPhotoUrl().toString());

                        sharedPrefManager.saveToken(mContext, user.getIdToken(true).toString());
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

