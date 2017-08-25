/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenergic.theskeleton.privilege.role;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.codenergic.theskeleton.privilege.PrivilegeEntity;
import org.codenergic.theskeleton.privilege.PrivilegeRepository;
import org.codenergic.theskeleton.privilege.role.impl.RoleServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
	static RoleService newInstance(RoleRepository roleRepository, PrivilegeRepository privilegeRepository,
			RolePrivilegeRepository rolePrivilegeRepository) {
		return new RoleServiceImpl(roleRepository, privilegeRepository, rolePrivilegeRepository);
	}

	void deleteRole(@NotNull String idOrCode);

	RoleEntity findRoleByCode(@NotNull String code);

	RoleEntity findRoleById(@NotNull String id);

	RoleEntity findRoleByIdOrCode(@NotNull String idOrCode);

	Page<RoleEntity> findRoles(Pageable pageable);

	Page<RoleEntity> findRoles(String keyword, Pageable pageable);

	RoleEntity saveRole(@NotNull @Valid RoleEntity role);

	RoleEntity updateRole(@NotNull String id, @NotNull @Valid RoleEntity role);

	RoleEntity addPrivilegeToRole(@NotNull String code, @NotNull String privilegeName);

	RoleEntity removePrivilegeFromRole(@NotNull String code, @NotNull String privilegeName);

	Set<PrivilegeEntity> findPrivilegesByRoleCode(@NotNull String code);

	Set<RoleEntity> findRolesByPrivilegeName(@NotNull String name);
}