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

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Agent;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.oauth.provider.model.ApplicationDetailResponse;
import org.picketlink.oauth.provider.security.UserLoggedIn;

/**
 * Endpoint for list of Oauth Applications registered
 * 
 * @author anil saldhana
 * @since Jan 14, 2013
 */
@Stateless
@Path("/appdetail")
@TransactionAttribute
public class ApplicationDetailEndpoint {

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;
     
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UserLoggedIn
    public ApplicationDetailResponse register(@QueryParam("app") String appid) {
        ApplicationDetailResponse response = new ApplicationDetailResponse();

        User user = identity.getUser();
        String identityID = user.getId();

        IdentityQuery<Agent> query = identityManager.createIdentityQuery(Agent.class);

        query.setParameter(Agent.LOGIN_NAME, appid);
        query.setParameter(IdentityType.ATTRIBUTE.byName("owner"), new String[] { identityID });

        List<Agent> result = query.getResultList();

        if (result.size() > 0) {
            Agent agent = result.get(0);
            response.setName(agent.getLoginName());
            Attribute<String> appURLAttribute = agent.getAttribute("appURL");
            if (appURLAttribute != null) {
                response.setUrl(appURLAttribute.getValue());
            }

            Attribute<String> clientIDAtt = agent.getAttribute("clientID");
            if (clientIDAtt != null) {
                response.setClientID(clientIDAtt.getValue());
            }

            Attribute<String> clientSecretAtt = agent.getAttribute("clientSecret");
            if (clientSecretAtt != null) {
                response.setClientSecret(clientSecretAtt.getValue());
            }
        }

        response.setToken(user.getId());
        //response.setToken(this.identity.getUserContext().getSession().getId().getId().toString());

        return response;
    }
}