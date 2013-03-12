/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.picketlink.oauth.provider.rest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 2637023097272776078L;

    private String userId;
    private String[] roles;

    private String fullName;
    
    private String serial;
    
    private String b32;

    public String getB32() {
        return b32;
    }

    public void setB32(String b32) {
        this.b32 = b32;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the roles
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getFullName() {
        return fullName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}