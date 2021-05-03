package scala2021.ashinkarev.task05

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
}