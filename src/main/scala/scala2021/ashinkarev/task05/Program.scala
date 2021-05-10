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
      employeeByName <- EmployeesProvider.findEmployeeByNameOrError(employeeName)
      employeeDepartment <- DepartmentsProvider.findEmployeeDepartmentOrError(employeeByName)
      departmentManager <- ManagersProvider.findDepartmentManagerOrError(employeeDepartment)
      managerEmployee <- EmployeesProvider.findEmployeeByIdOrError(departmentManager.employeeId)
    } yield managerEmployee.name
  }

  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать все это асинхронно
  def findManagerNameOrErrorAsync(employeeName: String): Future[Either[String, String]] = {
    val result = for {
      employeeByName <- EitherT(EmployeesProvider.findEmployeeByNameOrErrorAsync(employeeName))
      employeeDepartment <- EitherT(DepartmentsProvider.findEmployeeDepartmentOrErrorAsync(employeeByName))
      departmentManager <- EitherT(ManagersProvider.findDepartmentManagerOrErrorAsync(employeeDepartment))
      managerEmployee <- EitherT(EmployeesProvider.findEmployeeByIdOrErrorAsync(departmentManager.employeeId))
    } yield managerEmployee.name;

    return result.value;
  }

  def findEmployeeManagers(): List[Info] = {
    val allEmployees = EmployeesProvider.getAllEmployees()

    return allEmployees
      .map(employee => (employee, DepartmentsProvider.findEmployeeDepartment(employee)))
      .map(e => (e._1, e._2, e._2 match {
        case Some(department) => ManagersProvider.findDepartmentManager(department)
        case None => None
      }))
      .map(e => (e._1, e._2, e._3, e._3 match {
        case Some(manager) => EmployeesProvider.findEmployeeById(manager.employeeId)
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