package com.sc.svca.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sc.cmn.util.ResponseModel;
import com.sc.svca.client.FeignServiceBClient;
import com.sc.svca.service.RibbonServiceBClient;
import com.sc.svca.service.RibbonUserServiceClient;

/**
 * curl -X POST -vu client:secret http://192.168.28.8:8001/oauth/token -H "Accept: application/json" -d "password=password&username=admin&grant_type=password&scope=read%20write"
 * 
 * @author allan.chow@139.com
 *
 */
//Spring Cloud Consul Config support dynamic change, add @RefreshScope
@RefreshScope
@RestController
public class ServiceAController {

	private static final Logger log = LoggerFactory.getLogger(ServiceAController.class);

	//from application.yml config
	@Value("${msg:unknown}")
	private String msg;
	
	//from consul config
	@Value("${my.consul.val}")
    String myConsulVal;

	//@Autowired
	//EurekaDiscoveryClient discoveryClient;
	//replace eureka with consul
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private FeignServiceBClient serviceBClient;

	@Autowired
	private RibbonUserServiceClient userService;

	@GetMapping(value = "/")
	public String printServiceA() {

		log.info(discoveryClient.getInstances("consul-service-a").toString());		

		ServiceInstance serviceInstance = discoveryClient.getInstances("consul-service-a").get(0);

		return serviceInstance.getServiceId() + " (" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + ")"
				+ "===>name:" + msg + "<br/>";
		
		//consul-service-a (192.168.1.63:8901)===>name:test_string<br/>

	}

	@Value("${server.port}")
	String port;

	@RequestMapping("/hi2")
	public String home(@RequestParam(value = "name", defaultValue = "defName") String name) {
		return "hi " + name + " ,i am from port:" + port + ", get consul config my.consul.val=" + myConsulVal;
		/**
		 * dynamic config change on Consul
		 * 
			[root@hadoopnode4 sts]#  curl -i -H "Authorization: Bearer 8cb5bd5b-86c7-4f06-991c-ccc358060bfc" http://192.168.28.8:8900/svca/hi2
			hi defName ,i am from port:8901, get consul config my.consul.val=myVal
			
			[updated on Consul console...]
			
			[root@hadoopnode4 sts]#  curl -i -H "Authorization: Bearer 8cb5bd5b-86c7-4f06-991c-ccc358060bfc" http://192.168.28.8:8900/svca/hi2
			hi defName ,i am from port:8901, get consul config my.consul.val=myVal_new
		 * 
		 */
	}

	/**
	 * way 1 - feign fallback
	 * 
	 * the name parameter must be provided when calling
	 * 
	 * [root@hadoopnode4 ~]# curl -i -H "Authorization: Bearer
	 * a8914a80-c6c0-46ae-9828-2bb7c8cb521a" http://192.168.1.63:8080/hi?name=abc
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/hi")
	public String sayHi(@RequestParam String name) {

		log.info("calling into svca-hi with name======>" + name);
		return serviceBClient.sayHiFromClientOne(name);
	}

	/**
	 * 
	 * [root@hadoopnode4 ~]# curl -i -H "Authorization: Bearer
	 * 1c76f4f5-cf95-428c-ae96-c45715c8f7ae" http://192.168.1.63:8901/test
	 * {"status":200,"msg":"æˆåŠŸ","data":{},"perm":null}
	 * 
	 * @return
	 */
	@GetMapping(value = "/test")
	public String test() {
		return serviceBClient.test();
	}

	@Autowired
	RibbonServiceBClient serviceBService;

	/**
	 * way 2 - ribbon fallback
	 * 
	 * [root@hadoopnode4 ~]# curl -X POST -i -H "Authorization: Bearer
	 * 1c76f4f5-cf95-428c-ae96-c45715c8f7ae" http://192.168.1.63:8901/hitest -d
	 * 'name=zhou&b=2&c=3'
	 * 
	 * {"status":200,"msg":"æˆåŠŸ","data":{"b":"2","c":"3","name":"zhou"}}[root@hadoopnode4
	 * ~]#
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hitest", method = RequestMethod.POST)
	public ResponseModel hiTest(HttpServletRequest request) {
		// request.getParameter("name") //hib?name=abc
		return serviceBService.hiService(request);
	}

	@RequestMapping(value = "/hib")
	public String hiB(HttpServletRequest request) {
		return serviceBService.hi(request);
	}

	// @GetMapping(value = "/getCurrUserId")
	// [root@hadoopnode4 ~]# curl -i -H "Authorization: Bearer
	// 1c76f4f5-cf95-428c-ae96-c45715c8f7ae" http://192.168.1.63:8901/getCurrUserId
	// [root@hadoopnode4 ~]# curl -X POST -i -H "Authorization: Bearer
	// 1c76f4f5-cf95-428c-ae96-c45715c8f7ae" http://192.168.1.63:8901/getCurrUserId -d
	// 'a=1&b=2&c=3'
	@RequestMapping(value = "/getCurrUserId")
	public String getCurrUserId(HttpServletRequest request) {
		return userService.getCurrUserId(request);
	}

	@GetMapping(path = "/current")
	public Principal getCurrentAccount(Principal principal) {
		return principal;
	}
	
	
	@Autowired
	private LoadBalancerClient loadBalancer;
 

	/**
	 * 获取所有服务
	 */
	@RequestMapping("/services")
	public Object services() {
		// 获取对应服务名称的所有实例信息
		return discoveryClient.getInstances("consul-service-b");
		//[{"serviceId":"consul-a","host":"192.168.1.63","port":8201,"secure":false,"metadata":{"secure":"false"},"uri":"http://192.168.1.63:8201","scheme":null}]
	}
 
	@Autowired
    RestTemplate restTemplate;
	
	/**
	 * Demo for invalid call, without token passed
	 * error":"invalid_token
	 * 
	 * @return
	 */
	@RequestMapping("/callB")
	public String call() {
		ServiceInstance serviceInstance = loadBalancer.choose("consul-service-b");
		log.info("serviceInstance.getUri：" + serviceInstance.getUri());
		log.info("serviceInstance.getServiceId：" + serviceInstance.getServiceId());

		String callServiceResult = restTemplate.getForObject(serviceInstance.getUri().toString() + "/hi",
				String.class);
		log.info("callServiceResult：" + callServiceResult);
		return callServiceResult;
	}
	
	

}