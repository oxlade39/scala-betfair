package uk.co.softsquare.privetng.util

trait ReflectiveToString {
  override def toString: String = {
    val fields = getClass.getDeclaredFields.map{field => field.setAccessible(true); s"${field.getName} : ${field.get(this)}"}.mkString(", ")
    s"${getClass.getSimpleName}($fields)"
  }
}
