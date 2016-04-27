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

package fr.sii.dto.mapping;

import org.springframework.stereotype.Component;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkUser;
import fr.sii.dto.user.UserProfil;
import fr.sii.dto.user.CospeakerProfil;
import fr.sii.entity.Comment;
import fr.sii.entity.Talk;
import fr.sii.entity.User;
import ma.glasnost.orika.MapperFactory;

@Component
public class CommentMapping implements Mapping {

    @Override
    public void mapClasses(MapperFactory mapperFactory) {
        mapperFactory.classMap(Comment.class, CommentUser.class)
            .byDefault()
            .register();

        mapperFactory.classMap(User.class, UserProfil.class)
            .byDefault()
            .register();


    }
}
