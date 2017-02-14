package antidelma;

import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;

public class AntidelmaRequests {
	public static enum AntidelmaResultType { OK, ONLY_ONE, OTHER_ERROR, BAD_CAPTCHA }

	private String lastResultStr = "";
	
	public Map<String, Object> getCaptcha() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MyCurler curl = new MyCurler();
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
			if (response.getStatusLine().getStatusCode() != 200) return result;
			
			for (Cookie c:cookies)
				if (c.getName().equals("CAKEPHP"))
					result.put( "cookie", c.getValue());

			InputStream is = response.getEntity().getContent();
			Image image = ImageIO.read( is );
			result.put( "image", image );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public AntidelmaResultType doVote(String cookieStr, String captchaStr, String emailStr) throws ClientProtocolException, IOException {
		MyCurler curl = new MyCurler();
		curl.prepareRequest( MyCurler.RequestTypes.POST, "http://www.extrastolowki.pl/vote" );
		curl.setHeaders( new String[] { 
				"Host: www.extrastolowki.pl",
				"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:51.0) Gecko/20100101 Firefox/51.0",
				"Accept: */*",
				"Accept: application/json, text/javascript, */*; q=0.01",
				"Referer: http://www.extrastolowki.pl/",
				"Content-Type: multipart/form-data; boundary=---------------------------2712841497562100224893282",
				"X-Requested-With: XMLHttpRequest",
				"Cookie: _ga=GA1.2.1635511702.1487072502; _udmdid.be1e=515d45d1fc783465.1487072503.1.1487072630.1487072503; _udmdses.be1e=*; _dc_u0=1; _dc_u1=1; notice-accepted=1; _gali=page_1; CAKEPHP="+cookieStr
		} );
		String body = "-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"_method\"[NL]"+
		"[NL]POST[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][user_id]\"[NL]"
		+ "[NL]205[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][privacy]\"[NL]"
		+ "[NL]1[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][email]\"[NL]"
		+ "[NL]"+emailStr+"[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][captcha]\"[NL]"
		+ "[NL]"+captchaStr+"[NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][nr_kasy]\"[NL]"
		+ "[NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][nr_paragonu]\"[NL]"
		+ "[NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][data]\"[NL]"
		+ "[NL][NL]-----------------------------2712841497562100224893282[NL]Content-Disposition: form-data; name=\"data[Vote][file]\"; filename=\"\"[NL]Content-Type: application/octet-stream[NL]"
		+ "[NL][NL]-----------------------------2712841497562100224893282--";
		curl.setBody(body.replaceAll("\\[NL\\]", "\n"));
		HttpResponse response = curl.makeRequest();

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		} 
		System.out.println( "Result: "+result );
		line = result.toString();
		lastResultStr = line;
		if (line.contains("\"error\":false")) return AntidelmaResultType.OK;
		if (line.contains("tylko jeden")) return AntidelmaResultType.ONLY_ONE;
		if (line.contains("Popraw kod")) return AntidelmaResultType.BAD_CAPTCHA;
		
		return AntidelmaResultType.OTHER_ERROR;
	}

	public String getLastStr() {
		return lastResultStr;
	}


}
