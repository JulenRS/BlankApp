package com.jross.blankapp.utils;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

public class ConfirmFieldValidator extends BaseValidator{
    private final String mOtherFieldsEmptyMessage;

    public ConfirmFieldValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = "Passwords must be the same";
        mEmptyMessage = "Confirm field can not be empty";
        mOtherFieldsEmptyMessage = "Original Field can not be empty";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return super.isValid(charSequence);
    }

    @Override
    public boolean confirm(CharSequence s1, CharSequence s2) {
        if (TextUtils.isEmpty(s1)) {
            mErrorContainer.setError(mOtherFieldsEmptyMessage);
            return false;
        } else if (TextUtils.equals(s1, s2)) {
            mErrorContainer.setError("");
            return true;
        } else if (TextUtils.isEmpty(s2)) {
            mErrorContainer.setError(mErrorMessage);
            return false;
        } else {
            mErrorContainer.setError(mErrorMessage);
            return false;
        }
    }
}
