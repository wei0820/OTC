// Generated by view binder compiler. Do not edit!
package com.tools.payhelper.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.tools.payhelper.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogOrderStatusBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Spinner bankSpinner;

  @NonNull
  public final Button closeBtn;

  @NonNull
  public final RelativeLayout loading;

  @NonNull
  public final TextView message;

  @NonNull
  public final Button okBtn;

  private DialogOrderStatusBinding(@NonNull RelativeLayout rootView, @NonNull Spinner bankSpinner,
      @NonNull Button closeBtn, @NonNull RelativeLayout loading, @NonNull TextView message,
      @NonNull Button okBtn) {
    this.rootView = rootView;
    this.bankSpinner = bankSpinner;
    this.closeBtn = closeBtn;
    this.loading = loading;
    this.message = message;
    this.okBtn = okBtn;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogOrderStatusBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogOrderStatusBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_order_status, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogOrderStatusBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bank_spinner;
      Spinner bankSpinner = rootView.findViewById(id);
      if (bankSpinner == null) {
        break missingId;
      }

      id = R.id.closeBtn;
      Button closeBtn = rootView.findViewById(id);
      if (closeBtn == null) {
        break missingId;
      }

      RelativeLayout loading = (RelativeLayout) rootView;

      id = R.id.message;
      TextView message = rootView.findViewById(id);
      if (message == null) {
        break missingId;
      }

      id = R.id.okBtn;
      Button okBtn = rootView.findViewById(id);
      if (okBtn == null) {
        break missingId;
      }

      return new DialogOrderStatusBinding((RelativeLayout) rootView, bankSpinner, closeBtn, loading,
          message, okBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}