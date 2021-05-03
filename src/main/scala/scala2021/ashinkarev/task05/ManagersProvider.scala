package scala2021.ashinkarev.task05

case class Manager(department: String, employeeId: Int)

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
