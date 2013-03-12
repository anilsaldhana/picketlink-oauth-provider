/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.picketlink.oauth.provider.rest;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.oauth.provider.model.AuthenticationRequest;
import org.picketlink.oauth.provider.model.AuthenticationResponse;

/**
 * <p>JAX-RS Endpoint to authenticate users.</p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@Stateless
@Path("/signin")
@TransactionAttribute
public class SignInEndpoint {
    
    @Inject
    //private PicketBoxIdentity identity;
    private Identity identity;
    
    @Inject
    private DefaultLoginCredentials credential;
    
    @Context private HttpServletRequest httpServletRequest;
    
    /**
     * <p>Performs the authentication using the informations provided by the {@link AuthenticationRequest}</p>
     * 
     * @param authcRequest
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse login(final AuthenticationRequest authcRequest) {
        if (this.identity.isLoggedIn()) {
            return createResponse(authcRequest);
        }
        
        this.credential.setUserId(authcRequest.getUserId());
        this.credential.setCredential(new Password(authcRequest.getPassword().toCharArray()));
 
        this.identity.login();

        return createResponse(authcRequest);
    }

    private AuthenticationResponse createResponse(AuthenticationRequest authcRequest) {
        AuthenticationResponse response = new AuthenticationResponse();
        
        response.setUserId(authcRequest.getUserId());
        response.setLoggedIn(this.identity.isLoggedIn());
        
        if (response.isLoggedIn()) {
            //response.setToken(this.identity.getUserContext().getSession().getId().getId().toString());
            response.setToken(httpServletRequest.getSession().getId());
        }
        
        return response;
    }   
}