package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

case class Info(employee: String, department: String, manager: String)

object Program extends App {
  // Найти имя менеджера департамента, в котором работает сотрудник по имени сотрудника
  def findManagerName(employeeName: String): Option[String] = {
    findManagerNameOrError(employeeName) match {
      case Left(value) => None
      case Right(value) => Some(value)
    }
  }
   
  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так
  def findManagerNameOrError(employeeName: String): Either[String, String] = {
    for {
      employeeByName <- new EmployeesProvider().findEmployeeByNameOrError(employeeName)
      employeeDepartment <- new DepartmentsProvider().findEmployeeDepartmentOrError(employeeByName)
      departmentManager <- new ManagersProvider().findDepartmentManagerOrError(employeeDepartment)
      managerEmployee <- new EmployeesProvider().findEmployeeByIdOrError(departmentManager.employeeId)
    } yield managerEmployee.name
  }

  // ToDo
  // https://eed3si9n.com/herding-cats/stacking-future-and-either.html
  // https://stackoverflow.com/questions/47419415/for-comprehensions-future-and-either
  // https://typelevel.org/cats/datatypes/eithert.html
  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать все это асинхронно
  def findManagerNameOrErrorAsync(employeeName: String): Future[Either[String, String]] = {
    new EmployeesProvider().findEmployeeByNameOrErrorAsync(employeeName) flatMap {
        case Right(employeeByName) =>
          new DepartmentsProvider().findEmployeeDepartmentOrErrorAsync(employeeByName) flatMap {
            case Right(employeeDepartment) => 
              new ManagersProvider().findDepartmentManagerOrErrorAsync(employeeDepartment) flatMap {
                case Right(departmentManager) => 
                  new EmployeesProvider().findEmployeeByIdOrErrorAsync(departmentManager.employeeId) flatMap {
                    case Right(managerEmployee) => Future.successful(Right(managerEmployee.name))
                    case Left(value) => Future.successful(Left(value))
                  }
                case Left(value) => Future.successful(Left(value))
              }
            case Left(value) =>  Future.successful(Left(value))
          }
        case Left(value) =>
          Future.successful(Left(value))
      }
  }

  def findEmployeeManagers(): List[Info] = {
    val employeesProvider = new EmployeesProvider()
    val departmentsProvider = new DepartmentsProvider()
    val managersProvider = new ManagersProvider()

    val allEmployees = new EmployeesProvider().getAllEmployees()

    return allEmployees
      .map(employee => (employee, departmentsProvider.findEmployeeDepartment(employee)))
      .map(e => (e._1, e._2, e._2 match {
        case Some(department) => managersProvider.findDepartmentManager(department)
        case None => None
      }))
      .map(e => (e._1, e._2, e._3, e._3 match {
        case Some(manager) => employeesProvider.findEmployeeById(manager.employeeId)
        case None => None
      }))
      .map(e => Info(
        e._1.name, 
        e._2 match {
          case Some(value) => value.name
          case None => "Not Found"
        },
        e._4 match {
          case Some(value) => value.name
          case None => "Not Found"
        })
      )
  }
}