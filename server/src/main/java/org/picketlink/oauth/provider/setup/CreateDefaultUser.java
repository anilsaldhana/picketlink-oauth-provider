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
package org.picketlink.oauth.provider.setup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.SimpleRole;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;

/**
 * Create an Admin user
 * @author anil saldhana
 * @since Mar 12, 2013
 */
@Singleton
@Startup
public class CreateDefaultUser {
    @Inject IdentityManager identityManager;

    @PostConstruct
    public void create() {
        User admin = new SimpleUser("admin");
        admin.setEmail("admin@acme.com");

        this.identityManager.add(admin);
        this.identityManager.updateCredential(admin, new Password("secret"));

        Role roleAdmin = new SimpleRole("administrator");
        this.identityManager.add(roleAdmin);

        identityManager.grantRole(admin, roleAdmin);
    }
}