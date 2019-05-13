package com.zacwolf.linkmaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;

/**
 * @author Zac Morris <zac@zacwolf.com>
 *
 */
public class LinkMaker {
final			private	String	DESKTOP;
final			private	String	PICTURES;
final	static	private	String	ICONS_DIRNAME	=	"/DesktopUrlIcons_DONT_DELETE";

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 *
	 */
	public LinkMaker() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		DESKTOP		=	WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "Desktop");
		PICTURES	=	WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "My Pictures");
	}

	private static TrustManager[] getTrustingManager() {
final   TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
               @Override
               public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                     return null;
               }
               @Override
               public void checkClientTrusted(final X509Certificate[] certs, final String authType) throws CertificateException {
               }
               @Override
               public void checkServerTrusted(final X509Certificate[] certs, final String authType) throws CertificateException {
               }
        } };
        return trustAllCerts;
     }

	public String writeDotURL(final String u) throws Exception {
final	StringBuffer			content		=	new StringBuffer();
								content.append("[InternetShortcut]\r\nURL="+u+"\r\n");
final	File					icons		=	new File(PICTURES+ICONS_DIRNAME);
final	URL						url			=	new URL(u);
final	String					site		=	url.getHost();
		if (icons.isDirectory() || icons.mkdir()) {
final	File					icon		=	new File(PICTURES+ICONS_DIRNAME+"/"+site+".ico");
			if (!icon.exists()) {
				try {
final	InputStream				con			=	getConnection(new URL(url.getProtocol()+"://"+site+"/favicon.ico"));
final	List<BufferedImage>		image		=	ICODecoder.read(con);
								con.close();
final	FileOutputStream		fos			=	new FileOutputStream(icon);
								ICOEncoder.write(image,fos);
								fos.close();

				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			if (icon.exists()) {
				content.append("IconIndex=0\r\nIconFile="+icon.getAbsolutePath()+"\r\n");
			}
		}
final	Path					path		=	Paths.get(DESKTOP+"/"+site+".url");
		Files.write(path, content.toString().getBytes());
		return site;
	}

	private InputStream getConnection(final URL url) throws Exception {
			if(url.toString().indexOf("https://")!=-1){
final	SSLContext				sc			=	SSLContext.getInstance("SSL");
								sc.init(null, getTrustingManager(), new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
             }
		return url.openStream();
	}

	public static void main(final String[] args) {
		try {
final	LinkMaker	maker	=	new LinkMaker();
			System.out.println("URL:"+args[0]);
			System.out.println("DESKTOP:"+maker.DESKTOP);
			System.out.println("PICTURES:"+maker.PICTURES);
			maker.writeDotURL(args[0]);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
