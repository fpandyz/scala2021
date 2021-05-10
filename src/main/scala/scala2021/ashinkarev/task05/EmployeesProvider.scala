package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala2021.ashinkarev.task05.Utils._

case class Employee(id: Int, name: String, departmentId: Int)

class EmployeesProvider {
  private val employees = List(
    Employee(1, "Steve", 1),
    Employee(3, "Mark", 1),
    Employee(4, "Jane", 1),
    Employee(7, "Samuel", 2),
    Employee(10, "Igor", 2),
    Employee(11, "Naveen", 4),
    Employee(12, "Christy", 5),
    Employee(15, "Megan", 3)
  )

  def getAllEmployees(): List[Employee] = {
    return employees
  }

  def findEmployeeByNameOrErrorAsync(employeeName: String): Future[Either[String, Employee]] = {
    Future {
      findEmployeeByNameOrError(employeeName)
    }
  }

  def findEmployeeByNameOrError(employeeName: String): Either[String, Employee] = {
    toEitherWithErrorMessage(findEmployeeByName(employeeName), () => s"Cannot find employee with name: ${employeeName}")
  }

  def findEmployeeByName(employeeName: String): Option[Employee] = {
    return employees.find(employee => employee.name == employeeName)
  }

  def findEmployeeByIdOrErrorAsync(employeeId: Int): Future[Either[String, Employee]] = {
    Future {
      findEmployeeByIdOrError(employeeId)
    }
  }

  def findEmployeeByIdOrError(employeeId: Int): Either[String, Employee] = {
    toEitherWithErrorMessage(findEmployeeById(employeeId), () => s"Cannot find employee with id: ${employeeId}")
  }

  def findEmployeeById(employeeId: Int): Option[Employee] = {
    return employees.find(employee => employee.id == employeeId)
  }
}
