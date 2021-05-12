package scala2021.ashinkarev.task07

import scala.util.{Try,Success,Failure}

trait Connection {
  def close();

  def run();
}

abstract class ConnectionBase(port: Int) extends Connection {
  def close() = {
    println("Closed")
  }
  def run() = {
    println("Run")
  }
}

class PrintConnection(port: Int) extends ConnectionBase(port) {
}

object Utils {
  def withConnection[TResult](port: Int)(body: (Connection) => TResult) = {
    withResource(() => new PrintConnection(port))(body)((connection) => connection.close())
  }

  def withResource[TResource, TResult]
    (resourceFactory: () => TResource)
    (body: (TResource) => TResult)
    (cleanup: (TResource) => Unit = (resource: TResource) => {}): TResult = {
    var resource: TResource = null.asInstanceOf[TResource]
    var result: TResult = null.asInstanceOf[TResult]
    
    Try {
      resource = resourceFactory();

      result = body(resource);
    } match {
      case Success(value) => {
        println("creation and body passed fine")
        Try {
          cleanup(resource)
        } match {
          case Success(value) => {
            println("happy path cleanup successfully passed")
            result
          };
          case Failure(cleanupException) => {
            println("exception at cleanup " + cleanupException.getMessage());
            throw cleanupException;
          }
        }
      }
      case Failure(originalException) => {
        println("exception occured at creation or in body " + originalException)

        Try {
          cleanup(resource)
        } match {
          case Success(value) => {
            println("creation or body error cleanup successfully passed")
            throw originalException;
          }
          case Failure(cleanupException) => {
            println("exception at cleanup after original exception " + cleanupException.getMessage())
            throw originalException;
          };
        }
      }
    }
  }
}
