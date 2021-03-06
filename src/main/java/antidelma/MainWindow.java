package antidelma;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters.PatternConverter;

import antidelma.AntidelmaRequests.AntidelmaResultType;

import static javax.swing.SpringLayout.*;

public class MainWindow extends JFrame {

	private JTextField login;
	private JLabel wysylany;
	private DisplayCaptchaPanel dispCaptcha;
	private JTextField captchaTx;
	private JButton sendBtn;
	protected String cookieStr;
	private DotEmailGenerator generator;
	private Preferences prefs;
	private JLabel loginLabel;
	private JLabel sendLoginLabel;
	private JLabel captchaLabel;

	public void genNextLogin() {
		if (generator.hasNext()) {
			sendBtn.setEnabled(true);
			wysylany.setText( generator.next() );
		} else {
			sendBtn.setEnabled(false);
			wysylany.setText( "" );
		}
	}

	private Component prevTop = null;
	private JButton confirmBtn;
	public void addWithLabel( Component label, Component comp ) {
		Container c = getContentPane();
		SpringLayout layout = (SpringLayout)this.getContentPane().getLayout();

		c.add( label );
		c.add( comp );
		if (prevTop==null) prevTop=c;
		layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, c);
		layout.putConstraint(SpringLayout.WEST, comp, 150, SpringLayout.WEST, label);
		layout.putConstraint(SpringLayout.EAST, comp, -10, SpringLayout.EAST, c);
		if (prevTop != c) {
			layout.putConstraint(SpringLayout.NORTH, comp, 10, SpringLayout.SOUTH, prevTop);
			layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, comp);
		} else {
			layout.putConstraint(SpringLayout.NORTH, comp, 10, SpringLayout.NORTH, prevTop);
			layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, comp);
//			layout.putConstraint(SpringLayout.SOUTH, comp, 0, SpringLayout.SOUTH, label);
		}
		prevTop = comp;
	}

	public MainWindow() {
		Container c = getContentPane();
		SpringLayout layout;
		c.setLayout( layout = new SpringLayout() );

		addWithLabel( loginLabel = new JLabel( "login do gmaila" ), login = new JTextField(20) );

		login.getDocument().addDocumentListener( new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override public void removeUpdate(DocumentEvent e) { changedUpdate(e);	}
			@Override public void changedUpdate(DocumentEvent e) {
				generator = new DotEmailGenerator(login.getText());
				genNextLogin();
			}
		});
		addWithLabel( sendLoginLabel = new JLabel( "wysylany login" ), wysylany = new JLabel(".....................") );

		wysylany.addMouseListener( new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { genNextLogin(); } } );
		addWithLabel( new JLabel("Przepisz znaki"),  dispCaptcha = new DisplayCaptchaPanel() );
		addWithLabel( captchaLabel = new JLabel( "tekst captcha" ), captchaTx = new JTextField(20) );
		addWithLabel( new JLabel(""), sendBtn = new JButton( "Wyślij" ) );

		addWithLabel( new JLabel(""), confirmBtn = new JButton( "Potwierdzenia" ) );
		
		sendBtn.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent e) { tryToPost(); }  });
		confirmBtn.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent e) { confirm(); }  });
		getRootPane().setDefaultButton(sendBtn);
		dispCaptcha.setImage( null );
		dispCaptcha.addMouseListener( new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				getNewCaptcha();
			}
		} );
		setSize(400,220);
		setMinimumSize( new Dimension( 400,250 ));
		prefs = Preferences.userRoot().node(this.getClass().getName());

		login.setText( prefs.get("login", "wpisz login") );
		generator.setId( prefs.getInt("generator_id", 1)-1 );
		if (!prefs.get("date","").equals( dateToString())) generator.setId(0);
		
		genNextLogin();
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				getNewCaptcha();
			}} );
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainWindow.this.windowClosing();
			}
		} );
		this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		captchaTx.requestFocus();
		setVisible(true);
	}

	protected void confirm() {
		String mails = JOptionPane.showInputDialog("Wklej maile z linkami potwierdzajacymi");
		if (mails != null) {
			Pattern p = Pattern.compile( "(http://www\\.extrastolowki\\.pl/vote_confirmation/[a-z0-9/]*)" );
			Matcher m = p.matcher( mails );
			AntidelmaRequests ar = new AntidelmaRequests();
			while (m.find()) {
				System.out.println( m.group() );
				try {
					ar.confirm( m.group() );
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void windowClosing() {
		prefs.put("login", login.getText() );
		prefs.putInt("generator_id", generator.getId() );
		prefs.put( "date", dateToString());
		setVisible(false);
		try {
			prefs.flush();
			System.exit(0);
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public String dateToString() {
		return LocalDate.now().format( DateTimeFormatter.ISO_LOCAL_DATE );
	} 

	public void getNewCaptcha() {
		AntidelmaRequests ar = new AntidelmaRequests();
		Map<String,Object> captcha = ar.getCaptcha();
		cookieStr = (String)captcha.get("cookie");
		dispCaptcha.setImage( (Image)captcha.get("image"));
	}

	protected void tryToPost() {
		String login = this.login.getText();
		if (login.equals("")) {
			JOptionPane.showMessageDialog(this, "login nie moze byc pusty");
			return;
		}

		AntidelmaRequests ar = new AntidelmaRequests();
		try {
			AntidelmaResultType result = ar.doVote( cookieStr, captchaTx.getText(), wysylany.getText()+"@gmail.com" );
			switch (result) {
			case OK:
				storeImage( dispCaptcha.getImage(), captchaTx.getText().toUpperCase() );
				genNextLogin();
				getNewCaptcha();
				captchaTx.setText("");
				break;
			case BAD_CAPTCHA:
				JOptionPane.showMessageDialog( this, "Niepoprawnie odczytana captcha" );
				break;
			case ONLY_ONE:
				JOptionPane.showMessageDialog( this, "Podany adres był już dziś użyty" );
				break;
			case OTHER_ERROR:
				JOptionPane.showMessageDialog( this, "Inny blad: "+ar.getLastStr() );
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void storeImage(Image image, String text) {
		File folder = new File("captcha-storage");
		if (folder.exists() && folder.isDirectory()) {
			File outputFile;
			int idx = 0;
			do {
				outputFile = new File( folder.getAbsolutePath()+File.separator+text+( (idx>0)?"_"+String.format("%04d", idx):"")+".png" );
				idx++;
			} while (outputFile.exists());
			try {
				ImageIO.write((RenderedImage) image, "png", outputFile );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				new MainWindow();

			}

		});

	}

}
