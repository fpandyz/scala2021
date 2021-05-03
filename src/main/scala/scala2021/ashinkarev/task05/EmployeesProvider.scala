package scala2021.ashinkarev.task05

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

  def findEmployeeByName(employeeName: String): Option[Employee] = {
    return employees.find(employee => employee.name == employeeName)
  }

  def findEmployeeById(employeeId: Int): Option[Employee] = {
    return employees.find(employee => employee.id == employeeId)
  }
}
