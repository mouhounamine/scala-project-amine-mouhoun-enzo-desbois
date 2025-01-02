package parser

import scala.io.Source
import scala.util.Using

object CSVParser {

  /** Parse un fichier CSV et retourne une liste d'instances du type T (Country,
    * Airport ou Runway).
    *
    * @param filePath
    *   Le chemin vers le fichier CSV
    * @param fromCSV
    *   Une fonction de conversion d'une ligne CSV en Option[T]
    * @tparam T
    *   Le type de l'objet à parser
    * @return
    *   Une liste d'instances de type T
    */
  def parseFile[T](filePath: String, fromCSV: String => Option[T]): List[T] = {
    Using(Source.fromFile(filePath)) { source =>
      source.getLines().drop(1).flatMap(fromCSV).toList
    }.getOrElse(List())
  }
}
