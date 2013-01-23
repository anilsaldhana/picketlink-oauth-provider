/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

import org.picketlink.extensions.core.pbox.PicketBoxIdentity;
import org.picketlink.extensions.core.pbox.authorization.UserLoggedIn;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Agent;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.SimpleAgent;
import org.picketlink.idm.model.User;
import org.picketlink.oauth.provider.model.ApplicationRegistrationRequest;
import org.picketlink.oauth.provider.model.ApplicationRegistrationResponse;


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
    private PicketBoxIdentity identity;

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