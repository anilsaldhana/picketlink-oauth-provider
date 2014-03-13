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
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

/**
 * Create an Admin user
 * @author anil saldhana
 * @since Mar 12, 2013
 */
@Singleton
@Startup
public class CreateDefaultUser {
    @Inject PartitionManager partitionManager;

    @PostConstruct
    public void create() {
        String password = SecurityActions.getSystemProperty("picketlink.oauth.admin");
        if(password == null){
            throw new RuntimeException("picketlink.oauth.admin system property not setup");
        }
        User admin = new User("admin");
        admin.setEmail("admin@acme.com");

        IdentityManager identityManager = partitionManager.createIdentityManager();

        identityManager.add(admin);
        identityManager.updateCredential(admin, new Password(password));

        Role roleAdmin = new Role("administrator");
        identityManager.add(roleAdmin);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();
        BasicModel.grantRole(relationshipManager,admin,roleAdmin);
        //identityManager.grantRole(admin, roleAdmin);
    }
}
