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
package org.picketlink.oauth.provider.services;

import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Agent;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.SimpleAgent;
import org.picketlink.idm.model.User;
import org.picketlink.oauth.provider.model.ApplicationRegistrationRequest;
import org.picketlink.oauth.provider.model.ApplicationRegistrationResponse;
import org.picketlink.oauth.provider.security.UserLoggedIn;


/**
 * Endpoint for registering OAuth Applications
 * @author anil saldhana
 * @since Jan 9, 2013
 */
@Stateless
@Path("/appregister")
@TransactionAttribute
public class ApplicationRegistrationEndpoint {
    
    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UserLoggedIn
    public ApplicationRegistrationResponse register(ApplicationRegistrationRequest request){
        ApplicationRegistrationResponse response = new ApplicationRegistrationResponse();

        String appName = request.getAppName().trim();
        String appURL = request.getAppURL();

        User user = identity.getUser();

        if(existsAgent(appName)){
            response.setRegistered(false);
            response.setErrorMessage("Application Already Registered");
        } else {
            Agent oauthApp = createAgent(appName); 
            oauthApp.setAttribute( new Attribute<String>("appURL", appURL) );
            oauthApp.setAttribute( new Attribute<String>("owner", user.getId()) );
            
            oauthApp.setAttribute( new Attribute<String>("clientID", getUID()) );
            oauthApp.setAttribute( new Attribute<String>("clientSecret", getUID()) );
            identityManager.update(oauthApp);
            response.setRegistered(true);
        } 

        return response;
    }

    private boolean existsAgent(String id){
        Agent existingAgent = identityManager.getAgent(id);
        return existingAgent != null;
    }

    private Agent createAgent(String id) {
        Agent agent = new SimpleAgent(id);
        identityManager.add(agent);
        return identityManager.getAgent(id);
    }
    
    private String getUID(){
        return UUID.randomUUID().toString();
    }
}