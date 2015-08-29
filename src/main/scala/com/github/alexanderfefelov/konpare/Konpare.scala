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
import java.net.InetAddress
import java.nio.charset.UnmappableCharacterException

import scopt.OptionParser
import version._

object Konpare extends App {

  var confParser = new OptionParser[Conf]("java -jar konpare.jar") {
    head(s"${BuildInfo.name} v. ${BuildInfo.version}",
      """
        |
        |Copyright (C) 2015 Alexander Fefelov <https://github.com/alexanderfefelov>
        |This program comes with ABSOLUTELY NO WARRANTY; see LICENSE file for details.
        |This is free software, and you are welcome to redistribute it under certain conditions; see LICENSE file for details.
      """.stripMargin)
    opt[File]('i', "input") required() valueName "<PATH>" text "file or directory to be processed" action { (x, c) =>
      c.copy(input = x) }
    opt[Seq[InetAddress]]('l', "syslog-servers") valueName "<IP-ADDRESSES>" text "comma-delimited IP addresses of syslog servers" action { (x, c) =>
      c.copy(syslogServers = x) }
    opt[Seq[InetAddress]]('t', "sntp-servers") valueName "<IP-ADDRESSES>" text "comma-delimited IP addresses of SNTP servers" action { (x, c) =>
      c.copy(sntpServers = x) }
    help("help") text "prints this usage text"
    checkConfig { conf => success }
  }

  confParser.parse(args, Conf()) match {
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