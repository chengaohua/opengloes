package com.ulsee.camera.camera;

/**
 * Created by uriah on 17-8-28.
 */

import java.io.IOException;
import java.util.List;

import com.ulsee.camera.util.CamParaUtil;
import com.ulsee.camera.util.FileUtil;
import com.ulsee.camera.util.ImageUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraInterface {
    private static final String TAG = "yanzi";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;

    public interface CamOpenOverCallback{
        public void cameraHasOpened();
    }

    private CameraInterface(){

    }
    public static synchronized CameraInterface getInstance(){
        if(mCameraInterface == null){
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }
    /**Žò¿ªCamera
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback){
        Log.i(TAG, "Camera open....");
        if(mCamera == null){
            mCamera = Camera.open();
            Log.i(TAG, "Camera open over....");
            if(callback != null){
                callback.cameraHasOpened();
            }
        }else{
            Log.i(TAG, "Camera open Òì³£!!!");
            doStopCamera();
        }


    }
    /**Ê¹ÓÃSurfaceview¿ªÆôÔ€ÀÀ
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate){
        Log.i(TAG, "doStartPreview...");
        if(isPreviewing){
            mCamera.stopPreview();
            return;
        }
        if(mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }


    }
    /**Ê¹ÓÃTextureViewÔ€ÀÀCamera
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate){
        Log.i(TAG, "doStartPreview...");
        if(isPreviewing){
            mCamera.stopPreview();
            return;
        }
        if(mCamera != null){
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }

    }

    /**
     * Í£Ö¹Ô€ÀÀ£¬ÊÍ·ÅCamera
     */
    public void doStopCamera(){
        if(null != mCamera)
        {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }
    /**
     * ÅÄÕÕ
     */
    public void doTakePicture(){
        if(isPreviewing && (mCamera != null)){
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }
    public boolean isPreviewing(){
        return isPreviewing;
    }



    private void initCamera(float previewRate){
        if(mCamera != null){

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//ÉèÖÃÅÄÕÕºóŽæŽ¢µÄÍŒÆ¬žñÊœ
//			CamParaUtil.getInstance().printSupportPictureSize(mParams);
//			CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //ÉèÖÃPreviewSizeºÍPictureSize
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(),previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(90);

//			CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            mCamera.startPreview();//¿ªÆôÔ€ÀÀ



            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters(); //ÖØÐÂgetÒ»ŽÎ
            Log.i(TAG, "×îÖÕÉèÖÃ:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "×îÖÕÉèÖÃ:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }



    /*ÎªÁËÊµÏÖÅÄÕÕµÄ¿ìÃÅÉùÒôŒ°ÅÄÕÕ±£ŽæÕÕÆ¬ÐèÒªÏÂÃæÈýžö»Øµ÷±äÁ¿*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //¿ìÃÅ°ŽÏÂµÄ»Øµ÷£¬ÔÚÕâÀïÎÒÃÇ¿ÉÒÔÉèÖÃÀàËÆ²¥·Å¡°ßÇàê¡±ÉùÖ®ÀàµÄ²Ù×÷¡£Ä¬ÈÏµÄŸÍÊÇßÇàê¡£
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // ÅÄÉãµÄÎŽÑ¹ËõÔ­ÊýŸÝµÄ»Øµ÷,¿ÉÒÔÎªnull
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //¶ÔjpegÍŒÏñÊýŸÝµÄ»Øµ÷,×îÖØÒªµÄÒ»žö»Øµ÷
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//dataÊÇ×ÖœÚÊýŸÝ£¬œ«ÆäœâÎö³ÉÎ»ÍŒ
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //±£ŽæÍŒÆ¬µœsdcard
            if(null != b)
            {
                //ÉèÖÃFOCUS_MODE_CONTINUOUS_VIDEO)Ö®ºó£¬myParam.set("rotation", 90)Ê§Ð§¡£
                //ÍŒÆ¬Ÿ¹È»²»ÄÜÐý×ªÁË£¬¹ÊÕâÀïÒªÐý×ªÏÂ
                Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
             //   FileUtil.saveBitmap(rotaBitmap);
            }
            //ÔÙŽÎœøÈëÔ€ÀÀ
            mCamera.startPreview();
            isPreviewing = true;
        }
    };


}