package mobi.zty.sdk.game.intercept;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SMSHandler extends Handler
{
    public static final String TAG = "SMSHandler";
    private ContentResolver resolver;
    public SMSHandler(ContentResolver resolver)
    {
       super();
       this.resolver = resolver;
    }

    public void handleMessage(Message message)
    {
//       Log.i(TAG,  "handleMessage: " + message);
//       MessageItem item = (MessageItem) message.obj;
//       resolver.delete(
// 	          Uri.parse("content://sms"), "_id=" + item.getId(), null);
//       Log.i(TAG,  "delete sms item: " + item);
    }

}
