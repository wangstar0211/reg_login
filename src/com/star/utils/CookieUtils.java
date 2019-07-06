package com.star.utils;
/**
 * Cookie查找工具类
 * @author zhang
 *
 */

import javax.servlet.http.Cookie;

public class CookieUtils {

	public static Cookie findCookie(Cookie[]cookies, String name) {
		if(cookies == null) {
			//客户端没有携带cookie
			return null;
		}else {
			//客户端携带了cookie
			for(Cookie cookie : cookies) {
				if(name.equals(cookie.getName())) {
					return cookie;
				}
			}
			return null;
			
		}
	}
}
