// Generated by view binder compiler. Do not edit!
package com.example.jatelist.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.jatelist.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton addButton;

  @NonNull
  public final Button exitButton;

  @NonNull
  public final ImageView logo;

  @NonNull
  public final RecyclerView rvJatetxeak;

  @NonNull
  public final TabLayout tab;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull ImageButton addButton,
      @NonNull Button exitButton, @NonNull ImageView logo, @NonNull RecyclerView rvJatetxeak,
      @NonNull TabLayout tab) {
    this.rootView = rootView;
    this.addButton = addButton;
    this.exitButton = exitButton;
    this.logo = logo;
    this.rvJatetxeak = rvJatetxeak;
    this.tab = tab;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addButton;
      ImageButton addButton = ViewBindings.findChildViewById(rootView, id);
      if (addButton == null) {
        break missingId;
      }

      id = R.id.exitButton;
      Button exitButton = ViewBindings.findChildViewById(rootView, id);
      if (exitButton == null) {
        break missingId;
      }

      id = R.id.logo;
      ImageView logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.rv_jatetxeak;
      RecyclerView rvJatetxeak = ViewBindings.findChildViewById(rootView, id);
      if (rvJatetxeak == null) {
        break missingId;
      }

      id = R.id.tab;
      TabLayout tab = ViewBindings.findChildViewById(rootView, id);
      if (tab == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, addButton, exitButton, logo,
          rvJatetxeak, tab);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
