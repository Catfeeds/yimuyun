package com.yimuyun.lowraiseapp.base;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdhe.lowfrequency.SerialPort;
import com.yimuyun.lowraiseapp.R;
import com.yimuyun.lowraiseapp.ui.LowGetEarTagActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Timer;

import static android.R.attr.tag;


/**
 * @date: 2017/4/21
 * @desciption:
 */

public abstract class LowBaseRootActivity<T extends BasePresenter> extends RootActivity<T> implements View.OnClickListener {
    private int count = 0 ;
    public String tagId = "";
    @Override
    protected void initEventAndData() {
        initReadTag();
    }



    private static final int PORT = 14; //串口
    private SerialPort mSerialPort;//串口操作类
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    private boolean runFlag = true;
    private boolean startFlag = false;
    private Timer timerStartRead;//
    private void initReadTag(){
        try {
            mSerialPort = new SerialPort(PORT, 9600, 0);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mSerialPort == null){
            Toast.makeText(getApplicationContext(), R.string.serial_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        mSerialPort.MCUpoweron();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        //启动线程
        mReadThread = new ReadThread();
        mReadThread.start();
        Log.e("", "start thread");
    }

    private void startRead(){
        if(!startFlag){
            startFlag = true;
        }else{
            startFlag = false;
            if(timerStartRead != null){
                timerStartRead.cancel();
            }
        }
    }

    public void startReadTag(){
        if(runFlag){
            runFlag = false;
        }else {
            runFlag = true;
        }
    }

    private void stopReadTag(){
        startFlag = false;
        if(timerStartRead != null){
            timerStartRead.cancel();
        }
    }
    //读数据线程
    private class ReadThread extends Thread {
        int size;
        int available;
        byte[] buffer;
        byte[] respBytes;
        byte[] idBytes = new byte[4];
        @Override
        public void run() {
            super.run();
            while(runFlag) {
                buffer = new byte[128];
                if (mInputStream == null) return;
                respBytes = LowBaseRootActivity.this.read();
                if(respBytes != null){
                    String id = handleData(respBytes);
                    String id_hex = handleData2hex(respBytes);
                    if(id != null&&id_hex!=null){
//                        if (checkBox_hex.isChecked()) {
//                            updateUI(id_hex);
//                        }else {
                        count ++;
                        BigInteger srch = new BigInteger(id_hex, 16);
                        updateUI(srch.toString());

//                        }

                    }
                }

//				}
            }
        }
    }

    private String handleData(byte[] data){
        byte[] idBytes = new byte[6];
        if(data == null){
            return null;
        }
        //包头错误
        if(data[0] != 0x02){
            return null;
        }
        if(data.length > 10){
            System.arraycopy(data, 5, idBytes, 0, 6);
            String id = new String(idBytes);
            int idInt = Integer.parseInt(id, 16);
            id = "" + idInt;
            return id;
        }

        return null;
    }
    private String handleData2hex(byte[] data){
        byte[] idBytesASC = new byte[10];
        byte[] idBytes = new byte[5];
        if(data == null){
            return null;
        }
        //锟斤拷头锟斤拷锟斤拷
        if(data[0] != 0x02){
            return null;
        }
        if(data.length > 10){
            System.arraycopy(data, 1, idBytesASC, 0, 10);
            try {
                String id = new String(idBytesASC,"ASC-II");
                return id;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
//			String id = Tools.Bytes2HexString(idBytes, idBytes.length);

        }
        return null;
    }
    //从串口中读取完整的数据
    private byte[] read(){
        int count;
        int size;
        int allCount = 0;
        byte[] buffer = new byte[128];
        byte[] bytes = new byte[64];
        try {
            while(true){
                count = mInputStream.available();
                if(count > 0){
                    Thread.sleep(10);
                    int dataSize = mInputStream.available();
                    byte[] resp = new byte[dataSize];
                    mInputStream.read(resp);
                    Log.i("MainActivity ReadThread", Bytes2HexString(resp, resp.length));
                    if(dataSize > 3){
                        return resp;
                    }
                }
//					if(count < 3){
//						mInputStream.read(bytes);
//						return null;
//					}else{
//						size = mInputStream.available();
//						mInputStream.read(bytes);
//						System.arraycopy(bytes, 0, buffer, allCount, size);
//						allCount += size;
//						if(allCount >= 16 && allCount < 30) {
//							byte[] resp = new byte[allCount];
//							System.arraycopy(buffer, 0, resp, 0, allCount);
//							return resp;
//						}
//					}
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

//	//解析返回的数据包
//	private String resolveData(byte[] resp){
//		int length = resp.length;
//		if(resp[0] != (byte) 0x55 && resp[length - 1] != (byte) 0x55){
//			Log.e("Data error", "数据包有误");
//			return null;
//		}
//		byte[] tag = new byte[8];
//		System.arraycopy(resp, 9, tag, 0, 8);
//		String tagStr = Bytes2HexString(tag, 8);
////		int aa = Integer.parseInt(tagStr, 16);
////		System.out.println(aa);
//		return tagStr;
//	}

    //更新UI
    private void updateUI(String tagId){
        this.tagId = tagId;
        final String tagNewId = tagId;
        runOnUiThread(new Runnable() {

            private MediaPlayer mPlayer;

            @Override
            public void run() {

                //媒体播放
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource("/system/media/audio/ui/VideoRecord.ogg");  //选用系统声音文件
                    mPlayer.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlayer.start();
                synchronized (this) {
                    getTagId(tagNewId);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            runFlag = false;
        //mApplication;
        //mSerialPort = null;
//		mSerialPort.scaner_poweroff();
        try {
            if(mInputStream!=null)
                mInputStream.close();
            if(mOutputStream!=null)
                mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(timerStartRead != null){
            timerStartRead.cancel();
        }
        if(mSerialPort!=null) {
            mSerialPort.MCUpoweroff();
            mSerialPort.close(PORT);
        }
        Log.e("", "close .......");
        super.onDestroy();
    }

    //Byte 数组转十六进制字符串
    public static String Bytes2HexString(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte)(_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte)(_b0 ^ _b1);
        return ret;
    }


    //十六进制字符串转Bytes数组
    public static byte[] HexString2Bytes(String src){
        int len = src.length()/2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();

        for(int i=0; i<len; i++){
            ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
        }
        return ret;
    }



    public abstract void getTagId(String tagId);

}