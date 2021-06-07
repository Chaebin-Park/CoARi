/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cse.coari.helper;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/** A helper class to manage all communications with Firebase. */
public class FirebaseManager {
  private static final String TAG =
      com.cse.coari.activity.CloudAnchorActivity.class.getSimpleName() + "." + FirebaseManager.class.getSimpleName();

  /** Listener for a new room code. */
  public interface RoomCodeListener {

    /** Invoked when a new room code is available from Firebase. */
    void onNewRoomCode(Long newRoomCode);

    /** Invoked if a Firebase Database Error happened while fetching the room code. */
    void onError(DatabaseError error);
  }

  /** Listener for a new cloud anchor ID. */
  public interface CloudAnchorIdListener {

    /** Invoked when a new cloud anchor ID is available. */
    void onNewCloudAnchorId(List<String> cloudAnchorIdList);
  }

  // Names of the nodes used in the Firebase Database
  private static final String ROOT_FIREBASE_HOTSPOTS = "Co-ARi 9 floor Anchors";
  private static final String ROOT_LAST_ROOM_CODE = "last_room_code";

  // Some common keys and values used when writing to the Firebase Database.
  private static final String KEY_ANCHOR_ID = "hosted_anchor_id";


  private final FirebaseApp app;
  private final DatabaseReference hotspotListRef;
  private final DatabaseReference roomCodeRef;
  private DatabaseReference currentRoomRef = null;
  private ValueEventListener currentRoomListener = null;


  /**
   * Default constructor for the FirebaseManager.
   *
   * @param context The application context.
   */
  public FirebaseManager(Context context) {
    app = FirebaseApp.initializeApp(context);
    if (app != null) {
      DatabaseReference rootRef = FirebaseDatabase.getInstance(app).getReference();
      hotspotListRef = rootRef.child(ROOT_FIREBASE_HOTSPOTS);
      roomCodeRef = rootRef.child(ROOT_LAST_ROOM_CODE);

      DatabaseReference.goOnline();
    } else {
      Log.d(TAG, "Could not connect to Firebase Database!");
      hotspotListRef = null;
      roomCodeRef = null;
    }
  }

  /**
   * Gets a new room code from the Firebase Database. Invokes the listener method when a new room
   * code is available.
   */
  public void getNewRoomCode(RoomCodeListener listener) {
    Preconditions.checkNotNull(app, "Firebase App was null");
    roomCodeRef.runTransaction(
        new Transaction.Handler() {
          @Override
          public Transaction.Result doTransaction(MutableData currentData) {
            Long nextCode = Long.valueOf(1);
            Object currVal = currentData.getValue();
            if (currVal != null) {
              Long lastCode = Long.valueOf(currVal.toString());
              nextCode = lastCode + 1;
            }
            currentData.setValue(nextCode);
            return Transaction.success(currentData);
          }

          @Override
          public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
            if (!committed) {
              listener.onError(error);
              return;
            }
            Long roomCode = currentData.getValue(Long.class);
            listener.onNewRoomCode(roomCode);
          }
        });
  }

  /** Stores the given anchor ID in the given room code. */
  public void storeAnchorIdInRoom(Long roomCode, List<String> cloudAnchorIdList) {
    Preconditions.checkNotNull(app, "Firebase App was null");
    DatabaseReference roomRef = hotspotListRef.child(String.valueOf(roomCode));

    for(int i=0; i < cloudAnchorIdList.size(); i++) {
      String anchor_id = KEY_ANCHOR_ID + "_" + (901 + i);
      roomRef.child(anchor_id).setValue(cloudAnchorIdList.get(i));
    }

  }

  /**
   * Registers a new listener for the given room code. The listener is invoked whenever the data for
   * the room code is changed.
   */
  public void registerNewListenerForRoom(Long roomCode, CloudAnchorIdListener listener) {
    Preconditions.checkNotNull(app, "Firebase App was null");
    List<String> cloudAnchors = new ArrayList<>();
    clearRoomListener();
    currentRoomRef = hotspotListRef.child(String.valueOf(roomCode));
    currentRoomListener =
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            int size = 0;
            for(DataSnapshot datasnapshotChild : dataSnapshot.getChildren()) {
              Object valObj = datasnapshotChild.getValue();
              size++;
              Log.d("size", String.valueOf(valObj));
              if (valObj != null) {
                cloudAnchors.add(String.valueOf(valObj));
              }
            }
            if (!cloudAnchors.isEmpty()) {
              listener.onNewCloudAnchorId(cloudAnchors);
            }
          }
          @Override
          public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "The Firebase operation was cancelled.", databaseError.toException());
          }
        };
    currentRoomRef.addValueEventListener(currentRoomListener);
  }

  /**
   * Resets the current room listener registered using {@link #registerNewListenerForRoom(Long,
   * CloudAnchorIdListener)}.
   */
  public void clearRoomListener() {
    if (currentRoomListener != null && currentRoomRef != null) {
      currentRoomRef.removeEventListener(currentRoomListener);
      currentRoomListener = null;
      currentRoomRef = null;
    }
  }
}
