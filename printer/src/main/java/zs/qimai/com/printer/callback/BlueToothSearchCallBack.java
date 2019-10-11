package zs.qimai.com.printer.callback;

import android.bluetooth.BluetoothDevice;

//蓝牙搜索
 public interface BlueToothSearchCallBack {
     void onSearchStart();
    void searchFailed(int code,String msg);
    void onBlueToothFound(BluetoothDevice bluetoothDevice);
    void onSearchFinish();
    //拒绝打开蓝牙开关
    void onRefuseOpenBtEnable();
    void onSuccessOpenBtEnable();
    void onRefuseGrantPermission();
}
