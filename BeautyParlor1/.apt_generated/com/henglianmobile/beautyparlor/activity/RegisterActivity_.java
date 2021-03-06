//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package com.henglianmobile.beautyparlor.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import com.henglianmobile.beautyparlor.R.id;
import com.henglianmobile.beautyparlor.R.layout;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RegisterActivity_
    extends RegisterActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_register);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        requestWindowFeature(1);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static RegisterActivity_.IntentBuilder_ intent(Context context) {
        return new RegisterActivity_.IntentBuilder_(context);
    }

    public static RegisterActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new RegisterActivity_.IntentBuilder_(fragment);
    }

    public static RegisterActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new RegisterActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        et_code = ((EditText) hasViews.findViewById(id.et_code));
        et_phone = ((EditText) hasViews.findViewById(id.et_phone));
        tv_getcode = ((TextView) hasViews.findViewById(id.tv_getcode));
        et_password = ((EditText) hasViews.findViewById(id.et_password));
        tv_userType = ((TextView) hasViews.findViewById(id.tv_userType));
        et_nick = ((EditText) hasViews.findViewById(id.et_nick));
        if (tv_getcode!= null) {
            tv_getcode.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RegisterActivity_.this.tv_getcode();
                }

            }
            );
        }
        {
            View view = hasViews.findViewById(id.rl_userType);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        RegisterActivity_.this.rl_userType();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.btn_submit);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        RegisterActivity_.this.btn_submit();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.btn_register_back);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        RegisterActivity_.this.btn_register_back();
                    }

                }
                );
            }
        }
        initAttrs();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<RegisterActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, RegisterActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), RegisterActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), RegisterActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode);
                } else {
                    super.startForResult(requestCode);
                }
            }
        }

    }

}
