package repository

import com.typesafe.config.ConfigFactory
import org.mongodb.scala._
import org.mongodb.scala.model._
import scala.concurrent.Await
import scala.concurrent.duration._
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion

object MongoConfig {

  private val config = ConfigFactory.load()

  private val mongoUsername = config.getString("mongo.username")
  private val mongoPassword = config.getString("mongo.password")
  private val mongoDb = config.getString("mongo.db")

  val connectionString =
    s"mongodb+srv://${mongoUsername}:${mongoPassword}@airport-cluster.i7a79.mongodb.net/${mongoDb}?retryWrites=true&w=majority&appName=airport-cluster"

  val serverApi = ServerApi.builder.version(ServerApiVersion.V1).build()

  val settings = MongoClientSettings
    .builder()
    .applyConnectionString(new ConnectionString(connectionString))
    .serverApi(serverApi)
    .build()

  private val client: MongoClient = MongoClient(settings)

  val database: MongoDatabase = client.getDatabase(mongoDb)

  def testConnection(): Unit = {
    try {
      val databasePing = client.getDatabase("admin")
      val ping = databasePing.runCommand(Document("ping" -> 1)).head()
      Await.result(ping, 10.seconds)
      println("Pinged the mongo deployment. Successfully connected to MongoDB!")
    } catch {
      case ex: Exception =>
        println("Erreur de connexion à MongoDB.")
        println(s"Erreur : ${ex.getMessage}")
    }
  }

  def close(): Unit = {
    client.close()
  }
}
