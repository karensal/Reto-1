// Clase para representar un usuario con correo y contraseña
class Usuario(private val correo: String, private val contraseña: String) {
    fun getCorreo(): String {
        return correo
    }

    fun getContraseña(): String {
        return contraseña
    }
}

// Clase para representar un nodo en un árbol binario
class ArbolNodo<C : Comparable<C>, V>(val clave: C, var valor: V) {
    var izquierda: ArbolNodo<C, V>? = null
    var derecha: ArbolNodo<C, V>? = null
}

// Clase para representar un árbol binario
class ArbolBinario<C : Comparable<C>, V> {  
    private var raiz: ArbolNodo<C, V>? = null

    // Método para insertar un nuevo par clave-valor en el árbol binario
    fun insertar(clave: C, valor: V) {
        raiz = insertarRec(raiz, clave, valor)
    }

    // Función auxiliar para insertar un nodo de manera recursiva
    private fun insertarRec(nodo: ArbolNodo<C, V>?, clave: C, valor: V): ArbolNodo<C, V> { //la clave es el correo, el valor la contraseña
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

fun main() {
    val arbol = ArbolBinario<String, String>()

    // Registro de usuario
    println("Registro")
    println("Ingresa tu correo:")
    val correoRegistro = readLine() ?: ""
    println("Ingresa tu contraseña:")
    val contraseñaRegistro = readLine() ?: ""

    // Crear un objeto Usuario 
    val nuevoUsuario = Usuario(correoRegistro, contraseñaRegistro)

    // Insertar el nuevo usuario en el árbol binario
    arbol.insertar(nuevoUsuario.getCorreo(), nuevoUsuario.getContraseña())
    arbol.balancear()

    // Ingreso de usuario
    println("Ingreso")
    println("Ingresa tu correo:")
    val correoIngreso = readLine() ?: ""
    println("Ingresa tu contraseña:")
    val contraseñaIngreso = readLine() ?: ""

    // Buscar un usuario por correo y contraseña
    val usuario = arbol.buscarUsuario(correoIngreso, contraseñaIngreso)
    if (usuario != null) {
        println("Usuario encontrado, bienvenido")
    } else {
        println("Usuario no encontrado")
    }
}