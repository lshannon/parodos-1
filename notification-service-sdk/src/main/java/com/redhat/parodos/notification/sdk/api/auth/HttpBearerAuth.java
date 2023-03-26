/*
 * Parodos Notification Service API
 * This is the API documentation for the Parodos Notification Service. It provides operations to send out and check notification. The endpoints are secured with oAuth2/OpenID and cannot be accessed without a valid token.
 *
 * The version of the OpenAPI document: v1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.redhat.parodos.notification.sdk.api.auth;

import com.redhat.parodos.notification.sdk.api.ApiException;
import com.redhat.parodos.notification.sdk.api.Pair;

import java.net.URI;
import java.util.Map;
import java.util.List;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class HttpBearerAuth implements Authentication {

	private final String scheme;

	private String bearerToken;

	public HttpBearerAuth(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * Gets the token, which together with the scheme, will be sent as the value of the
	 * Authorization header.
	 * @return The bearer token
	 */
	public String getBearerToken() {
		return bearerToken;
	}

	/**
	 * Sets the token, which together with the scheme, will be sent as the value of the
	 * Authorization header.
	 * @param bearerToken The bearer token to send in the Authorization header
	 */
	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	@Override
	public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams,
			Map<String, String> cookieParams, String payload, String method, URI uri) throws ApiException {
		if (bearerToken == null) {
			return;
		}

		headerParams.put("Authorization", (scheme != null ? upperCaseBearer(scheme) + " " : "") + bearerToken);
	}

	private static String upperCaseBearer(String scheme) {
		return ("bearer".equalsIgnoreCase(scheme)) ? "Bearer" : scheme;
	}

}
