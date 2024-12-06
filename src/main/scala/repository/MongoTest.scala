package repository

import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import scala.concurrent.Await
import scala.concurrent.duration._
import repository.MongoConfig
object MongoTest {
  def testConnection(): Unit = {
    val db = MongoConfig.database

    println("Vérification des collections dans la base de données...")
    val collections = db.listCollectionNames().toFuture()

    try {
      val collectionList = Await.result(collections, 10.seconds)
      println(s"Collections trouvées : ${collectionList.mkString(", ")}")
      println("Connexion réussie !")
    } catch {
      case ex: Exception =>
        println("Échec de la connexion à MongoDB.")
        println(s"Erreur : ${ex.getMessage}")
    } finally {
      MongoConfig.close()
    }
  }
}
