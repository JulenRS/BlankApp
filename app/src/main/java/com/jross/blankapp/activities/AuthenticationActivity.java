package com.jross.blankapp.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.jross.blankapp.R;
import com.jross.blankapp.adapters.AuthAdapter;
import com.jross.blankapp.fragments.LogInFragment;
import com.jross.blankapp.fragments.SignUpFragment;
import com.jross.blankapp.views.AnimatedViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    private static String TAG = "Authentication";

    @BindView(R.id.pager)
    AnimatedViewPager pager;
    @BindView(R.id.scrolling_background)
    ImageView background;
    @BindView(R.id.logo)
    TextView txtLogo;

    private FirebaseAuth mAuth;

    private AuthAdapter adapter;

    private Snackbar errorSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //initializing firebase auth object
        mAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (mAuth.getCurrentUser() != null) {
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
                            adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, txtLogo);
                            pager.setAdapter(adapter);
                        });
                    }
                });
        buildSnackBar();
    }

    private int[] screenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    @Override
    public void onLoginRequested(String email, String password) {
        logIn(email, password);
    }

    @Override
    public void onSignupRequested(String email, String password) {
        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        startActivity(new Intent(this, MainActivity.class));
                    }
                })
                .addOnFailureListener(e -> showError(e.getLocalizedMessage()));
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        startActivity(new Intent(this, MainActivity.class));
                    }
                })
                .addOnFailureListener(e -> showError(e.getLocalizedMessage()));
    }

    private void showError(String errorMessage){
        errorSnackbar.setText(errorMessage);
        errorSnackbar.show();
    }

    private void buildSnackBar(){
        errorSnackbar = Snackbar.make(findViewById(R.id.root), TAG, Snackbar.LENGTH_LONG);
        errorSnackbar.setActionTextColor(Color.WHITE);
        View snackBarView = errorSnackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
    }

}

