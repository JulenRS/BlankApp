package com.jross.blankapp.utils;

import android.support.design.widget.TextInputLayout;

import com.jross.blankapp.R;

public class PasswordFieldValidator extends BaseValidator{

    private int mMinLength;

    public PasswordFieldValidator(TextInputLayout errorContainer, int length) {
        super(errorContainer);
        mMinLength = length;
        mErrorMessage = mErrorContainer.getResources().getQuantityString(R.plurals.error_week_password, mMinLength, mMinLength);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence.length() >= mMinLength;
    }
}
