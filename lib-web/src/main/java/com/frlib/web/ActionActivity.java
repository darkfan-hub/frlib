package com.frlib.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.frlib.basic.permissions.OnPermissionCallback;
import com.frlib.basic.permissions.PermissionUtils;
import com.frlib.basic.permissions.XXPermissions;
import timber.log.Timber;

import java.io.File;
import java.util.List;

import static android.provider.MediaStore.EXTRA_OUTPUT;

/**
 * @author ???
 * @since 2.0.0
 */
public final class ActionActivity extends Activity {

    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_URI = "KEY_URI";
    public static final String KEY_FROM_INTENTION = "KEY_FROM_INTENTION";
    public static final String KEY_FILE_CHOOSER_INTENT = "KEY_FILE_CHOOSER_INTENT";
    private static PermissionListener mPermissionListener;
    private static ChooserListener mChooserListener;
    private Action mAction;
    public static final int REQUEST_CODE = 0x254;

    public static void start(Activity activity, Action action) {
        Intent mIntent = new Intent(activity, ActionActivity.class);
        mIntent.putExtra(KEY_ACTION, action);
        activity.startActivity(mIntent);

    }

    public static void setChooserListener(ChooserListener chooserListener) {
        mChooserListener = chooserListener;
    }

    public static void setPermissionListener(PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
    }

    private void cancelAction() {
        mChooserListener = null;
        mPermissionListener = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Timber.i("savedInstanceState:" + savedInstanceState);
            return;
        }
        Intent intent = getIntent();
        mAction = intent.getParcelableExtra(KEY_ACTION);
        if (mAction == null) {
            cancelAction();
            this.finish();
            return;
        }
        if (mAction.getAction() == Action.ACTION_PERMISSION) {
            permission(mAction);
        } else if (mAction.getAction() == Action.ACTION_CAMERA) {
            realOpenCamera();
        } else if (mAction.getAction() == Action.ACTION_VIDEO) {
            realOpenVideo();
        } else {
            fetchFile(mAction);
        }
    }

    private void fetchFile(Action action) {
        if (mChooserListener == null) {
            finish();
        }
        realOpenFileChooser();
    }

    private void realOpenFileChooser() {
        try {
            if (mChooserListener == null) {
                finish();
                return;
            }
            Intent mIntent = getIntent().getParcelableExtra(KEY_FILE_CHOOSER_INTENT);
            if (mIntent == null) {
                cancelAction();
                return;
            }
            this.startActivityForResult(mIntent, REQUEST_CODE);
        } catch (Throwable throwable) {
            Timber.e(throwable);
            Timber.i("找不到文件选择器");
            chooserActionCallback(-1, null);
        }
    }

    private void chooserActionCallback(int resultCode, Intent data) {
        if (mChooserListener != null) {
            mChooserListener.onChoiceResult(REQUEST_CODE, resultCode, data);
            mChooserListener = null;
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            chooserActionCallback(resultCode, mUri != null ? new Intent().putExtra(KEY_URI, mUri) : data);
        }
    }

    private void permission(Action action) {
        List<String> permissionList = action.getPermissions();
        if (AgentWebUtil.isEmptyCollection(permissionList)) {
            mPermissionListener = null;
            finish();
            return;
        }

        if (!PermissionUtils.isPermissionPermanentDenied(this, permissionList)) {
            if (mPermissionListener != null) {
                Bundle mBundle = new Bundle();
                mBundle.putInt(KEY_FROM_INTENTION, mAction.getFromIntention());
                mPermissionListener.onGranted(mBundle);
            }
            mPermissionListener = null;
            finish();
        } else {
            requestPermission(permissionList);
        }
    }

    private void requestPermission(List<String> permissionList) {
        XXPermissions.with(this)
                .permission(permissionList)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (mPermissionListener != null && all) {
                            Bundle mBundle = new Bundle();
                            mBundle.putInt(KEY_FROM_INTENTION, mAction.getFromIntention());
                            mPermissionListener.onGranted(mBundle);
                            mPermissionListener = null;
                            finish();
                        } else {
                            requestPermission(permissionList);
                        }
                    }
                });
    }

    private Uri mUri;

    private void realOpenCamera() {
        try {
            if (mChooserListener == null) {
                finish();
            }
            File mFile = AgentWebUtil.createImageFile(this);
            if (mFile == null) {
                mChooserListener.onChoiceResult(REQUEST_CODE, Activity.RESULT_CANCELED, null);
                mChooserListener = null;
                finish();
            }
            Intent intent = AgentWebUtil.getIntentCaptureCompat(this, mFile);
            // 指定开启系统相机的Action
            mUri = intent.getParcelableExtra(EXTRA_OUTPUT);
            this.startActivityForResult(intent, REQUEST_CODE);
        } catch (Throwable ignore) {
            Timber.e("找不到系统相机");
            Timber.e(ignore);
            if (mChooserListener != null) {
                mChooserListener.onChoiceResult(REQUEST_CODE, Activity.RESULT_CANCELED, null);
            }
            mChooserListener = null;
        }
    }

    private void realOpenVideo() {
        try {
            if (mChooserListener == null) {
                finish();
            }
            File mFile = AgentWebUtil.createVideoFile(this);
            if (mFile == null) {
                mChooserListener.onChoiceResult(REQUEST_CODE, Activity.RESULT_CANCELED, null);
                mChooserListener = null;
                finish();
            }
            Intent intent = AgentWebUtil.getIntentVideoCompat(this, mFile);
            // 指定开启系统相机的Action
            mUri = intent.getParcelableExtra(EXTRA_OUTPUT);
            this.startActivityForResult(intent, REQUEST_CODE);
        } catch (Throwable ignore) {
            Timber.e("找不到系统相机");
            Timber.e(ignore);
            if (mChooserListener != null) {
                mChooserListener.onChoiceResult(REQUEST_CODE, Activity.RESULT_CANCELED, null);
            }
            mChooserListener = null;
        }
    }

    public interface PermissionListener {
        void onGranted(Bundle extras);

        void onDenied(Bundle extras);
    }

    public interface ChooserListener {
        void onChoiceResult(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
