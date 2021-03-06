package com.moonsister.tcjy.widget.takevideo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.moonsister.tcjy.R;
import com.moonsister.tcjy.center.widget.DynamicSendActivity;
import com.moonsister.tcjy.utils.ActivityUtils;
import com.moonsister.tcjy.utils.ConfigUtils;
import com.moonsister.tcjy.utils.LogUtils;
import com.moonsister.tcjy.utils.SDUtils;
import com.moonsister.tcjy.utils.UIUtils;


@SuppressWarnings("deprecation")
public class TakeVideoActivity extends Activity implements OnClickListener,
        SurfaceHolder.Callback, OnErrorListener, OnInfoListener {
    private static final String TAG = "RecordActivity";
    private final static String CLASS_LABEL = "RecordActivity";
    private PowerManager.WakeLock mWakeLock;
    private TextView btnStart;// 开始录制按钮
    private MediaRecorder mediaRecorder;// 录制视频的类
    private VideoView mVideoView;// 显示视频的控件
    String localPath = "";// 录制的视频路径
    private Camera mCamera;
    // 预览的宽高
    private int previewWidth = 480;
    private int previewHeight = 480;
    // private Chronometer chronometer;
    private int frontCamera = 0;// 0是后置摄像头，1是前置摄像头
    private ImageView btn_switch;
    private View recorder_send;
    private Parameters cameraParameters = null;
    private SurfaceHolder mSurfaceHolder;
    int defaultVideoFrameRate = -1;
    private final static int FLAG_PROGRESS_UP = 1;
    private int progress;
    //	private LocalVideo localvideo;
    private LinearLayout ll_atakevideo_uploadlocal;
    private ImageView iv_atakevideo_del;
    private File file;
    private int duration = 0;
    private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
            } else {// 未对焦成功
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case FLAG_PROGRESS_UP:
                    duration++;
                    progress += 2;
                    if (pb_show_progress != null) {
                        pb_show_progress.setProgress(progress);
                        upProgrss();
                    }
                    break;
                case 2:
                    initTakeVideo();
                    break;
                case 3:
                    pb_show_progress.setProgress(0);
                    break;

            }
        }

        ;
    };
    private boolean isStartRecord = false;

    private void upProgrss() {
        if (isStartRecord) {
            mHandler.sendEmptyMessageDelayed(FLAG_PROGRESS_UP, 1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 自动聚焦变量回调

        // activity的 onCreate 函数中

        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 选择支持半透明模式，在有surfaceview的activity中使用
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_takevideo);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                CLASS_LABEL);
        mWakeLock.acquire();
        initViews();
    }

    private void initViews() {

        findViewById(R.id.rl_takevideo_back).setOnClickListener(this);
        pb_show_progress = (ProgressBar) findViewById(R.id.pb_show_progress);
        recorder_play = findViewById(R.id.recorder_play);
        recorder_play.setOnClickListener(this);
        recorder_send = findViewById(R.id.recorder_send);
        recorder_send.setOnClickListener(this);
        mVideoView = (VideoView) findViewById(R.id.mVideoView);
        btnStart = (TextView) findViewById(R.id.recorder_start);
        // btnStart.setOnTouchListener(new RecorderTouchListener());
        btnStart.setOnClickListener(this);
        mSurfaceHolder = mVideoView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ll_atakevideo_uploadlocal = (LinearLayout) findViewById(R.id.ll_atakevideo_uploadlocal);
        iv_atakevideo_del = (ImageView) findViewById(R.id.iv_atakevideo_del);

        iv_atakevideo_del.setOnClickListener(this);
        ll_atakevideo_uploadlocal.setOnClickListener(this);
    }

    // 最初始样式
    private void initTakeVideo() {
        startActivity(new Intent(TakeVideoActivity.this,
                TakeVideoActivity.class));
        finish();
    }

    class RecorderTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecorder();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecorder();
                        break;
                    default:
                        return false;
                }
                return true;
            } catch (Exception e) {
                stopRecorder();
            }
            return false;
        }

    }

    /**
     * 开始录像
     */
    private void startRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder = null;
        }
        if (localPath != null) {
            File file = new File(localPath);
            if (file.exists())
                file.delete();
        }
        // start recording
        if (!startRecording())
            return;
        if (recorder_play != null) {
            recorder_play.setVisibility(View.GONE);
            recorder_play.setClickable(true);
        }
        if (recorder_send != null) {
            recorder_send.setVisibility(View.GONE);
            recorder_send.setClickable(true);

        }
        if (ll_atakevideo_uploadlocal != null) {
            ll_atakevideo_uploadlocal.setVisibility(View.GONE);
        }
        if (iv_atakevideo_del != null) {
            iv_atakevideo_del.setVisibility(View.GONE);
        }
        // 重置其他
        // chronometer.setBase(SystemClock.elapsedRealtime());
        // chronometer.start();
        isStartRecord = true;
        if (progress != 0) {
            progress = 0;
            mHandler.sendEmptyMessage(3);
        }
        upProgrss();
    }

    /**
     * 结束录像
     */
    private void stopRecorder() {
        // 停止拍摄
        stopRecording();
        // chronometer.stop();
        isStartRecord = false;
//        if (recorder_play != null) {
//            recorder_play.setVisibility(View.VISIBLE);
//            recorder_play.setClickable(true);
//        }
        if (recorder_send != null) {
            recorder_send.setVisibility(View.VISIBLE);
            recorder_send.setClickable(true);

        }
        if (iv_atakevideo_del != null) {
            iv_atakevideo_del.setVisibility(View.VISIBLE);
        }
        if (ll_atakevideo_uploadlocal != null) {
            ll_atakevideo_uploadlocal.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mWakeLock == null) {
            // 获取唤醒锁,保持屏幕常亮
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    CLASS_LABEL);
            mWakeLock.acquire();
        }
        // if (!initCamera()) {
        // showFailDialog();
        // }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private boolean initCamera() {
        try {
            if (frontCamera == 0) {
                mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
            }
            Parameters camParams = mCamera.getParameters();
            camParams = mCamera.getParameters();
            // camParams.setPictureFormat(PixelFormat.JPEG);
            // parameters.setPictureSize(surfaceView.getWidth(),
            // surfaceView.getHeight()); // 部分定制手机，无法正常识别该方法。
            camParams.setFlashMode(Parameters.ANTIBANDING_AUTO);
            camParams.setFocusMode(Parameters.FOCUS_MODE_AUTO);// 1连续对焦
            // setDispaly(camParams,mCamera);

            mCamera.setParameters(camParams);
            mCamera.startPreview();
            mCamera.cancelAutoFocus();
            mCamera.lock();
            mSurfaceHolder = mVideoView.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mCamera.setDisplayOrientation(90);
            // setCameraDisplayOrientation(this, frontCamera, mCamera);
        } catch (RuntimeException ex) {
            LogUtils.e("FREE_VIDEO", "init Camera fail " + ex.getMessage());
            return false;
        }
        return true;
    }

    private void handleSurfaceChanged() {
        if (mCamera == null) {
            finish();
            return;
        }
        boolean hasSupportRate = false;
        List<Integer> supportedPreviewFrameRates = mCamera.getParameters()
                .getSupportedPreviewFrameRates();
        if (supportedPreviewFrameRates != null
                && supportedPreviewFrameRates.size() > 0) {
            Collections.sort(supportedPreviewFrameRates);
            for (int i = 0; i < supportedPreviewFrameRates.size(); i++) {
                int supportRate = supportedPreviewFrameRates.get(i);
                LogUtils.e(this, "supportRate = " + supportRate);
                if (supportRate == 30) {
                    hasSupportRate = true;
                }

            }
            if (hasSupportRate) {
                defaultVideoFrameRate = 30;
            } else {
                defaultVideoFrameRate = supportedPreviewFrameRates.get(0);
            }

        }
        // 获取摄像头的所有支持的分辨率
        List<Size> resolutionList = Utils.getResolutionList(mCamera);
        if (resolutionList != null && resolutionList.size() > 0) {
            Collections.sort(resolutionList, new Utils.ResolutionComparator());
            Size previewSize = null;
            boolean hasSize = false;
            // 如果摄像头支持640*480，那么强制设为640*480
            for (int i = 0; i < resolutionList.size(); i++) {
                Size size = resolutionList.get(i);
                LogUtils.e(this, "width   =  " + size.width + " :　　　height  =  "
                        + size.height);
                if (size != null && size.width == 640 && size.height == 480) {

                    previewSize = size;
                    previewWidth = previewSize.width;
                    previewHeight = previewSize.height;
                    hasSize = true;
                    break;
                }
            }
            // 如果不支持设为中间的那个
            if (!hasSize) {
                int mediumResolution = resolutionList.size() / 2;
                if (mediumResolution >= resolutionList.size())
                    mediumResolution = resolutionList.size() - 1;
                previewSize = resolutionList.get(mediumResolution);
                previewWidth = previewSize.width;
                previewHeight = previewSize.height;


            }

        }

    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        LogUtils.e("DisplayOrientatio", "DisplayOrientatio前： " + degrees);
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        LogUtils.e("DisplayOrientatio", "DisplayOrientatio后result： " + result);
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//
//		case R.id.iv_title_righticon:
//			switchCamera();
//			break;
//		case R.id.iv_title_backicon:
//			releaseRecorder();
//			releaseCamera();
//			finish();
//			break;
            case R.id.recorder_send:
                file = new File(localPath);
                if (file.exists()) {
//				Intent intent = new Intent(TakeVideoActivity.this,
//						UploadPhoneVideoActivity.class);
//				Bundle bundle = new Bundle();
//				localvideo = new LocalVideo();
//				localvideo.path = localPath;
//				localvideo.imgPath = VideoUtils.getInstance().getVideoData(
//						localPath);
//				localvideo.size = file.length();
//				localvideo.duration = --duration + "";
//				// localvideo.duration = VideoUtils.getVideoTime(file) + "";
//				bundle.putSerializable(GlobalConstant.INTENT_BUNDLE, localvideo);
//				intent.putExtras(bundle);
//				startActivity(intent);
                    Intent data = new Intent();
                    data.putExtra("path", localPath);
                    setResult(-2, data);
                    finish();

                } else {
                    UIUtils.showToast(this, "文件损坏或拍摄时间过短");
                    startActivity(new Intent(TakeVideoActivity.this,
                            TakeVideoActivity.class));
                }
                finish();
                break;
            case R.id.recorder_play:
                stopRecording();
                isStartRecord = false;
                if (recorder_play != null && !TextUtils.isEmpty(localPath)) {
                    file = new File(localPath);
                    if (file.exists()) {
//					startActivity(new Intent(this, ShowShortVideoActivity.class)
//							.putExtra(GlobalConstant.SHOW_SHORT_VIDEO_PATH,
//									localPath));
                    } else {
                        UIUtils.showToast(this, "文件损坏或拍摄时间过短");
                        initTakeVideo();
                    }
                }
                break;
            case R.id.ll_atakevideo_uploadlocal:
                // mHandler.sendEmptyMessage(2);
                stopRecording();
                isStartRecord = false;
//			startActivity(new Intent(TakeVideoActivity.this,
//					LocalVideoActivity.class));
//			finish();
                break;
            case R.id.iv_atakevideo_del:
                file = new File(localPath);
                if (file != null && file.exists())
                    file.delete();
                // mHandler.sendEmptyMessage(2);
                mHandler.sendEmptyMessageDelayed(2, 1000);
                break;
            case R.id.rl_takevideo_back:
                stopRecording();
                isStartRecord = false;
                finish();
                break;
            case R.id.recorder_start:
                try {
                    if (!btnStart.isSelected()) {
                        startRecorder();
                        btnStart.setSelected(true);
                    } else {
                        btnStart.setSelected(false);
                        stopRecorder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopRecorder();
                }
                break;

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            if (!initCamera()) {
                showFailDialog();
                return;
            }

        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            handleSurfaceChanged();
        } catch (Exception e1) {
            LogUtils.e("FREE_VIDEO", "start preview fail " + e1.getMessage());
            showFailDialog();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        LogUtils.v("FREE_VIDEO", "surfaceDestroyed");
    }

    public boolean startRecording() {
        if (mediaRecorder == null) {
            if (!initRecorder())
                return false;
        }
        try {
            mediaRecorder.setOnInfoListener(this);
            mediaRecorder.setOnErrorListener(this);
            mediaRecorder.start();
        } catch (Exception e) {
            stopRecording();
            return false;
        }

        //
        // }

        return true;
    }

    @SuppressLint("NewApi")
    private boolean initRecorder() {
//		if (!CommonUtils.isExitsSdcard()) {
//			showNoSDCardDialog();
//			return false;
//		}

        if (mCamera == null) {
            if (!initCamera()) {
                showFailDialog();
                return false;
            }
        }
        mVideoView.setVisibility(View.VISIBLE);
        // TODO init button
        mCamera.stopPreview();
        mediaRecorder = new MediaRecorder();

        /*****************************************/
        // //设置视频源
        // mediaRecorder.setVideoSource(MediaRecorder.VideoSource.FREE_PIC);
        // //设置音频源
        // mediaRecorder.setAudioSource(MediaRecorder.AudioSource.FREE_PIC);
        //
        // //相机参数配置类
        // CamcorderProfile cProfile =
        // CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        // mediaRecorder.setProfile(cProfile);
        // 设置录制的视频帧率,注意文档的说明:
        // mediaRecorder.setVideoFrameRate(30);
        // 设置输出路径
        // mediaRecorder.setOutputFile("/sdcard/Document/data/"+ currentTemp +
        // "/"
        // + currentXml + "/"+System.currentTimeMillis()+".mp4");
        // 设置预览画面
        /*****************************************/
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // 设置录制视频源为Camera（相机）
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (frontCamera == 1) {
            mediaRecorder.setOrientationHint(270);
        } else {
            mediaRecorder.setOrientationHint(90);
        }
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置录制的视频编码h263 h264
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mediaRecorder.setVideoSize(previewWidth, previewHeight);

        // mediaRecorder.setVideoSize(720, 539);
        // 设置视频的比特率 5*1920*1080 //384 * 1024
        mediaRecorder.setVideoEncodingBitRate(1 * 1280 * 720);
        // mediaRecorder.setVideoEncodingBitRate(4000);
        // // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        if (defaultVideoFrameRate != -1) {
            mediaRecorder.setVideoFrameRate(defaultVideoFrameRate);
            LogUtils.e(this, "defaultVideoFrameRate = " + defaultVideoFrameRate);
        }
        String path = SDUtils.getInternalMemoryPath() + "/data/" + ConfigUtils.getInstance().getApplicationContext().getPackageName();
        // 设置视频文件输出的路径
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        localPath = path + "/"
                + System.currentTimeMillis() + ".mp4";
        Log.e(TAG, localPath);
        mediaRecorder.setOutputFile(localPath);
        mediaRecorder.setMaxDuration(30000);
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {

        }
        return true;

    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                LogUtils.e("FREE_VIDEO", "stopRecording error:" + e.getMessage());
            }
        }
        releaseRecorder();
        if (mCamera != null) {
            mCamera.stopPreview();
            releaseCamera();
        }
    }

    @Override
    protected void onStop() {
        releaseRecorder();
        super.onStop();
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    protected void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("NewApi")
    public void switchCamera() {

        if (mCamera == null) {
            return;
        }
        if (Camera.getNumberOfCameras() >= 2) {
            btn_switch.setEnabled(false);
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            switch (frontCamera) {
                case 0:
                    mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
                    frontCamera = 1;
                    break;
                case 1:
                    mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
                    frontCamera = 0;
                    break;
            }
            try {
                mCamera.lock();
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mVideoView.getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
            btn_switch.setEnabled(true);

        }

    }

    MediaScannerConnection msc = null;
    ProgressDialog progressDialog = null;
    private View recorder_play;
    private ProgressBar pb_show_progress;

    public void sendVideo() {
        if (TextUtils.isEmpty(localPath)) {
            LogUtils.e("Recorder", "recorder fail please try again!");
            return;
        }

        if (msc == null)
            msc = new MediaScannerConnection(this,
                    new MediaScannerConnectionClient() {

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            LogUtils.d(TAG, "scanner completed");
                            msc.disconnect();
                            progressDialog.dismiss();
                            // setResult(RESULT_OK,
                            // getIntent().putExtra("uri", uri));
                            backAndData(uri);
                            // finish();
                        }

                        @Override
                        public void onMediaScannerConnected() {
                            msc.scanFile(localPath, "FREE_VIDEO/*");
                        }
                    });

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("processing...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        msc.connect();

    }

    private void backAndData(Uri uri) {
        String[] projects = new String[]{MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION};

        Cursor cursor = getContentResolver().query(uri, projects, null, null,
                null);
        int duration = 0;
        String filePath = null;

        if (cursor.moveToFirst()) {
            // 路径：MediaStore.Audio.Media.DATA
            filePath = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            // 总播放时长：MediaStore.Audio.Media.DURATION
            duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            LogUtils.d(TAG, "duration:" + duration);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        setResult(Activity.RESULT_OK, getIntent().putExtra("path", filePath)
                .putExtra("dur", duration));
        finish();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        LogUtils.v("FREE_VIDEO", "onInfo");
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            LogUtils.v("FREE_VIDEO", "max duration reached");
            btnStart.setSelected(false);
            stopRecorder();
            // stopRecording();
            // chronometer.stop();
            // chronometer.stop();
            if (localPath == null) {
                return;
            }
            // String st3 = getResources().getString(R.string.Whether_to_send);
            // new AlertDialog.Builder(this)
            // .setMessage(st3)
            // .setPositiveButton(R.string.ok,
            // new DialogInterface.OnClickListener() {
            //
            // @Override
            // public void onClick(DialogInterface arg0,
            // int arg1) {
            // arg0.dismiss();
            // sendVideo();
            //
            // }
            // }).setNegativeButton(R.string.cancel, null)
            // .setCancelable(false).show();
        }

    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        LogUtils.e("FREE_VIDEO", "recording onError:");
        stopRecording();
        Toast.makeText(this,
                "Recording error has occurred. Stopping the recording",
                Toast.LENGTH_SHORT).show();

    }

    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "a.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showFailDialog() {
        new AlertDialog.Builder(this)
//				.setTitle(R.string.prompt)
//				.setMessage(R.string.Open_the_equipment_failure)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();

                            }
                        }).setCancelable(false).show();

    }

    private void showNoSDCardDialog() {
        new AlertDialog.Builder(this)
//				.setTitle(R.string.prompt)
                .setMessage("No sd card!")
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();

                            }
                        }).setCancelable(false).show();
    }

}