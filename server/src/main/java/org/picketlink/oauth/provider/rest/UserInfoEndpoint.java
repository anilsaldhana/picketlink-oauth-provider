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
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.picketlink.Identity;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.User;

/**
 * <p>JAX-RS Endpoint to authenticate users.</p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@Stateless
@Path("/userinfo")
public class UserInfoEndpoint {

    @Inject
    private Identity identity;

    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public UserInfo getInfo() {
        UserInfo userInfo = new UserInfo();

        if (identity.isLoggedIn()) {
            Account account = identity.getAccount();

            User user = (User) account;
            //User user = this.identity.getUser();
            userInfo.setUserId(user.getLoginName());
            userInfo.setFullName(user.getFirstName() + " " + user.getLastName());

           /* UserContext userContext = this.identity.getUserContext();

            Collection<Role> roles = userContext.getRoles();
            String[] rolesArray = new String[roles.size()];

            int i = 0;

            for (Role role : roles) {
                rolesArray[i] = role.getName();
                i++;
            }

            userInfo.setRoles(rolesArray);*/
        }

        return userInfo;
    }

}