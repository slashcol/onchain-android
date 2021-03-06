package stamp.app;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * @author Eric Larchevêque
 */
public class BitID
{
    public static final boolean checkBitidUriValidity(URI uri)
    {
        if (!"bitid".equals(uri.getScheme()))
        {
            return false;
        }

        if (uri.getHost() == null || uri.getPath() == null || uri.getPath().length() == 0 || "/".equals(uri.getPath()) )
        {
            return false;
        }

        if (extractNonceFromBitidUri(uri) == null)
        {
            return false;
        }

        return true;
    }

    public static URI buildCallbackUriFromBitidUri(final URI bitidUri)
    {
        try {
            String scheme = "https";
            final List<NameValuePair> params = URLEncodedUtils.parse(bitidUri, "UTF-8");
            for (NameValuePair param : params)
            {
                if ("u".equals(param.getName()) && Integer.valueOf(param.getValue()) == 1)
                {
                    scheme = "http";
                }
            }
            return new URI(scheme, null, bitidUri.getHost(), bitidUri.getPort(), bitidUri.getPath(), null, null);
        } catch (URISyntaxException x) {
            return null;
        }
    }

    public static String extractNonceFromBitidUri(final URI bitidUri)
    {
        final List<NameValuePair> params = URLEncodedUtils.parse(bitidUri, "UTF-8");
        for (NameValuePair param : params)
        {
            if ("x".equals(param.getName()))
            {
                return param.getValue();
            }
        }
        return null;
    }
}
