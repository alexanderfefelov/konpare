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

package com.github.alexanderfefelov.konpare.syntax.subject.config

import com.github.alexanderfefelov.konpare.Parser
import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject}

object DosPrevention extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    data.head match {
      case Syntax.PARAMETER_DOS_TYPE =>
        // config dos_prevention dos_type land_attack action drop state enable
        // config dos_prevention dos_type blat_attack action drop state enable
        // config dos_prevention dos_type tcp_null_scan action drop state enable
        // config dos_prevention dos_type tcp_xmasscan action drop state enable
        // config dos_prevention dos_type tcp_synfin action drop state enable
        // config dos_prevention dos_type tcp_syn_srcport_less_1024 action drop state enable
        // config dos_prevention dos_type ping_death_attack action drop state enable
        // config dos_prevention dos_type tcp_tiny_frag_attack action drop state enable
        Parser.addPairsToModel(data.drop(2), model, s"${Syntax.SUBJECT_DOS_PREVENTION}=${data(1)}")
      case _ =>
        // config dos_prevention trap enable
        // config dos_prevention log enable
        Parser.addPairsToModel(data, model, s"${Syntax.SUBJECT_DOS_PREVENTION}")
    }
  }

}