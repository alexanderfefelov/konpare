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

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Predicate}
import com.github.alexanderfefelov.konpare.syntax.subject.enable._

object Enable extends Predicate {

  override val subjects = Map(
    Syntax.SUBJECT_ADDRESS_BINDING -> AddressBinding,
    Syntax.SUBJECT_CLIPAGING -> new GenericEnable(Syntax.SUBJECT_CLIPAGING),
    Syntax.SUBJECT_DHCP_RELAY -> new GenericEnable(Syntax.SUBJECT_DHCP_RELAY),
    Syntax.SUBJECT_LLDP -> new GenericEnable(Syntax.SUBJECT_LLDP),
    Syntax.SUBJECT_LOOPDETECT -> new GenericEnable(Syntax.SUBJECT_LOOPDETECT),
    Syntax.SUBJECT_MIRROR -> new GenericEnable(Syntax.SUBJECT_MIRROR),
    Syntax.SUBJECT_PASSWORD -> new GenericEnable2(Syntax.SUBJECT_PASSWORD),
    Syntax.SUBJECT_PASSWORD_RECOVERY -> new GenericEnable(Syntax.SUBJECT_PASSWORD_RECOVERY),
    Syntax.SUBJECT_PORT_SECURITY -> PortSecurity,
    Syntax.SUBJECT_SNTP -> new GenericEnable(Syntax.SUBJECT_SNTP),
    Syntax.SUBJECT_SYSLOG -> new GenericEnable(Syntax.SUBJECT_SYSLOG),
    Syntax.SUBJECT_TELNET -> Telnet,
    Syntax.SUBJECT_WEB -> Web
  )

}