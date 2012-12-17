/**
 * 
 */
package jabara.sample.web.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import net.arnx.jsonic.JSON;

/**
 * @author jabaraster
 */
@Provider
public class JsonMessageBodyReaderWriter implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType)
     */
    @SuppressWarnings("unused")
    @Override
    public long getSize( //
            final Object pT //
            , final Class<?> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType) {
        return 0;
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType)
     */
    @SuppressWarnings("unused")
    @Override
    public boolean isReadable( //
            final Class<?> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType) {
        return MediaType.APPLICATION_JSON_TYPE.isCompatible(pMediaType);
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType)
     */
    @SuppressWarnings("unused")
    @Override
    public boolean isWriteable( //
            final Class<?> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType) {
        return MediaType.APPLICATION_JSON_TYPE.isCompatible(pMediaType);
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.InputStream)
     */
    @SuppressWarnings("unused")
    @Override
    public Object readFrom( //
            final Class<Object> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType //
            , final MultivaluedMap<String, String> pHttpHeaders //
            , final InputStream pEntityStream) throws IOException, WebApplicationException {
        return JSON.decode(pEntityStream, pType);
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
     */
    @SuppressWarnings("unused")
    @Override
    public void writeTo( //
            final Object pT //
            , final Class<?> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType //
            , final MultivaluedMap<String, Object> pHttpHeaders //
            , final OutputStream pEntityStream) throws IOException, WebApplicationException {
        final StringBuilder sb = new StringBuilder();
        JSON.encode(pT, sb, true);
        System.out.println(new String(sb));

        JSON.encode(pT, pEntityStream, true);
    }

}
