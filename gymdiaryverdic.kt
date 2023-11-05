import java.util.Scanner

// Clase Usuario
data class Usuario(
    var correo: String,
    var contraseña: String,
    var nombre: String = "",
    var edad: Int = 0,
    var genero: String = "",
    var masaCorporal: Double = 0.0,
    var estatura: Double = 0.0,
    var objetivos: String = ""
) {
    fun editarPerfil() {
        print("Nuevo edad (Deja en blanco para mantener el actual): ")
        val nuevaEdad = readLine()?.toIntOrNull()
        if (nuevaEdad != null) {
            edad = nuevaEdad
        }

        print("Nuevo peso (kg): ")
        val nuevoPeso = readLine()?.toDoubleOrNull()
        if (nuevoPeso != null) {
            masaCorporal = nuevoPeso
        }

        // Recalcula el IMC
        val imc = calcularIMC()
        println("Tu nuevo IMC es: $imc")
    }

    fun verPerfil() {
        println("Perfil de usuario:")
        println("Nombre: $nombre")
        println("Correo: $correo")
        println("Edad: $edad")
        println("Género: $genero")
        println("Masa Corporal: $masaCorporal kg")
        println("Estatura: $estatura m")
        val imc = calcularIMC()
        println("IMC: $imc")
    }

    fun calcularIMC(): Double {
        return masaCorporal / (estatura * estatura)
    }

    fun establecerObjetivos() {
        println("Establecer Objetivos:")
        print("Objetivos (por ejemplo: perder peso, ganar masa muscular, mantenerse en forma, etc.): ")
        val nuevosObjetivos = readLine()
        if (nuevosObjetivos?.isNotBlank() == true) {
            objetivos = nuevosObjetivos
            println("Objetivos actualizados exitosamente.")
        } else {
            println("No se han realizado cambios en los objetivos.")
        }
    }
}

class ArbolNodo<C : Comparable<C>, V>(val clave: C, var valor: V) {
    var izquierda: ArbolNodo<C, V>? = null
    var derecha: ArbolNodo<C, V>? = null
}

class ArbolBinario<C : Comparable<C>, V> {
    private var raiz: ArbolNodo<C, V>? = null

    // Inserta un nuevo par clave-valor en el árbol binario
    fun insertar(clave: C, valor: V) {
        raiz = insertarRec(raiz, clave, valor)
    }

    // Función auxiliar para insertar un nodo de manera recursiva
    private fun insertarRec(nodo: ArbolNodo<C, V>?, clave: C, valor: V): ArbolNodo<C, V> {
        // Si el árbol está vacío, crea un nuevo nodo
        if (nodo == null) {
            return ArbolNodo(clave, valor)
        }

        // Compara la clave actual con la clave a insertar
        val comparar = clave.compareTo(nodo.clave)

        // Si la nueva clave es menor, la insertamos en el subárbol izquierdo
        if (comparar < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, clave, valor)
        }
        // Si la nueva clave es mayor, la insertamos en el subárbol derecho
        else if (comparar > 0) {
            nodo.derecha = insertarRec(nodo.derecha, clave, valor)
        }
        // Si la clave ya existe, actualizamos el valor
        else {
            nodo.valor = valor
        }

        return nodo
    }

    // Busca un usuario en el árbol binario a través del correo y contraseña
    fun buscarUsuario(correo: C, contraseña: C): Usuario? {
        return buscarRec(raiz, correo, contraseña)
    }
    // Función auxiliar para buscar un usuario de manera recursiva
    private fun buscarRec(nodo: ArbolNodo<C, V>?, correo: C, contraseña: C): Usuario? {
        // Si el nodo es nulo o la clave coincide, retornamos el valor
        if (nodo == null || correo == nodo.clave) {
            val contraseñaNodo = nodo?.valor as? String 
            return if (contraseñaNodo != null && contraseñaNodo == contraseña) {
                Usuario(correo as String, contraseñaNodo)
            } else {
                null
            }
        }

        // Comparamos la clave con la clave del nodo actual
        val comparar = correo.compareTo(nodo.clave)

        // Si la clave es menor, buscamos en el subárbol izquierdo
        if (comparar < 0) {
            return buscarRec(nodo.izquierda, correo, contraseña)
        }
        // Si la clave es mayor, buscamos en el subárbol derecho
        else {
            return buscarRec(nodo.derecha, correo, contraseña)
        }
    } 

    // Función para actualizar la contraseña de un usuario en el árbol binario
    fun actualizarContraseña(correo: C, nuevaContraseña: V) {
        raiz = actualizarContraseñaRec(raiz, correo, nuevaContraseña)
    }

    // Función auxiliar para actualizar la contraseña de manera recursiva
    private fun actualizarContraseñaRec(nodo: ArbolNodo<C, V>?, correo: C, nuevaContraseña: V): ArbolNodo<C, V>? {
        if (nodo == null) {
            return null
        }

        val comparar = correo.compareTo(nodo.clave)

        if (comparar < 0) {
            nodo.izquierda = actualizarContraseñaRec(nodo.izquierda, correo, nuevaContraseña)
        } else if (comparar > 0) {
            nodo.derecha = actualizarContraseñaRec(nodo.derecha, correo, nuevaContraseña)
        } else {
            nodo.valor = nuevaContraseña
        }

        return nodo
    }

    private fun altura(nodo: ArbolNodo<C, V>?): Int {
        return nodo?.let {
            1 + maxOf(altura(nodo.izquierda), altura(nodo.derecha))
        } ?: 0
    }

    // Función para calcular el factor de equilibrio de un nodo
    private fun factorEquilibrio(nodo: ArbolNodo<C, V>?): Int {
        return altura(nodo?.izquierda) - altura(nodo?.derecha)
    }

    // Función para realizar la rotación simple a la derecha
    private fun rotacionDerecha(nodo: ArbolNodo<C, V>): ArbolNodo<C, V> {
        val nuevoNodo = nodo.izquierda!!
        nodo.izquierda = nuevoNodo.derecha
        nuevoNodo.derecha = nodo
        return nuevoNodo
    }

    // Función para realizar la rotación simple a la izquierda
    private fun rotacionIzquierda(nodo: ArbolNodo<C, V>): ArbolNodo<C, V> {
        val nuevoNodo = nodo.derecha!!
        nodo.derecha = nuevoNodo.izquierda
        nuevoNodo.izquierda = nodo
        return nuevoNodo
    }

    // Función para balancear un nodo y sus subárboles
    private fun balancearNodo(nodo: ArbolNodo<C, V>): ArbolNodo<C, V> {
        val factor = factorEquilibrio(nodo)

        // Rotación simple a la derecha (LL)
        if (factor > 1 && factorEquilibrio(nodo.izquierda) >= 0) {
            return rotacionDerecha(nodo)
        }

        // Rotación simple a la izquierda (RR)
        if (factor < -1 && factorEquilibrio(nodo.derecha) <= 0) {
            return rotacionIzquierda(nodo)
        }

        // Rotación doble a la derecha-izquierda (LR)
        if (factor > 1 && factorEquilibrio(nodo.izquierda) < 0) {
            nodo.izquierda = rotacionIzquierda(nodo.izquierda!!)
            return rotacionDerecha(nodo)
        }

        // Rotación doble a la izquierda-derecha (RL)
        if (factor < -1 && factorEquilibrio(nodo.derecha) > 0) {
            nodo.derecha = rotacionDerecha(nodo.derecha!!)
            return rotacionIzquierda(nodo)
        }

        return nodo
    }

    // Función para balancear todo el árbol
    fun balancear() {
        raiz = balancearRec(raiz)
    }

    // Función auxiliar para balancear todo el árbol de manera recursiva
    private fun balancearRec(nodo: ArbolNodo<C, V>?): ArbolNodo<C, V>? {
        if (nodo == null) {
            return null
        }

        // Balancear los subárboles izquierdo y derecho
        nodo.izquierda = balancearRec(nodo.izquierda)
        nodo.derecha = balancearRec(nodo.derecha)

        return balancearNodo(nodo)
    }
    
}

// Clase Ejercicio
data class Ejercicio(
    var nombreEjercicio: String,
    var series: Int,
    var repeticiones: Int,
    var descanso: Int,
    var musculoTrabajado: String,
    var tiempoRealizacion: Int,
    var validador: Boolean = false
) {
    fun validar() {
        if (nombreEjercicio.isNotEmpty() && series > 0 && repeticiones > 0 && descanso >= 0 && musculoTrabajado.isNotEmpty() && tiempoRealizacion > 0) {
            validador = true
        }
    }

    fun validarEjercicioRealizado() {
        println("Validar ejercicio realizado:")
        print("Número de series realizadas: ")
        val seriesRealizadas = readLine()?.toIntOrNull() ?: 0
        print("Número de repeticiones realizadas: ")
        val repeticionesRealizadas = readLine()?.toIntOrNull() ?: 0

        if (seriesRealizadas == series && repeticionesRealizadas == repeticiones) {
            println("Has completado todas las series y repeticiones del ejercicio correctamente. ¡Buen trabajo!")
        } else {
            println("No has completado todas las series y repeticiones del ejercicio. Sigue trabajando en ello.")
        }
    }
}

// Clase Rutina
data class Rutina(
    var nombreRutina: String,
    var duracionTotal: Int,
    var listaActividades: MutableList<Ejercicio> = mutableListOf()
) {
    fun validarRutina(): Boolean {
        for (ejercicio in listaActividades) {
            if (!ejercicio.validador) {
                return false
            }
        }
        return true
    }

    fun agregarActividadFisica(ejercicio: Ejercicio) {
        listaActividades.add(ejercicio)
        duracionTotal += ejercicio.series * (ejercicio.repeticiones + ejercicio.descanso)
    }

    fun validarRutinaPorInput() {
        println("Validación de la rutina '$nombreRutina':")
        for (ejercicio in listaActividades) {
            print("¿Has completado el ejercicio '${ejercicio.nombreEjercicio}'? (Sí/No): ")
            val respuesta = readLine()?.trim()?.toLowerCase()
            if (respuesta == "si") {
                ejercicio.validador = true
            }
        }

        val rutinaValidada = validarRutina()
        if (rutinaValidada) {
            println("¡La rutina ha sido validada con éxito!")
        } else {
            println("La rutina no ha sido validada. Asegúrate de completar todos los ejercicios propuestos.")
        }
    }

    fun calcularDuracionTotal(): Int {
        var duracionTotal = 0
        for (ejercicio in listaActividades) {
            // Calcula la duración de cada ejercicio y agrégala a la duración total
            val duracionEjercicio = ejercicio.series * (ejercicio.repeticiones + ejercicio.descanso + ejercicio.tiempoRealizacion)
            duracionTotal += duracionEjercicio
        }
        return duracionTotal
    }
}


fun main() {
    val arbol = ArbolBinario<String, String>() // implementación del árbol
    val usuarios = mutableListOf<Usuario>() //implementacion de la lista enlazada
    var menuPrincipal = 0
    var menuSecundario = 0
    var usuario: Usuario? = null

    while (menuPrincipal != 3) {
        println("Menú Principal:")
        println("1. Registrarse")
        println("2. Ingresar")
        println("3. Salir")
        print("Selecciona una opción del menú principal: ")
        menuPrincipal = readLine()?.toIntOrNull() ?: 0

        when (menuPrincipal) {
            1 -> {
                // Registrar usuario
                println("Registro")
                print("Correo: ")
                val correo = readLine() ?: ""
                print("Contraseña: ")
                val contraseña = readLine() ?: ""
                print("Nombre: ")
                val nombre = readLine() ?: ""
                print("Edad: ")
                val edad = readLine()?.toIntOrNull() ?: 0
                print("Género: ")
                val genero = readLine() ?: ""
                print("Masa Corporal (kg): ")
                val masaCorporal = readLine()?.toDoubleOrNull() ?: 0.0
                print("Altura (m): ")
                val estatura = readLine()?.toDoubleOrNull() ?: 0.0

                val nuevoUsuario = Usuario(correo, contraseña, nombre, edad, genero, masaCorporal, estatura)
                usuarios.add(nuevoUsuario)
                arbol.insertar(nuevoUsuario.correo, nuevoUsuario.contraseña) //insertar usuario
                arbol.balancear() 

                println("Usuario registrado exitosamente.")
            }
            2 -> {
                // Ingresar usuario
                print("Correo: ")
                val correoIngreso = readLine() ?: ""
                print("Contraseña: ")
                val contraseñaIngreso = readLine() ?: ""

                val usuario = arbol.buscarUsuario(correoIngreso, contraseñaIngreso) //buscar usuario en el árbol

                if (usuario != null) {
                    println("Bienvenido, ${usuario.nombre}!")

                    while (menuSecundario != 6) {
                        println("Menú Secundario:")
                        println("1. Editar perfil")
                        println("2. Ver perfil")
                        println("3. Establecer objetivos")
                        println("4. Ingresar rutina")
                        println("5. Volver al Menú Principal")
                        print("Selecciona una opción del menú secundario: ")
                        menuSecundario = readLine()?.toIntOrNull() ?: 0

                        when (menuSecundario) {
                            1 -> {
                                // Editar perfil del usuario actual
                                println("Editar perfil:")
                                print("Nueva contraseña (Deja en blanco para mantener la actual): ")
                                val nuevaContraseña = readLine()
                                if (nuevaContraseña?.isNotBlank() == true) {
                                    usuario.contraseña = nuevaContraseña
                                }
                                if (usuario != null && nuevaContraseña != null) {
                                    arbol.actualizarContraseña(usuario.correo, nuevaContraseña)
                                }
                                usuario.editarPerfil()
                            }
                            2 -> {
                                // Ver perfil del usuario actual
                                usuario.verPerfil()
                            }
                            3 -> {
                                // Establecer objetivos
                                usuario.establecerObjetivos()
                            }
                            4 -> {
                                // Ingresar rutina
                                val nuevaRutina = Rutina("Mi Rutina", 0)
                                while (true) {
                                    val nuevoEjercicio = Ejercicio("", 0, 0, 0, "", 0)
                                    println("Ingresar los datos del ejercicio:")
                                    print("Nombre del ejercicio (Deja en blanco para finalizar): ")
                                    val nombreEjercicio = readLine()
                                    if (nombreEjercicio.isNullOrBlank()) {
                                        break
                                    }
                                    nuevoEjercicio.nombreEjercicio = nombreEjercicio
                                    print("Número de series: ")
                                    nuevoEjercicio.series = readLine()?.toIntOrNull() ?: 0
                                    print("Número de repeticiones: ")
                                    nuevoEjercicio.repeticiones = readLine()?.toIntOrNull() ?: 0
                                    print("Tiempo de descanso (segundos): ")
                                    nuevoEjercicio.descanso = readLine()?.toIntOrNull() ?: 0
                                    print("Músculo trabajado: ")
                                    nuevoEjercicio.musculoTrabajado = readLine() ?: ""
                                    print("Tiempo de realización (segundos): ")
                                    nuevoEjercicio.tiempoRealizacion = readLine()?.toIntOrNull() ?: 0
                                    nuevoEjercicio.validar()

                                    // Agregar el ejercicio a la rutina
                                    nuevaRutina.agregarActividadFisica(nuevoEjercicio)
                                }
                            }
                            5 -> {
                                println("Volviendo al Menú Principal.")
                            }
                            else -> println("Opción no válida en el Menú Secundario.")
                        }
                    }
                    menuSecundario = 0 // Reiniciar el menú secundario para futuros usos.
                }
            }
            3 -> {
                // Salir
                println("Saliendo del programa.")
            }
            else -> println("Opción no válida en el Menú Principal.")
        }
    }
}
