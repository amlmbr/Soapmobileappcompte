package com.ensa.soap.config;

import com.ensa.soap.ws.CompteSoapService;
import lombok.AllArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CxfConfig {
	private CompteSoapService compteSoapService;
	private Bus bus;
	@Bean
	public EndpointImpl endpoint() {
		EndpointImpl endpoint=new EndpointImpl(bus,compteSoapService);
		endpoint.publish("/ws");
		return endpoint;
	}
	
	

}
