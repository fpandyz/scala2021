package scala2021.ashinkarev.task05

case class Info(employee: String, department: String, manager: String)

object Program extends App {
  // Найти имя менеджера департамента, в котором работает сотрудник по имени сотрудника
  def findManagerName(employeeName: String): Option[String] = {
    for {
      employeeByName <- new EmployeesProvider().findEmployeeByName(employeeName)
      employeeDepartment <- new DepartmentsProvider().findEmployeeDepartment(employeeByName)
      departmentManager <- new ManagersProvider().findDepartmentManager(employeeDepartment)
      managerEmployee <- new EmployeesProvider().findEmployeeById(departmentManager.employeeId)
    } yield managerEmployee.name
  }
}