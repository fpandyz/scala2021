package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

case class Manager(department: String, employeeId: Int)

class ManagersProvider {
  private val managers = List(
    Manager("Marketing", 1),
    Manager("Sales", 10),
    Manager("IT", 14),
  )

  def findDepartmentManagerOrErrorAsync(department: Department): Future[Either[String, Manager]] = {
    Future {
      findDepartmentManagerOrError(department)
    }
  }

  def findDepartmentManagerOrError(department: Department): Either[String, Manager] = {
    findDepartmentManager(department) match {
      case Some(value) => Right(value)
      case None => Left(s"Cannot find manager for department with name: ${department.name}")
    }
  }

  def findDepartmentManager(department: Department): Option[Manager] = {
    return managers.find(manager => manager.department == department.name)
  }
}
