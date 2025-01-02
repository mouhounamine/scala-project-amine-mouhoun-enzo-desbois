package repositories

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import repository.MongoConfig
import scala.jdk.CollectionConverters._

@Singleton
class RunwayRepository @Inject()(implicit ec: ExecutionContext) {

  private val db = MongoConfig.database

  private val runwaysCollection: MongoCollection[Document] = db.getCollection("runways")

  def getRunwayTypesByCountry: Future[Map[String, Set[String]]] = {
    runwaysCollection.aggregate(
      Seq(
        group("$iso_country", addToSet("runwayTypes", "$surface")),
        project(fields(computed("country", "$_id"), include("runwayTypes"), excludeId()))
      )
    ).toFuture()
      .map { documents =>
        documents.map { doc =>
          val country = doc.getString("country")
          val runwayTypes = doc.getList("runwayTypes", classOf[String]).asScala.toSet
          country -> runwayTypes
        }.toMap
      }
  }
}
