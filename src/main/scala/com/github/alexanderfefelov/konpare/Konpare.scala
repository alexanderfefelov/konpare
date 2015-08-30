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
import java.nio.charset.UnmappableCharacterException

object Konpare extends App {

  Conf.confParser.parse(args, Conf()) match {
    case Some(conf) =>
      processFile(conf)
    case None =>
  }

  private def processFile(conf: Conf) = {
    val f = conf.input
    var files = Seq[File]()
    (f.exists, f.isDirectory) match {
      case (true, true) =>
        files = f.listFiles.filter(_.isFile)
      case (true, false) =>
        files = List(f)
      case (false, _) =>
        println("The specified file or directory does not exist")
        System.exit(1)
    }

    files foreach { file =>
      println(s"---===[ $file ]===---")
      try {
        val model = collection.mutable.Map.empty[String, String]
        Parser.parse(file.getCanonicalPath, model)
        Analyzer.analyze(conf, model)
      } catch {
        case e: UnmappableCharacterException =>
          println("Binary file")
      }
    }
  }

}