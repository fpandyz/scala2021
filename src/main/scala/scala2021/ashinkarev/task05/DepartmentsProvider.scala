package scala2021.ashinkarev.task05

case class Department(id: Int, name: String)

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
