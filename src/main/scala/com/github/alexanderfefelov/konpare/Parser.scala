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

package com.github.alexanderfefelov.konpare

import java.io.{PrintWriter, StringWriter}

import com.github.alexanderfefelov.konpare.syntax.Syntax

object Parser {

  def parse(path: String, model: collection.mutable.Map[String, String]) = {
    io.Source.fromFile(path).getLines().toList foreach { line =>
      try {
        processLine(line, model)
      } catch {
        case e: Exception =>
          println(s"Exception '$e' thrown while processing '$line'")
      }
    }
    model
  }

  private def processLine(line: String, model: collection.mutable.Map[String, String]) = {
    line.trim match {
      case l if l.startsWith(Syntax.COMMENT_START) =>
        processComment(l, model)
      case l if l.nonEmpty =>
        processNonEmptyLine(l, model)
      case _ =>
        // Empty line
    }
  }

  private def processComment(line: String, model: collection.mutable.Map[String, String]) = {
    line match {
      case Syntax.RE_MODEL(g) => if (!model.contains(Syntax.MODEL)) model += Syntax.MODEL -> g
      case Syntax.RE_FW(g) => model += Syntax.FW -> g
      case _ =>
    }
  }

  private def processNonEmptyLine(line: String, model: collection.mutable.Map[String, String]) = {
    val r = """('.*?'|".*?"|\S+)""".r // http://stackoverflow.com/a/366229
    val data = r.findAllIn(line.replaceAll(" +", " ")).toList.map(_.replaceAll("\"", ""))
    Syntax.predicates.get(data.head) match {
      case Some(predicate) =>
        predicate.process(data.tail, model)
      case _ =>
        // Unknown predicate
    }
  }

  def addPairsToModel(data: List[String], model: collection.mutable.Map[String, String], prefix: String) = {
    data.grouped(2).foreach((pair: List[String]) => model += s"$prefix=${pair.head}" -> pair(1))
  }

}