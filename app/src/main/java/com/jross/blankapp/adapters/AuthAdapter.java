package com.jross.blankapp.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jross.blankapp.R;
import com.jross.blankapp.fragments.AuthFragment;
import com.jross.blankapp.fragments.LogInFragment;
import com.jross.blankapp.fragments.SignUpFragment;
import com.jross.blankapp.views.AnimatedViewPager;

import java.util.List;

public class AuthAdapter extends FragmentStatePagerAdapter
        implements AuthFragment.Callback{

    private final AnimatedViewPager pager;
    private final SparseArray<AuthFragment> authArray;
    private final TextView imgLogo;
    private final ImageView authBackground;
    private float factor;

    public AuthAdapter(FragmentManager manager,
                       AnimatedViewPager pager,
                       ImageView authBackground,
                       TextView imgLogo){
        super(manager);
        this.authBackground=authBackground;
        this.pager=pager;
        this.authArray=new SparseArray<>(getCount());
        this.imgLogo=imgLogo;
        pager.setDuration(350);
        final float textSize=pager.getResources().getDimension(R.dimen.folded_size);
        final float textPadding=pager.getResources().getDimension(R.dimen.folded_label_padding);
        factor=1-(textSize+textPadding)/(pager.getWidth());
    }

    @Override
    public AuthFragment getItem(int position) {
        AuthFragment fragment=authArray.get(position);
        if(fragment==null){
            fragment=position!=1?new LogInFragment():new SignUpFragment();
            authArray.put(position,fragment);
            fragment.setCallback(this);
        }
        return fragment;
    }

    @Override
    public void show(AuthFragment fragment) {
        final int index=authArray.keyAt(authArray.indexOfValue(fragment));
        pager.setCurrentItem(index,true);
        shiftSharedElements(getPageOffsetX(fragment), index==1);
        for(int jIndex=0;jIndex<authArray.size();jIndex++){
            if(jIndex!=index){
                authArray.get(jIndex).fold();
            }
        }
    }

    private float getPageOffsetX(AuthFragment fragment){
        int pageWidth=fragment.getView().getWidth();
        return pageWidth-pageWidth*factor;
    }

    private void shiftSharedElements(float pageOffsetX, boolean forward){
        final Context context=pager.getContext();
        //since we're clipping the page, we have to adjust the shared elements
        AnimatorSet shiftAnimator=new AnimatorSet();

//        float translationX=forward?pageOffsetX:-pageOffsetX;
//        float temp=imgLogo.getWidth()/6f;
//        translationX-=forward?temp:-temp;
//        ObjectAnimator shift=ObjectAnimator.ofFloat(imgLogo,View.TRANSLATION_X,0,translationX);
//        shiftAnimator.playTogether(shift);

        //scroll the background by x
        int offset=authBackground.getWidth()/2;
        ObjectAnimator scrollAnimator=ObjectAnimator.ofInt(authBackground,"scrollX",forward?offset:-offset);
        shiftAnimator.playTogether(scrollAnimator);
        shiftAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        shiftAnimator.setDuration(pager.getResources().getInteger(R.integer.duration)/2);
        shiftAnimator.start();
    }

    @Override
    public void scale(boolean hasFocus) {

        final float scale=hasFocus?1:1.4f;
        final float logoScale=hasFocus?0.75f:1f;
        View logo=imgLogo;

        AnimatorSet scaleAnimation=new AnimatorSet();
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo,View.SCALE_X,logoScale));
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo,View.SCALE_Y,logoScale));
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(authBackground,View.SCALE_X,scale));
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(authBackground,View.SCALE_Y,scale));
        scaleAnimation.setDuration(200);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.start();
    }

    @Override
    public float getPageWidth(int position) {
        return factor;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
