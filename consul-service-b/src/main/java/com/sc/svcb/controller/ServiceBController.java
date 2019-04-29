package com.sc.svcb.controller;

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sc.cmn.util.ResponseModel;


/**
我们可能只需要定义一个默认的回调处理函数即可，
那么我们就可以使用@DefaultProperties注解来定义默认的回调函数，
这样就不需要每个 @HystrixCommand 注解都指定一个回调函数了
@DefaultProperties(defaultFallback = "defaultFallback")
 */
@RefreshScope
@RestController
public class ServiceBController {
	private static final Logger log = LoggerFactory.getLogger(ServiceBController.class);

	//@Autowired
	//EurekaDiscoveryClient discoveryClient;
	@Autowired
	private DiscoveryClient discoveryClient;

	@Value("${msg:unknown}")
	private String msg;

	@GetMapping(value = "/")
	public String printServiceB() {
		log.info(discoveryClient.getServices().toString());

		ServiceInstance serviceInstance = discoveryClient.getInstances("service-b").get(0);

		return serviceInstance.getServiceId() + " (" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + ")"
				+ "===>msg in config center:" + msg + "<br/>";

	}

	@Value("${server.port}")
	String port;

	@GetMapping("/hi")
	public String home(@RequestParam(value = "name", defaultValue = "defaultNameVal") String name) {
		log.info("======now in SVCB-hi=======");
		return "hi " + name + " ,i am svc-b, from port:" + port;
	}

	@GetMapping(path = "/current")
	public Principal getCurrentAccount(Principal principal) {
		return principal;
	}

	@RequestMapping(value = "/test")
	public ResponseModel testFeignClient(HttpServletRequest request) {
		ResponseModel responseModel = new ResponseModel(200, "成功", null);
		HashMap<String, Object> par = new HashMap<String, Object>();
		//	把request中的参数放到HashMap中

		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			if (parameterMap.get(key) != null && parameterMap.get(key).length == 1) {
				par.put(key, parameterMap.get(key)[0]);
				log.info(
						"=============request.getParameterMap KEY:" + key + "=======value:" + parameterMap.get(key)[0]);
			}
		}

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			log.info("============reqeust.getHeaderNames=key:" + key + "=============value:" + value);
		}

		log.info("=============par:" + par.toString());

		/**
		 *  传怎什么值过来 就返回怎么值 检测服务之间通信是否畅通 
		*/
		responseModel.setData(par);
		return responseModel;
	}
	
	/**
	 @HystrixCommand(commandProperties = {
     	@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
     	
     	@HystrixProperty(name = "circuitBreaker.enabled", value = "true"), // 开启熔断机制
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 设置当请求失败的数量达到10个后，打开断路器，默认值为20
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 设置打开断路器多久以后开始尝试恢复，默认为5s
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),  // 设置出错百分比阈值，当达到此阈值后，打开断路器，默认50%

     	
		})
	 */
	@RequestMapping("/timeout")
	@HystrixCommand
    public String timeout(){
        try{
            //睡5秒，网关Hystrix3秒超时
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "timeout";
    }
	
	
	@RequestMapping("/err1")
	@HystrixCommand
    public String exception(){
        int a = 4/0;
        return "exception";
    }
	
	@RequestMapping("/err2")
    public String exception2(){
        int a = 4/0;
        return "exception";
    }

}