package main

import userinterface.UserInterface
import repository.MongoTest

object MainApp {

  def db_connection_test(): Unit = {
    println("Test de connexion à MongoDB...")
    MongoTest.testConnection()
  }

  def main(args: Array[String]): Unit = {
    UserInterface.start()
  }
}
