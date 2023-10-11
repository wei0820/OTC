// Generated by view binder compiler. Do not edit!
package com.tools.payhelper.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.tools.payhelper.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class PaymentmatchingListItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView amount;

  @NonNull
  public final TextView bankname;

  @NonNull
  public final Button cancelButton;

  @NonNull
  public final TextView cardno;

  @NonNull
  public final LinearLayout layout;

  @NonNull
  public final TextView orderno;

  @NonNull
  public final TextView payname;

  @NonNull
  public final Button sureButton;

  @NonNull
  public final TextView time;

  @NonNull
  public final TextView tx;

  @NonNull
  public final TextView username;

  private PaymentmatchingListItemBinding(@NonNull LinearLayout rootView, @NonNull TextView amount,
      @NonNull TextView bankname, @NonNull Button cancelButton, @NonNull TextView cardno,
      @NonNull LinearLayout layout, @NonNull TextView orderno, @NonNull TextView payname,
      @NonNull Button sureButton, @NonNull TextView time, @NonNull TextView tx,
      @NonNull TextView username) {
    this.rootView = rootView;
    this.amount = amount;
    this.bankname = bankname;
    this.cancelButton = cancelButton;
    this.cardno = cardno;
    this.layout = layout;
    this.orderno = orderno;
    this.payname = payname;
    this.sureButton = sureButton;
    this.time = time;
    this.tx = tx;
    this.username = username;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static PaymentmatchingListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static PaymentmatchingListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.paymentmatching_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static PaymentmatchingListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.amount;
      TextView amount = rootView.findViewById(id);
      if (amount == null) {
        break missingId;
      }

      id = R.id.bankname;
      TextView bankname = rootView.findViewById(id);
      if (bankname == null) {
        break missingId;
      }

      id = R.id.cancel_button;
      Button cancelButton = rootView.findViewById(id);
      if (cancelButton == null) {
        break missingId;
      }

      id = R.id.cardno;
      TextView cardno = rootView.findViewById(id);
      if (cardno == null) {
        break missingId;
      }

      id = R.id.layout;
      LinearLayout layout = rootView.findViewById(id);
      if (layout == null) {
        break missingId;
      }

      id = R.id.orderno;
      TextView orderno = rootView.findViewById(id);
      if (orderno == null) {
        break missingId;
      }

      id = R.id.payname;
      TextView payname = rootView.findViewById(id);
      if (payname == null) {
        break missingId;
      }

      id = R.id.sure_button;
      Button sureButton = rootView.findViewById(id);
      if (sureButton == null) {
        break missingId;
      }

      id = R.id.time;
      TextView time = rootView.findViewById(id);
      if (time == null) {
        break missingId;
      }

      id = R.id.tx;
      TextView tx = rootView.findViewById(id);
      if (tx == null) {
        break missingId;
      }

      id = R.id.username;
      TextView username = rootView.findViewById(id);
      if (username == null) {
        break missingId;
      }

      return new PaymentmatchingListItemBinding((LinearLayout) rootView, amount, bankname,
          cancelButton, cardno, layout, orderno, payname, sureButton, time, tx, username);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
