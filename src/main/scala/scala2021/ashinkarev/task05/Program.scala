package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

import cats._
import cats.data.EitherT

import scala2021.ashinkarev.task05.Utils._

case class Info(employee: String, department: String, manager: String)

object Program extends App {
  // Найти имя менеджера департамента, в котором работает сотрудник по имени сотрудника
  def findManagerName(employeeName: String): Option[String] = {
    toOption(findManagerNameOrError(employeeName))
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

  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать все это асинхронно
  def findManagerNameOrErrorAsync(employeeName: String): Future[Either[String, String]] = {
    val result = for {
      employeeByName <- EitherT(new EmployeesProvider().findEmployeeByNameOrErrorAsync(employeeName))
      employeeDepartment <- EitherT(new DepartmentsProvider().findEmployeeDepartmentOrErrorAsync(employeeByName))
      departmentManager <- EitherT(new ManagersProvider().findDepartmentManagerOrErrorAsync(employeeDepartment))
      managerEmployee <- EitherT(new EmployeesProvider().findEmployeeByIdOrErrorAsync(departmentManager.employeeId))
    } yield managerEmployee.name;

    return result.value;
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