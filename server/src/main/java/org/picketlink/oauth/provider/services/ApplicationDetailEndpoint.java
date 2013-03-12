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