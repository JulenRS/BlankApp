package com.jross.blankapp.utils.validation;

import android.support.design.widget.TextInputLayout;

public class RequiredFieldValidator extends BaseValidator {

    public RequiredFieldValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mEmptyMessage = "This Field is required";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }
}
