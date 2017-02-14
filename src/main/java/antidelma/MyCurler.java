package antidelma;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyCurler {

	public enum  RequestTypes {GET, POST};

	final static Logger logger = LogManager.getLogger(MyCurler.class);

	private HttpClient client;
	private HttpRequestBase request;
	private HttpResponse response;
	private HttpClientContext context;

	public MyCurler() {
		client = HttpClientBuilder.create().build();
		context = HttpClientContext.create();
	}

	public void prepareRequest(RequestTypes type, String url) {
		switch (type) {
		case GET: request = new HttpGet(url); break;
		case POST: request = new HttpPost(url);
		}

	}

	public void setHeaders(String[] headers) {
		Arrays.stream(headers).map( h->{
			return h.split(": ",2);
		} ).forEach( h->{ request.addHeader(h[0], h[1]); } ); 
	}

	public HttpResponse makeRequest() throws ClientProtocolException, IOException {
		response = client.execute( request, context );
		return response;
	}

	public List<Cookie> getCookies() {
		CookieStore cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		return cookies;
	}

	public void setBody(String string) throws UnsupportedEncodingException {
		((HttpPost)request).setEntity( new StringEntity(string) );

	}

}
