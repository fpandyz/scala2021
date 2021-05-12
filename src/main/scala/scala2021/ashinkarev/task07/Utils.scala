package scala2021.ashinkarev.task07

import scala.util.{Try,Success,Failure}
import java.io.File
import java.io.FileInputStream

trait Connection {
  def close();

  def run();
}

class PrintConnection(port: Int) extends Connection {
  def close() = {
    println("Closed")
  }
  def run() = {
    println("Run")
  }
}

object Utils {
  def withConnection[TResult](port: Int)(body: (Connection) => TResult) = {
    withResource(() => new PrintConnection(port))(body)((connection) => connection.close())
  }

  def withFile[TResult](path: String)(body: (File) => TResult) = {
    withResource(() => new File(path))(body)()
  }

  def withFileInputStream[TResult](path: String)(body: (FileInputStream) => TResult) = {
    withResource(() => new FileInputStream(path))(body)((fileInputStream: FileInputStream) => fileInputStream.close())
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
        Try {
          cleanup(resource)
        } match {
          case Success(value) => {
            result
          };
          case Failure(cleanupException) => {
            throw cleanupException;
          }
        }
      }
      case Failure(originalException) => {
        Try {
          cleanup(resource)
        } match {
          case Success(value) => {
            throw originalException;
          }
          case Failure(cleanupException) => {
            throw originalException;
          };
        }
      }
    }
  }
}
