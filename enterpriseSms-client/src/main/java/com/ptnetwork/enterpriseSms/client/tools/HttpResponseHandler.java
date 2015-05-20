/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.tools;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * @author jasonzhang
 *
 */
public class HttpResponseHandler implements ResponseHandler<HttpRequestResponse> {

	@Override
	public HttpRequestResponse handleResponse(HttpResponse response) throws ClientProtocolException,
			IOException {
		HttpRequestResponse httpResponse = new HttpRequestResponse();
		httpResponse.setStatusCode(response.getStatusLine()
				.getStatusCode());
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// ContentType contentType =
			// ContentType.getOrDefault(entity);
			// Charset charset = contentType.getCharset();
			// Reader reader = new
			// InputStreamReader(entity.getContent(), charset);
			// reader.close();
			String responsContent = EntityUtils.toString(entity);
			httpResponse.setMsg(responsContent);
		}

		return httpResponse;
	}

}
