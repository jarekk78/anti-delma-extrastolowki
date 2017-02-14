package antidelma;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import antidelma.AntidelmaRequests.AntidelmaResultType;

public class AntidelmaRequestsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		AntidelmaRequests ar = new AntidelmaRequests();
		Map<String,Object> captcha = ar.getCaptcha();
		assertNotNull( captcha.get("image" ));
		assertNotNull( captcha.get("cookie" ));
		
		Image image = (Image)(captcha.get("image" ));
		ImageIO.write((RenderedImage) image, "png", new File("captcha.png") );
		
		System.out.print( "Enter captcha text: " );
//		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ));
		String captchaStr = "123"; //br.readLine(); 
		AntidelmaResultType result = ar.doVote( (String)captcha.get("cookie"), captchaStr, "email.email.email@gmail.com" );
		assertEquals( AntidelmaResultType.BAD_CAPTCHA, result );
	}

}
