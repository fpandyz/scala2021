package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala2021.ashinkarev.task05.Utils._

case class Department(id: Int, name: String)

object DepartmentsProvider {
  private var departments = List(
    Department(1, "Marketing"),
    Department(2, "Sales"),
    Department(3, "Research"),
    Department(4, "IT"),
  )

  def findEmployeeDepartmentOrErrorAsync(employee: Employee): Future[Either[String, Department]] = {
    Future {
      findEmployeeDepartmentOrError(employee)
    }
  }

  def findEmployeeDepartmentOrError(employee: Employee): Either[String, Department] = {
    toEitherWithErrorMessage(findEmployeeDepartment(employee), () => s"Cannot find department with id: ${employee.departmentId} for employee with name: ${employee.name}")
  }

  def findEmployeeDepartment(employee: Employee): Option[Department] = {
    return departments.find(department => department.id == employee.departmentId)
  }
}
