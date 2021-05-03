package scala2021.ashinkarev.task05

case class Info(employee: String, department: String, manager: String)

object Program extends App {
  def findManagerName(employeeName: String): Option[String] = {
    var employeeByName = new EmployeesProvider().findEmployeeByName(employeeName)

    val manager = employeeByName match {
      case Some(employee) => findEmployeeManager(employee)
      case None => None
    }

    val managerEmployee = manager match {
      case Some(manager) => new EmployeesProvider().findEmployeeById(manager.employeeId)
      case None => None
    }

    return managerEmployee.map(_.name)
  }

  def findEmployeeManager(employee: Employee): Option[Manager] = {
    new DepartmentsProvider().findEmployeeDepartment(employee) match {
      case Some(department) => new ManagersProvider().findDepartmentManager(department)
      case None => None 
    }
  }
}