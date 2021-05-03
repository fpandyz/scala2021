package scala2021.ashinkarev.task05

case class Employee(id: Int, name: String, departmentId: Int)
case class Department(id: Int, name: String)
case class Manager(department: String, employeeId: Int)
case class Info(employee: String, department: String, manager: String)

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

  def findEmployeeByName(employeeName: String): Option[Employee] = {
    return employees.find(employee => employee.name == employeeName)
  }

  def findEmployeeById(employeeId: Int): Option[Employee] = {
    return employees.find(employee => employee.id == employeeId)
  }
}

class DepartmentsProvider {
  private val departments = List(
    Department(1, "Marketing"),
    Department(2, "Sales"),
    Department(3, "Research"),
    Department(4, "IT"),
  )

  def findEmployeeDepartment(employee: Employee): Option[Department] = {
    return departments.find(department => department.id == employee.departmentId)
  }
}

class ManagersProvider {
  private val managers = List(
    Manager("Marketing", 1),
    Manager("Sales", 10),
    Manager("IT", 14),
  )

  def findDepartmentManager(department: Department): Option[Manager] = {
    return managers.find(manager => manager.department == department.name)
  }
}

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