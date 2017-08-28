package com.test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestHost {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String link = "http://ct.ctrip.com/m/SingleSignOn/";
		// String link="https://baidu.com";
		// URI uri;
		// try {
		// uri = new URI(link);
		// String host = uri.getHost();
		// System.out.println(host);
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			System.out.println(getDomainName(link));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	// public static String getDomainName(String url) throws
	// MalformedURLException{
	// if(!url.startsWith("http") && !url.startsWith("https")){
	// url = "http://" + url;
	// }
	// URL netUrl = new URL(url);
	// String host = netUrl.getHost();
	// if(host.startsWith("www")){
	// host = host.substring("www".length()+1);
	// }
	// return host;
	// }

	// public static String GetDomainName(String url)
	// {
	// if (url == null)
	// {
	// throw new Exception("输入的url为空");
	// }
	// Regex reg = new Regex(@"(?<=://)([\w-]+\.)+[\w-]+(?<=/?)");
	// return reg.Match(url, 0).Value.Replace("/", string.Empty);
	// }
	// public static String getHost(String url) {
	// if (!(StringUtils.startsWithIgnoreCase(url, "http://") || StringUtils
	// .startsWithIgnoreCase(url, "https://"))) {
	// url = "http://" + url;
	// }
	//
	// String returnVal = StringUtils.EMPTY;
	// try {
	// Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
	// Matcher m = p.matcher(url);
	// if (m.find()) {
	// returnVal = m.group();
	// }
	//
	// } catch (Exception e) {
	// }
	// if ((StringUtils.endsWithIgnoreCase(returnVal, ".html") || StringUtils
	// .endsWithIgnoreCase(returnVal, ".htm"))) {
	// returnVal = StringUtils.EMPTY;
	// }
	// return returnVal;
	// }
}
