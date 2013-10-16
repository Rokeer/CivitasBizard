package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Create WebClient and adjust options for it
		WebClient webClient = new WebClient();
		WebClientOptions webClientOptions = webClient.getOptions();
		webClientOptions.setJavaScriptEnabled(true);
		webClientOptions.setCssEnabled(false);
		webClientOptions.setTimeout(35000);
		webClientOptions.setThrowExceptionOnScriptError(false);

		// read and load cookies
		File file = new File("cookie.file");
		ObjectInputStream in;
		Set<Cookie> cookies = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			cookies = (Set<Cookie>) in.readObject(); // ¶ÁCookie
			in.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CookieManager cookieManager = webClient.getCookieManager();
		cookieManager.setCookiesEnabled(true);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			cookieManager.addCookie(i.next());
		}

		try {
			HtmlPage page = webClient.getPage("http://civitas.soobb.com");
			
			// if not logged in, log in
			if (page.getTitleText().equals("×¢²á - ¹ÅµäÉç»áÄ£Äâ CIVITAS.Soobb.Com")) {
				HtmlPage login = webClient
						.getPage("http://www.soobb.com/Accounts/Login/?Continue=http%3a%2f%2fcivitas.soobb.com%2f");
				HtmlForm form = login.getFormByName("");
				System.out.println(login.asXml());
				List<HtmlInput> buttons = form.getInputsByName("");
				HtmlInput button = buttons.get(0);
				HtmlTextInput email = form.getInputByName("Email");
				HtmlPasswordInput pwd = form.getInputByName("Password");
				email.setText("me@colinzhang.com");
				pwd.setText("lovefist1A");
				System.out.println(button.asXml());
				button.click();
			}

			// direct to trading page and get page count
			page = webClient.getPage("http://civitas.soobb.com/Markets/Commodities/?InventoryID=17248&Page=1");
			DomNodeList<DomElement> uls = page.getElementsByTagName("ul");
			HtmlUnorderedList ul = (HtmlUnorderedList) uls.get(2);
			int pageCount = ul.getChildElementCount();
			
			
			// Collect data from each page
			
			System.out.println(ul.getChildElementCount());

			// Save cookies and close connections
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
					"cookie.file"));
			out.writeObject(cookieManager.getCookies());
			out.close();
			webClient.closeAllWindows();

		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
