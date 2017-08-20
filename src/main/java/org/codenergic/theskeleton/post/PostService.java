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
package org.codenergic.theskeleton.post;

import org.codenergic.theskeleton.post.impl.PostServiceImpl;
import org.codenergic.theskeleton.role.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PostService {
	static PostService newInstance(PostRepository postRepository) {
		return new PostServiceImpl(postRepository);
	}

	PostEntity savePost(@NotNull @Valid PostEntity post);

	PostEntity updatePost(@NotNull String id, @NotNull @Valid PostEntity post);

	void deletePost(@NotNull String id);

	Page<PostEntity> findPostByTitleContaining(@NotNull String title, Pageable pageable);
}