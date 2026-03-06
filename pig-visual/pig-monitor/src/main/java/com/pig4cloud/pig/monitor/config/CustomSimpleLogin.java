//package com.pig4cloud.pig.monitor.config;
//
//import cn.dev33.satoken.stp.StpUtil;
//import com.teligen.sso.client.webSSO.client.ILoginAction;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import com.pig4cloud.pig.common.core.entity.dto.Message;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Enumeration;
//
//public class CustomSimpleLogin implements ILoginAction {
//	/**
//	 * 登录成功的逻辑
//	 * @param userId
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Throwable
//	 */
//	public Object loginSucc(String userId, HttpServletRequest request,
//		HttpServletResponse response,HttpSession session) throws Throwable
//	{
//
//		Enumeration en = request.getHeaderNames();
//		while(en.hasMoreElements()){
//			String name = (String)en.nextElement();
//			System.out.println(String.format("name:%s, value:%s",name,request.getHeader(name) ));
//		}
//
//		System.out.println("attribute===================");
//		Enumeration enarr = request.getSession().getAttributeNames();
//		while(enarr.hasMoreElements()){
//			String name = (String)enarr.nextElement();
//			System.out.println(String.format("%s=%s",name,
//				request.getSession().getAttribute(name) ));
//		}
//		//获取登录方式，将此登录方式直接写入执法监督的LoginType字段即可
//		String austyle = (String)request.getSession().getAttribute("austyle");
//		System.out.println(">>>>>>>>>>>>>>> loginType="+austyle);
//
//		request.getSession().setAttribute("userId", userId);
//		System.out.println(userId+":登录成功的逻辑");
//
//		System.out.println("[MySimpleLogin.loginSucc] "+userId+":登录成功的逻辑");
//
//		StpUtil.login(1);
//		String token = StpUtil.getTokenValue();
//		return ResponseEntity.ok(Message.success(token));
//	}
//	/**
//	 * 登出的逻辑
//	 * @param userId
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Throwable
//	 */
//	public Object logout(HttpServletRequest request,
//			HttpServletResponse response,HttpSession session) throws Throwable
//			{
//		      System.out.println("登出的逻辑");
//				StpUtil.logout(1);
//		      return null;
//			}
//
//
//	/**
//	 * 二次登录成功的逻辑
//	 * @param userId
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Throwable
//	 */
//	public Object secondLoginSucc(String secondUserId, HttpServletRequest request,
//			HttpServletResponse response,HttpSession session) throws Throwable
//			{
//		         return null;
//			}
//
//
//	/**
//	 * 二次登出的逻辑
//	 * @param userId
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Throwable
//	 */
//	public Object secondLogout(HttpServletRequest request,
//			HttpServletResponse response,HttpSession session) throws Throwable{
//		 return null;
//	}
//}
