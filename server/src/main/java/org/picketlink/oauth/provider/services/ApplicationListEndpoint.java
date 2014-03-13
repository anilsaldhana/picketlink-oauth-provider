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

import java.util.ArrayList;
import java.util.List;

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

import org.jboss.logging.Logger;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.AttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.Agent;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.oauth.provider.model.ApplicationDetailResponse;
import org.picketlink.oauth.provider.model.ApplicationListRequest;
import org.picketlink.oauth.provider.model.ApplicationListResponse;
import org.picketlink.oauth.provider.security.UserLoggedIn;

/**
 * Endpoint for list of Oauth Applications registered
 *
 * @author anil saldhana
 * @since Jan 14, 2013
 */
@Stateless
@Path("/applist")
@TransactionAttribute
public class ApplicationListEndpoint {

    @Inject
    private Logger log;

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UserLoggedIn
    public ApplicationListResponse register(ApplicationListRequest listRequest) {
        ApplicationListResponse response = new ApplicationListResponse();
        String userID = listRequest.getUserId();

        User user = (User) identity.getAccount();
        String identityID = user.getLoginName();

        //User user = identity.getUser();
        //String identityID = user.getId();
        if (identityID.equals(userID) == false) {
            log.error("WRONG USER::" + userID + " :: Needed :" + identityID);
        }

        IdentityQuery<Agent> query = identityManager.createIdentityQuery(Agent.class);

        query.setParameter(IdentityType.QUERY_ATTRIBUTE.byName("owner"), new String[] { identityID });

        List<Agent> result = query.getResultList();
        ArrayList<ApplicationDetailResponse> apps = new ArrayList<ApplicationDetailResponse>();

        for (Agent agent : result) {
            ApplicationDetailResponse app = new ApplicationDetailResponse();
            app.setName(agent.getLoginName());
            Attribute<String> appURLAttribute = agent.getAttribute("appURL");
            if (appURLAttribute != null) {
                app.setUrl(appURLAttribute.getValue());
            }
            apps.add(app);
        }

        ApplicationDetailResponse[] arr = new ApplicationDetailResponse[apps.size()];

        apps.toArray(arr);

        response.setToken(identityID);
        //response.setToken(this.identity.getUserContext().getSession().getId().getId().toString());
        response.setApplications(arr);

        return response;
    }
}