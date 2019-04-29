package com.sc.svca.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sc.cmn.config.RestTemplateClient;
import com.sc.cmn.util.ResponseModel;

/**
 * RestTemplate(Oauth2 supported)+Ribbon 
 **/
@Service
public class RibbonServiceBClient extends RestTemplateClient {
 
	// @Autowired
	// private LoadBalancerClient loadBalancerClient;

	@HystrixCommand(fallbackMethod = "hiError")
	public ResponseModel hiService(HttpServletRequest request) {		 
		ResponseModel res = postRestObject("consul-service-b", "/test", request, ResponseModel.class);
		return res;
	}

	public ResponseModel hiError(HttpServletRequest request, Throwable e) {
		ResponseModel responseModel = new ResponseModel(200,("hi,sorry,service-b failed!" + e.toString()),null);
		return responseModel;
		// hi,def,sorry,service-b failed!
	}
	
	@HystrixCommand(fallbackMethod = "hi")
	public String hi(HttpServletRequest request) {
	
		String res = getRestObject("consul-service-b", "/hi", getHeaders(request), getNamedParamMap(request, new String[] {"name"}), String.class);
		return res;
	}
	
	public String hi(HttpServletRequest request, Throwable e) {
		String res =  "hi," + request.getParameter("name") + ", sorry, service-b failed!" + e.toString();
		return res;
	}
	
	
}