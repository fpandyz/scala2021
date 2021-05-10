package scala2021.ashinkarev.task05

import scala.concurrent._
import ExecutionContext.Implicits.global

import cats._
import cats.data.EitherT

import scala2021.ashinkarev.task05.Utils._

case class Info(employee: String, department: String, manager: String)

object Program extends App 
  with DepartmentsProvider
  with EmployeesProvider
  with ManagersProvider
{
  // Найти имя менеджера департамента, в котором работает сотрудник по имени сотрудника
  def findManagerName(employeeName: String): Option[String] = {
    toOption(findManagerNameOrError(employeeName))
  }
   
  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так
  def findManagerNameOrError(employeeName: String): Either[String, String] = {
    for {
      employeeByName <- findEmployeeByNameOrError(employeeName)
      employeeDepartment <- findEmployeeDepartmentOrError(employeeByName)
      departmentManager <- findDepartmentManagerOrError(employeeDepartment)
      managerEmployee <- findEmployeeByIdOrError(departmentManager.employeeId)
    } yield managerEmployee.name
  }

  // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать все это асинхронно
  def findManagerNameOrErrorAsync(employeeName: String): Future[Either[String, String]] = {
    val result = for {
      employeeByName <- EitherT(findEmployeeByNameOrErrorAsync(employeeName))
      employeeDepartment <- EitherT(findEmployeeDepartmentOrErrorAsync(employeeByName))
      departmentManager <- EitherT(findDepartmentManagerOrErrorAsync(employeeDepartment))
      managerEmployee <- EitherT(findEmployeeByIdOrErrorAsync(departmentManager.employeeId))
    } yield managerEmployee.name;

    return result.value;
  }

  def findEmployeeManagers(): List[Info] = {
    return getAllEmployees()
      .map(employee => (employee, findEmployeeDepartment(employee)))
      .map(e => (e._1, e._2, e._2 match {
        case Some(department) => findDepartmentManager(department)
        case None => None
      }))
      .map(e => (e._1, e._2, e._3, e._3 match {
        case Some(manager) => findEmployeeById(manager.employeeId)
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