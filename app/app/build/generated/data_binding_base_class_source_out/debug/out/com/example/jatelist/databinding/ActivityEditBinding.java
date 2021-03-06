// Generated by view binder compiler. Do not edit!
package com.example.jatelist.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.jatelist.R;
import com.google.android.gms.maps.MapView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEditBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final ImageButton callButton;

  @NonNull
  public final EditText comments;

  @NonNull
  public final ImageButton editButton;

  @NonNull
  public final ImageButton fotoButton;

  @NonNull
  public final EditText izena;

  @NonNull
  public final MapView mapview;

  @NonNull
  public final EditText phoneNumber;

  @NonNull
  public final RatingBar rating;

  @NonNull
  public final ImageButton removeButton;

  @NonNull
  public final ImageButton saveButton;

  @NonNull
  public final EditText ubi;

  private ActivityEditBinding(@NonNull ConstraintLayout rootView, @NonNull ImageButton backButton,
      @NonNull ImageButton callButton, @NonNull EditText comments, @NonNull ImageButton editButton,
      @NonNull ImageButton fotoButton, @NonNull EditText izena, @NonNull MapView mapview,
      @NonNull EditText phoneNumber, @NonNull RatingBar rating, @NonNull ImageButton removeButton,
      @NonNull ImageButton saveButton, @NonNull EditText ubi) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.callButton = callButton;
    this.comments = comments;
    this.editButton = editButton;
    this.fotoButton = fotoButton;
    this.izena = izena;
    this.mapview = mapview;
    this.phoneNumber = phoneNumber;
    this.rating = rating;
    this.removeButton = removeButton;
    this.saveButton = saveButton;
    this.ubi = ubi;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEditBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEditBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_edit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEditBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.callButton;
      ImageButton callButton = ViewBindings.findChildViewById(rootView, id);
      if (callButton == null) {
        break missingId;
      }

      id = R.id.comments;
      EditText comments = ViewBindings.findChildViewById(rootView, id);
      if (comments == null) {
        break missingId;
      }

      id = R.id.editButton;
      ImageButton editButton = ViewBindings.findChildViewById(rootView, id);
      if (editButton == null) {
        break missingId;
      }

      id = R.id.fotoButton;
      ImageButton fotoButton = ViewBindings.findChildViewById(rootView, id);
      if (fotoButton == null) {
        break missingId;
      }

      id = R.id.izena;
      EditText izena = ViewBindings.findChildViewById(rootView, id);
      if (izena == null) {
        break missingId;
      }

      id = R.id.mapview;
      MapView mapview = ViewBindings.findChildViewById(rootView, id);
      if (mapview == null) {
        break missingId;
      }

      id = R.id.phoneNumber;
      EditText phoneNumber = ViewBindings.findChildViewById(rootView, id);
      if (phoneNumber == null) {
        break missingId;
      }

      id = R.id.rating;
      RatingBar rating = ViewBindings.findChildViewById(rootView, id);
      if (rating == null) {
        break missingId;
      }

      id = R.id.removeButton;
      ImageButton removeButton = ViewBindings.findChildViewById(rootView, id);
      if (removeButton == null) {
        break missingId;
      }

      id = R.id.saveButton;
      ImageButton saveButton = ViewBindings.findChildViewById(rootView, id);
      if (saveButton == null) {
        break missingId;
      }

      id = R.id.ubi;
      EditText ubi = ViewBindings.findChildViewById(rootView, id);
      if (ubi == null) {
        break missingId;
      }

      return new ActivityEditBinding((ConstraintLayout) rootView, backButton, callButton, comments,
          editButton, fotoButton, izena, mapview, phoneNumber, rating, removeButton, saveButton,
          ubi);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
