package aleksa.heler.smarthospital;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DeviceService extends Service {
    DeviceBinder binder;

    public DeviceService() { }

    @Override
    public IBinder onBind(Intent intent) {
        if(binder==null)
            binder = new DeviceBinder();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binder.stop();
        return super.onUnbind(intent);
    }
}
