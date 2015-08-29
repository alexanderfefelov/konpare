/*
 * konpare
 * Copyright (C) 2015 Alexander Fefelov <https://github.com/alexanderfefelov>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.alexanderfefelov.konpare.syntax.predicate

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Predicate, Subject}
import com.github.alexanderfefelov.konpare.syntax.subject.disable.{GenericDisable, GenericDisable2}

object Disable extends Predicate {

  override val subjects = Map[String, Subject](
    Syntax.SUBJECT_ADDRESS_BINDING -> new GenericDisable2(Syntax.SUBJECT_ADDRESS_BINDING),
    Syntax.SUBJECT_COMMAND -> new GenericDisable2(Syntax.SUBJECT_COMMAND),
    Syntax.SUBJECT_DHCP_LOCAL_RELAY -> new GenericDisable(Syntax.SUBJECT_DHCP_LOCAL_RELAY),
    Syntax.SUBJECT_DHCP_RELAY -> new GenericDisable(Syntax.SUBJECT_DHCP_RELAY),
    Syntax.SUBJECT_GVRP -> new GenericDisable(Syntax.SUBJECT_GVRP),
    Syntax.SUBJECT_JUMBO_FRAME -> new GenericDisable(Syntax.SUBJECT_JUMBO_FRAME),
    Syntax.SUBJECT_LLDP -> new GenericDisable(Syntax.SUBJECT_LLDP),
    Syntax.SUBJECT_LOOPDETECT -> new GenericDisable(Syntax.SUBJECT_LOOPDETECT),
    Syntax.SUBJECT_PASSWORD -> new GenericDisable2(Syntax.SUBJECT_PASSWORD),
    Syntax.SUBJECT_PASSWORD_RECOVERY -> new GenericDisable(Syntax.SUBJECT_PASSWORD_RECOVERY),
    Syntax.SUBJECT_PORT_SECURITY -> new GenericDisable2(Syntax.SUBJECT_PORT_SECURITY),
    Syntax.SUBJECT_QINQ -> new GenericDisable(Syntax.SUBJECT_QINQ),
    Syntax.SUBJECT_SFLOW -> new GenericDisable(Syntax.SUBJECT_SFLOW),
    Syntax.SUBJECT_SNTP -> new GenericDisable(Syntax.SUBJECT_SNTP),
    Syntax.SUBJECT_SSL -> new GenericDisable(Syntax.SUBJECT_SSL),
    Syntax.SUBJECT_SYSLOG -> new GenericDisable(Syntax.SUBJECT_SYSLOG),
    Syntax.SUBJECT_VLAN_TRUNK -> new GenericDisable(Syntax.SUBJECT_VLAN_TRUNK)
  )

}