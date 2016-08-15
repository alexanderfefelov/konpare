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

import java.io.File

object Out {

  def info(message: String)(implicit conf: Conf, file: File) {
    print("INFO", message)
  }

  def info(message: String, list: List[String])(implicit conf: Conf, file: File) {
    print("INFO", message, list)
  }

  def warning(message: String)(implicit conf: Conf, file: File) {
    print("WARNING", message)
  }

  def warning(message: String, list: List[String])(implicit conf: Conf, file: File) {
    print("WARNING", message, list)
  }

  def error(message: String, list: List[String])(implicit conf: Conf, file: File) {
    print("ERROR", message, list)
  }

  private def print(prefix: String, message: String)(implicit conf: Conf, file: File) {
    println(s"${if (conf.grepFriendlyOutput) file + " "}$prefix $message")
  }

  private def print(prefix: String, message: String, list: List[String])(implicit conf: Conf, file: File) {
    if (list.nonEmpty) {
      println(s"${if (conf.grepFriendlyOutput) file + " "}$prefix $message: ${sortLexicographically(list).mkString(conf.outputListSeparator)}")
    }
  }

  private def sortLexicographically(list: List[String]) = {
    list.sortWith(_.reverse.padTo(20, " ").reverse.toString() < _.reverse.padTo(20, " ").reverse.toString())
  }

}
