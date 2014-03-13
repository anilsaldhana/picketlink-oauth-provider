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

package org.picketlink.oauth.provider.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.picketlink.Identity;

/**
 * <p>
 * Provides all authorization capabilities for applications using PicketBox.
 * </p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@ApplicationScoped
public class AuthorizationManager {

    /**
     * <p>
     * Authorization method for the {@link RolesAllowed} annotation.
     * </p>
     *
     * @param ctx
     * @param identity
     * @return
     */
    @Secures
    @RolesAllowed
    public boolean restrictRoles(InvocationContext ctx, Identity identity) {
        if (!identity.isLoggedIn()) {
            return false;
        }

        String[] restrictedRoles = getRestrictedRoles(ctx);

        for (String restrictedRole : restrictedRoles) {
            throw new RuntimeException("NYI");
            /*if (identity.hasRole(restrictedRole)) {
                return true;
            }*/
        }

        return false;
    }

    /**
     * <p>Checks if the resources protected with the {@link UserLoggedIn} annotation are visible only for authenticated users.</p>
     *
     * @param ctx
     * @param identity
     * @return
     */
    @Secures
    @UserLoggedIn
    public boolean isUserLoggedIn(InvocationContext ctx, Identity identity) {
        return identity.isLoggedIn();
    }

    /**
     * <p>
     * Returns the restricted roles defined by the use of the {@link RolesAllowed} annotation. If the annotation is not
     * present a empty array is returned.
     * </p>
     *
     * @param ctx
     * @return
     */
    private String[] getRestrictedRoles(InvocationContext ctx) {
        RolesAllowed restrictedRolesAnnotation = getDeclaredAnnotation(RolesAllowed.class, ctx);

        if (restrictedRolesAnnotation != null) {
            return restrictedRolesAnnotation.value();
        }

        return new String[] {};
    }

    /**
     * <p>
     * Returns the an {@link Annotation} instance giving its class. The annotation will be looked up on method and type levels,
     * only.
     * </p>
     *
     * @param annotationClass
     * @param ctx
     * @return
     */
    private <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass, InvocationContext ctx) {
        Method method = ctx.getMethod();
        Class<?> type = method.getDeclaringClass();

        if (method.isAnnotationPresent(annotationClass)) {
            return method.getAnnotation(annotationClass);
        }

        if (type.isAnnotationPresent(annotationClass)) {
            return type.getAnnotation(annotationClass);
        }

        return null;
    }
}