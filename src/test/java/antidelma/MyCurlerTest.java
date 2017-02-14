package antidelma;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Before;
import org.junit.Test;

public class MyCurlerTest {

		@Before
		public void setUp() throws Exception {
		}

//		@Test
		public void test1() throws ClientProtocolException, IOException {
			MyCurler curl = new MyCurler();
			/*
			 *  curl 
			 *  -H "Host: www.extrastolowki.pl" 
			 *  -H "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:51.0) Gecko/20100101 Firefox/51.0" 
			 *  -H "Accept: * /*"
			 *  -H "Accept-Language: en-US,en;q=0.5" 
			 *  -H "Referer: http://www.extrastolowki.pl/" 
			 *  -H "Cookie: _ga=GA1.2.1635511702.1487072502; _udmdid.be1e=515d45d1fc783465.1487072503.1.1487072630.1487072503; _udmdses.be1e=*; _dc_u0=1; _dc_u1=1; CAKEPHP=m3ti6q07q8m49lp5rettaf42s0; notice-accepted=1; _gali=page_1" 
			 *  --compressed 
			 *  "http://www.extrastolowki.pl/captcha/BRDKNMDYSWXMFDJBSEVBASSR88ZX63"
			 */
			curl.prepareRequest( MyCurler.RequestTypes.GET, "http://www.extrastolowki.pl/captcha/BRDKNMDYSWXMFDJBSEVBASSR88ZX63" );
			curl.setHeaders( new String[] { 
					"Host: www.extrastolowki.pl",
					"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:51.0) Gecko/20100101 Firefox/51.0",
					"Accept: */*",
					"Accept-Language: en-US,en;q=0.5",
					"Referer: http://www.extrastolowki.pl/",
					"Cookie: _ga=GA1.2.1635511702.1487072502; _udmdid.be1e=515d45d1fc783465.1487072503.1.1487072630.1487072503; _udmdses.be1e=*; _dc_u0=1; _dc_u1=1; notice-accepted=1; _gali=page_1"
			} );
			HttpResponse response = curl.makeRequest();

			List<Cookie> cookies = curl.getCookies();

			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());

			for (Cookie c:cookies) System.out.println( "Cookie: "+c.getName()+" - "+c.getValue() );

			InputStream is = response.getEntity().getContent();
			Image image = ImageIO.read( is ); 
		}

		@Test
		public void test2() throws ClientProtocolException, IOException {
			MyCurler curl = new MyCurler();
			/*
			 *  curl 
			 *  -H "Host: www.extrastolowki.pl" 
			 *  -H "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:51.0) Gecko/20100101 Firefox/51.0" 
			 *  -H "Accept: * /*"
			 *  -H "Accept-Language: en-US,en;q=0.5" 
			 *  -H "Referer: http://www.extrastolowki.pl/" 
			 *  -H "Cookie: _ga=GA1.2.1635511702.1487072502; _udmdid.be1e=515d45d1fc783465.1487072503.1.1487072630.1487072503; _udmdses.be1e=*; _dc_u0=1; _dc_u1=1; CAKEPHP=m3ti6q07q8m49lp5rettaf42s0; notice-accepted=1; _gali=page_1" 
			 *  --compressed 
			 *  "http://www.extrastolowki.pl/captcha/BRDKNMDYSWXMFDJBSEVBASSR88ZX63"
			 */
//			curl.prepareRequest( MyCurlerTest.RequestTypes.POST, "http://requestb.in/s48jx8s4" ); 
			curl.prepareRequest( MyCurler.RequestTypes.POST, "http://www.extrastolowki.pl/vote" );
			curl.setHeaders( new String[] { 
					"Host: www.extrastolowki.pl",
//					"Host: requestb.in",
					"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:51.0) Gecko/20100101 Firefox/51.0",
					"Accept: */*",
					"Accept: application/json, text/javascript, */*; q=0.01",
					"Referer: http://www.extrastolowki.pl/",
					"Content-Type: multipart/form-data; boundary=---------------------------2712841497562100224893282",
					"X-Requested-With: XMLHttpRequest",
					"Cookie: _ga=GA1.2.1635511702.1487072502; _udmdid.be1e=515d45d1fc783465.1487072503.1.1487072630.1487072503; _udmdses.be1e=*; _dc_u0=1; _dc_u1=1; notice-accepted=1; _gali=page_1; CAKEPHP=j48m7k2m8af52a0vmre2gqohe7"
			} );
			curl.setBody("-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"_method\"[NL][NL]POST[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][user_id]\"[NL][NL]205[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][privacy]\"[NL][NL]1[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][email]\"[NL][NL]email.email@gmail.com[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][captcha]\"[NL][NL]SYDKRF[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][nr_kasy]\"[NL][NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][nr_paragonu]\"[NL][NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][data]\"[NL][NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][file]\"; filename=\"\"[NL]Content-Type: application/octet-stream[NL][NL][NL]-----------------------------2712841497562100224893282--".replaceAll("\\[NL\\]", "\n"));
			HttpResponse response = curl.makeRequest();

			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			} 
			System.out.println( "Result: "+result );
			assertTrue( result.toString().contains("\"error\":true"));
		}
}
