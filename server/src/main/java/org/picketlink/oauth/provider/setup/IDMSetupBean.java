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
package org.picketlink.oauth.provider.setup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.picketbox.core.PicketBoxManager;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.PlainTextPassword;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;

/**
 * Set up the IdentityManager
 * @author anil saldhana
 * @since Jan 8, 2013
 */
@Singleton
@Startup
public class IDMSetupBean { 
    private Logger log = Logger.getLogger(IDMSetupBean.class);
    
    @Inject 
    PicketBoxManager picketboxManager;
    
    @PostConstruct
    public void initialize(){
        log.info("INITIAL");
        if(picketboxManager == null){
            throw new RuntimeException("PicketBox Manager has not been injected");
        }
        IdentityManager identityManager = picketboxManager.getIdentityManager();
        
        if(identityManager == null){
            throw new RuntimeException("Identity Manager has not been injected");
        }
        User admin = identityManager.getUser("admin");
        if(admin == null){
            //Instantiate an admin user
            admin = new SimpleUser("admin");
            admin.setFirstName("OAuth Provider");
            admin.setLastName("Admin");

            identityManager.add(admin);
            
            identityManager.updateCredential(admin, new PlainTextPassword("admin123".toCharArray()));

            System.out.println("ADMIN user INJECTED!!!");
        } else{
            System.out.println("ADMIN user EXISTS!!!!");
        }
    }
}