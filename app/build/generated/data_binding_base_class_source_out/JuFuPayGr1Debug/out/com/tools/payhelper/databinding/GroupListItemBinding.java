// Generated by view binder compiler. Do not edit!
package com.tools.payhelper.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.tools.payhelper.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class GroupListItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView alipayRebate;

  @NonNull
  public final TextView amount;

  @NonNull
  public final TextView bankname;

  @NonNull
  public final TextView cardno;

  @NonNull
  public final TextView frozen;

  @NonNull
  public final LinearLayout layout;

  @NonNull
  public final TextView orderno;

  @NonNull
  public final TextView time;

  private GroupListItemBinding(@NonNull LinearLayout rootView, @NonNull TextView alipayRebate,
      @NonNull TextView amount, @NonNull TextView bankname, @NonNull TextView cardno,
      @NonNull TextView frozen, @NonNull LinearLayout layout, @NonNull TextView orderno,
      @NonNull TextView time) {
    this.rootView = rootView;
    this.alipayRebate = alipayRebate;
    this.amount = amount;
    this.bankname = bankname;
    this.cardno = cardno;
    this.frozen = frozen;
    this.layout = layout;
    this.orderno = orderno;
    this.time = time;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GroupListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GroupListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.group_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GroupListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.alipayRebate;
      TextView alipayRebate = rootView.findViewById(id);
      if (alipayRebate == null) {
        break missingId;
      }

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

      id = R.id.cardno;
      TextView cardno = rootView.findViewById(id);
      if (cardno == null) {
        break missingId;
      }

      id = R.id.frozen;
      TextView frozen = rootView.findViewById(id);
      if (frozen == null) {
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

      id = R.id.time;
      TextView time = rootView.findViewById(id);
      if (time == null) {
        break missingId;
      }

      return new GroupListItemBinding((LinearLayout) rootView, alipayRebate, amount, bankname,
          cardno, frozen, layout, orderno, time);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}