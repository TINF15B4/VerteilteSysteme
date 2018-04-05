package de.tinf15b4.quizduell;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RestInterface {
	private WebResource service;
	
	public RestInterface(String restServiceUrl) {
		DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
		Client client = Client.create(defaultClientConfig);
		service = client.resource(UriBuilder.fromUri(restServiceUrl).build());
	}
	
	public void postAnswer(String answer) {
		
	}
	
	public void postReady() {
		
	}
	
	public String getQuestion() {
		return null;
	}
	
	public int getPoints() {
		return 0;
	}

}
