package com.muki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.muki.core.MukiCupApi;
import com.muki.core.MukiCupCallback;
import com.muki.core.model.Action;
import com.muki.core.model.DeviceInfo;
import com.muki.core.model.ErrorCode;
import com.muki.core.model.ImageProperties;
import com.muki.core.util.ImageUtils;


class Api {
    private static Api instance = null;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private boolean mIsOk = false;
    private MukiCupCallback callback = new MukiCupCallback() {
        @Override
        public void onCupConnected() {
            showToast("Cup connected");
            mIsOk = true;
        }

        @Override
        public void onCupDisconnected() {
            showToast("Cup disconnected");
        }

        @Override
        public void onDeviceInfo(DeviceInfo deviceInfo) {
            hideProgress();
            showToast(deviceInfo.toString());
            mIsOk = true;
        }

        @Override
        public void onImageCleared() {
            showToast("Image cleared");
            mIsOk = true;
        }

        @Override
        public void onImageSent() {
            showToast("Image sent");
            mIsOk = true;
        }

        @Override
        public void onError(Action action, ErrorCode errorCode) {
            //showToast("Error:" + errorCode + " on action:" + action);
        }
    };
    private MukiCupApi mMukiCupApi;
    private Bitmap mImage;
    private int mContrast = ImageProperties.DEFAULT_CONTRACT;
    private String mCupId;
    private String mSerialNumber;
    private Context mContext;

    Api(Activity activity, Context context, String serialNumber) {
        mActivity = activity;
        mContext = context;
        mSerialNumber = serialNumber;
        mMukiCupApi = new MukiCupApi(mContext, callback);
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading. Please wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        instance = this;
    }

    Api(Activity activity, Context context, String serialNumber, String cupId) {
        mActivity = activity;
        mContext = context;
        mSerialNumber = serialNumber;
        mCupId = cupId;

        mMukiCupApi = new MukiCupApi(mContext, callback);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading. Please wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        instance = this;
    }

    static Api getInstance() {
        return instance;
    }

    boolean isOk() {
        boolean ret = mIsOk;
        mIsOk = false;
        return ret;
    }

    void setupImage() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap result = Bitmap.createBitmap(mImage);
                ImageUtils.convertImageToCupImage(result, mContrast);
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //mCupImage.setImageBitmap(bitmap);
                hideProgress();
            }
        }.execute();
    }

    void crop(int sender_image) {
        showProgress();
        Bitmap image = BitmapFactory.decodeResource(mActivity.getResources(), sender_image);
        mImage = ImageUtils.cropImage(image, new Point(100, 0));
        image.recycle();
        setupImage();
    }

    void reset() {
        showProgress();
        Bitmap image = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.test_image);
        mImage = ImageUtils.scaleBitmapToCupSize(image);
        mContrast = ImageProperties.DEFAULT_CONTRACT;
        setupImage();
        image.recycle();
    }

    void send() {
        showProgress();
        Log.e("Sender", "Start");
        mMukiCupApi.sendImage(mImage, new ImageProperties(mContrast), mCupId);
        Log.e("Sender", "Finish");
    }

    public void clear() {
        showProgress();
        mMukiCupApi.clearImage(mCupId);
    }

    void request() {
        showProgress();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String serialNumber = strings[0];
                    return MukiCupApi.cupIdentifierFromSerialNumber(serialNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                mCupId = s;
                mIsOk = true;
                Preferences.setString("cupid", mCupId);
                Preferences.setString("serialnumber", mSerialNumber);
                showToast(mCupId);
                hideProgress();
            }
        }.execute(mSerialNumber);
        mIsOk = true;
    }

    void deviceInfo() {
        showProgress();
        mMukiCupApi.getDeviceInfo(mCupId);
    }

    private void showToast(final String text) {
        hideProgress();
        Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
    }

    private void showProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();
            }
        });
    }

    private void hideProgress() {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        });
    }
}
