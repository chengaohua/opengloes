package com.ulsee.camera;

import com.ulsee.camera.camera.CameraInterface;
import com.ulsee.camera.camera.CameraInterface.CamOpenOverCallback;
import com.ulsee.camera.camera.preview.CameraGLSurfaceView;
import com.ulsee.camera.util.DisplayUtil;

import android.Manifest;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks{
    private static final String TAG = "yanzi";
    CameraGLSurfaceView glSurfaceView = null;
    ImageButton shutterBtn;
    float previewRate = -1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initUI();
        initViewParams();

        //所要申请的权限
        String[] perms = {Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //  Log.i(TAG, "已获取权限");
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }

        shutterBtn.setOnClickListener(new BtnListeners());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    private void initUI(){
        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.camera_textureview);
        shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
    }
    private void initViewParams(){
        LayoutParams params = glSurfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //Ä¬ÈÏÈ«ÆÁµÄ±ÈÀýÔ€ÀÀ
        glSurfaceView.setLayoutParams(params);

        //ÊÖ¶¯ÉèÖÃÅÄÕÕImageButtonµÄŽóÐ¡Îª120dip¡Á120dip,Ô­ÍŒÆ¬ŽóÐ¡ÊÇ64¡Á64
        LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);;
        shutterBtn.setLayoutParams(p2);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private class BtnListeners implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    CameraInterface.getInstance().doTakePicture();
                    break;
                default:break;
            }
        }

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        glSurfaceView.bringToFront();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        glSurfaceView.onPause();
    }


}
