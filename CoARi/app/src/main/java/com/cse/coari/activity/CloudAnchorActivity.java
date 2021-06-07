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

package com.cse.coari.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.GuardedBy;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.cse.coari.R;
import com.cse.coari.activity.PrivacyNoticeDialogFragment.*;
import com.cse.coari.data.LoginType;
import com.cse.coari.helper.CameraPermissionHelper;
import com.cse.coari.helper.CloudAnchorManager;
import com.cse.coari.helper.DisplayRotationHelper;
import com.cse.coari.helper.FirebaseManager;
import com.cse.coari.helper.FullScreenHelper;
import com.cse.coari.helper.InstantPlacementSettings;
import com.cse.coari.helper.SnackbarHelper;
import com.cse.coari.helper.TapHelper;
import com.cse.coari.helper.TrackingStateHelper;
import com.cse.coari.rendering.BackgroundRenderer;
import com.cse.coari.rendering.ObjectRenderer;
import com.cse.coari.rendering.ObjectRenderer.BlendMode;
import com.cse.coari.rendering.PlaneRenderer;
import com.cse.coari.rendering.PointCloudRenderer;
import com.google.ar.core.Anchor;
import com.google.ar.core.Anchor.CloudAnchorState;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Config.CloudAnchorMode;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Point.OrientationMode;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.common.base.Preconditions;
import com.google.firebase.database.DatabaseError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main Activity for the Cloud Anchor Example
 *
 * <p>This is a simple example that shows how to host and resolve anchors using ARCore Cloud Anchors
 * API calls. This app only has at most one anchor at a time, to focus more on the cloud aspect of
 * anchors.
 */
public class CloudAnchorActivity extends AppCompatActivity
    implements GLSurfaceView.Renderer, NoticeDialogListener, View.OnClickListener {
  private static final String TAG = CloudAnchorActivity.class.getSimpleName();
  private static final float[] OBJECT_COLOR = new float[] {139.0f, 195.0f, 74.0f, 255.0f};

  private enum HostResolveMode {
    NONE,
    HOSTING,
    RESOLVING,
  }

  // Rendering. The Renderers are created here, and initialized when the GL surface is created.
  private GLSurfaceView surfaceView;
  private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
  private final ObjectRenderer virtualObject = new ObjectRenderer();
  private final ObjectRenderer virtualObjectShadow = new ObjectRenderer();
  private final PlaneRenderer planeRenderer = new PlaneRenderer();
  private final PointCloudRenderer pointCloudRenderer = new PointCloudRenderer();

  private boolean installRequested;

  // Temporary matrices allocated here to reduce number of allocations for each frame.
  private final float[] anchorMatrix = new float[16];
  private final float[] viewMatrix = new float[16];
  private final float[] projectionMatrix = new float[16];

  // Locks needed for synchronization
  private final Object singleTapLock = new Object();
  private final Object anchorLock = new Object();

  private static final float APPROXIMATE_DISTANCE_METERS = 2.0f;

  // Tap handling and UI.
  private GestureDetector gestureDetector;
  private final SnackbarHelper snackbarHelper = new SnackbarHelper();
  private DisplayRotationHelper displayRotationHelper;
  private TapHelper tapHelper; // ADD JEONGNI
  private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this);
  private Button hostButton;
  private ImageButton resolveButton;
  private ImageButton undoButton;
  private TextView roomCodeText;
  private SharedPreferences sharedPreferences;
  private static final String PREFERENCE_FILE_KEY = "allow_sharing_images";
  private static final String ALLOW_SHARE_IMAGES_KEY = "ALLOW_SHARE_IMAGES";
  private final InstantPlacementSettings instantPlacementSettings = new InstantPlacementSettings();
  private String loginCase;
  private LoginType enumCase;

  @GuardedBy("singleTapLock")
  private BlockingQueue<MotionEvent> queuedSingleTaps = new ArrayBlockingQueue<>(21);

  private Session session;

  @GuardedBy("anchorLock")
  private final ArrayList<Anchor> anchors = new ArrayList<>();

  // Cloud Anchor Components.
  private FirebaseManager firebaseManager;
  private final CloudAnchorManager cloudManager = new CloudAnchorManager();
  private HostResolveMode currentMode;
  private RoomCodeAndCloudAnchorIdListener hostListener;

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_anchor);

    surfaceView = findViewById(R.id.surfaceview);
    displayRotationHelper = new DisplayRotationHelper(this);

    // Set up touch listener.
    tapHelper = new TapHelper(/*context=*/ this); // ADD JEONGNI
    surfaceView.setOnTouchListener(tapHelper); // ADD JEONGNI
    queuedSingleTaps = tapHelper.getQueuedSingleTaps();

    // Set up renderer.
    surfaceView.setPreserveEGLContextOnPause(true);
    surfaceView.setEGLContextClientVersion(2);
    surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
    surfaceView.setRenderer(this);
    surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    surfaceView.setWillNotDraw(false);
    installRequested = false;

    // Initialize UI components.
    hostButton = findViewById(R.id.host_button);
    undoButton = findViewById(R.id.undo_button);
    undoButton.setOnClickListener(this);
    resolveButton = findViewById(R.id.resolve_button);
    resolveButton.setOnClickListener((view) -> onResolveButtonPress());

    roomCodeText = findViewById(R.id.room_code_text);
    roomCodeText.setVisibility(View.INVISIBLE);
    instantPlacementSettings.onCreate(this); // ADD JEONGNI

    if(getIntent().hasExtra("CASE")){
      enumCase = (LoginType) getIntent().getSerializableExtra("CASE");
      if(enumCase == LoginType.ADMIN){
        hostButton.setOnClickListener((view) -> onHostButtonPress());
      } else {
        hostButton.setVisibility(View.INVISIBLE);
      }
    }

    // Initialize Cloud Anchor variables.
    firebaseManager = new FirebaseManager(this);
    currentMode = HostResolveMode.NONE;
    sharedPreferences = getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
  }

  @Override
  public void onClick(View v) {
    if(v.getId() == R.id.undo_button){
      finish();
    }
  }

  @Override
  protected void onDestroy() {
    // Clear all registered listeners.
    resetMode();

    if (session != null) {
      // Explicitly close ARCore Session to release native resources.
      // Review the API reference for important considerations before calling close() in apps with
      // more complicated lifecycle requirements:
      // https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/Session#close()
      session.close();
      session = null;
    }

    super.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      createSession();
    }
    snackbarHelper.showMessage(this, getString(R.string.snackbar_initial_message));
    surfaceView.onResume();
    displayRotationHelper.onResume();
  }

  private void createSession() {
    if (session == null) {
      Exception exception = null;
      int messageId = -1;
      try {
        switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
          case INSTALL_REQUESTED:
            installRequested = true;
            return;
          case INSTALLED:
            break;
        }
        // ARCore requires camera permissions to operate. If we did not yet obtain runtime
        // permission on Android M and above, now is a good time to ask the user for it.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
          CameraPermissionHelper.requestCameraPermission(this);
          return;
        }
        session = new Session(this);
      } catch (UnavailableArcoreNotInstalledException e) {
        messageId = R.string.snackbar_arcore_unavailable;
        exception = e;
      } catch (UnavailableApkTooOldException e) {
        messageId = R.string.snackbar_arcore_too_old;
        exception = e;
      } catch (UnavailableSdkTooOldException e) {
        messageId = R.string.snackbar_arcore_sdk_too_old;
        exception = e;
      } catch (Exception e) {
        messageId = R.string.snackbar_arcore_exception;
        exception = e;
      }

      if (exception != null) {
        snackbarHelper.showError(this, getString(messageId));
        Log.e(TAG, "Exception creating session", exception);
        return;
      }

      // Create default config and check if supported.
      Config config = new Config(session);
      config.setCloudAnchorMode(CloudAnchorMode.ENABLED);
      session.configure(config);

      // Setting the session in the HostManager.
      cloudManager.setSession(session);
    }

    // Note that order matters - see the note in onPause(), the reverse applies here.
    try {
      session.resume();
    } catch (CameraNotAvailableException e) {
      snackbarHelper.showError(this, getString(R.string.snackbar_camera_unavailable));
      session = null;
      return;
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (session != null) {
      // Note that the order matters - GLSurfaceView is paused first so that it does not try
      // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
      // still call session.update() and get a SessionPausedException.
      displayRotationHelper.onPause();
      surfaceView.onPause();
      session.pause();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if (!CameraPermissionHelper.hasCameraPermission(this)) {
      Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
          .show();
      if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
        // Permission denied with checking "Do not ask again".
        CameraPermissionHelper.launchPermissionSettings(this);
      }
      finish();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
  }

  /**
   * Handles the most recent user tap.
   *
   * <p>We only ever handle one tap at a time, since this app only allows for a single anchor.
   *
   * @param frame the current AR frame
   * //@param cameraTrackingState the current camera tracking state
   */
  // Handle only one tap per frame, as taps are usually low frequency compared to frame rate.
  private void handleTap(Frame frame, TrackingState cameraTrackingState) { // ADD JEONGNI
    synchronized (singleTapLock) {
      synchronized (anchorLock) {
        MotionEvent tap = tapHelper.poll();
        if (tap != null && cameraTrackingState == TrackingState.TRACKING) {
          List<HitResult> hitResultList;

          Preconditions.checkState(
                  currentMode == HostResolveMode.HOSTING,
                  "We should only be creating an anchor in hosting mode.");

          if (instantPlacementSettings.isInstantPlacementEnabled()) {
            hitResultList =
                    frame.hitTestInstantPlacement(tap.getX(), tap.getY(), APPROXIMATE_DISTANCE_METERS);
          } else {
            hitResultList = frame.hitTest(tap);
          }
          // If any plane, Oriented Point, or Instant Placement Point was hit, create an anchor.
          for (HitResult hit : hitResultList) {
            if (shouldCreateAnchorWithHit(hit)) {
              // Cap the number of objects created. This avoids overloading both the
              // rendering system and ARCore.
              if (anchors.size() < 20) {
                Anchor newAnchor = hit.createAnchor();
                Preconditions.checkNotNull(hostListener, "The host listener cannot be null.");
                anchors.add(newAnchor);
                cloudManager.hostCloudAnchor(newAnchor, hostListener);
                snackbarHelper.showMessage(this, getString(R.string.snackbar_anchor_placed));
              }
              // Hits are sorted by depth. Consider only closest hit on a plane, Oriented Point, or
              // Instant Placement Point.
              break;
            }
          }
        }
      }
    }
  }

  /** Returns {@code true} if and only if the hit can be used to create an Anchor reliably. */
  private static boolean shouldCreateAnchorWithHit(HitResult hit) {
    Trackable trackable = hit.getTrackable();
    if (trackable instanceof Plane) {
      // Check if the hit was within the plane's polygon.
      return ((Plane) trackable).isPoseInPolygon(hit.getHitPose());
    } else if (trackable instanceof Point) {
      // Check if the hit was against an oriented point.
      return ((Point) trackable).getOrientationMode() == OrientationMode.ESTIMATED_SURFACE_NORMAL;
    }
    return false;
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

    // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
    try {
      // Create the texture and pass it to ARCore session to be filled during update().
      backgroundRenderer.createOnGlThread(this);
      planeRenderer.createOnGlThread(this, "models/trigrid.png");
      pointCloudRenderer.createOnGlThread(this);

      virtualObject.createOnGlThread(this, "models/andy.obj", "models/andy.png");
      virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);

      virtualObjectShadow.createOnGlThread(
          this, "models/andy_shadow.obj", "models/andy_shadow.png");
      virtualObjectShadow.setBlendMode(BlendMode.Shadow);
      virtualObjectShadow.setMaterialProperties(1.0f, 0.0f, 0.0f, 1.0f);
    } catch (IOException ex) {
      Log.e(TAG, "Failed to read an asset file", ex);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    displayRotationHelper.onSurfaceChanged(width, height);
    GLES20.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    // Clear screen to notify driver it should not load any pixels from previous frame.
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    if (session == null) {
      return;
    }
    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session);

    try {
      session.setCameraTextureName(backgroundRenderer.getTextureId());

      // Obtain the current frame from ARSession. When the configuration is set to
      // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
      // camera framerate.
      Frame frame = session.update();
      Camera camera = frame.getCamera();
      TrackingState cameraTrackingState = camera.getTrackingState();

      // Notify the cloudManager of all the updates.
      cloudManager.onUpdate();

      // Handle user input.
      handleTap(frame, cameraTrackingState); //ADD JEONGNI

      // If frame is ready, render camera preview image to the GL surface.
      backgroundRenderer.draw(frame);

      // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
      trackingStateHelper.updateKeepScreenOnFlag(cameraTrackingState);

      // If not tracking, don't draw 3d objects.
      if (cameraTrackingState == TrackingState.PAUSED) {
        return;
      }

      // Get camera and projection matrices.
      camera.getViewMatrix(viewMatrix, 0);
      camera.getProjectionMatrix(projectionMatrix, 0, 0.1f, 100.0f);

      // Visualize tracked points.
      // Use try-with-resources to automatically release the point cloud.
      try (PointCloud pointCloud = frame.acquirePointCloud()) {
        pointCloudRenderer.update(pointCloud);
        pointCloudRenderer.draw(viewMatrix, projectionMatrix);
      }

      // Visualize planes.
      planeRenderer.drawPlanes(
          session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projectionMatrix);

      // Check if the anchor can be visualized or not, and get its pose if it can be.
      boolean shouldDrawAnchor = false;

      synchronized (anchorLock) {
        for (Anchor anchor : anchors) {
          if (anchor == null && anchor.getTrackingState() != TrackingState.TRACKING) {
            continue;
          }

          // Get the current pose of an Anchor in world space. The Anchor pose is updated
          // during calls to session.update() as ARCore refines its estimate of the world.
          anchor.getPose().toMatrix(anchorMatrix, 0);
          shouldDrawAnchor = true;

          // Visualize anchor.
          if (shouldDrawAnchor) {
            float[] colorCorrectionRgba = new float[4];
            frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);

            // Update and draw the model and its shadow.
            float scaleFactor = 1.0f;
            virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
            virtualObjectShadow.updateModelMatrix(anchorMatrix, scaleFactor);
            virtualObject.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, OBJECT_COLOR);
            virtualObjectShadow.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, OBJECT_COLOR);
          }
        }
      }
    } catch (Throwable t) {
      // Avoid crashing the application due to unhandled exceptions.
      Log.e(TAG, "Exception on the OpenGL thread", t);
    }
  }


  /** Callback function invoked when the Host Button is pressed. */
  private void onHostButtonPress() {
    if (currentMode == HostResolveMode.HOSTING) {
      resetMode();
      return;
    }

    if (!sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      showNoticeDialog(this::onPrivacyAcceptedForHost);
    } else {
      onPrivacyAcceptedForHost();
    }
  }

  private void onPrivacyAcceptedForHost() {
    if (hostListener != null) {
      return;
    }
    resolveButton.setEnabled(false);
    hostButton.setText(R.string.cancel);
    snackbarHelper.showMessageWithDismiss(this, getString(R.string.snackbar_on_host));

    hostListener = new RoomCodeAndCloudAnchorIdListener();
    firebaseManager.getNewRoomCode(hostListener);
  }

  /** Callback function invoked when the Resolve Button is pressed. */
  private void onResolveButtonPress() {
    if (currentMode == HostResolveMode.RESOLVING) {
      resetMode();
      return;
    }

    if (!sharedPreferences.getBoolean(ALLOW_SHARE_IMAGES_KEY, false)) {
      showNoticeDialog(this::onPrivacyAcceptedForResolve);
    } else {
      onPrivacyAcceptedForResolve();
    }
  }

  private void onPrivacyAcceptedForResolve() {
    ResolveDialogFragment dialogFragment = new ResolveDialogFragment();
    dialogFragment.setOkListener(this::onRoomCodeEntered);
    dialogFragment.show(getSupportFragmentManager(), "ResolveDialog");
  }

  /** Resets the mode of the app to its initial state and removes the anchors. */
  private void resetMode() {
    hostButton.setText(R.string.host_button_text);
    hostButton.setEnabled(true);
//    resolveButton.setText(R.string.resolve_button_text);
    resolveButton.setEnabled(true);
    roomCodeText.setText(R.string.initial_room_code);
    currentMode = HostResolveMode.NONE;
    firebaseManager.clearRoomListener();
    hostListener = null;
    snackbarHelper.hide(this);
    cloudManager.clearListeners();
    for(Anchor anchor: anchors) {
      anchor.detach();
    }
    anchors.clear();
  }

  /** Callback function invoked when the user presses the OK button in the Resolve Dialog. */
  private void onRoomCodeEntered(Long roomCode) {
    currentMode = HostResolveMode.RESOLVING;
    hostButton.setEnabled(false);
//    resolveButton.setText(R.string.cancel);
    roomCodeText.setText(String.valueOf(roomCode));
    snackbarHelper.showMessageWithDismiss(this, getString(R.string.snackbar_on_resolve));

    // Register a new listener for the given room.
    firebaseManager.registerNewListenerForRoom(
            roomCode,
            cloudAnchorIdList -> {
              // When the cloud anchor ID is available from Firebase.
              CloudAnchorResolveStateListener resolveListener =
                      new CloudAnchorResolveStateListener(roomCode);
              Preconditions.checkNotNull(resolveListener, "The resolve listener cannot be null.");
              cloudAnchorIdList.forEach(cloudAnchorId -> {
                cloudManager.resolveCloudAnchor(
                        cloudAnchorId, resolveListener, SystemClock.uptimeMillis());
              });
            });
  }

  /**
   * Listens for both a new room code and an anchor ID, and shares the anchor ID in Firebase with
   * the room code when both are available.
   */
  private final class RoomCodeAndCloudAnchorIdListener
      implements CloudAnchorManager.CloudAnchorHostListener, FirebaseManager.RoomCodeListener {

    private Long roomCode;
    private List<String> cloudAnchorIdList = new ArrayList<String>();

    @Override
    public void onNewRoomCode(Long newRoomCode) {
      Preconditions.checkState(roomCode == null, "The room code cannot have been set before.");
      roomCode = newRoomCode;
      roomCodeText.setText(String.valueOf(roomCode));
      snackbarHelper.showMessageWithDismiss(
              CloudAnchorActivity.this, getString(R.string.snackbar_room_code_available));
      checkAndMaybeShare();
      synchronized (singleTapLock) {
        // Change currentMode to HOSTING after receiving the room code (not when the 'Host' button
        // is tapped), to prevent an anchor being placed before we know the room code and able to
        // share the anchor ID.
        currentMode = HostResolveMode.HOSTING;
      }
    }

    @Override
    public void onError(DatabaseError error) {
      Log.w(TAG, "A Firebase database error happened.", error.toException());
      snackbarHelper.showError(
          CloudAnchorActivity.this, getString(R.string.snackbar_firebase_error));
    }

    @Override
    public void onCloudTaskComplete(Anchor anchor) {
      CloudAnchorState cloudState = anchor.getCloudAnchorState();
      if (cloudState.isError()) {
        Log.e(TAG, "Error hosting a cloud anchor, state " + cloudState);
        snackbarHelper.showMessageWithDismiss(
                CloudAnchorActivity.this, getString(R.string.snackbar_host_error, cloudState));
        return;
      }
      cloudAnchorIdList.add(anchor.getCloudAnchorId());

      if(cloudAnchorIdList.size() == 20) checkAndMaybeShare();
    }

    private void checkAndMaybeShare() {
      if (roomCode == null || cloudAnchorIdList.isEmpty()) {
        return;
      }
//      firebaseManager.storeAnchorIdInRoom(roomCode, cloudAnchorId);
      firebaseManager.storeAnchorIdInRoom(roomCode, cloudAnchorIdList);
      snackbarHelper.showMessageWithDismiss(
              CloudAnchorActivity.this, getString(R.string.snackbar_cloud_id_shared));
    }
  }

  private final class CloudAnchorResolveStateListener
      implements CloudAnchorManager.CloudAnchorResolveListener {
    private final long roomCode;

    CloudAnchorResolveStateListener(long roomCode) {
      this.roomCode = roomCode;
    }

    @Override
    public void onCloudTaskComplete(Anchor anchor) {
      // When the anchor has been resolved, or had a final error state.
      CloudAnchorState cloudState = anchor.getCloudAnchorState();
      if (cloudState.isError()) {
        Log.w(
            TAG,
            "The anchor in room "
                + roomCode
                + " could not be resolved. The error state was "
                + cloudState);
        snackbarHelper.showMessageWithDismiss(
                CloudAnchorActivity.this, getString(R.string.snackbar_resolve_error, cloudState));
        return;
      }
      snackbarHelper.showMessageWithDismiss(
              CloudAnchorActivity.this, getString(R.string.snackbar_resolve_success));
      anchors.add(anchor);
    }

    @Override
    public void onShowResolveMessage() {
      snackbarHelper.setMaxLines(4);
      snackbarHelper.showMessageWithDismiss(
              CloudAnchorActivity.this, getString(R.string.snackbar_resolve_no_result_yet));
    }
  }

  public void showNoticeDialog(HostResolveListener listener) {
    DialogFragment dialog = PrivacyNoticeDialogFragment.createDialog(listener);
    dialog.show(getSupportFragmentManager(), PrivacyNoticeDialogFragment.class.getName());
  }

  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
    if (!sharedPreferences.edit().putBoolean(ALLOW_SHARE_IMAGES_KEY, true).commit()) {
      throw new AssertionError("Could not save the user preference to SharedPreferences!");
    }
    createSession();
  }
}
