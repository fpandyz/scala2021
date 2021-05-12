package scala2021.ashinkarev.task07

import scala.util.{Try,Success,Failure}

abstract class Connection(port: Int) {
  def close() = {
    println("Closed")
  }
  def run() = {
    println("Run")
  }
}

case class PrintConnection(port: Int) extends Connection(port) {
}

object Utils {
  def withConnection[TResult](port: Int)(body: (Connection) => TResult) = {
    withResource(() => PrintConnection(port))(body)((connection) => connection.close())
  }

  def withResource[TResource, TResult]
    (resourceFactory: () => TResource)
    (body: (TResource) => TResult)
    (cleanup: (TResource) => Unit = (resource: TResource) => {}): TResult = {
    var resource: TResource = null.asInstanceOf[TResource]
    var result: TResult = null.asInstanceOf[TResult]
    // try {
    //   resource = resourceFactory();

    //   body(resource);
    // } finally {
    //   println("Get to finally");
    //   cleanup(resource)
    // }

    // try {
    //   resource = resourceFactory();

    //   body(resource);
    // } 
    // catch {
    //     case _: Throwable => println("Got some other kind of Throwable exception"); throw
    // } finally {
    //     // your scala code here, such as closing a database connection
    //     // or file handle
    // }

    // val tryReslut = Try {
    //   resource = resourceFactory();

    //   result = body(resource);
    // } recoverWith {
    //   case originalException => {
    //     println("exception occured at creation or in body")

    //     Try {
    //       cleanup(resource)
    //     } match {
    //       case Success(value) => throw originalException;
    //       case Failure(cleanupException) => throw originalException;
    //     }
    //   }
    // };

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

    // Try {
    //   resource = resourceFactory();

    //   body(resource);
    // } match {
    //   case Success(value) => value
    //   case Failure(exception) => Try {
    //     cleanup(resource)
    //   } match {
    //     case Success(value) => throw exception
    //     case Failure(cleanupException) => throw cleanupException
    //   }
    // }
  }
}
