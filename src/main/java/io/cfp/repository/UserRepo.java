/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.repository;

import io.cfp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("select u.email from User u, Role r where r.name = :role and r.event.id = :eventId and r.user.id = u.id")
    List<String> findEmailByRole(@Param("role") String role, @Param("eventId") String eventId);


    @Query("select u from User u where u.id in (select distinct s.userId from Submission s where s.event.id = :eventId and s.state = 'ACCEPTED')")
    List<User> findUserWithAcceptedProposal(@Param("eventId") String eventId);

}
